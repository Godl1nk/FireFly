package ac.firefly.check.impl.combat.velocity;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketPlayerEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import ac.firefly.events.update.UpdateEvent;
import ac.firefly.events.update.UpdateType;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.util.interaction.TimerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class VelocityB extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> VelocityTicks;
    private Map<Player, Map.Entry<Double, Long>> velocity;
    private Map<Player, Long> LastUpdate;

    public VelocityB() {
        super("Velocity [B]", CheckType.COMBAT, true);
        this.VelocityTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();
        this.velocity = new HashMap<Player, Map.Entry<Double, Long>>();
        this.LastUpdate = new HashMap<Player, Long>();
     }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onKnockback(final EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return;
        }
        final Player player = (Player)event.getEntity();
        if (this.velocity.containsKey(player)) {
            return;
        }
        final Location pL = player.getLocation().clone();
        pL.add(0.0, player.getEyeHeight() + 1.0, 0.0);
        if (PlayerUtils.blocksNear(pL)) {
            return;
        }
        this.velocity.put(player, new AbstractMap.SimpleEntry<Double, Long>(player.getLocation().getY(), System.currentTimeMillis()));
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final Player player = event.getEntity();
        if (this.velocity.containsKey(player)) {
            this.velocity.remove(player);
        }
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final double Y = event.getTo().getY();
        if (this.velocity.containsKey(player)) {
            final Map.Entry<Double, Long> ye = this.velocity.get(player);
            if (Y > ye.getKey()) {
                this.velocity.remove(player);
            }
        }
    }

    @EventHandler
    public void onPacket(final PacketPlayerEvent event) {
        this.LastUpdate.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onUpdate(final UpdateEvent event) {
        if (!event.getType().equals(UpdateType.TICK)) {
            return;
        }
        for (final Player player : this.velocity.keySet()) {
            if (this.LastUpdate.containsKey(player)) {
                if (this.LastUpdate.get(player) == null) {
                    continue;
                }
                int Count = 0;
                long Time = System.currentTimeMillis();
                if (this.VelocityTicks.containsKey(player.getUniqueId())) {
                    Count = this.VelocityTicks.get(player.getUniqueId()).getKey();
                    Time = this.VelocityTicks.get(player.getUniqueId()).getValue();
                    if (TimerUtils.elapsed(Time, 5000L)) {
                        Count = 0;
                        Time = System.currentTimeMillis();
                    }
                }
                final Map.Entry<Double, Long> ye = this.velocity.get(player);
                if (System.currentTimeMillis() >= ye.getValue() + 1000L) {
                    this.velocity.remove(player);
                    if (System.currentTimeMillis() - this.LastUpdate.get(player) < 60L) {
                        ++Count;
                        Time = System.currentTimeMillis();
                    }
                    else {
                        Count = 0;
                    }
                }
                if (Count > 3) {
                    Count = 0;
                    flag(player, String.format("T: %s L: %s VT: %s", Time, LastUpdate, VelocityTicks.get(player.getUniqueId()).getValue()), String.format("T: %s L: %s VT: %s", Time, LastUpdate, VelocityTicks.get(player.getUniqueId()).getValue()));
                }
                this.VelocityTicks.put(player.getUniqueId(), new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
            }
        }
    }

    public int getPing(Player who) {
        try {
            String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle").invoke(who);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return 404;
        }
    }
}
