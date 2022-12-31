package rip.firefly.util.bounding;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.ReflectionsUtil;

import java.util.ArrayList;
import java.util.List;

public class RBoundingBox {

    public float minX, minY, minZ, maxX, maxY, maxZ;

    public RBoundingBox(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public RBoundingBox(Vector min, Vector max) {
        this.minX = (float) Math.min(min.getX(), max.getX());
        this.minY = (float) Math.min(min.getY(), max.getY());
        this.minZ = (float) Math.min(min.getZ(), max.getZ());
        this.maxX = (float) Math.max(min.getX(), max.getX());
        this.maxY = (float) Math.max(min.getY(), max.getY());
        this.maxZ = (float) Math.max(min.getZ(), max.getZ());
    }

    public RBoundingBox add(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new RBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public RBoundingBox add(Vector vector) {
        float x = (float) vector.getX(), y = (float) vector.getY(), z = (float) vector.getZ();

        float newMinX = minX + x;
        float newMaxX = maxX + x;
        float newMinY = minY + y;
        float newMaxY = maxY + y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ + z;

        return new RBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public RBoundingBox expandMax(double x, double y, double z) {
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public RBoundingBox expand(double x, double y, double z) {
        this.minX -= x;
        this.minY -= y;
        this.minZ -= z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public RBoundingBox addXYZ(double x, double y, double z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
        return this;
    }

    public RBoundingBox grow(float x, float y, float z) {
        float newMinX = minX - x;
        float newMaxX = maxX + x;
        float newMinY = minY - y;
        float newMaxY = maxY + y;
        float newMinZ = minZ - z;
        float newMaxZ = maxZ + z;

        return new RBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public RBoundingBox shrink(float x, float y, float z) {
        float newMinX = minX + x;
        float newMaxX = maxX - x;
        float newMinY = minY + y;
        float newMaxY = maxY - y;
        float newMinZ = minZ + z;
        float newMaxZ = maxZ - z;

        return new RBoundingBox(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public RBoundingBox add(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new RBoundingBox(this.minX + minX, this.minY + minY, this.minZ + minZ, this.maxX + maxX, this.maxY + maxY, this.maxZ + maxZ);
    }

    public RBoundingBox subtract(float minX, float minY, float minZ, float maxX, float maxY, float maxZ) {
        return new RBoundingBox(this.minX - minX, this.minY - minY, this.minZ - minZ, this.maxX - maxX, this.maxY - maxY, this.maxZ - maxZ);
    }

    public boolean intersectsWithBox(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public List<CollideEntry> getCollidedBlocks(Player player) {
        //  player.sendMessage("" + player.getLocation().getBlock().getType().name());

        List<CollideEntry> toReturn = new ArrayList<>();
        int minX = floor(this.minX);
        int maxX = floor(this.maxX + 1);
        int minY = floor(this.minY);
        int maxY = floor(this.maxY + 1);
        int minZ = floor(this.minZ);
        int maxZ = floor(this.maxZ + 1);

        for (double x = minX; x < maxX; x++) {
            for (double z = minZ; z < maxZ; z++) {
                for (double y = minY - 1; y < maxY; y++) {
                    toReturn.add(new CollideEntry(getBlock(new Location(player.getWorld(), x, y, z)),
                            this));
                }
            }
        }

        return toReturn;
    }

    public Vector getMinimum() {
        return new Vector(minX, minY, minZ);
    }

    public Vector getMaximum() {
        return new Vector(maxX, maxY, maxZ);
    }

    public static boolean isChunkLoaded(Location location) {
        return (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4));
    }

    public static Block getBlock(Location location) {
        if (location.getWorld().isChunkLoaded(location.getBlockX() >> 4, location.getBlockZ() >> 4)) {
            return location.getBlock();
        } else {
            return null;
        }
    }

    public static Block getBlockNoChunk(Location location) {
        return location.getBlock();
    }

    public List<Block> getAllBlocks(Player player) {
        Location min = new Location(player.getWorld(), floor(minX), floor(minY), floor(minZ));
        Location max = new Location(player.getWorld(), floor(maxX), floor(maxY), floor(maxZ));
        List<Block> all = new ArrayList<>();
        for (float x = (float) min.getX(); x < max.getX(); x++) {
            for (float y = (float) min.getY(); y < max.getY(); y++) {
                for (float z = (float) min.getZ(); z < max.getZ(); z++) {

                    Block block = getBlock(new Location(player.getWorld(), x, y, z));

                    assert block != null;
                    if (!block.getType().equals(Material.AIR)) {
                        all.add(block);
                    }
                }
            }
        }
        return all;
    }

    public static int floor(double var0) {
        int var2 = (int) var0;
        return var0 < var2 ? var2 - 1 : var2;
    }

    public boolean intersectsWithBox(Object other) {
        if (other instanceof RBoundingBox) {
            RBoundingBox otherBox = (RBoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY > this.minY && otherBox.minY < this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMaxX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMinZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public boolean collides(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }

    public boolean collides(Object other) {
        if (other instanceof RBoundingBox) {
            RBoundingBox otherBox = (RBoundingBox) other;
            return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesHorizontally(Vector vector) {
        return (vector.getX() >= this.minX && vector.getX() <= this.maxX) && ((vector.getY() > this.minY && vector.getY() < this.maxY) && (vector.getZ() >= this.minZ && vector.getZ() <= this.maxZ));
    }



    public boolean b(RBoundingBox var1) {
        if (var1.minX > this.maxX && var1.minX < this.minX) {
            if (var1.minZ > this.maxZ && var1.minZ < this.maxZ) {
                return var1.minY > this.maxY && var1.minY < this.maxY;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

//    public RBoundingBox a(BlockPosition blockposition) {
//        return new RBoundingBox(blockposition.getX() + this.minX,
//                blockposition.getY() + this.minY, blockposition.getZ()
//                + this.minZ, blockposition.getX() + this.maxX, blockposition.getY()
//                + this.maxY, blockposition.getZ() + this.maxZ);
//    }
//
//
//    public RBoundingBox expandWithBlock(BlockPosition blockposition, IBlockData iblockdata) {
//        return new RBoundingBox(blockposition.getX() + this.minX, blockposition.getY() + this.minY, blockposition.getZ() + this.minZ,
//                blockposition.getX() + this.maxX, blockposition.getY() + this.maxY, blockposition.getZ() + this.maxZ);
//    }

    public boolean collidesHorizontally(Object other) {
        if (other instanceof RBoundingBox) {
            RBoundingBox otherBox = (RBoundingBox) other;
            return otherBox.maxX >= this.minX
                    && otherBox.minX <= this.maxX
                    && otherBox.maxY > this.minY
                    && otherBox.minY < this.maxY
                    && otherBox.maxZ >= this.minZ
                    && otherBox.minZ <= this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX >= minX && otherMinX <= maxX && otherMaxY > minY && otherMinY < maxY && otherMaxZ >= minZ && otherMinZ <= maxZ;
        }
    }

    public boolean collidesHorizontally(Object other, double addX, double addY, double addZ) {
        RBoundingBox otherBox = (RBoundingBox) other;

        return otherBox.maxX >= this.minX && otherBox.minX <= this.maxX
                && otherBox.maxZ >= this.minZ && otherBox.minZ <= this.maxZ;
    }

    public boolean collidesVertically(Vector vector) {
        return (vector.getX() > this.minX && vector.getX() < this.maxX) && ((vector.getY() >= this.minY && vector.getY() <= this.maxY) && (vector.getZ() > this.minZ && vector.getZ() < this.maxZ));
    }

    public boolean collidesVertically(Object other) {
        if (other instanceof RBoundingBox) {
            RBoundingBox otherBox = (RBoundingBox) other;
            return otherBox.maxX > this.minX && otherBox.minX < this.maxX && otherBox.maxY >= this.minY && otherBox.minY <= this.maxY && otherBox.maxZ > this.minZ && otherBox.minZ < this.maxZ;
        } else {
            float otherMinX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "a"), other);
            float otherMinY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "b"), other);
            float otherMinZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "c"), other);
            float otherMaxX = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "d"), other);
            float otherMaxY = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "e"), other);
            float otherMaxZ = (float) (double) ReflectionsUtil.getFieldValue(ReflectionsUtil.getFieldByName(other.getClass(), "f"), other);
            return otherMaxX > minX && otherMinX < maxX && otherMaxY >= minY && otherMinY <= maxY && otherMaxZ > minZ && otherMinZ < maxZ;
        }
    }

    public Object toAxisAlignedBB() {
        return ReflectionsUtil.newAxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }



    public String toString() {
        return "[" + minX + ", " + minY + ", " + minZ + ", " + maxX + ", " + maxY + ", " + maxZ + "]";
    }
}
