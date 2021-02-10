package rarityeg.harmonyauthsmart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.codec.binary.Hex;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Util {
    @Nonnull
    public static String getAndTranslate(@Nonnull String key) {
        String str = Objects.requireNonNullElse(HarmonyAuthSMART.instance.getConfig().getString(key, ""), "");
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @Nonnull
    public static String calculateMD5(@Nonnull String origin) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(origin.getBytes(StandardCharsets.UTF_8));
            return String.valueOf(Hex.encodeHex(md.digest()));
        } catch (NoSuchAlgorithmException e) {
            HarmonyAuthSMART.instance.getLogger().severe("必要的 MD5 哈希算法不可用，正在禁用本插件……。");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(HarmonyAuthSMART.instance);
            return "";
        }
    }

    public static List<String> generateHooks(@Nonnull String key, @Nonnull String playerName) {
        List<String> origin = HarmonyAuthSMART.instance.getConfig().getStringList(key);
        List<String> output = new ArrayList<>();
        for (String cmd : origin) {
            if (cmd != null && !cmd.equals("")) {
                output.add(cmd.replaceAll("\\$\\{playerName}", playerName));
            }
        }
        return output;
    }

    public static synchronized void dispatchCommandAsServer(String cmd) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }.runTask(HarmonyAuthSMART.instance);
    }

}
