package rarityeg.harmonyauthsmart;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EventHarmony implements Listener {
    public static Map<UUID, Float> originSpeed = new HashMap<>();
    public static Map<UUID, UUID> lastJudgeUUID = new HashMap<>();

    @EventHandler
    public void antiMove(PlayerMoveEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            if (e.getFrom().distance(e.getTo()) != 0) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
        }
    }

    @EventHandler
    public void noHurt(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (RuntimeDataManager.hasRestrictUUID(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                e.getEntity().sendMessage(Util.getAndTranslate("msg.hint"));
            }
        }
    }

    @EventHandler
    public void noInteract(PlayerInteractEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
        }
    }

    @EventHandler
    public void noBreak(BlockBreakEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
        }
    }

    @EventHandler
    public void noInventory(InventoryOpenEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
        }
    }

    @EventHandler
    public void noTeleport(PlayerTeleportEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint"));
        }
    }

    public static final List<String> allowCmd = Arrays.asList("/hl", "/L", "/l", "/reg", "/register", "/login", "/log", "/iforgot", "/ifg");

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        HarmonyAuthSMART.instance.getLogger().info(e.getMessage());
        UUID id = e.getPlayer().getUniqueId();
        if (RuntimeDataManager.getIForgotMode(id) != 0) {
            switch (RuntimeDataManager.getIForgotMode(id)) {
                case 1:
                    e.setCancelled(true);
                    if (e.getMessage().split(" ").length != 1 || e.getMessage().startsWith("/")) {
                        e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-newpwd"));
                        return;
                    }
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
                                idm.setPasswordHash(id, idm.getIForgotNewPasswordHash(id));
                                RuntimeDataManager.exitIForgotMode(id);
                                e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-accepted"));
                                return;
                            }
                            idm.setIForgotNewPasswordHash(id, Util.calculateMD5(e.getMessage()));
                            RuntimeDataManager.toIForgotMode(id, 2);
                            e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-hint"));
                        }
                    }.runTaskAsynchronously(HarmonyAuthSMART.instance);

                    break;
                case 2:
                    e.setCancelled(true);
                    if (e.getMessage().equals("")) {
                        e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-hint"));
                        return;
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            IDataManager idm;
                            if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                                idm = new DBDataManager();
                            } else {
                                idm = new FileDataManager();
                            }
                            idm.setIForgotManualReason(id, e.getMessage());
                            idm.setIForgotState(id, true);
                            RuntimeDataManager.exitIForgotMode(id);
                            e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-commit"));
                        }
                    }.runTaskAsynchronously(HarmonyAuthSMART.instance);
                    break;
            }
        } else {
            if (RuntimeDataManager.isInReadMode(id)) {
                if (e.getMessage().startsWith("/")) {
                    if (!e.getMessage().startsWith("/iforgot")) {
                        return;
                    } else {
                        RuntimeDataManager.exitReadMode(id);
                        e.getPlayer().sendMessage(Util.getAndTranslate("msg.audit-out"));
                    }
                }

                e.setCancelled(true);
                if (e.getMessage().toLowerCase().startsWith("q")) {
                    RuntimeDataManager.exitReadMode(id);
                    e.getPlayer().sendMessage(Util.getAndTranslate("msg.audit-out"));
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            IDataManager idm;
                            if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                                idm = new DBDataManager();
                            } else {
                                idm = new FileDataManager();
                            }
                            UUID jid = lastJudgeUUID.get(e.getPlayer().getUniqueId());
                            if (e.getMessage().toLowerCase().startsWith("y")) {
                                idm.setPasswordHash(jid, idm.getIForgotNewPasswordHash(jid));
                                idm.setIForgotState(jid, false);
                                idm.setIForgotManualReason(jid, "<Internal> Accepted.");
                            } else if (e.getMessage().toLowerCase().startsWith("n")) {
                                idm.setIForgotState(jid, false);
                                idm.setIForgotManualReason(jid, "<Internal> Rejected.");
                            }
                            Player player = e.getPlayer();
                            UUID nextId = idm.getNextRequest();
                            lastJudgeUUID.put(player.getUniqueId(), nextId);
                            if (nextId.equals(UUID.fromString("00000000-0000-0000-0000-000000000000"))) {
                                RuntimeDataManager.exitReadMode(id);
                                player.sendMessage(Util.getAndTranslate("msg.audit-out"));
                            } else {
                                player.sendMessage(Util.getAndTranslate("msg.audit-uuid") + nextId.toString());
                                player.sendMessage(Util.getAndTranslate("msg.audit-reason") + idm.getIForgotManualReason(nextId));
                                player.sendMessage(Util.getAndTranslate("msg.audit-hint"));
                            }
                        }
                    }.runTaskAsynchronously(HarmonyAuthSMART.instance);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        if (RuntimeDataManager.hasRestrictUUID(e.getPlayer().getUniqueId())) {
            String msg = e.getMessage();
            e.setCancelled(true);
            for (String a : allowCmd) {
                if (msg.startsWith(a)) {
                    e.setCancelled(false);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        System.out.println("Walk speed set from " + e.getPlayer().getWalkSpeed());
        originSpeed.put(e.getPlayer().getUniqueId(), e.getPlayer().getWalkSpeed());
        e.getPlayer().setWalkSpeed((float) 0.00001);
    }


    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        RuntimeDataManager.addRestrictUUID(id);
        new BukkitRunnable() {
            @Override
            public void run() {
                Date crDate = new Date();
                IDataManager idm;
                if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                    idm = new DBDataManager();
                } else {
                    idm = new FileDataManager();
                }
                if (idm.isExist(id)) {
                    Date lLogin = idm.getLastLoginTime(id);
                    double seconds = (crDate.getTime() - lLogin.getTime()) / 1000.0;
                    if (seconds <= HarmonyAuthSMART.instance.getConfig().getInt("auto-login")) {
                        RuntimeDataManager.removeRestrictUUID(id);
                        e.getPlayer().sendMessage(Util.getAndTranslate("msg.login-success"));
                        List<String> hooks = Util.generateHooks("hook.on-login-success", e.getPlayer().getName());
                        for (String cmd : hooks) {
                            Util.dispatchCommandAsServer(cmd);
                        }
                    } else {
                        e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint-login"));
                        String ifState = idm.getIForgotManualReason(id);
                        if (ifState.equals("<Internal> Accepted.")) {
                            e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-accepted"));
                        } else if (ifState.equals("<Internal> Rejected.")) {
                            e.getPlayer().sendMessage(Util.getAndTranslate("msg.iforgot-rejected"));
                        }
                    }
                } else {
                    e.getPlayer().sendMessage(Util.getAndTranslate("msg.hint-register"));
                }
            }
        }.runTaskAsynchronously(HarmonyAuthSMART.instance);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UUID id = e.getPlayer().getUniqueId();
        RuntimeDataManager.removeRestrictUUID(id);
        RuntimeDataManager.exitIForgotMode(id);
        RuntimeDataManager.exitReadMode(id);
        e.getPlayer().setWalkSpeed(originSpeed.get(id));
        originSpeed.remove(id);
        new BukkitRunnable() {
            @Override
            public void run() {
                IDataManager idm;
                if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                    idm = new DBDataManager();
                } else {
                    idm = new FileDataManager();
                }
                idm.setLastLoginTime(e.getPlayer().getUniqueId(), new Date());
            }
        }.runTaskAsynchronously(HarmonyAuthSMART.instance);
    }
}
