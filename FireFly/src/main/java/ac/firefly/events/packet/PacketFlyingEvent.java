package ac.firefly.events.packet;


import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketFlyingEvent
        extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private boolean look;
    private PacketContainer packet;

    public PacketFlyingEvent(PacketContainer packet, Player player, double x, double y, double z, float yaw, float pitch, boolean look) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.look = look;
        this.packet = packet;
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

    public boolean isLook() {
        return look;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public PacketContainer getPacket() {
        return packet;
    }
}
