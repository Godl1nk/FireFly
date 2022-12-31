package rip.firefly.packet.tinyprotocol.packet.in;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;

@Getter
public class WrappedInHeldItemSlotPacket extends NMSObject {
    private static final String packet = Client.HELD_ITEM;

    // Fields
    private static FieldAccessor<Integer> fieldHeldSlot = fetchField(packet, int.class, 0);

    // Decoded data
    private int slot;


    public WrappedInHeldItemSlotPacket(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        slot = fetch(fieldHeldSlot);
    }
}
