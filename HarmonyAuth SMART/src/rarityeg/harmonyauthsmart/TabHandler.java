package rarityeg.harmonyauthsmart;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class TabHandler implements TabCompleter {
    private static final Map<UUID, Boolean> QUERY_BUFFER = new HashMap<>();

    @Override
    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        if (args.length >= 3) {
            return null;
        }
        UUID id = ((Player) sender).getUniqueId();
        if (!RuntimeDataManager.hasRestrictUUID(id)) {
            return null;
        }
        if (QUERY_BUFFER.containsKey(id)) {
            if (QUERY_BUFFER.get(id)) {
                if (args.length == 0 || args.length == 1) {
                    return Collections.singletonList("<输入您的密码>");
                }
                return null;
            } else {
                if (args.length == 0 || args.length == 1) {
                    return Collections.singletonList("<设置您的密码>");
                }
                return Collections.singletonList("<再输入一遍以确认>");
            }
        } else {
            IDataManager idm;
            if (HarmonyAuthSMART.instance.getConfig().getBoolean("mysql.enabled") && !HarmonyAuthSMART.dbError) {
                idm = new DBDataManager();
            } else {
                idm = new FileDataManager();
            }
            if (idm.isExist(id)) {
                QUERY_BUFFER.put(id, true);
                if (args.length == 0 || args.length == 1) {
                    return Collections.singletonList("<输入您的密码>");
                }
                return null;
            } else {
                QUERY_BUFFER.put(id, false);
                if (args.length == 0 || args.length == 1) {
                    return Collections.singletonList("<设置您的密码>");
                }
                return Collections.singletonList("<再输入一遍以确认>");
            }
        }

    }
}
