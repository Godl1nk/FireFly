package rip.firefly.check.impl.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@CheckData(name = "Speed", subType = "B", description = "Checks For Modified Air Friction", type = CheckType.MOVEMENT, experimental = true, threshold = 7)
public class SpeedB extends PacketCheck {

    public Map<UUID, Map.Entry<Integer, Long>> speedTicks = new HashMap<>();
    public Map<UUID, Map.Entry<Integer, Long>> tooFastTicks = new HashMap<>();
    public Map<UUID, Long> lastHit = new HashMap<>();

    public SpeedB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fpacket) {
        if (fpacket instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket) fpacket;

            PlayerLocation from = playerData.getFrom().clone();
            PlayerLocation to = playerData.getTo().clone();
            Player p = Bukkit.getPlayer(playerData.getUuid());

            if(p.isFlying() || p.getAllowFlight()) {
                return;
            }

            Location l = p.getLocation();
            int x = l.getBlockX();
            int y = l.getBlockY();
            int z = l.getBlockZ();
            Location blockLoc = new Location(p.getWorld(), x, y - 1, z);
            Location loc = new Location(p.getWorld(), x, y, z);
            Location loc2 = new Location(p.getWorld(), x, y + 1, z);
            Location above = new Location(p.getWorld(), x, y + 2, z);
            Location above3 = new Location(p.getWorld(), x - 1, y + 2, z - 1);
            long lastHitDiff = Math.abs(System.currentTimeMillis() - SpeedC.lastHit.getOrDefault(p.getUniqueId(), 0L));

            if ((playerData.getTo().getX() == playerData.getFrom().getX()) && (playerData.getTo().getZ() == playerData.getFrom().getZ())
                    && (playerData.getTo().getY() == playerData.getFrom().getY())
                    || lastHitDiff < 1500L
                    || p.getNoDamageTicks() != 0
                    || p.getVehicle() != null
                    || p.getGameMode().equals(GameMode.CREATIVE)
                    || p.getGameMode().equals(GameMode.CREATIVE)
                    || p.getAllowFlight()
                    || p.isFlying()
                    || VelocityUtil.didTakeVel(p)
                    || ((System.currentTimeMillis() -  playerData.getLastTeleport()) < 300L)
                    || packet.isCancelled()) return;

            double Airmaxspeed = 0.4;
            double maxSpeed = 0.42;
            double newmaxspeed = 0.75;
            if (isOnIce(p)) {
                newmaxspeed = 2.0;
            }


            if (isOnIce(p)) {
                newmaxspeed = 3.0;
            }

            double ig = 0.28;
            double speed = MathUtil.offset(getHV(to.toVector()), getHV(from.toVector()));
            double onGroundDiff = (to.getY() - from.getY());

            if (p.hasPotionEffect(PotionEffectType.SPEED)) {
                int level = getPotionEffectLevel(p, PotionEffectType.SPEED);
                if (level > 0) {
                    newmaxspeed = (newmaxspeed * (((level * 20) * 0.011) + 1));
                    Airmaxspeed = (Airmaxspeed * (((level * 20) * 0.011) + 1));
                    maxSpeed = (maxSpeed * (((level * 20) * 0.011) + 1));
                    ig = (ig * (((level * 20) * 0.011) + 1));
                }
            }
            Airmaxspeed += p.getWalkSpeed() > 0.2 ? p.getWalkSpeed() * 0.8 * 0.03 : 0;
            maxSpeed += p.getWalkSpeed() > 0.2 ? p.getWalkSpeed() * 0.8 * 0.03: 0;

            /** ONGROUND SPEEDS **/
            if (isReallyOnGround(p) && to.getY() == from.getY()) {
                if (speed >= maxSpeed && p.getFallDistance() < 0.15
                        && blockLoc.getBlock().getType() != Material.ICE
                        && blockLoc.getBlock().getType() != Material.PACKED_ICE
                        && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR
                        && above3.getBlock().getType() == Material.AIR) {
                    flag(playerData);
                }
            }

            /** MIDAIR MODIFIED SPEEDS **/
            if (!isReallyOnGround(p) && speed >= Airmaxspeed && !isOnIce(p)
                    && blockLoc.getBlock().getType() != Material.ICE && !blockLoc.getBlock().isLiquid()
                    && !loc.getBlock().isLiquid() && blockLoc.getBlock().getType() != Material.PACKED_ICE
                    && above.getBlock().getType() == Material.AIR && above3.getBlock().getType() == Material.AIR
                    && blockLoc.getBlock().getType() != Material.AIR) {
                flag(playerData);
            }
            /** GOING ABOVE THE SPEED LIMIT **/
            if (speed >= newmaxspeed && isOnIce(p) && p.getFallDistance() < 0.6
                    && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR
                    && loc2.getBlock().getType() == Material.AIR) {
                flag(playerData);
            }

            /** Vanilla speeds check **/
            if (speed > ig && !isAir(p) && onGroundDiff <= -0.4 && p.getFallDistance() <= 0.4
                    && !flaggyStuffNear(p.getLocation()) && blockLoc.getBlock().getType() != Material.ICE
                    && playerData.getTo().getY() != playerData.getFrom().getY() && blockLoc.getBlock().getType() != Material.PACKED_ICE
                    && loc2.getBlock().getType() != Material.TRAP_DOOR && above.getBlock().getType() == Material.AIR
                    && above3.getBlock().getType() == Material.AIR) {
                flag(playerData);
            }
        }
    }


    public static boolean isReallyOnGround(Player p) {
        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Location b = new Location(p.getWorld(), x, y - 1, z);

        return b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB
                && !b.getBlock().isLiquid();
    }

    public static boolean flaggyStuffNear(Location loc) {
        boolean nearBlocks = false;
        for (Block bl : PlayerUtil.getSurrounding(loc.getBlock(), true)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_STEP))
                    || (bl.getType().equals(Material.ICE))
                    || (bl.getType().equals(Material.PACKED_ICE))
                    || (bl.getType().equals(Material.SLIME_BLOCK))) {
                nearBlocks = true;
                break;
            }
        }
        for (Block bl : PlayerUtil.getSurrounding(loc.getBlock(), false)) {
            if ((bl.getType().equals(Material.STEP)) || (bl.getType().equals(Material.DOUBLE_STEP))
                    || (bl.getType().equals(Material.BED)) || (bl.getType().equals(Material.WOOD_DOUBLE_STEP))
                    || (bl.getType().equals(Material.WOOD_STEP))) {
                nearBlocks = true;
                break;
            }
        }
        if (isBlock(loc.getBlock().getRelative(BlockFace.DOWN), new Material[]{Material.STEP, Material.BED,
                Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.WOOD_STEP})) {
            nearBlocks = true;
        }
        return nearBlocks;
    }

    public static boolean isBlock(Block block, Material[] materials) {
        Material type = block.getType();
        Material[] arrayOfMaterial;
        int j = (arrayOfMaterial = materials).length;
        for (int i = 0; i < j; i++) {
            Material m = arrayOfMaterial[i];
            if (m == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAir(final Player player) {
        final Block b = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        return b.getType().equals(Material.AIR)
                && b.getRelative(BlockFace.WEST).getType().equals(Material.AIR)
                && b.getRelative(BlockFace.NORTH).getType().equals(Material.AIR)
                && b.getRelative(BlockFace.EAST).getType().equals(Material.AIR)
                && b.getRelative(BlockFace.SOUTH).getType().equals(Material.AIR);
    }

    public boolean isOnIce(final Player player) {
        final Location a = player.getLocation().clone();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals(Material.ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals(Material.ICE);
    }

    public boolean isOnPIce(final Player player) {
        final Location a = player.getLocation();
        a.setY(a.getY() - 1.0);
        if (a.getBlock().getType().equals(Material.PACKED_ICE)) {
            return true;
        }
        a.setY(a.getY() - 1.0);
        return a.getBlock().getType().equals(Material.PACKED_ICE);
    }

    private int getPotionEffectLevel(Player p, PotionEffectType pet) {
        for (PotionEffect pe : p.getActivePotionEffects()) {
            if (pe.getType().getName().equals(pet.getName())) {
                return pe.getAmplifier() + 1;
            }
        }
        return 0;
    }

    private Vector getHV(Vector V) {
        V.setY(0);
        return V;
    }
}
