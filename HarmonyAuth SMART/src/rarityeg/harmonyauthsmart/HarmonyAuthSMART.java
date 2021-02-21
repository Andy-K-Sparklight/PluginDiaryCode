package rarityeg.harmonyauthsmart;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import rarityeg.harmonyauth.api.ILoginManager;
import rarityeg.harmonyauth.api.IStoredDataManager;

import java.util.Objects;
import java.util.logging.Level;

public class HarmonyAuthSMART extends JavaPlugin {
    public static JavaPlugin instance;
    public static boolean dbError = false;

    @Override
    public void onLoad() {
        Bukkit.getServicesManager().register(ILoginManager.class, new APILoginManager(), this, ServicePriority.Normal);
        Bukkit.getServicesManager().register(IStoredDataManager.class, new APIStoredDataManager(), this, ServicePriority.Normal);
    }

    @Override
    public void onEnable() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            getLogger().log(Level.WARNING, "数据库驱动加载失败，将使用备用存储方法。");
            e.printStackTrace();
            dbError = true;
        }
        saveDefaultConfig();
        saveResource("data.yml", false);
        instance = this;
        Bukkit.getPluginManager().registerEvents(new EventHarmony(), this);
        Objects.requireNonNull(Bukkit.getPluginCommand("hl")).setExecutor(new CommandHandler());
        Objects.requireNonNull(Bukkit.getPluginCommand("iforgot")).setExecutor(new CommandHandler());
        if (getConfig().getBoolean("mysql.enabled")) {
            new DBDataManager().loadAll();
        }
        new FileDataManager().loadAll();

    }

    @Override
    public void onDisable() {
        new DBDataManager().saveAll();
        new FileDataManager().saveAll();
    }
}
