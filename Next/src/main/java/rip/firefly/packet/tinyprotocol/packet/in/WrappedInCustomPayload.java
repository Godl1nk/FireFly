package rip.firefly.packet.tinyprotocol.packet.in;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;

@Getter
public class WrappedInCustomPayload extends NMSObject {

    private static final String packet = Client.CUSTOM_PAYLOAD;

    public WrappedInCustomPayload(Object object, Player player) {
        super(object, player);
    }

    private static FieldAccessor<String> messageAccessor = fetchField(packet, String.class, 0);

    private static FieldAccessor<String> channelAccessor = fetchFieldByName(packet, "a", String.class);

    private String message;
    private String channel;

    @Override
    public void process(Player player, ProtocolVersion version) {
        message = fetch(messageAccessor);
        channel = fetch(channelAccessor);
    }
}
