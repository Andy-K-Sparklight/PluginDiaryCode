package rarityeg.harmonyauth;

import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigReader {
    public static boolean isPlayerRegistered(String playerName) {
        FileConfiguration config = HarmonyAuth.instance.getConfig();
        return config.contains(playerName.toLowerCase());
    }

    public static boolean verifyPassword(String playerName, String password) {
        if (!isPlayerRegistered(playerName)) {
            return false;
        }
        FileConfiguration config = HarmonyAuth.instance.getConfig();
        return password.equals(config.getString(playerName.toLowerCase()));
    }

    public static void addPlayer(String playerName, String password) {
        HarmonyAuth.instance.getConfig().set(playerName.toLowerCase(), password);
        HarmonyAuth.instance.saveConfig();
    }
}
