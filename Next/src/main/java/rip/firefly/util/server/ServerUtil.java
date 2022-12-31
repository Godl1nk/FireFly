package rip.firefly.util.server;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import rip.firefly.util.player.PlayerUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import static org.bukkit.Bukkit.getServer;

public class ServerUtil {

    private static final String version = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
    public static final Class<?> EntityPlayer = ServerUtil.getNMSClass("EntityPlayer");
    public static final Class<?> Entity = ServerUtil.getNMSClass("Entity");
    private static final Class<?> CraftPlayer = ServerUtil.getCBClass("entity.CraftPlayer");
    private static final Class<?> CraftEntity = ServerUtil.getCBClass("entity.CraftEntity");
    private static final Class<?> CraftWorld = ServerUtil.getCBClass("CraftWorld");
    private static final Class<?> World = ServerUtil.getNMSClass("World");
    private static final Method getCubes = ServerUtil.getMethod(World, "a", ServerUtil.getNMSClass("AxisAlignedBB"));

    public static Object getEntityPlayer(Player player) {
        return ServerUtil.getMethodValue(ServerUtil.getMethod(CraftPlayer, "getHandle"), player);
    }

    public static Entity getEntity(Player player, Object packet) {
        Object playerWorld = ServerUtil.getMethodValue(ServerUtil.getMethod(ServerUtil.getEntityPlayer(player).getClass(), "getWorld"), packet);
        int entityId = (int) ServerUtil.getMethodValue(ServerUtil.getMethod(playerWorld.getClass(), "getId"), playerWorld);

        for (org.bukkit.entity.Entity entity2 : player.getWorld().getEntities()) {
            if (entity2.getEntityId() == entityId) {
                return entity2;
            }
        }
        return null;
    }

    public static Object getTeleportBox(Player player) {
        Object box = getBoundingBox(player);
        return getMethodValue(getMethod(box.getClass(), "grow", double.class, double.class, double.class), box, 0.1, 0, 0.1);
    }

    public static Object getPhaseBox(Player player) {
        Object box = getBoundingBox(player);
        return getMethodValue(getMethod(box.getClass(), "shrink", double.class, double.class, double.class), box, 0.01, 0.01, 0.01);
    }

    public static Object getBoundingBox(Player player) {
        return ServerUtil.isBukkitVerison("1_7") ? ServerUtil.getFieldValue(ServerUtil.getFieldByName(Entity, "boundingBox"), getEntityPlayer(player)) : ServerUtil.getMethodValue(ServerUtil.getMethod(EntityPlayer, "getBoundingBox"), getEntityPlayer(player));
    }

    public static boolean isBukkitVerison(String version) {
        String bukkit = getServer().getClass().getPackage().getName().substring(23);

        return bukkit.contains(version);
    }

    public static boolean isNewVersion() {
        return isBukkitVerison("1_9") || isBukkitVerison("1_1");
    }

    public static Class<?> getCBClass(String string) {
        return getClass("org.bukkit.craftbukkit." + version + "." + string);
    }

    public static Class<?> getClass(String string) {
        try {
            return Class.forName(string);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Enum<?> getEnum(Class<?> clazz, String enumName) {
        return Enum.valueOf((Class<Enum>) clazz, enumName);
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean inBlock(Player player, Object axisAlignedBB) {
        Object world = ServerUtil.getMethodValue(ServerUtil.getMethod(CraftWorld, "getHandle"), player.getWorld());
        return ((Collection<?>) ServerUtil.getMethodValue(getCubes, world, axisAlignedBB)).size() > 0;
    }

    public static Collection<?> getCollidingBlocks(Player player, Object axisAlignedBB) {
        Object world = ServerUtil.getMethodValue(ServerUtil.getMethod(CraftWorld, "getHandle"), player.getWorld());
        return ((Collection<?>) ServerUtil.getMethodValue(getCubes, world, axisAlignedBB));
    }

    public static Field getFirstFieldByType(Class<?> clazz, Class<?> type) {
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().equals(type)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... args) {
        try {
            Method method = clazz.getMethod(methodName, args);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getMethodValue(Method method, Object object, Object... args) {
        try {
            return method.invoke(object, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(Field field, Object object) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String string) {
        return getClass("net.minecraft.server." + version + "." + string);
    }

    //Will return null if chunk in location is not in memory. Do not modify blocks asynchronously!
    public static Block getBlockAsync(Location loc) {
        if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4))
            return loc.getBlock();
        return null;
    }

    //Will return null if chunk in location is not in memory. Do not modify chunks asynchronously!
    public static Chunk getChunkAsync(Location loc) {
        if (loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4))
            return loc.getChunk();
        return null;
    }

    public static boolean isFullyStuck(Player player) {
        Block block1 = player.getLocation().clone().getBlock();
        Block block2 = player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock();
        if (block1.getType().isSolid() && block2.getType().isSolid()) {
            return true;
        }
        if (block1.getRelative(BlockFace.DOWN).getType().isSolid()
                || block1.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()
                && block2.getRelative(BlockFace.DOWN).getType().isSolid()
                || block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
            return true;
        }
        return false;
    }

    public static boolean isPartiallyStuck(Player player) {
        if (player.getLocation().clone().getBlock() == null) {
            return false;
        }
        Block block = player.getLocation().clone().getBlock();
        if (PlayerUtil.isSlab(block) || PlayerUtil.isStair(block)) {
            return false;
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()
                || player.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
            return true;
        }
        if (player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().getRelative(BlockFace.DOWN).getType()
                .isSolid()
                || player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().getRelative(BlockFace.UP).getType()
                .isSolid()) {
            return true;
        }
        if (block.getType().isSolid()) {
            return true;
        }
        return false;
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long nowlong() {
        return System.currentTimeMillis();
    }

    public static boolean isInAir(Player player) {
        boolean nearBlocks = false;
        for (Block block : PlayerUtil.getSurrounding(player.getLocation().getBlock(), true)) {
            if (block.getType() != Material.AIR) {
                nearBlocks = true;
                break;
            }
        }
        return nearBlocks;
    }
}