package rip.firefly.util.player;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;
import rip.firefly.compat.reflect.CraftReflection;
import rip.firefly.compat.reflect.Reflections;
import rip.firefly.compat.wrapper.asm.WrappedClass;
import rip.firefly.compat.wrapper.asm.WrappedField;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.util.bounding.RCollisionBox;
import rip.firefly.util.bounding.SRayCBox;
import rip.firefly.util.location.CustomLocation;
import rip.firefly.util.location.PlayerLocation;

import java.util.HashMap;
import java.util.Map;

public class EntityData {
    private static Map<EntityType, RCollisionBox> entityBounds = new HashMap<>();

    private static WrappedClass entity = Reflections.getNMSClass("Entity"), entitySize;
    private static WrappedField fieldWidth, fieldLength, fieldSize;

    public static RCollisionBox bounds(Entity entity) {
        return entityBounds.computeIfAbsent(entity.getType(), type -> {
            if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_14)) {
                Object ventity = CraftReflection.getEntity(entity);

                //We cast this as a float since the fields are floats.

                return new SRayCBox(new Vector(), (float)fieldWidth.get(ventity), (float)fieldLength.get(ventity));
            } else {
                return new SRayCBox(new Vector(), (float)fieldWidth.get(entity), (float)fieldLength.get(entity));
            }
        }).copy();
    }

    public static RCollisionBox getEntityBox(Location location, Entity entity) {
        return bounds(entity).offset(location.getX(), location.getY(), location.getZ());
    }

    public static RCollisionBox getEntityBox(Vector vector, Entity entity) {
        return bounds(entity).offset(vector.getX(), vector.getY(), vector.getZ());
    }


    public static RCollisionBox getEntityBox(PlayerLocation location, Entity entity) {
        return bounds(entity).offset(location.getX(), location.getY(), location.getZ());
    }

    public static RCollisionBox getEntityBox(CustomLocation location, Entity entity) {
        return bounds(entity).offset(location.getX(), location.getY(), location.getZ());
    }



    static {
        entityBounds.put(EntityType.WOLF, new SRayCBox(new Vector(), 0.62f, .8f));
        entityBounds.put(EntityType.SHEEP, new SRayCBox(new Vector(), 0.9f, 1.3f));
        entityBounds.put(EntityType.COW, new SRayCBox(new Vector(), 0.9f, 1.3f));
        entityBounds.put(EntityType.PIG, new SRayCBox(new Vector(), 0.9f, 0.9f));
        entityBounds.put(EntityType.MUSHROOM_COW, new SRayCBox(new Vector(), 0.9f, 1.3f));
        entityBounds.put(EntityType.WITCH, new SRayCBox(new Vector(), 0.62f, 1.95f));
        entityBounds.put(EntityType.BLAZE, new SRayCBox(new Vector(), 0.62f, 1.8f));
        entityBounds.put(EntityType.PLAYER, new SRayCBox(new Vector(), 0.6f, 1.8f));
        entityBounds.put(EntityType.VILLAGER, new SRayCBox(new Vector(), 0.62f, 1.8f));
        entityBounds.put(EntityType.CREEPER, new SRayCBox(new Vector(), 0.62f, 1.8f));
        entityBounds.put(EntityType.GIANT, new SRayCBox(new Vector(), 3.6f, 10.8f));
        entityBounds.put(EntityType.SKELETON, new SRayCBox(new Vector(), 0.62f, 1.8f));
        entityBounds.put(EntityType.ZOMBIE, new SRayCBox(new Vector(), 0.62f, 1.8f));
        entityBounds.put(EntityType.SNOWMAN, new SRayCBox(new Vector(), 0.7f, 1.9f));
        entityBounds.put(EntityType.HORSE, new SRayCBox(new Vector(), 1.4f, 1.6f));
        entityBounds.put(EntityType.ENDER_DRAGON, new SRayCBox(new Vector(), 3f, 1.5f));
        entityBounds.put(EntityType.ENDERMAN, new SRayCBox(new Vector(), 0.62f, 2.9f));
        entityBounds.put(EntityType.CHICKEN, new SRayCBox(new Vector(), 0.4f, 0.7f));
        entityBounds.put(EntityType.OCELOT, new SRayCBox(new Vector(), 0.62f, 0.7f));
        entityBounds.put(EntityType.SPIDER, new SRayCBox(new Vector(), 1.4f, 0.9f));
        entityBounds.put(EntityType.WITHER, new SRayCBox(new Vector(), 0.9f, 3.5f));
        entityBounds.put(EntityType.IRON_GOLEM, new SRayCBox(new Vector(), 1.4f, 2.9f));
        entityBounds.put(EntityType.GHAST, new SRayCBox(new Vector(), 4f, 4f));

        if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_14)) {
            fieldWidth = entity.getFieldByName("width");
            fieldLength = entity.getFieldByName("length");
        } else {
            entitySize = Reflections.getNMSClass("EntitySize");
            fieldWidth = entitySize.getFieldByName("width");
            fieldLength = entitySize.getFieldByName("length");
            fieldSize = entity.getFieldByType(entitySize.getParent(), 0);
        }
    }
}