package rip.firefly.packet.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class PacketSendEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private Player player;

    @Setter
    private Object packet;
    @Setter
    private boolean cancelled;

    private String type;
    private long timeStamp;

    public PacketSendEvent(Player player, Object packet, String type) {
        this.player = player;
        this.packet = packet;
        this.type = type;

        timeStamp = System.currentTimeMillis();
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}