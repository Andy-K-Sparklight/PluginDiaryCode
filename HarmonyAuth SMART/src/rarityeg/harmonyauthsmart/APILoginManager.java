package rarityeg.harmonyauthsmart;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rarityeg.harmonyauth.api.ILoginManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class APILoginManager implements ILoginManager {
    @Override
    public boolean isLoggedIn(UUID id) {
        return !RuntimeDataManager.hasRestrictUUID(id);
    }

    @Override
    public void login(UUID id) {
        if (isLoggedIn(id)) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Player p = Bukkit.getPlayer(id);
                if (p == null) {
                    return;
                }
                RuntimeDataManager.removeRestrictUUID(id);
                p.setWalkSpeed(EventHarmony.originSpeed.get(id));
                p.sendMessage(Util.getAndTranslate("msg.login-success"));
                List<String> hooks = Util.generateHooks("hook.on-login-success", p.getName());
                for (String cmd : hooks) {
                    Util.dispatchCommandAsServer(cmd);
                }
                IDataManager idm;
                if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                    idm = new DBDataManager();
                } else {
                    idm = new FileDataManager();
                }
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        idm.setIForgotManualReason(id, "");
                        idm.setIForgotState(id, false);
                        idm.setLastLoginTime(id, new Date());
                    }
                }.runTaskAsynchronously(HarmonyAuthSMART.instance);
            }
        }.runTask(HarmonyAuthSMART.instance);
    }
}
