package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AuraH extends Check {

    private float lastYaw;
    private float lastBad;


    // Huzuni Check //

    public AuraH() {
        super("Aura (H)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        float f;
        if (!(entityDamageByEntityEvent.getDamager() instanceof Player)) {
            return;
        }
        if (!(entityDamageByEntityEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityDamageByEntityEvent.getDamager();
        this.lastYaw = f = player.getLocation().getYaw();
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
    }

    public boolean onAim(Player player, float f) {
        float f2 = Math.abs(f - this.lastYaw) % 180.0f;
        this.lastYaw = f;
        this.lastBad = (float)Math.round(f2 * 10.0f) * 0.1f;
        if ((double)f < 0.1) {
            return true;
        }
        if (f2 > 1.0f && (float)Math.round(f2 * 10.0f) * 0.1f == f2 && (float)Math.round(f2) != f2) {
            if (f2 == this.lastBad) {
                flag(player, "Huzuni", "Huzuni Aura");
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}

