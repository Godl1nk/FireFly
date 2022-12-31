package rip.firefly.packet.tinyprotocol.packet.in;

import lombok.Getter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.ReflectionsUtil;
import rip.firefly.packet.tinyprotocol.packet.types.Vec3D;

import java.util.List;
import java.util.Objects;

@Getter
public class WrappedInUseEntityPacket extends NMSObject {
    private static String packet = Client.USE_ENTITY;

    private static FieldAccessor<Integer> fieldId = fetchField(packet, int.class, 0);
    private static FieldAccessor<Enum> fieldAction = fetchField(packet, Enum.class, 0);

    private static FieldAccessor<Object> fieldBody = WrappedInUseEntityPacket.fetchField(packet, Object.class, 0);
    private int id;
    private EnumEntityUseAction action;
    private Vec3D body;
    private Entity entity;

    public WrappedInUseEntityPacket(Object packet, Player player) {
        super(packet, player);
    }

    @Override
    public void process(Player player, ProtocolVersion version) {
        id = Objects.requireNonNull(fetch(fieldId));
        Enum fieldAct = Objects.nonNull(fetch(fieldAction)) ? fetch(fieldAction) : null;
        action = fieldAct == null ? EnumEntityUseAction.INTERACT_AT : EnumEntityUseAction.valueOf(fieldAct.name());

        List<Entity> entities = ReflectionsUtil.getEntitiesInWorld(player.getWorld());

        for (Entity ent : entities) {
            if(id == ent.getEntityId()) {
                entity = ent;
                break;
            }
        }

        if (this.action == EnumEntityUseAction.INTERACT_AT) {
            if (ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_9)) {
                fieldBody = WrappedInUseEntityPacket.fetchField(packet, Object.class, 1);
            } else if (ProtocolVersion.getGameVersion().isOrAbove(ProtocolVersion.V1_9)) {
                fieldBody = WrappedInUseEntityPacket.fetchField(packet, Object.class, 2);
            }
            this.body = new Vec3D(this.fetch(fieldBody));
        }else {
            this.body = new Vec3D(0.0, 0.0, 0.0);
        }
    }




    public enum EnumEntityUseAction {
        INTERACT("INTERACT"),
        ATTACK("ATTACK"),
        INTERACT_AT("INTERACT_AT");

        @Getter
        private String name;

        EnumEntityUseAction(String name) {
            this.name = name;
        }
    }
}
