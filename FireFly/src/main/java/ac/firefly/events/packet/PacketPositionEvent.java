package ac.firefly.events.packet;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketPositionEvent
        extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;

    public PacketPositionEvent(Player player, double x, double y, double z, float yaw, float pitch) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
