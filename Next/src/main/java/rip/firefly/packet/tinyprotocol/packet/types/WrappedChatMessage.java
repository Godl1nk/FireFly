package rip.firefly.packet.tinyprotocol.packet.types;

import lombok.Getter;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.ReflectionsUtil;

@Getter
public class WrappedChatMessage extends NMSObject {
    private static String type = Type.CHATMESSAGE;

    private String chatMessage;
    private Object[] objects;

    private static FieldAccessor<String> messageField = fetchField(type, String.class, 0);
    private static FieldAccessor<Object[]> objectsField = fetchField(type, Object[].class, 0);

    public WrappedChatMessage(String chatMessage, Object... object) {
        this.chatMessage = chatMessage;
        this.objects = object;
    }

    public WrappedChatMessage(String chatMessage) {
        this(chatMessage, new Object[]{});
    }

    public void setPacket(String packet, Object... args) {
        Class<?> chatMsgClass = ReflectionsUtil.getClass(type);

        Object o = ReflectionsUtil.newInstance(chatMsgClass, packet, args);

        setObject(o);
    }

    public WrappedChatMessage(Object object) {
        super(object);

        chatMessage = fetch(messageField);
        objects = fetch(objectsField);
    }
}
