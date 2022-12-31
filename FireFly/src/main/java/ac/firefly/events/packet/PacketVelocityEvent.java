package ac.firefly.events.packet;


import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketVelocityEvent
        extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private double velocityX;
    private double velocityY;
    private double velocityZ;

    public PacketVelocityEvent(Player player, double velocityX, double velocityY, double velocityZ) {
        this.player = player;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return this.player;
    }


    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public double getVelocityZ() {
        return velocityZ;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
