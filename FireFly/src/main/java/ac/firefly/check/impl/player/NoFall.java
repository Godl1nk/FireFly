package ac.firefly.check.impl.player;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.*;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class NoFall extends Check {

    public static Map<UUID, Map.Entry<Long, Integer>> NoFallTicks;
    public static Map<UUID, Double> FallDistance;
    public static ArrayList<Player> cancel;

    public NoFall() {
        super("NoFall (A)", CheckType.MISC, true);

        NoFallTicks = new HashMap<UUID, Map.Entry<Long, Integer>>();
        FallDistance = new HashMap<UUID, Double>();
        cancel = new ArrayList<Player>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Player player = e.getPlayer();
        if (player.getAllowFlight()
                || player.getGameMode().equals(GameMode.CREATIVE)
                || player.getVehicle() != null
                || PlayerUtils.isOnClimbable(player, 0)
                || PlayerUtils.isInWater(player)
                || PlayerUtils.isInWeb(player)
                || PlayerUtils.isOnStair(player.getLocation())) {
            return;
        }
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (p.getGameMode().equals((Object) GameMode.CREATIVE)) {
            return;
        }
        if (data != null) {
            if (e.getTo().getY() > e.getFrom().getY()) {
                return;
            }
            if (data.isLastBlockPlaced_GroundSpoof()) {
                if (TimerUtils.elapsed(data.getLastBlockPlacedTicks(), 500L)) {
                    data.setLastBlockPlaced_GroundSpoof(false);
                }
                return;
            }
            Location to = e.getTo();
            Location from = e.getFrom();
            double diff = to.toVector().distance(from.toVector());
            int dist = PlayerUtils.getDistanceToGround(p);
            if (p.getLocation().add(0, -1.50, 0).getBlock().getType() != Material.AIR) {
                data.setGroundSpoofVL(0);
                return;
            }
            if (e.getTo().getY() > e.getFrom().getY() || PlayerUtils.isOnGround4(p) || NEW_Velocity_Utils.didTakeVel(p) || VelocityUtils.didTakeVelocity(p)) {
                data.setGroundSpoofVL(0);
                return;
            }
            if (p.isOnGround() && diff > 0.0 && !PlayerUtils.isOnGround(p) && dist >= 2 && e.getTo().getY() < e.getFrom().getY()) {
                if (data.getGroundSpoofVL() >= 4) {
                    if (data.getAirTicks() >= 10) {
                        flag(p, "Ground", "" + data.getGroundSpoofVL());

                    }
                } else {
                    data.setGroundSpoofVL(data.getGroundSpoofVL() + 1);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (p.getGameMode().equals((Object) GameMode.CREATIVE)) {
            return;
        }
        if (data != null) {
            if (!data.isLastBlockPlaced_GroundSpoof()) {
                data.setLastBlockPlaced_GroundSpoof(true);
                data.setLastBlockPlacedTicks(TimerUtils.nowlong());
            }
        }
    }

    // No Fall check 2

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        cancel.add(e.getEntity());
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        if (FallDistance.containsKey(e.getPlayer().getUniqueId())) {
            FallDistance.remove(e.getPlayer().getUniqueId());
        }
        if (FallDistance.containsKey(e.getPlayer().getUniqueId())) {
            FallDistance.containsKey(e.getPlayer().getUniqueId());
        }
        if(cancel.contains(e.getPlayer())) {
            cancel.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().getGameMode().equals((Object) GameMode.CREATIVE)) {
            return;
        }
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            cancel.add(e.getPlayer());
        }
    }

    @EventHandler
    public void Move(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (player.getAllowFlight()
                || player.getGameMode().equals(GameMode.CREATIVE)
                || player.getVehicle() != null
                || cancel.remove(player)
                || PlayerUtils.isOnClimbable(player, 0)
                || PlayerUtils.isInWater(player)
                || PlayerUtils.isInWeb(player)
                || PlayerUtils.isOnStair(player.getLocation())) {
            return;
        }
        Damageable dplayer = (Damageable) e.getPlayer();

        if (dplayer.getHealth() <= 0.0D) {
            return;
        }

        double Falling = 0.0D;
        if ((!PlayerUtils.isOnGround(player)) && (e.getFrom().getY() > e.getTo().getY())) {
            if (FallDistance.containsKey(player.getUniqueId())) {
                Falling = FallDistance.get(player.getUniqueId()).doubleValue();
            }
            Falling += e.getFrom().getY() - e.getTo().getY();
        }
        FallDistance.put(player.getUniqueId(), Double.valueOf(Falling));
        if (Falling < 3.0D) {
            return;
        }
        long Time = System.currentTimeMillis();
        int Count = 0;
        if (NoFallTicks.containsKey(player.getUniqueId())) {
            Time = NoFallTicks.get(player.getUniqueId()).getKey().longValue();
            Count = NoFallTicks.get(player.getUniqueId()).getValue().intValue();
        }
        if ((player.isOnGround()) || (player.getFallDistance() == 0.0F)) {
            player.damage(5);
            Count += 2;
        } else {
            Count--;
        }
        if (NoFallTicks.containsKey(player.getUniqueId()) && ServerUtils.elapsed(Time, 10000L)) {
            Count = 0;
            Time = System.currentTimeMillis();
        }
        if (Count >= 4) {
            Count = 0;

            FallDistance.put(player.getUniqueId(), Double.valueOf(0.0D));
            flag(player, "Packet", "Distance: " + Falling + ". Count: " + Count);

        }
        NoFallTicks.put(player.getUniqueId(),
                new AbstractMap.SimpleEntry<Long, Integer>(Time, Count));
        return;
    }
}
