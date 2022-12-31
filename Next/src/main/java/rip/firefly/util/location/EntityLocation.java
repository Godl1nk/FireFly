package rip.firefly.util.location;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import rip.firefly.compat.wrapper.math.MathHelper;
import rip.firefly.util.misc.EvictingList;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
//Duplicate class for obfuscation purposes
public class EntityLocation {
    public final Entity entity;
    public double newX, newY, newZ, x, y, z;
    public float newYaw, newPitch, yaw, pitch;
    public int increment = 0;
    public boolean sentTeleport = false;
    public PlayerLocation oldLocation, location;
    public List<PlayerLocation> oldLocations = new EvictingList<>(3),
            interpolatedLocations = new EvictingList<>(3);

    public void interpolateLocations() {
        increment = 3;
        interpolatedLocations.clear();
        while(increment > 0) {
            double d0 = x + (newX - x) / increment;
            double d1 = y + (newY - y) / increment;
            double d2 = z + (newZ - z) / increment;
            double d3 = MathHelper.wrapAngleTo180_float(newYaw - yaw);

            yaw = (float) ((double) yaw + d3 / (double) increment);
            pitch = (float) ((double) pitch + (newPitch - (double) pitch) / (double) increment);

            increment--;

            this.x = d0;
            this.y = d1;
            this.z = d2;
            interpolatedLocations.add(new PlayerLocation(x, y, z, yaw, pitch));
        }
    }

    public List<PlayerLocation> getInterpolatedLocations() {
        int increment = 3;
        double x = this.x, y = this.y, z = this.z, newX = this.newX, newY = this.newY, newZ = this.newZ;
        float yaw = this.yaw, pitch = this.pitch, newYaw = this.newYaw, newPitch = this.newPitch;
        List<PlayerLocation> locations = new ArrayList<>();
        while(increment > 0) {
            double d0 = x + (newX - x) / increment;
            double d1 = y + (newY - y) / increment;
            double d2 = z + (newZ - z) / increment;
            double d3 = MathHelper.wrapAngleTo180_float(newYaw - yaw);

            yaw = (float) ((double) yaw + d3 / (double) increment);
            pitch = (float) ((double) pitch + (newPitch - (double) pitch) / (double) increment);

            increment--;

            x = d0;
            y = d1;
            z = d2;
            locations.add(new PlayerLocation(x, y, z, yaw, pitch));
        }

        return locations;
    }

    public void interpolateLocation() {
        oldLocation = new PlayerLocation(x, y, z, yaw, pitch);
        oldLocations.add(oldLocation);
        if(increment > 0) {
            double d0 = x + (newX - x) / increment;
            double d1 = y + (newY - y) / increment;
            double d2 = z + (newZ - z) / increment;
            double d3 = MathHelper.wrapAngleTo180_float(newYaw - yaw);

            yaw = (float) ((double) yaw + d3 / (double) increment);
            pitch = (float) ((double) pitch + (newPitch - (double) pitch) / (double) increment);

            increment--;

            this.x = d0;
            this.y = d1;
            this.z = d2;
            interpolatedLocations.add(new PlayerLocation(x, y, z, yaw, pitch));
        }
    }

    public EntityLocation clone() {
        final EntityLocation loc = new EntityLocation(entity);

        loc.x = x;
        loc.y = y;
        loc.z = z;
        loc.yaw = yaw;
        loc.pitch = pitch;
        loc.increment = increment;
        loc.newX = newX;
        loc.newY = newY;
        loc.newZ = newZ;
        loc.newYaw = newYaw;
        loc.newPitch = newPitch;
        loc.sentTeleport = sentTeleport;
        loc.interpolatedLocations.addAll(interpolatedLocations);

        return loc;
    }
}