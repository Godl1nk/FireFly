package rip.firefly.api;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import rip.firefly.FireFly;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.compat.wrapper.player.WrappedPlayer;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;

public class FireFlyAPI {
    public static void silentBan(Player player) {
        Bukkit.getScheduler().runTask(FireFly.getInstance().getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.settings.getConfiguration().getString("banCommand").replace("$player", player.getPlayer().getName())));

        DataManager.getData(player).resetVl();
        BridgeData.totalBans++;
    }

    public static void banPlayer(Player player) {
        Bukkit.getScheduler().runTask(FireFly.getInstance().getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.settings.getConfiguration().getString("banCommand").replace("$player", player.getPlayer().getName())));

        for (String string : ConfigManager.messages.getConfiguration().getStringList("banBroadcast")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', string.replace("$player", player.getName())));
        }

        DataManager.getData(player).resetVl();
        BridgeData.totalBans++;
    }

    public static void setExempt(Player player, boolean state) {
        PlayerData data = DataManager.getData(player);
        data.setExempt(state);
    }

    public static long getLastAttackPacket(Player player) {
        PlayerData data = DataManager.getData(player);
        return data.getLastAttack();
    }

    public static long getLastFlyingPacket(Player player) {
        PlayerData data = DataManager.getData(player);
        return data.getLastFlying();
    }

    public static long getSensativity(Player player) {
        PlayerData data = DataManager.getData(player);
        return Math.round(data.getSensitivity() * 200.0);
    }

    public static long getPing(Player player) {
        PlayerData data = DataManager.getData(player);
        return data.getPing();
    }

    public static int getNMSPing(Player player) {
        return new WrappedPlayer(player).getPing();
    }

    public static double getNMSMotionX(Player player) {
        return new WrappedPlayer(player).getMotionX();
    }

    public static double getNMSMotionY(Player player) {
        return new WrappedPlayer(player).getMotionY();
    }

    public static double getNMSMotionZ(Player player) {
        return new WrappedPlayer(player).getMotionZ();
    }

    public static int getNMSProtocolVersion(Player player) {
        return new WrappedPlayer(player).getProtocolVersion();
    }

    public static Location getTo(Player player) {
        return DataManager.getData(player).getTo().toBukkit(player.getWorld());
    }

    public static Location getFrom(Player player) {
        return DataManager.getData(player).getFrom().toBukkit(player.getWorld());
    }

    public static void reloadConfig() {
        ConfigManager.messages.reloadConfig();
        ConfigManager.settings.reloadConfig();
    }

    public static void gc() {
        System.gc();
        Runtime.getRuntime().gc();
    }

    public static String getVersion(Player player) {
        PlayerData data = DataManager.getData(player);
        return data.getPlayerVersion().getServerVersion();
    }

    public static void setAlerts(Player player, boolean state) {
        PlayerData data = DataManager.getData(player);
        data.setAlerts(state);
    }
}
