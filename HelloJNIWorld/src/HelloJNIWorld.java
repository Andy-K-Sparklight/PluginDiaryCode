import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HelloJNIWorld extends JavaPlugin {
    @Override
    public void onLoad() {
        saveResource("lib-hello.dll", false);
        System.load(new File(getDataFolder(), "lib-hello.dll").getAbsolutePath());
    }

    @Override
    public void onEnable() {

        new HelloJNIWorld().printHello();

    }

    public native void printHello();
}
