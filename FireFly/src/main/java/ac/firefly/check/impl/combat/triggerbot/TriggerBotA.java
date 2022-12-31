package ac.firefly.check.impl.combat.triggerbot;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.PlayerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class TriggerBotA extends Check {
    public TriggerBotA() {
        super("TriggerBot (A)", CheckType.COMBAT, true);
    }

    @EventHandler(ignoreCancelled=true, priority= EventPriority.MONITOR)
    public void onDamage(EntityDamageByEntityEvent e) {
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            return;
        }
        if (!(e.getDamager() instanceof Player)) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player damager = (Player)e.getDamager();
        Player player = (Player)e.getEntity();
        if (damager.getAllowFlight()) {
            return;
        }
        if (player.getAllowFlight()) {
            return;
        }
        double MaxReach = 4.1 + player.getVelocity().length() * 4.0;
        if (damager.getVelocity().length() > 0.08) {
            MaxReach += damager.getVelocity().length();
        }
        double Reach2 = PlayerUtils.getEyeLocation(damager).distance(player.getLocation());
        int Ping = Lag.getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            MaxReach += 0.2;
        } else if (Ping >= 200 && Ping < 250) {
            MaxReach += 0.4;
        } else if (Ping >= 250 && Ping < 300) {
            MaxReach += 0.7;
        } else if (Ping >= 300 && Ping < 350) {
            MaxReach += 1.0;
        } else if (Ping >= 350 && Ping < 400) {
            MaxReach += 1.4;
        } else if (Ping > 400) {
            return;
        }
        if (damager.getLocation().getY() > player.getLocation().getY()) {
            double Difference = damager.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 4.0;
        } else if (player.getLocation().getY() > damager.getLocation().getY()) {
            double Difference = player.getLocation().getY() - damager.getLocation().getY();
            MaxReach += Difference / 4.0;
        }
        if (Reach2 > MaxReach) {
            flag(damager, null, null);
        }
    }
}
