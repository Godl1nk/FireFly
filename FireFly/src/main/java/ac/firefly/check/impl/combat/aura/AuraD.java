package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketUseEntityEvent;
import ac.firefly.util.interaction.TimerUtils;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuraD extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> AimbotTicks;
    public static Map<UUID, Double> Differences;
    public static Map<UUID, Location> LastLocation;

    public AuraD() {
        super("Aura (D)", CheckType.COMBAT, true);
        AimbotTicks = new HashMap<>();
        Differences = new HashMap<>();
        LastLocation = new HashMap<>();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        AimbotTicks.remove(e.getPlayer().getUniqueId());
        Differences.remove(e.getPlayer().getUniqueId());
        LastLocation.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK
                || !((e.getEntity()) instanceof Player)) return;
        Player damager = e.getPlayer();
        if (damager.getAllowFlight()) return;

        Location from = null;
        Location to = damager.getLocation();
        if (LastLocation.containsKey(damager.getUniqueId())) {
            from = LastLocation.get(damager.getUniqueId());
        }
        LastLocation.put(damager.getUniqueId(), damager.getLocation());
        double Count = 0;
        long Time = System.currentTimeMillis();
        double LastDifference = -111111.0;
        if (Differences.containsKey(damager.getUniqueId())) {
            LastDifference = Differences.get(damager.getUniqueId());
        }
        if (AimbotTicks.containsKey(damager.getUniqueId())) {
            Count = AimbotTicks.get(damager.getUniqueId()).getKey();
            Time = AimbotTicks.get(damager.getUniqueId()).getValue();
        }
        if (from == null || (to.getX() == from.getX() && to.getZ() == from.getZ())) return;
        double Difference = Math.abs(to.getYaw() - from.getYaw());
        if (Difference == 0.0) return;

        if (Difference > 2.4) {
            double diff = Math.abs(LastDifference - Difference);
            if (e.getEntity().getVelocity().length() < 0.1) {
                if (diff < 1.4) {
                    Count += 1;
                } else {
                    Count = 0;
                }
            } else {
                if (diff < 1.8) {
                    Count += 1;
                } else {
                    Count = 0;
                }
            }
        }
        Differences.put(damager.getUniqueId(), Difference);
        if (AimbotTicks.containsKey(damager.getUniqueId()) && TimerUtils.elapsed(Time, 5000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if (Count > 5) {
            Count = 0;
            flag(damager, null, null);
        }
        AimbotTicks.put(damager.getUniqueId(),
                new AbstractMap.SimpleEntry<>((int) Math.round(Count), Time));
    }
}