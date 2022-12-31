package rip.firefly.handler;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import rip.firefly.FireFly;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.FieldAccessor;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflection;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutPositionPacket;
import rip.firefly.util.location.Cuboid;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.location.PlayerPosition;
import rip.firefly.util.player.PlayerUtil;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MovementHandler {
    private FieldAccessor<Boolean> fieldCheckMovement = Reflection.getField("{nms}.PlayerConnection", "checkMovement", boolean.class);
    private FieldAccessor<Boolean> fieldJustTeleported = Reflection.getField("{nms}.PlayerConnection", "justTeleported", boolean.class);
    private int velSent;
    private int velReceived;
    private WrappedInFlyingPacket lastFlying;
    private boolean smallMove;

    public MovementHandler(JavaPlugin plugin) {

    }

    public static void handle(PlayerData data, NMSObject fpacket) {
        if(fpacket instanceof WrappedInFlyingPacket) {
            ++data.currentTick;
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket)fpacket;

            Bukkit.getScheduler().callSyncMethod(FireFly.getInstance().getPlugin(), () ->  { updateMoveData(data); return new Object(); });

            boolean moving = packet.isPos();
            boolean rotating = packet.isLook();

            if (data.getFrom() != null && System.currentTimeMillis() - data.getFrom().getTimestamp() > 110L) {
                data.setLastDelayedFlyingPacket(System.currentTimeMillis());
            }

            data.setTeleporting(false);

            if(PlayerUtil.isOnGround(packet.getPlayer().getLocation()) != packet.isGround()) {
                data.setGsTicks(0);
            }
            data.setGsTicks(data.getGsTicks() + 1);

            if (packet.isGround()) {
                data.setOnGround(true);
                data.setGroundTicks(data.getGroundTicks() + 1);
                data.setAirTicks(0);
            } else {
                data.setOnGround(false);
                data.setAirTicks(data.getAirTicks() + 1);
                data.setGroundTicks(0);
            }
            if (data.getTeleportTicks() > 0) {
                data.setTeleportTicks(data.getTeleportTicks() - 1);
            }

            if (data.getAltTeleportTicks() > 0) {
                data.setTeleportTicks(data.getTeleportTicks() - 5);
            }
            if (packet.isPos()) {

                PlayerPosition playerPosition = new PlayerPosition(packet.getX(), packet.getZ());

                data.setPlayerPosition(playerPosition);
                data.getLocations().add(playerPosition);

                data.movement.fx = data.movement.tx;
                data.movement.fy = data.movement.ty;
                data.movement.fz = data.movement.tz;
                PlayerLocation from = new PlayerLocation();
                from.set(data.movement.fx, data.movement.fy, data.movement.fz, data.movement.fyaw, data.movement.fpitch, packet.isGround());
                data.setFrom(from);

                data.movement.tx = packet.getX();
                data.movement.ty = packet.getY();
                data.movement.tz = packet.getZ();
                PlayerLocation to = new PlayerLocation();
                to.set(data.movement.tx, data.movement.ty, data.movement.tz, data.movement.tyaw, data.movement.tpitch, packet.isGround());
                data.setTo(to);

                data.movement.lastDeltaH = data.movement.deltaH;
                data.movement.lastDeltaV = data.movement.deltaV;
                data.movement.deltaH = Math.hypot(data.movement.tx - data.movement.fx, data.movement.tz - data.movement.fz);
                data.movement.deltaV = data.movement.ty - data.movement.fy;



                if (data.movement.hasJumped) {
                    data.movement.hasJumped = false;
                    data.movement.inAir = true;
                }
                if (packet.isGround()) data.movement.inAir = false;

                if (System.currentTimeMillis() - data.getTo().getTimestamp() > 110L) {
                    data.setLastDelayedFlyingPacket(System.currentTimeMillis());
                }
                final Cuboid cuboid = new Cuboid(to.toBukkit(packet.getPlayer().getWorld())).expand(0.5, 0.07, 0.5).move(0.0, -0.55, 0.0);
                AtomicBoolean touchingAir = new AtomicBoolean(false);

                Bukkit.getScheduler().callSyncMethod(FireFly.getInstance().getPlugin(), () ->  {
                    touchingAir.set(cuboid.checkBlocks(packet.getPlayer().getWorld(), material -> material == Material.AIR));
                    return new Object();
                });

                data.setTouchingAir(touchingAir.get());
            }

            Vector vec = new Vector(Bukkit.getPlayer(data.getUuid()).getLocation().getX(), Bukkit.getPlayer(data.getUuid()).getLocation().getY(), Bukkit.getPlayer(data.getUuid()).getLocation().getZ());

            if (data.getTeleports().contains(vec)) {
                data.getTeleports().remove(vec);
                data.setTeleporting(true);
            }

            if (packet.isLook()) {
                data.movement.fyaw = data.movement.tyaw;
                data.movement.fpitch = data.movement.tpitch;
                PlayerLocation from = new PlayerLocation();
                from.set(data.movement.fx, data.movement.fy, data.movement.fz, data.movement.fyaw, data.movement.fpitch, packet.isGround());
                data.setFrom(from);

                data.movement.tyaw = packet.getYaw();
                data.movement.tpitch = packet.getPitch();
                PlayerLocation to = new PlayerLocation();
                to.set(data.movement.tx, data.movement.ty, data.movement.tz, data.movement.tyaw, data.movement.tpitch, packet.isGround());
                data.setTo(to);





                float deltaYaw = getDistanceBetweenAngles(data.movement.tyaw, data.movement.fyaw);
                float deltaPitch = getDistanceBetweenAngles(data.movement.tpitch, data.movement.fpitch);
                data.movement.yawDifference = Math.abs(deltaYaw - data.movement.deltaYaw);
                data.movement.pitchDifference = Math.abs(deltaPitch - data.movement.deltaPitch);
                data.movement.lastDeltaYaw = data.movement.deltaYaw;
                data.movement.lastDeltaPitch = data.movement.deltaPitch;
                data.movement.deltaYaw = deltaYaw;
                data.movement.deltaPitch = deltaPitch;
            }
        }
        if(fpacket instanceof WrappedOutPositionPacket) {
            WrappedOutPositionPacket packet = (WrappedOutPositionPacket) fpacket;

            data.setTeleportTicks(50);
            data.setAltTeleportTicks(500);

            double posX = packet.getX();
            double posY = packet.getY();
            double posZ = packet.getZ();

            Vector vec = new Vector(posX, posY, posZ);
            data.getTeleports().add(vec);
            data.setLastTeleport(System.currentTimeMillis());
        }
    }

    public static float getDistanceBetweenAngles(final float angle1, final float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    // Holy shit this needs rewrite so badly
    public static void updateMoveData(PlayerData data) {
        if((System.currentTimeMillis() - data.getLastJoin()) < 5000) {
            return;
        }

        Chunk chunk = data.getTo().toBukkit(Bukkit.getPlayer(data.getUuid()).getWorld()).getChunk();

        //ground, ladder, liquid, ice, under, inside, piston
        data.playerOnGround = (data.onLadder = data.inWater = data.inLava = data.onIce = data.underBlock = data.onPiston = data.inWeb = false);

        if(chunk.isLoaded()) {
            for (PlayerLocation loc : getLocationsAround(data.getTo())) {
                Material below = loc.clone().add(0, -1.0E-13D, 0).getBlock(Bukkit.getPlayer(data.getUuid())).getType();
                Material liquid = loc.clone().add(0, 0.0625D, 0).getBlock(Bukkit.getPlayer(data.getUuid())).getType();
                Material special = loc.clone().add(0, -0.5000000000001D, 0).getBlock(Bukkit.getPlayer(data.getUuid())).getType();
                Material above = loc.clone().add(0, 2.2D, 0).getBlock(Bukkit.getPlayer(data.getUuid())).getType();
                if (!data.playerOnGround) {
                    data.playerOnGround = isSolid(below);
                }
                if (!data.inWeb) {
                    data.inWeb = below.equals(Material.WEB) || above.equals(Material.WEB);
                }
                if (!data.onLadder) {
                    data.onLadder = below.equals(Material.LADDER) || above.equals(Material.LADDER) || below.equals(Material.VINE) || above.equals(Material.VINE);
                }
                if (!data.inWater) {
                    data.inWater = liquid.equals(Material.STATIONARY_WATER) || liquid.equals(Material.WATER) || above.equals(Material.STATIONARY_WATER) || above.equals(Material.WATER);
                }
                if (!data.inLava) {
                    data.inLava = liquid.equals(Material.STATIONARY_LAVA) || liquid.equals(Material.LAVA) || above.equals(Material.STATIONARY_LAVA) || above.equals(Material.LAVA);
                }
                if (!data.onPiston) {
                    data.onPiston = below.equals(Material.PISTON_BASE) || below.equals(Material.PISTON_EXTENSION) || below.equals(Material.PISTON_MOVING_PIECE) || below.equals(Material.PISTON_STICKY_BASE);
                }
                if (!data.onIce) {
                    data.onIce = below.equals(Material.ICE) || below.equals(Material.PACKED_ICE);
                }
                if (!data.underBlock) {
                    data.underBlock = isSolid(above);
                }
            }
        } else {
            return;
        }

        if (data.getTo().getBlock(Bukkit.getPlayer(data.getUuid())).getType().isTransparent()) {
            data.setLagback(data.getTo());
        }

    }

    public static List<PlayerLocation> getLocationsAround(PlayerLocation location) {
        List<PlayerLocation> locations = new ArrayList<>();
        for (double x = -0.3D; x <= 0.3D; x += 0.3D) {
            for (double z = -0.3D; z <= 0.3D; z += 0.3D) {
                locations.add(location.clone().add(x, 0, z));
            }
        }

        locations.add(location);

        return locations;
    }


    private static final Set<Material> SOLID_MATERIAL_WHITELIST = new HashSet<>();

    private static final Set<Integer> SPECIAL_SOLID_MATERIAL_ID_WHITELIST = new HashSet<>(); //Fences, etc

    static {
        SOLID_MATERIAL_WHITELIST.addAll(Arrays.asList(Material.SNOW, Material.SNOW_BLOCK, Material.CARPET, Material.DIODE,
                Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
                Material.REDSTONE_COMPARATOR_ON, Material.SKULL, Material.SKULL_ITEM, Material.LADDER, Material.WATER_LILY));

        SPECIAL_SOLID_MATERIAL_ID_WHITELIST.addAll(Arrays.asList(85, 188, 189, 190, 191, 192, 113, 107, 183, 184, 185, 186, 187, 139, 65));
    }

    public static boolean isSolid(Material material) {
        if (material.isSolid() || SOLID_MATERIAL_WHITELIST.contains(material)) {
            return true;
        } else if (SPECIAL_SOLID_MATERIAL_ID_WHITELIST.contains(material.getId())) {
            return true;
        }
        return false;
    }
}

