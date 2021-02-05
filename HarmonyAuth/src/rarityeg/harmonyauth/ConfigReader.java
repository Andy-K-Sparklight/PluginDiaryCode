package rarityeg.harmonyauth;

import org.bukkit.configuration.file.FileConfiguration;

public final class ConfigReader {
    public static FileConfiguration config = HarmonyAuth.instance.getConfig();

    public static boolean isPlayerRegistered(String playerName) {

        return config.contains(playerName.toLowerCase());
    }

    public static boolean verifyPassword(String playerName, String password) {
        return password.equals(config.getString(playerName.toLowerCase()));
    }

    public static void addPlayer(String playerName, String password) {
        HarmonyAuth.instance.getConfig().set(playerName.toLowerCase(), password);
        HarmonyAuth.instance.saveConfig();
    }
}
