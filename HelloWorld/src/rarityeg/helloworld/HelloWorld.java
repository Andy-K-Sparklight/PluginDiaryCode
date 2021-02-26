package rarityeg.helloworld;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HelloWorld extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("Hello, world!");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        e.getPlayer().sendMessage("My\tLittle\tPony\nFriendship\tIs\tMagic!");
    }
}
