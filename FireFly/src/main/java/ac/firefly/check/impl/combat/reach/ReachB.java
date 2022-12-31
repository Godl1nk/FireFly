package ac.firefly.check.impl.combat.reach;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketUseEntityEvent;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.util.interaction.TimerUtils;
import ac.firefly.util.math.MathUtils;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReachB extends Check {

    private Map<Player, Map.Entry<Double, Double>> offsets;
    private Map<Player, Long> reachTicks;
    private ArrayList<Player> projectileHit;

    public ReachB() {
        super("Reach (B)", CheckType.COMBAT, true);

        this.offsets = new HashMap<>();
        this.reachTicks = new HashMap<>();
        this.projectileHit = new ArrayList<>();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() && event.getFrom().getZ() == event.getTo().getZ()) return;
        double OffsetXZ = MathUtils.offset(MathUtils.getHorizontalVector(event.getFrom().toVector()),
                MathUtils.getHorizontalVector(event.getTo().toVector()));
        double horizontal = Math.sqrt(Math.pow(event.getTo().getX() - event.getFrom().getX(), 2.0)
                + Math.pow(event.getTo().getZ() - event.getFrom().getZ(), 2.0));
        this.offsets.put(event.getPlayer(),
                new AbstractMap.SimpleEntry<>(OffsetXZ, horizontal));
    }

    @EventHandler
    public void onDmg(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player)
                || e.getCause() != DamageCause.PROJECTILE) return;

        Player player = (Player) e.getDamager();

        this.projectileHit.add(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent e) {
        offsets.remove(e.getPlayer());
        reachTicks.remove(e.getPlayer());
        projectileHit.remove(e.getPlayer());
    }

    @EventHandler
    public void onDamage(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK
                || !(e.getEntity() instanceof Player)
                || e.getPlayer().getAllowFlight()
                /*|| Lag.getTPS() < 17.0*/) return;

        Player damager = e.getPlayer();
        Player player = (Player) e.getEntity();
        double ydist = Math.abs(damager.getEyeLocation().getY() - player.getEyeLocation().getY());
        double Reach = MathUtils.trim(2,
                (PlayerUtils.getEyeLocation(damager).distance(player.getEyeLocation()) - ydist) - 0.32);
        int PingD = Lag.getPing(damager);
        int PingP = Lag.getPing(player);

        long attackTime = System.currentTimeMillis();
        if (this.reachTicks.containsKey(damager)) {
            attackTime = reachTicks.get(damager);
        }
        double yawdif = Math.abs(180 - Math.abs(damager.getLocation().getYaw() - player.getLocation().getYaw()));
        if (Lag.getPing(damager) > 92 || Lag.getPing(player) > 92) return;
        double offsetsp = 0.0D;
        double lastHorizontal = 0.0D;
        double offsetsd = 0.0D;
        if (this.offsets.containsKey(damager)) {
            offsetsd = (this.offsets.get(damager)).getKey();
            lastHorizontal = (this.offsets.get(damager)).getValue();
        }
        if (this.offsets.containsKey(player)) {
            offsetsp = (this.offsets.get(player)).getKey();
            lastHorizontal = (this.offsets.get(player)).getValue();
        }
        Reach -= MathUtils.trim(2, offsetsd);
        Reach -= MathUtils.trim(2, offsetsp);
        double maxReach2 = 3.1;
        if (yawdif > 90) {
            maxReach2 += 0.38;
        }
        maxReach2 += lastHorizontal * 0.87;

        maxReach2 += ((PingD + PingP) / 2) * 0.0024;
        if (Reach > maxReach2 && TimerUtils.elapsed(attackTime, 1100) && !projectileHit.contains(player)) {
            flag(damager, "(First Hit Reach) Range: " + Reach + " > " + maxReach2, null);
        }

        reachTicks.put(damager, TimerUtils.nowlong());
        projectileHit.remove(player);
    }

}