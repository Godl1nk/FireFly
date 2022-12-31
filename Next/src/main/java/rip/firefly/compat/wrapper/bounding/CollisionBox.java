package rip.firefly.compat.wrapper.bounding;

import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.packet.types.WrappedEnumParticle;

import java.util.Collection;
import java.util.List;

public interface CollisionBox {
    boolean isCollided(CollisionBox other);
    boolean isIntersected(CollisionBox other);
    void draw(WrappedEnumParticle particle, Collection<? extends Player> players);
    CollisionBox copy();
    CollisionBox offset(double x, double y, double z);
    CollisionBox shrink(double x, double y, double z);
    CollisionBox expand(double x, double y, double z);
    void downCast(List<SimpleCollisionBox> list);
    boolean isNull();
}