package rip.firefly.check.impl.movement.fly;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.player.PlayerUtil;
import rip.firefly.util.velocity.VelocityUtil;

import java.util.Arrays;


@CheckData(name = "Fly", subType = "A", description = "Checks If A Player Is Hovering", type = CheckType.MOVEMENT, threshold = 2)
public class FlyA extends PacketCheck {

    long flyTicks;

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInFlyingPacket) {

            WrappedInFlyingPacket packet = (WrappedInFlyingPacket)fPacket;
            PlayerLocation from = playerData.getFrom();
            PlayerLocation to = playerData.getTo();
            Player p = packet.getPlayer();

            if(playerData.getTeleportTicks() > 0
                    || Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk() == null
                    || !Bukkit.getPlayer(playerData.getUuid()).getLocation().getChunk().isLoaded()
            ) return;


            if (p.getGameMode().equals(GameMode.CREATIVE)
                    || p.getAllowFlight()
                    || p.getVehicle() != null
                    || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
//                    || PlayerUtil.isOnClimbable(p, 0)
//                    || PlayerUtil.isOnClimbable(p, 1)
                    || onClimbable(p)
                    || VelocityUtil.didTakeVel(p)
                    || PlayerUtil.isInWeb(p)
                    || PlayerUtil.isInWater(p)
                    || p.getVehicle() != null
                    || (to.getX() == from.getX()) && (to.getZ() == from.getZ())) {
                return;
            }

            if (PlayerUtil.blocksNear(p.getLocation())) {
                flyTicks = 0;
                return;
            }
            if (Math.abs(to.getY() - from.getY()) > 0.06) {
                flyTicks = 0;
                return;
            }

            long current = System.currentTimeMillis();
            if (flyTicks != 0L) {
                current = flyTicks;
            }
            long delta = System.currentTimeMillis() - current;
            if (delta > 200L) {
                flag(playerData, String.format("T: %s", MathUtil.trim(1, (double) (delta / 1000))), String.format("GD: %s", getDistanceToGround(p)));
                flyTicks = 0;
                return;
            }
            flyTicks = current;
        }
    }

    public boolean onLadder(Player player) {
        return player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.LADDER;
    }

    public boolean onVine(Player player) {
        return player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.VINE;
    }

    public boolean onWeb(Player player) {
        return player.getLocation().clone().subtract(0, 0.1, 0).getBlock().getType() == Material.WEB;
    }

    public boolean onClimbable(Player p) {
        return onWeb(p) || onLadder(p) || onVine(p);
    }

    private int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) break;
            distance++;
        }
        return distance;
    }

  /*  int verbose;

    public FlyA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fPacket) {
        if(fPacket instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket)fPacket;
            Player p = packet.getPlayer();
            if (p.getGameMode().equals(GameMode.CREATIVE)
                    || p.getAllowFlight()
                    || packet.getPlayer().getVehicle() != null
                    || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
                    || PlayerUtil.isOnClimbable(p, 1)
                    || PlayerUtil.isOnCarpet(p)
                    || ((System.currentTimeMillis() -  playerData.getLastTeleport()) < 300L)
                    || VelocityUtil.didTakeVel(p)
                    || PlayerUtil.isInWeb(p)) {
                return;
            }

            if (!PlayerUtil.isOnGround(p.getLocation()) && !PlayerUtil.groundBlocksAround(p) && !PlayerUtil.isOnGround4(p) && !PlayerUtil.isReallyOnground(p) && !PlayerUtil.isInWater(p) && !PlayerUtil.inInLilyPad(p) || PlayerUtil.blocksNear(p.getLocation())) {
                double distanceToGround = getDistanceToGround(p);
                double yDiff = MathUtil.getVerticalDistance(playerData.getFrom().toBukkit(p.getWorld()), playerData.getTo().toBukkit(p.getWorld()));

                if (distanceToGround >= (p.hasPotionEffect(PotionEffectType.JUMP) ? 4 : 2)) {
                    verbose = yDiff == 0 ? verbose + 6 : yDiff < 0.06 ? verbose + 4 : 0;
                } else if (playerData.getAirTicks() > 7
                        && yDiff < 0.001) {
                    verbose += 2;
                } else {
                    verbose = 0;
                }


                if (!PlayerUtil.isOnGround(p.getLocation()) && !PlayerUtil.groundBlocksAround(p) && !PlayerUtil.isOnGround4(p) && !PlayerUtil.isReallyOnground(p) &&  verbose > 10) {
                    flag(playerData, new String[]{String.format("V: %s", verbose), String.format("GD: %s", getDistanceToGround(playerData.getPlayer()))});
                    verbose = 0;
                }
            }
        }
    }

    private int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) break;
            distance++;
        }
        return distance;
    }*/
}
