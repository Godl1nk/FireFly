package rip.firefly.util.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import rip.firefly.util.bounding.BoundingBox;


@AllArgsConstructor
@Getter
@Setter
public class PlayerLocation {
    public long timestamp;
    public double x, y, z;
    public float yaw, pitch;
    public boolean onGround;

    public PlayerLocation(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.timestamp = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public PlayerLocation(double x, double y, double z, float yaw, float pitch) {
        this.timestamp = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public PlayerLocation(double x, double y, double z) {
        new PlayerLocation(x, y, z, 0.0F, 0.0F, false);
    }

    public PlayerLocation() {
        new PlayerLocation(0, 0, 0, 0f, 0f, false);
    }

    public PlayerLocation(Location location) {
        this.timestamp = System.currentTimeMillis();
        this.x = location.clone().getX();
        this.y = location.clone().getY();
        this.z = location.clone().getZ();
        this.yaw = location.clone().getYaw();
        this.pitch = location.clone().getPitch();
    }

    public PlayerLocation(double x, double y, double z, float yaw, float pitch, long timestamp) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location toBukkit(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }

    public BoundingBox toBoundingBox(float width, float height) {
        float f = width / 2.0F;

        return new BoundingBox(
                x - width,
                y,
                z - width,
                x + width,
                y + height,
                z + width
        );
    }

    public PlayerLocation clone() {
        return new PlayerLocation(x, y, z, yaw, pitch, onGround);
    }

    public void set(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        this.timestamp = System.currentTimeMillis();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    public void set(PlayerLocation location) {
        this.timestamp = System.currentTimeMillis();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.onGround = location.isOnGround();
    }

    public double distance(PlayerLocation other) {
        double dx = x - other.getX();
        double dy = y - other.getY();
        double dz = z - other.getZ();

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    public double squareDistance(PlayerLocation other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double dz = z - other.z;

        return dx * dx + dy * dy + dz * dz;
    }

    public Vector toVector() {
        return new Vector(x, y, z);
    }

    public PlayerLocation add(PlayerLocation vec) {
        if (vec != null) {
            this.x += vec.x;
            this.y += vec.y;
            this.z += vec.z;
            return this;
        } else {
            throw new IllegalArgumentException("Vector Can Not Be Null!");
        }
    }

    public PlayerLocation add(Vector vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }

    public PlayerLocation add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public PlayerLocation subtract(PlayerLocation vec) {
        if (vec != null) {
            this.x -= vec.x;
            this.y -= vec.y;
            this.z -= vec.z;
            return this;
        } else {
            throw new IllegalArgumentException("Vector Can Not Be Null!");
        }
    }

    public PlayerLocation subtract(Vector vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }

    public PlayerLocation subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Block getBlock(Player p) {
        return p.getWorld().getBlockAt(toBukkit(p.getWorld()));
    }
}