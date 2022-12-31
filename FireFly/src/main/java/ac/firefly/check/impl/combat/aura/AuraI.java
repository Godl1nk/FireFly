package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AuraI extends Check {

    private double vl;
    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private List<Float> previousYaws = new ArrayList<Float>();
    private float previousPitchChange = 0.0f;
    private float previousYawChange = 0.0f;
    private boolean flagged;
    private int count;

    // GCD Check //

    public AuraI() {
        super("Aura (I)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());
        if (hasExpired(data.getLastAttack(), 3L) || data.getPlayer().isSneaking()) {
            return;
        }
        Location to = e.getTo();
        Location from = e.getFrom();
        float pitchChange = Math.abs(to.getPitch() - from.getPitch());
        float yawChange = Math.abs(to.getYaw() - from.getYaw());
        float pitchChangeDelta = Math.abs(pitchChange - this.previousPitchChange);
        float yawChangeDelta = Math.abs(yawChange - this.previousYawChange);
        float pitchYawCoef = Math.abs(pitchChange % yawChange);
        float previousPitchYawCoef = Math.abs(this.previousPitchChange % this.previousYawChange);
        float pycDelta = Math.abs(pitchYawCoef - previousPitchYawCoef);
        long a = (long)((double)pitchChange * this.multiplier);
        long b = (long)((double)this.previousPitchChange * this.multiplier);
        long pGCD = MathUtils.gcd(16384L, a, b);
        long c = (long)((double)yawChange * this.multiplier);
        long d = (long)((double)this.previousYawChange * this.multiplier);
        long yGCD = MathUtils.gcd(16384L, c, d);
        long g = (long)((double)pitchYawCoef * this.multiplier);
        long h = (long)((double)previousPitchYawCoef * this.multiplier);
        long coefGCD = MathUtils.gcd(16384L, g, h);
        if ((double)yawChange > 0.9 && (double)pitchChange < 15.0 && (double)pitchChangeDelta > 0.5 && yGCD < 131072L && pGCD < 131072L && pycDelta > 1.0f && Math.abs(to.getPitch()) < 30.0f) {
            if (this.count++ < 20) {
                flag(e.getPlayer(), String.format("PD: %s PG: %s YG: %s", pycDelta, pGCD, yGCD), String.format("PD: %s PG: %s YG: %s", pycDelta, pGCD, yGCD));
                this.flagged = true;
            } else {
                this.count -= 5;
            }
        }
        this.previous = pitchChange;
        this.previousPitchChange = pitchChange;
        this.previousYawChange = yawChange;
        this.flagged = false;
    }

    public static boolean hasExpired(long timestamp, long seconds) {
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(seconds);
    }

}

