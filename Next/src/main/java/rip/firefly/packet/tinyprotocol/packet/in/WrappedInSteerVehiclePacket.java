package rip.firefly.packet.tinyprotocol.packet.in;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;

@Getter
public class WrappedInSteerVehiclePacket extends NMSObject {
    private static final String packet = Client.STEER_VEHICLE;

    // Fields

    // Decoded data


    public WrappedInSteerVehiclePacket(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {

    }
}
