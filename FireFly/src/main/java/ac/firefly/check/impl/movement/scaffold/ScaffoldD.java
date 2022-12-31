package ac.firefly.check.impl.movement.scaffold;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.DataManager;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;

public class ScaffoldD extends Check {

    private double multiplier = Math.pow(2.0, 24.0);
    private float previous;
    private double vl;
    private double streak;

    public ScaffoldD() {
        super("Scaffold (D)", CheckType.WORLD, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Location to = e.getTo();
        Location from = e.getFrom();
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());
        if (System.currentTimeMillis() - data.getLastPlace() > 1000L) {
            this.vl = 0.0;
            return;
        }
        float pitchChange = MathUtils.getDistanceBetweenAngles(to.getPitch(), from.getPitch());
        long a = (long)((double)pitchChange * this.multiplier);
        long b = (long)((double)this.previous * this.multiplier);
        long gcd = this.gcd(a, b);
        float magicVal = pitchChange * 100.0f / this.previous;
        if (magicVal > 24.0f) {
            this.vl = Math.max(0.0, this.vl - 1.0);
        }
        if ((double)pitchChange >= 0.05 && pitchChange <= 20.0f && gcd < 131072L) {
            double d = 0;
            this.vl += 1.0;
            if (d > 1.0) {
                this.streak += 1.0;
            }
            if (this.streak > 6.0) {
                flag(e.getPlayer(), "G: " + gcd + " S: " + this.streak + " V: " + this.vl + " P: " + pitchChange + " M: " + magicVal, "");
            }
        } else {
            this.vl = Math.max(0.0, this.vl - 1.0);
            this.streak = Math.max(0.0, this.streak - 0.25);
        }
        this.previous = pitchChange;
    }

    private long gcd(long a, long b) {
        if (b <= 16384L) {
            return a;
        }
        return this.gcd(b, a % b);
    }
}
