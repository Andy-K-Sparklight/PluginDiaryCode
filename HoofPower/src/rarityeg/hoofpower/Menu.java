package rarityeg.hoofpower;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Menu {
    public Inventory components;
    public Player owner;
    public static final String TITLE = "HoofPower 菜单";
    public static final String QUIT_SERVER = "退出服务器";
    public static final String SHOW_ANNOUNCEMENT = ChatColor.GOLD + "显示公告";
    public static final String TELEPORT = ChatColor.GREEN + "随机传送";

    public Menu(Player player) {
        components = Bukkit.createInventory(player, 9, TITLE);
        owner = player;

        ItemStack quitServer = new ItemStack(Material.BARRIER);
        ItemMeta quitServerMeta = quitServer.getItemMeta();
        quitServerMeta.setDisplayName(QUIT_SERVER);
        quitServerMeta.setLore(Collections.singletonList(ChatColor.GRAY + "" + ChatColor.ITALIC + "离开此服务器"));
        quitServer.setItemMeta(quitServerMeta);

        ItemStack showAnnouncement = new ItemStack(Material.BOOK);
        ItemMeta showAnnouncementMeta = showAnnouncement.getItemMeta();
        showAnnouncementMeta.setDisplayName(SHOW_ANNOUNCEMENT);
        showAnnouncementMeta.setLore(Collections.singletonList(ChatColor.GRAY + "" + ChatColor.ITALIC + "查看公告"));
        showAnnouncement.setItemMeta(showAnnouncementMeta);

        ItemStack teleport = new ItemStack(Material.COMPASS);
        ItemMeta teleportMeta = teleport.getItemMeta();
        teleportMeta.setDisplayName(TELEPORT);
        teleportMeta.setLore(Collections.singletonList(ChatColor.GRAY + "" + ChatColor.ITALIC + "在当前世界随机传送"));
        teleport.setItemMeta(teleportMeta);

        components.setItem(0, quitServer);
        components.setItem(4, showAnnouncement);
        components.setItem(8, teleport);
    }

    public void open() {
        owner.openInventory(components);
    }
}
