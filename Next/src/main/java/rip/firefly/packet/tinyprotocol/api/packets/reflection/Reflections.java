package rip.firefly.packet.tinyprotocol.api.packets.reflection;

import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class Reflections {
    private static final String craftBukkitString;
    private static final String netMinecraftServerString;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        craftBukkitString = "org.bukkit.craftbukkit." + version + ".";
        netMinecraftServerString = "net.minecraft.server." + version + ".";
    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static WrappedClass getCBClass(String name) {
        return getClass(craftBukkitString + name);
    }

    public static WrappedClass getNMSClass(String name) {
        return getClass(netMinecraftServerString + name);
    }

    public static WrappedClass getNewNetworkManagerClass() {
        return getClass("net.minecraft.network.NetworkManager");
    }

    public static WrappedClass getNewPlayerConnectionClass() {
        return getClass("net.minecraft.server.network.PlayerConnection");
    }

    public static WrappedClass getNewEntityPlayerClass() {
        return getClass("net.minecraft.server.level.EntityPlayer");
    }

    public static WrappedClass getClass(String name) {
        try {
            return new WrappedClass(Class.forName(name));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static WrappedClass getClass(Class clazz) {
        return new WrappedClass(clazz);
    }
}
