package rip.firefly.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class API {
    /**
     * Bans A Player Without A Broadcast
     * @param player Target
     */
    public static void silentBan(Player player) {

    }

    /**
     * Bans A Player
     * @param player Target
     */
    public static void banPlayer(Player player) {

    }

    /**
     * Sets A Player's Exempt Status
     * @param player Player To Set Exempt
     * @param state Defines What To Set The Exempt Status To
     */
    public static void setExempt(Player player, boolean state) {

    }

    /**
     * Gets Player's Last Attack Packet
     * @param player Target
     * @return Player Last Attack Packet
     */
    public static long getLastAttackPacket(Player player) {
        return 1L;
    }

    /**
     * Gets Player's Last Flying Packet
     * @param player Target
     * @return Player Last Flying Packet
     */
    public static long getLastFlyingPacket(Player player) {
        return 1L;
    }

    /**
     * Gets Player's Mouse Sensitivity
     * @param player Target
     * @return Player Mouse Sensitivity
     */
    public static long getSensitivity(Player player) {
        return 1L;
    }

    /**
     * Gets Player's Ping According To KeepAlive Packets
     * @param player Target
     * @return Player Ping
     */
    public static long getPing(Player player) {
        return 1L;
    }

    /**
     * Gets Player's Ping According To NMS
     * @param player Target
     * @return Player Ping
     */
    public static int getNMSPing(Player player) {
        return 0;
    }

    /**
     * Gets Player's Motion X According To NMS
     * @param player Target
     * @return Player Motion X
     */
    public static double getNMSMotionX(Player player) {
        return 0D;
    }

    /**
     * Gets Player's Motion Y According To NMS
     * @param player Target
     * @return Player Motion Y
     */
    public static double getNMSMotionY(Player player) {
        return 0D;
    }

    /**
     * Gets Player's Motion Z According To NMS
     * @param player Target
     * @return Player Motion Z
     */
    public static double getNMSMotionZ(Player player) {
        return 0D;
    }

    /**
     * Gets Player's Version According To NMS
     * ie: 1.8.8 is 47
     * @param player Target
     * @return Player Version
     */
    public static int getNMSProtocolVersion(Player player) {
        return 0;
    }

    /**
     * Gets Player's To Location
     * @param player Target
     * @return Player To Location
     */
    public static Location getTo(Player player) {
        return null;
    }

    /**
     * Gets Player's From Location
     * @param player Target
     * @return Player From Location
     */
    public static Location getFrom(Player player) {
        return null;
    }

    /**
     * Reloads FireFly's Configuration
     */
    public static void reloadConfig() {

    }

    /**
     * Runs The Java Garbage Cleaner
     */
    public static void gc() {

    }

    /**
     * Sets A Player's Version
     * ie: 1.8.8 is v1_8_R3
     * @param player Target To Get Version
     */

    public static String getVersion(Player player) {
        return "";
    }

    /**
     * Sets A Player's Alerts
     * @param player Player To Set Alerts
     * @param state Defines What To Set The Alerts To
     */
    public static void setAlerts(Player player, boolean state) {

    }
}
