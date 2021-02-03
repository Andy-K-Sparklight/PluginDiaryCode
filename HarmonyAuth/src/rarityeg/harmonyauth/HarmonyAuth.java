package rarityeg.harmonyauth;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class HarmonyAuth extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("login")).setExecutor(new CommandHandler());
        instance = this;
    }

    @Override
    public void onDisable() {
        saveConfig();
    }
}
