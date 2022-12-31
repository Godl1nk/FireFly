package rip.firefly.compat.wrapper.player;

import com.viaversion.viaversion.api.Via;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class WrappedPlayer {

    public UUID uuid;
    public String username;
    public Player player;

    public WrappedPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.username = player.getName();
    }

    public int getPing() {
        int ping;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            ping = (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            ping = 0;
        }
        return ping;
    }

    public boolean isOnGround() {
        boolean onGround;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            onGround = (boolean) entityPlayer.getClass().getField("onGround").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            onGround = false;
        }
        return onGround;
    }

    public double getMotionY() {
        double motionY;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            motionY = (double) entityPlayer.getClass().getField("motY").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            motionY = 0;
        }
        return motionY * -1;
    }

    public double getMotionX() {
        double motionX;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            motionX = (double) entityPlayer.getClass().getField("motX").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            motionX = 0;
        }
        return motionX;
    }

    public double getMotionZ() {
        double motionZ;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            motionZ = (double) entityPlayer.getClass().getField("motZ").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            motionZ = 0;
        }
        return motionZ;
    }

    public boolean isInWater() {
        boolean inWater;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            inWater = (boolean) entityPlayer.getClass().getField("inWater").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            inWater = false;
        }
        return inWater;
    }

    public double getX() {
        double x;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            x = (double) entityPlayer.getClass().getField("locX").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            x = 0;
        }
        return x;
    }

    public double getY() {
        double y;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            y = (double) entityPlayer.getClass().getField("locY").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            y = 0;
        }
        return y;
    }

    public double getZ() {
        double z;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            z = (double) entityPlayer.getClass().getField("locZ").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            z = 0;
        }
        return z;
    }

    public float getFallDistance() {
        float z;
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            z = (float) entityPlayer.getClass().getField("fallDistance").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            z = 0;
        }
        return z;
    }

    public boolean isFalling() {
        if(isFallingMotion() || isFallingDistance()){
            return true;
        } else {
            return false;
        }
    }

    public boolean isFallingMotion() {
        if(getMotionY() > 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean isFallingDistance() {
        if(player.getFallDistance() > 0.0F) {
            return true;
        } else {
            return false;
        }
    }

    public int getProtocolVersion() {
        if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            return Via.getAPI().getPlayerVersion(player.getUniqueId());
        } else {
            int protocolVersion;
            try {
                Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
                Object networkManager = entityPlayer.getClass().getField("networkManager").get(playerConnection);
                protocolVersion = (int) player.getClass().getMethod("getVersion").invoke(networkManager);


            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                protocolVersion = 0;
            }
            return protocolVersion;
        }
    }


}