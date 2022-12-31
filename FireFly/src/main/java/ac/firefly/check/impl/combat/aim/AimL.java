package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class AimL extends Check {

    private double vl;
    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private List<Float> previousYaws = new ArrayList<Float>();


    // GCD Check //

    public AimL() {
        super("Aim (L)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location to = e.getTo();
        Location from = e.getFrom();
        float pitchChange = Math.abs(to.getPitch() - from.getPitch());
        float yawChange = Math.abs(to.getYaw() - from.getYaw());
        long a = (long)((double)pitchChange * this.multiplier);
        long b = (long)((double)this.previous * this.multiplier);
        long gcd = MathUtils.gcd(16384L, a, b);
        if ((double)yawChange > 0.9 && (double)pitchChange < 15.0 && gcd < 131072L) {
            if ((double)yawChange < 9.7 && (double)pitchChange > 0.05) {
                this.previousYaws.add(yawChange);
                if (vl++ > 17.0) {
                    if (MathUtils.averageFloat(this.previousYaws) > 0.0f) {
                        flag(e.getPlayer(), String.format("Y: %s P: %s GC: %s FA: %s", yawChange, pitchChange, gcd, MathUtils.averageFloat(this.previousYaws)), String.format("Y: %s P: %s GC: %s FA: %s", yawChange, pitchChange, gcd, MathUtils.averageFloat(this.previousYaws)));
                    } else {
                        this.vl = 0.0;
                        this.previousYaws.clear();
                    }
                }
            }
        } else if (this.vl > 0.0) {
            this.vl -= 1.0;
        }
        this.previous = pitchChange;
    }
}

