package rip.firefly.util.bounding;

import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.packet.types.WrappedEnumParticle;

import java.util.Collection;
import java.util.List;

public interface RCollisionBox {
    boolean isCollided(RCollisionBox other);
    RCollisionBox copy();
    RCollisionBox offset(double x, double y, double z);
    void downCast(List<SRayCBox> list);
    boolean isNull();
}