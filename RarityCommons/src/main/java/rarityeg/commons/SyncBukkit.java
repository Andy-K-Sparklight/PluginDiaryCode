package rarityeg.commons;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.ParametersAreNullableByDefault;

/**
 * Call Bukkit methods asynchronously with BukkitRunnable, not supported for getters.
 *
 * @see Bukkit
 */
public class SyncBukkit {
    @Nonnull
    private final JavaPlugin pluginInstance;

    @ParametersAreNonnullByDefault
    public SyncBukkit(JavaPlugin plugin) {
        pluginInstance = plugin;
    }

    @ParametersAreNullableByDefault
    public void dispatchCommand(CommandSender cs, String cmd) {
        if (cs == null || cmd == null) {
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(cs, cmd);
            }
        }.runTask(pluginInstance);
    }

    public void setMaxPlayers(int count) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.setMaxPlayers(count);
            }
        }.runTask(pluginInstance);
    }

    public void setWhiteList(boolean useWhiteList) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.setWhitelist(useWhiteList);
            }
        }.runTask(pluginInstance);
    }

    public void reloadWhiteList() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.reloadWhitelist();
            }
        }.runTask(pluginInstance);
    }

    @ParametersAreNullableByDefault
    public void broadcast(String... msg) {
        if (msg == null) {
            return;
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                for (String s : msg) {
                    if (s != null) {
                        Bukkit.broadcastMessage(s);
                    }
                }
            }
        }.runTask(pluginInstance);
    }

    @ParametersAreNullableByDefault
    public void broadcast(BaseComponent... components) {
        if (components == null) {
            return;
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.broadcast(components);
            }
        }.runTask(pluginInstance);
    }

    public void reload() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.reload();
            }
        }.runTask(pluginInstance);
    }

    public void reloadData() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.reloadData();
            }
        }.runTask(pluginInstance);
    }

    @ParametersAreNullableByDefault
    public void addRecipe(Recipe... recipes) {
        if (recipes == null) {
            return;
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Recipe r : recipes) {
                    Bukkit.addRecipe(r);
                }
            }
        }.runTask(pluginInstance);
    }

    public void clearRecipes() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.clearRecipes();
            }
        }.runTask(pluginInstance);
    }


    public void resetRecipes() {
        new BukkitRunnable() {

            @Override
            public void run() {
                Bukkit.resetRecipes();
            }
        }.runTask(pluginInstance);
    }

    @ParametersAreNullableByDefault
    public void removeRecipe(NamespacedKey... name) {
        if (name == null) {
            return;
        }
        new BukkitRunnable() {

            @Override
            public void run() {
                for (NamespacedKey n : name) {
                    if (n != null) {
                        Bukkit.removeRecipe(n);
                    }
                }
            }
        }.runTask(pluginInstance);
    }

    public void setSpawnRadius(int radius) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.setSpawnRadius(radius);
            }
        }.runTask(pluginInstance);
    }

    public void shutdown() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTask(pluginInstance);
    }

    public void setDefaultGameMode(GameMode mode) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.setDefaultGameMode(mode);
            }
        }.runTask(pluginInstance);
    }

    public void reloadPermissions() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.reloadPermissions();
            }
        }.runTask(pluginInstance);
    }

    public void reloadCommandAliases() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.reloadCommandAliases();
            }
        }.runTask(pluginInstance);
    }
}
