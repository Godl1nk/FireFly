package rip.firefly.packet.tinyprotocol.packet.in;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflection;

public class WrappedIn13KeepAlive extends NMSObject {
    private static final String packet = Client.KEEP_ALIVE;
    @Getter
    private long ping;
    private FieldAccessor<Long> pingField = Reflection.getFieldSafe(packet, long.class, 0);

    public WrappedIn13KeepAlive(Object object, Player player) {
        super(object, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        ping = fetch(pingField);
    }
}
