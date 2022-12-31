package rip.firefly.packet.tinyprotocol.packet.handshake;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflection;

public class WrappedHandshakeClientProtocol extends NMSObject {
    private static final String packet = Handshake.HANDSHAKE_PROTOCOL;
    @Getter private int protocolVersion;
    @Getter private String serverAddressOrIp;
    private FieldAccessor<Integer> protocolVersionField = Reflection.getField(packet, int.class, 0);
    private FieldAccessor<String> serverAddressOrIpField = Reflection.getFieldSafe(packet, String.class, 0);

    public WrappedHandshakeClientProtocol(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        protocolVersion = fetch(protocolVersionField);
        serverAddressOrIp = fetch(serverAddressOrIpField);
    }
}
