package rip.firefly.check.impl.movement.speed;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.firefly.FireFly;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.VelocityManager;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Comparator;

@CheckData(name = "Speed", subType = "A", description = "Checks For A Player Going Over The Speed Limit", type = CheckType.MOVEMENT, threshold = 7)
public class SpeedA extends MovementCheck {

    private int invalidTicks = 0;
    private int vInvalidTicks = 0;
    private int invalidStage = 0;
    private int speedAmplifier = 0;
    private int speedTicks = 0;

    public SpeedA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if (update.isPositionUpdate()) {
            if(System.currentTimeMillis() - playerData.getLastJoin() < 1000
                    || playerData.getTeleportTicks() > 0
                    || System.currentTimeMillis() - playerData.getLastTeleport() > 2500
                    || Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk() == null
                    || !Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk().isLoaded()
            ) return;
        //    VelocityManager.VelocitySnapshot velocityMax = playerData.getVelocityManager().getVelocities().stream().max(Comparator.comparing(VelocityManager.VelocitySnapshot::getXz)).orElse(null);
            double velocityMax = playerData.getVelocityManager().getMaxHorizontal();
            // Bukkit.getScheduler().scheduleSyncDelayedTask(FireFly.getInstance().getPlugin(), () -> {
                int currentSpeedAmplifier;
                double deltaXZ = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
                double deltaY = update.getTo().getY() - update.getFrom().getY();
                PotionEffect speedEffect = Bukkit.getPlayer(playerData.getUuid()).getActivePotionEffects().stream().filter(p -> p.getType().equals(PotionEffectType.SPEED)).findFirst().orElse(null);
                int n = currentSpeedAmplifier = speedEffect == null ? 0 : speedEffect.getAmplifier() + 1;
                if (currentSpeedAmplifier >= speedAmplifier) {
                    speedAmplifier = currentSpeedAmplifier;
                    speedTicks = 200;
                } else if (speedTicks > 0 && --speedTicks == 0) {
                    speedAmplifier = currentSpeedAmplifier;
                }
                if (Bukkit.getPlayer(playerData.getUuid()).isFlying() || Bukkit.getPlayer(playerData.getUuid()).getGameMode() == GameMode.CREATIVE || Bukkit.getPlayer(playerData.getUuid()).getWalkSpeed() > 0.2f || Bukkit.getPlayer(playerData.getUuid()).isInsideVehicle()) {
                    invalidStage = 200;
                } else if (invalidStage > 0) {
                    --invalidStage;
                }
                Bukkit.getScheduler().callSyncMethod(FireFly.getInstance().getPlugin(), () -> {
                    World world = Bukkit.getPlayer(playerData.getUuid()).getWorld();
                    if (isUnderBlock(world, update.getTo()) || isInvalid(world, update.getTo())) {
                        invalidTicks = 20;
                    } else if (invalidTicks > 0) {
                        --invalidTicks;
                    }
                    if (isOnIce(world, update.getTo())) {
                        vInvalidTicks = 50;
                    } else if (vInvalidTicks > 0) {
                        --vInvalidTicks;
                    }
                    if (invalidStage == 0) {
                        double maxDeltaXZ = 0.923;
                        if (deltaY >= 0.41) {
                            maxDeltaXZ = 0.970;
                        } else if (deltaY == 0.0) {
                            maxDeltaXZ = 0.561;
                        }
                        if (speedEffect != null) {
                            maxDeltaXZ += (double)speedAmplifier * 0.06;
                        }
//                        if (velocityMax != null) {
//                            maxDeltaXZ += (velocityMax.getXz() * 4);
//                        }
//                        if (velocityMax != null) {
                            maxDeltaXZ += (velocityMax * 5);
//                        }
                        if (invalidTicks > 0) {
                            maxDeltaXZ *= 2.0;
                        }
                        if (vInvalidTicks > 0) {
                            maxDeltaXZ *= 2.0;
                        }
                        if(Bukkit.getPlayer(playerData.getUuid()).isSprinting()) {
                            maxDeltaXZ +=  0.30000001192092896D;
                        }
                        if (deltaXZ > maxDeltaXZ) {
                            if(getBuffer() == Integer.MAX_VALUE) {
                                setBuffer(0);
                            }
                            if (increaseBuffer(20) >= 0) {
//                                flag(playerData, String.format("%.3f > %.3f VL: %s", deltaXZ, maxDeltaXZ, getBuffer()));
//                                if (getBuffer() >= 400) {
//                                    punish(Bukkit.getPlayer(playerData.getUuid()));
//                                }
                            }
                        } else {
                            decrementBuffer();
                        }
                    }
                    return new Object();
                });
//                World world = Bukkit.getPlayer(playerData.getUuid()).getWorld();
//                if (isUnderBlock(world, update.getTo()) || isInvalid(world, update.getTo())) {
//                    invalidTicks = 20;
//                } else if (invalidTicks > 0) {
//                    --invalidTicks;
//                }
//                if (isOnIce(world, update.getTo())) {
//                    vInvalidTicks = 50;
//                } else if (vInvalidTicks > 0) {
//                    --vInvalidTicks;
//                }
//                if (invalidStage == 0) {
//                    double maxDeltaXZ = 0.923;
//                    if (deltaY >= 0.41) {
//                        maxDeltaXZ = 0.970;
//                    } else if (deltaY == 0.0) {
//                        maxDeltaXZ = 0.561;
//                    }
//                    if (speedEffect != null) {
//                        maxDeltaXZ += (double)speedAmplifier * 0.06;
//                    }
//                    if (velocityMax != null) {
//                        maxDeltaXZ += velocityMax.getXz();
//                    }
//                    if (invalidTicks > 0) {
//                        maxDeltaXZ *= 2.0;
//                    }
//                    if (vInvalidTicks > 0) {
//                        maxDeltaXZ *= 2.0;
//                    }
//                    if(Bukkit.getPlayer(playerData.getUuid()).isSprinting()) {
//                         maxDeltaXZ +=  0.30000001192092896D;
//                    }
//                    if (deltaXZ > maxDeltaXZ) {
//                        if(getBuffer() == Integer.MAX_VALUE) {
//                            setBuffer(0);
//                        }
//                        if (increaseBuffer(20) >= 0) {
//                            flag(playerData, String.format("%.3f > %.3f VL: %s", deltaXZ, maxDeltaXZ, getBuffer()));
//                            if (getBuffer() >= 400) {
//                                punish(Bukkit.getPlayer(playerData.getUuid()));
//                            }
//                        }
//                    } else {
//                        decrementBuffer();
//                    }
//                }
            //});
        }
    }

    public Boolean isUnderBlock(World world, PlayerLocation location) {
        if(playerData.getTeleportTicks() > 0 || world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()) == null || !world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()).isLoaded()) return false;
        int minX = (int)Math.floor(location.getX() - 0.4);
        int minY = (int)Math.floor(location.getY() + 1.8);
        int minZ = (int)Math.floor(location.getZ() - 0.4);
        int maxX = (int)Math.floor(location.getX() + 0.4);
        int maxY = (int)Math.floor(location.getY() + 2.0 + 1.0E-5);
        int maxZ = (int)Math.floor(location.getZ() + 0.4);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    Location loc = new Location(world, x, y, z).clone();
                    if (!world.isChunkLoaded(loc.getChunk())) {
                         return true;
                    }
                    if (loc.getBlock().getType() == Material.AIR) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isOnIce(World world, PlayerLocation location) {
        if(playerData.getTeleportTicks() > 0 || world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()) == null || !world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()).isLoaded()) return false;

        int minX = (int)Math.floor(location.getX() - 0.4);
        int minY = (int)Math.floor(location.getY() - 1.0 - 1.0E-5);
        int minZ = (int)Math.floor(location.getZ() - 0.4);
        int maxX = (int)Math.floor(location.getX() + 0.4);
        int maxY = (int)Math.floor(location.getY());
        int maxZ = (int)Math.floor(location.getZ() + 0.4);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    Location loc = new Location(world, x, y, z);
                    if (!world.isChunkLoaded(loc.getChunk())) {
                        return true;
                    }
                    if (!loc.getBlock().getType().name().contains("ICE")) continue;
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean isInvalid(World world, PlayerLocation location) {
        if(playerData.getTeleportTicks() > 0 || world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()) == null || !world.getChunkAt(new Location(world, location.getX(), location.getY(), location.getZ()).clone()).isLoaded()) return false;
        int minX = (int)Math.floor(location.getX() - 0.4);
        int minY = (int)Math.floor(location.getY() - 0.5 - 1.0E-5);
        int minZ = (int)Math.floor(location.getZ() - 0.4);
        int maxX = (int)Math.floor(location.getX() + 0.4);
        int maxY = (int)Math.floor(location.getY() + 2.0 + 1.0E-5);
        int maxZ = (int)Math.floor(location.getZ() + 0.4);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    Location loc = new Location(world, x, y, z);
                    Block block = loc.getBlock();
                    Material material = block.getType();
                    String name = material.name();
                    if (!world.isChunkLoaded(loc.getChunk())) {
                        return true;
                    }
                    if (!name.contains("STAIR") && !name.contains("STEP")) continue;
                    return true;
                }
            }
        }
        return false;
    }

}
