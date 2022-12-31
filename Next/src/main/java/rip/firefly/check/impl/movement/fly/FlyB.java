package rip.firefly.check.impl.movement.fly;

import com.google.common.math.DoubleMath;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutPositionPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.player.PlayerUtil;
import rip.firefly.util.velocity.VelocityUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@CheckData(name = "Fly", subType = "B", description = "Checks If A Player Is Moving Vertically", type = CheckType.MOVEMENT, threshold = 6)
public class FlyB extends PacketCheck {

    public static final Set<Material> invalidMaterials = new HashSet<>();

    static {
        invalidMaterials.add(Material.AIR);
        invalidMaterials.add(Material.LONG_GRASS);
        invalidMaterials.add(Material.YELLOW_FLOWER);
        invalidMaterials.add(Material.RED_ROSE);
        invalidMaterials.add(Material.DOUBLE_PLANT);
    }

    private double lastDeltaY;
    private int streak;

    public FlyB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket update = (WrappedInFlyingPacket)packet;
            if (update.isPos()) {
                if(playerData.getTeleportTicks() > 0
                        || Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk() == null
                        || !Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk().isLoaded()
                ) return;

                //   Bukkit.getScheduler().scheduleSyncDelayedTask(FireFly.getInstance().getPlugin(), () -> {
                double deltaY = playerData.getTo().getY() - playerData.getFrom().getY();
                if (deltaY != 0.0 && deltaY == this.lastDeltaY && deltaY > -3.92 && !DoubleMath.fuzzyEquals(deltaY, -0.098, 1.0E-5) && !Bukkit.getPlayer(playerData.getUuid()).isFlying() && Bukkit.getPlayer(playerData.getUuid()).getGameMode() != GameMode.CREATIVE && playerData.getVelocityManager().getVelocities().stream().noneMatch(v -> DoubleMath.fuzzyEquals(deltaY, v.getY(), 1.25E-4)) && this.isInAir(Bukkit.getPlayer(playerData.getUuid()).getWorld(), playerData.getTo())) {
                    if (++this.streak >= 2) {
                        boolean known = DoubleMath.fuzzyEquals(Math.abs(deltaY), 0.1, 1.0E-5) || DoubleMath.fuzzyEquals(deltaY, 0.06, 1.0E-5) || DoubleMath.fuzzyEquals(deltaY, -0.17, 1.0E-5) || DoubleMath.fuzzyEquals(Math.abs(deltaY), 0.04, 1.0E-5) || DoubleMath.fuzzyEquals(deltaY, 0.1176, 1.0E-5) || DoubleMath.fuzzyEquals(deltaY, -0.15, 1.0E-5);
                        this.flag(playerData, String.format("Y: %.5f S: %s K: %s", deltaY, this.streak, known));
                        if (this.streak == 20 && !known) {
                            this.punish(Bukkit.getPlayer(playerData.getUuid()));
                        }
                    }
                } else {
                    this.streak = 0;
                }
                this.lastDeltaY = deltaY;
                //   });
            }
        } else if(packet instanceof WrappedOutPositionPacket) {
            this.streak = 0;
        }
    }

    public Boolean isInAir(World world, PlayerLocation location) {
        int minX = (int)Math.floor(location.getX() - 0.3);
        int minY = (int)Math.floor(location.getY() - 0.5 - 1.0E-5);
        int minZ = (int)Math.floor(location.getZ() - 0.3);
        int maxX = (int)Math.floor(location.getX() + 0.3);
        int maxY = (int)Math.floor(location.getY() + 1.8);
        int maxZ = (int)Math.floor(location.getZ() + 0.3);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();
                    if (!world.isChunkLoaded(loc.getChunk())) {
                        return false;
                    }
                    if (invalidMaterials.contains(block.getType())) continue;
                    return false;
                }
            }
        }
        return true;
    }
}
