package rarityeg.commons;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;
import java.io.*;

public class JavaPluginR extends JavaPlugin {
    private static JavaPluginR instance;
    @Nonnull
    protected final SyncBukkit syncBukkit;

    /**
     * Gets the instance of this plugin.
     *
     * @return A instance of this plugin.
     * @throws IllegalStateException When this plugin hasn't been loaded.
     */
    @Nonnull
    public static JavaPluginR getInstance() {
        if (instance == null) {
            throw new IllegalStateException("This plugin hasn't been loaded yet!");
        }
        return instance;
    }

    /**
     * Return a synced Bukkit instance so that you can call them in asynchronous methods.
     *
     * @return The synced Bukkit.
     * @see SyncBukkit
     */
    @Nonnull
    public SyncBukkit getSyncBukkit() {
        return syncBukkit;
    }


    public JavaPluginR() {
        super();
        instance = this;
        syncBukkit = new SyncBukkit(this);
    }

    @ParametersAreNonnullByDefault
    public String readFile(File f) {
        try {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String temp;
            while ((temp = reader.readLine()) != null) {
                builder.append(temp);
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @ParametersAreNonnullByDefault
    public void writeFile(File f, Serializable data) {
        String dataS = data.toString();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            writer.write(dataS);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @ParametersAreNullableByDefault
    public void registerTabExecutor(String command, TabExecutor executor) {
        if (command == null || executor == null) {
            return;
        }
        PluginCommand cmd = Bukkit.getPluginCommand(command);
        if (cmd == null) {
            return;
        }
        cmd.setExecutor(executor);
        cmd.setTabCompleter(executor);
    }


}
