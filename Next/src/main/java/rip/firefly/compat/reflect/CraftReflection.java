package rip.firefly.compat.reflect;

import com.google.common.util.concurrent.ListenableFuture;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.firefly.compat.wrapper.asm.WrappedClass;
import rip.firefly.compat.wrapper.asm.WrappedMethod;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;

public class CraftReflection {
    public static WrappedClass craftHumanEntity = Reflections.getCBClass("entity.CraftHumanEntity"); //1.7-1.14
    public static WrappedClass craftEntity = Reflections.getCBClass("entity.CraftEntity"); //1.7-1.14
    public static WrappedClass craftItemStack = Reflections.getCBClass("inventory.CraftItemStack"); //1.7-1.14
    public static WrappedClass craftBlock = Reflections.getCBClass("block.CraftBlock"); //1.7-1.14
    public static WrappedClass craftPlayer = Reflections.getCBClass("entity.CraftPlayer");
    public static WrappedClass craftWorld = Reflections.getCBClass("CraftWorld"); //1.7-1.14
    public static WrappedClass craftInventoryPlayer = Reflections.getCBClass("inventory.CraftInventoryPlayer"); //1.7-1.14
    public static WrappedClass craftServer = Reflections.getCBClass("CraftServer"); //1.7-1.14\
    public static WrappedClass craftChunk = Reflections.getCBClass("CraftChunk");
    public static WrappedClass craftMagicNumbers = Reflections.getCBClass("util.CraftMagicNumbers");
    public static WrappedClass craftChatMessage = Reflections.getCBClass("util.CraftChatMessage");

    //Vanilla Instances
    private static WrappedMethod itemStackInstance = craftItemStack.getMethod("asNMSCopy", ItemStack.class); //1.7-1.14
    private static WrappedMethod humanEntityInstance = craftHumanEntity.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod entityInstance = craftEntity.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod worldInstance = craftWorld.getMethod("getHandle"); //1.7-1.14
    private static WrappedMethod getInventory = craftInventoryPlayer.getMethod("getInventory"); //1.7-1.14
    private static WrappedMethod mcServerInstance = craftServer.getMethod("getServer"); //1.7-1.14
    private static WrappedMethod entityPlayerInstance = craftPlayer.getMethod("getHandle");
    private static WrappedMethod chunkInstance = craftChunk.getMethod("getHandle");
    private static WrappedMethod methodGetBlockFromMaterial = ProtocolVersion.getGameVersion()
            .isOrAbove(ProtocolVersion.V1_13) ? craftMagicNumbers.getMethod("getBlock", Material.class)
            : craftMagicNumbers.getMethod("getBlock", int.class);
    private static WrappedMethod fromComponent;

    public static <T> T getVanillaItemStack(ItemStack stack) {
        return itemStackInstance.invoke(null, stack);
    }

    public static <T> T getEntityHuman(HumanEntity entity) {
        return humanEntityInstance.invoke(entity);
    }

    public static <T> T getEntity(Entity entity) {
        return entityInstance.invoke(entity);
    }

    public static <T> T getEntityPlayer(Player player) {
        return entityPlayerInstance.invoke(player);
    }

    public static <T> T getVanillaWorld(World world) {
        return worldInstance.invoke(world);
    }

    public static <T> T getVanillaInventory(Player player) {
        return getInventory.invoke(player.getInventory());
    }

    public static <T> T getMinecraftServer() {
        return mcServerInstance.invoke(Bukkit.getServer());
    }

    public static <T> T getVanillaChunk(Chunk chunk) {
        return chunkInstance.invoke(chunk);
    }

    public static <T> T getVanillaBlock(Material material) {
        if(ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_13)) {
            return methodGetBlockFromMaterial.invoke(null, material);
        } else {
            return methodGetBlockFromMaterial.invoke(null, material.getId());
        }
    }

    public static <V> ListenableFuture<V> addPostTickTask(Callable<V> callable) {
        ListenableFuture<V> result;
        try {
            Object serverClass = CraftReflection.getMinecraftServer().getClass().getMethod("getServer").invoke(CraftReflection.getMinecraftServer().getClass());
            result = (ListenableFuture<V>) serverClass.getClass().getMethod("a").invoke(serverClass, callable);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            result = null;
        }
        return result;
    }
}