package ac.firefly.events.packet;


import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketUseEntityEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public EnumWrappers.EntityUseAction action;
    public Player player;
    public Entity entity;
    public PacketContainer packet;

    public PacketUseEntityEvent(EnumWrappers.EntityUseAction action, Player player, Entity entity, PacketContainer packet) {
        this.action = action;
        this.player = player;
        this.entity = entity;
        this.packet = packet;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public EnumWrappers.EntityUseAction getAction() {
        return this.action;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public PacketContainer getPacket() {
        return packet;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}