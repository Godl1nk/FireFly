package ac.firefly.check.impl.combat.reach;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.util.math.MathUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class ReachC extends Check {

    public static HashMap<UUID, Integer> toBan;

    public ReachC() {
        super("Reach (C)", CheckType.COMBAT, true);

        toBan = new HashMap<>();
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                || !(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)
                /*|| Lag.getTPS() < 17*/) return;

        Player player = (Player) e.getDamager();
        Player damaged = (Player) e.getEntity();

        if (player.getAllowFlight()) return;

        double YawDifference = Math.abs(180 - Math.abs(damaged.getLocation().getYaw() - player.getLocation().getYaw()));
        double Difference = PlayerUtils.getEyeLocation(player).distance(damaged.getEyeLocation()) - 0.35;

        int Ping = Lag.getPing(player);
        double MaxReach = 4.0 + damaged.getVelocity().length();

        if (player.isSprinting()) {
            MaxReach += 0.2;
        }

        if (player.getLocation().getY() > damaged.getLocation().getY()) {
            Difference = player.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 2.5;
        } else if (player.getLocation().getY() > player.getLocation().getY()) {
            Difference = player.getLocation().getY() - player.getLocation().getY();
            MaxReach += Difference / 2.5;
        }
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                MaxReach += 0.2D * (effect.getAmplifier() + 1);
            }
        }

        double velocity = player.getVelocity().length() + damaged.getVelocity().length();

        MaxReach += velocity * 1.5;
        MaxReach += Ping < 250 ? Ping * 0.00212 : Ping * 0.031;
        MaxReach += YawDifference * 0.008;

        double ChanceVal = Math.round(Math.abs((Difference - MaxReach) * 100));

        if (ChanceVal > 100) {
            ChanceVal = 100;
        }

        if (MaxReach < Difference) {
            flag( player, MathUtils.trim(4, Difference) + " > " + MaxReach + " Ping:" + Ping
                    + " Yaw Difference: " + YawDifference, null);
        }
    }

}