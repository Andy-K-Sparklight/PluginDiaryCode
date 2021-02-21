package rarityeg.commons;

import org.bukkit.Bukkit;

public class Reflector {
    private static String VERSION = "";
    private static String NMS_PACKAGE = "";
    private static String OBC_PACKAGE = "";

    static {
        String version = Bukkit.getMinecraftVersion();
        String v1 = version.split("\\.")[0];
        String v2 = version.split("\\.")[1];
        String nmsBaseHead = "net.minecraft.server.";
        for (int i = 1; i <= 20; i++) {
            try {
                Class.forName(nmsBaseHead + "v" + v1 + "_" + v2 + "_R" + i + ".ItemStack");
                VERSION = "v" + v1 + "_" + v2 + "_R" + i;
                NMS_PACKAGE = nmsBaseHead + VERSION;
                OBC_PACKAGE = "org.bukkit.craftbukkit." + VERSION;
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    public static String getNMSPackage() {
        return NMS_PACKAGE;
    }

    public static String getOBCPackage() {
        return OBC_PACKAGE;
    }

    public static String getVersion() {
        return VERSION;
    }
    
}
