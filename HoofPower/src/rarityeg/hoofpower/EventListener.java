package rarityeg.hoofpower;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Objects;
import java.util.Random;

public class EventListener implements Listener {
    public static final Random RANDOM = new Random();

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        InventoryView inv = player.getOpenInventory();
        if (inv.getTitle().equals(Menu.TITLE)) {
            e.setCancelled(true);
            if (e.getRawSlot() < 0 || e.getRawSlot() > e.getInventory().getSize()) {
                return;
            }
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null) {
                return;
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.QUIT_SERVER)) {
                player.kickPlayer("您已离开服务器");
                return;
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.TELEPORT)) {
                player.closeInventory();
                World playerWorld = Bukkit.getWorlds().get(0);
                double randX = RANDOM.nextInt(200000) - 100000;
                double randZ = RANDOM.nextInt(200000) - 100000;
                Location offset = new Location(playerWorld, randX, 0, randZ).toHighestLocation();
                player.teleport(player.getLocation().add(offset));
                player.sendMessage(ChatColor.GREEN + "传送成功！");
                return;
            }
            if (clickedItem.getItemMeta().getDisplayName().equals(Menu.SHOW_ANNOUNCEMENT)) {
                ItemStack ann = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta annBm = (BookMeta) ann.getItemMeta();
                String[] acText = Objects.requireNonNullElse(HoofPower.instance.getConfig().getString("announcement"), "").split("\\+\\+\\+");
                annBm.setPages(acText);
                annBm.setAuthor("HoofPower");
                annBm.setTitle("服务器公告");
                ann.setItemMeta(annBm);
                player.openBook(ann);
            }

        }
    }


}
