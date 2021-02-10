package rarityeg.harmonyauthsmart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    public static List<UUID> NoInterruptList = new ArrayList<>();


    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        UUID id = ((Player) commandSender).getUniqueId();
        if (RuntimeDataManager.getIForgotMode(id) != 0 || RuntimeDataManager.isInReadMode(id)) {
            return true;
        }
        if (getIF(id)) {
            commandSender.sendMessage(Util.getAndTranslate("msg.command-handling"));
            return true;
        }
        cli(id);
        if (command.getName().equals("hl")) {
            return onLoginCommand(commandSender, args);
        } else if (command.getName().equals("iforgot")) {
            if (!HarmonyAuthSMART.instance.getConfig().getBoolean("iforgot")) {
                commandSender.sendMessage(Util.getAndTranslate("msg.iforgot-no-available"));
                sti(id);
                return true;
            }
            return onIForgotCommand(commandSender);
        } else {
            sti(id);
            return false;
        }
    }

    public boolean onLoginCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        UUID id = player.getUniqueId();
        if (RuntimeDataManager.hasRestrictUUID(id)) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    IDataManager idm;
                    if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                        idm = new DBDataManager();
                    } else {
                        idm = new FileDataManager();
                    }
                    if (idm.isExist(id)) {
                        if (args[0] == null) {
                            player.sendMessage(Util.getAndTranslate("msg.login-failed"));
                            sti(id);
                            List<String> hooks = Util.generateHooks("hook.on-login-failed", player.getName());
                            for (String cmd : hooks) {
                                Util.dispatchCommandAsServer(cmd);
                            }
                            return;
                        }
                        if (idm.getPasswordHash(id).equals(Util.calculateMD5(args[0]))) {
                            RuntimeDataManager.removeRestrictUUID(id);
                            player.setWalkSpeed(EventHarmony.originSpeed.get(id));
                            player.sendMessage(Util.getAndTranslate("msg.login-success"));
                            idm.setIForgotManualReason(id, "");
                            idm.setIForgotState(id, false);
                            idm.setLastLoginTime(id, new Date());
                            sti(id);
                            List<String> hooks = Util.generateHooks("hook.on-login-success", player.getName());
                            for (String cmd : hooks) {
                                Util.dispatchCommandAsServer(cmd);
                            }
                            return;
                        }
                        player.sendMessage(Util.getAndTranslate("msg.login-failed"));
                        List<String> hooks = Util.generateHooks("hook.on-login-failed", player.getName());
                        for (String cmd : hooks) {
                            Util.dispatchCommandAsServer(cmd);
                        }
                        sti(id);
                        return;
                    }
                    if (args.length < 2 || !args[0].equals(args[1])) {
                        player.sendMessage(Util.getAndTranslate("msg.register-failed"));
                        sti(id);
                        return;
                    }
                    idm.setPasswordHash(id, Util.calculateMD5(args[0]));
                    RuntimeDataManager.removeRestrictUUID(id);
                    player.setWalkSpeed(EventHarmony.originSpeed.get(id));
                    player.sendMessage(Util.getAndTranslate("msg.register-success"));
                    idm.setIForgotManualReason(id, "");
                    idm.setIForgotState(id, false);
                    idm.setLastLoginTime(id, new Date());
                    sti(id);
                    List<String> hooks = Util.generateHooks("hook.on-register-success", player.getName());
                    for (String cmd : hooks) {
                        Util.dispatchCommandAsServer(cmd);
                    }
                }
            }.runTaskAsynchronously(HarmonyAuthSMART.instance);
        } else {
            player.sendMessage(Util.getAndTranslate("msg.login-success"));
            sti(id);
        }
        return true;
    }

    public boolean onIForgotCommand(CommandSender commandSender) {
        Player player = (Player) commandSender;
        UUID id = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                IDataManager idm;
                if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                    idm = new DBDataManager();
                } else {
                    idm = new FileDataManager();
                }
                if (!RuntimeDataManager.hasRestrictUUID(id)) {
                    if (!player.isOp()) {
                        idm.setIForgotState(id, false);
                        idm.setIForgotManualReason(id, "");
                        RuntimeDataManager.toIForgotMode(id, 1);

                        player.sendMessage(Util.getAndTranslate("msg.iforgot-newpwd"));
                    } else {
                        RuntimeDataManager.toReadMode(id);
                        player.sendMessage(Util.getAndTranslate("msg.audit-in"));
                        UUID firstId = idm.getNextRequest();
                        EventHarmony.lastJudgeUUID.put(player.getUniqueId(), firstId);
                        if (firstId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                            RuntimeDataManager.exitReadMode(id);
                            player.sendMessage(Util.getAndTranslate("msg.audit-out"));
                        } else {
                            player.sendMessage(Util.getAndTranslate("msg.audit-uuid") + firstId.toString());
                            player.sendMessage(Util.getAndTranslate("msg.audit-reason") + idm.getIForgotManualReason(firstId));
                            player.sendMessage(Util.getAndTranslate("msg.audit-hint"));

                        }
                    }

                } else {
                    RuntimeDataManager.toIForgotMode(id, 1);
                    player.sendMessage(Util.getAndTranslate("msg.iforgot-newpwd"));
                }
                sti(id);
            }
        }.runTaskAsynchronously(HarmonyAuthSMART.instance);
        return true;
    }

    private static synchronized void cli(UUID id) {
        NoInterruptList.add(id);
    }

    private static synchronized void sti(UUID id) {
        NoInterruptList.remove(id);
    }

    private static synchronized boolean getIF(UUID id) {
        return NoInterruptList.contains(id);
    }

}
