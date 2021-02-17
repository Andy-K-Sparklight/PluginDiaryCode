package rarityeg.helloworld;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class HelloWorld extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("Hello, world!");
    }
}
