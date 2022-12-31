package ac.firefly.managers;

import ac.firefly.check.Check;
import ac.firefly.check.impl.combat.Criticals;
import ac.firefly.check.impl.combat.FastBow;
import ac.firefly.check.impl.combat.aim.*;
import ac.firefly.check.impl.combat.aura.*;
import ac.firefly.check.impl.combat.autoclicker.AutoClickerA;
import ac.firefly.check.impl.combat.autoclicker.AutoClickerB;
import ac.firefly.check.impl.combat.noswing.NoSwingA;
import ac.firefly.check.impl.combat.noswing.NoSwingB;
import ac.firefly.check.impl.combat.reach.ReachA;
import ac.firefly.check.impl.combat.Regen;
import ac.firefly.check.impl.combat.reach.ReachB;
import ac.firefly.check.impl.combat.reach.ReachC;
import ac.firefly.check.impl.combat.triggerbot.TriggerBotA;
import ac.firefly.check.impl.combat.velocity.VelocityA;
import ac.firefly.check.impl.combat.velocity.VelocityB;
import ac.firefly.check.impl.movement.*;
import ac.firefly.check.impl.movement.fly.*;
import ac.firefly.check.impl.movement.inventory.InventoryA;
import ac.firefly.check.impl.movement.inventory.InventoryB;
import ac.firefly.check.impl.movement.noslow.NoSlowA;
import ac.firefly.check.impl.movement.noslow.NoSlowB;
import ac.firefly.check.impl.movement.noslow.NoSlowC;
import ac.firefly.check.impl.movement.noslow.NoSlowD;
import ac.firefly.check.impl.movement.phase.PhaseA;
import ac.firefly.check.impl.movement.phase.PhaseB;
import ac.firefly.check.impl.movement.phase.PhaseC;
import ac.firefly.check.impl.movement.scaffold.ScaffoldD;
import ac.firefly.check.impl.movement.speed.*;
import ac.firefly.check.impl.movement.step.StepA;
import ac.firefly.check.impl.movement.waterwalk.WaterWalk;
import ac.firefly.check.impl.other.payload.PayloadA;
import ac.firefly.check.impl.movement.scaffold.ScaffoldA;
import ac.firefly.check.impl.movement.scaffold.ScaffoldB;
import ac.firefly.check.impl.movement.scaffold.ScaffoldC;
import ac.firefly.check.impl.other.payload.PayloadB;
import ac.firefly.check.impl.player.NoFall;
import ac.firefly.check.impl.player.derp.DerpA;
import ac.firefly.check.impl.player.ChestAura;
import ac.firefly.check.impl.player.derp.DerpB;
import ac.firefly.check.impl.player.timer.TimerA;
import ac.firefly.data.PlayerData;
import org.bukkit.entity.Player;

import java.util.*;

public class DataManager {

    public List<Check> checks;
    public List<PlayerData> players;
    private Map<Player, Map<Check, Integer>> violations;

    public DataManager() {
        checks = new ArrayList<>();
        violations = new WeakHashMap<>();
        players = new ArrayList<>();
        addChecks();
    }

    private void addChecks() {

        // Combat
        addCheck(new Criticals());

        addCheck(new FastBow());

        addCheck(new AuraA());
        addCheck(new AuraB());
        addCheck(new AuraC());
        addCheck(new AuraD());
        addCheck(new AuraE());
        addCheck(new AuraF());
        addCheck(new AuraG());
        addCheck(new AuraH());
        addCheck(new AuraI());

        addCheck(new ReachA());
        addCheck(new ReachB());
        addCheck(new ReachC());

        addCheck(new NoSwingA());
        addCheck(new NoSwingB());

        addCheck(new Regen());

        addCheck(new AutoClickerA());
        addCheck(new AutoClickerB());

        addCheck(new TriggerBotA());

        addCheck(new AimA());
        addCheck(new AimB());
        addCheck(new AimC());
        addCheck(new AimD());
        addCheck(new AimE());
        addCheck(new AimF());
        addCheck(new AimG());
        addCheck(new AimH());
        addCheck(new AimI());
        addCheck(new AimJ());
        addCheck(new AimK());
        addCheck(new AimL());

        addCheck(new VelocityA());
        addCheck(new VelocityB());

        // Movement
        addCheck(new FlyA());
        addCheck(new FlyB());
        addCheck(new FlyC());
        addCheck(new FlyD());
        addCheck(new FlyE());
        addCheck(new FlyF());
        addCheck(new FlyG());

        addCheck(new SpeedA());
        addCheck(new SpeedB());
        addCheck(new SpeedC());
        addCheck(new SpeedD());
        addCheck(new SpeedE());
        addCheck(new SpeedF());
        addCheck(new SpeedG());
        addCheck(new SpeedH());

        addCheck(new Gravity());
        addCheck(new WaterWalk());

        addCheck(new NoSlowA());
        addCheck(new NoSlowB());
        addCheck(new NoSlowC());
        addCheck(new NoSlowD());

        addCheck(new PhaseA());
        addCheck(new PhaseB());
        addCheck(new PhaseC());

        addCheck(new StepA());

        addCheck(new FastLadder());

        addCheck(new InventoryA());
        addCheck(new InventoryB());

        // Player //
        addCheck(new NoFall());

        addCheck(new DerpA());
        addCheck(new DerpB());

        addCheck(new ChestAura());

        addCheck(new TimerA());

        // World
        addCheck(new ScaffoldA());
        addCheck(new ScaffoldB());
        addCheck(new ScaffoldC());
        addCheck(new ScaffoldD());

        // Other
        addCheck(new PayloadA());
        addCheck(new PayloadB());
    }

    public void removeCheck(Check check) {
        if (checks.contains(check)) checks.remove(check);
    }

    public boolean isCheck(Check check) {
        return checks.contains(check);
    }

    public Check getCheckByName(String checkName) {
        for (Check checkLoop : Collections.synchronizedList(checks)) {
            if (checkLoop.getName().equalsIgnoreCase(checkName)) return checkLoop;
        }

        return null;
    }

    public Map<Player, Map<Check, Integer>> getViolationsMap() {
        return violations;
    }

    public int getViolatonsPlayer(Player player, Check check) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            return vlMap.getOrDefault(check, 0);
        }
        return 0;
    }

    public void addViolation(Player player, Check check) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            vlMap.put(check, vlMap.getOrDefault(check, 0) + 1);
            violations.put(player, vlMap);
        } else {
            Map<Check, Integer> vlMap = new HashMap<>();

            vlMap.put(check, 1);

            violations.put(player, vlMap);
        }
    }

    public void resetViolation(Player player) {
        if (violations.containsKey(player)) {
            Map<Check, Integer> vlMap = violations.get(player);

            for (Check check : PluginManager.instance.getDataManager().getChecks()) {
                vlMap.put(check, 0);
            }
            violations.put(player, vlMap);
        } else {
            Map<Check, Integer> vlMap = new HashMap<>();

            for (Check check : PluginManager.instance.getDataManager().getChecks()) {
                vlMap.put(check, 1);
            }
            violations.put(player, vlMap);
        }
    }

    public void addPlayerData(Player player) {
        players.add(new PlayerData(player));
    }

    public PlayerData getData(Player player) {
        for (PlayerData dataLoop : Collections.synchronizedList(players)) {
            if (dataLoop.getPlayer() == player) {
                return dataLoop;
            }
        }
        return null;
    }

    public void removePlayerData(Player player) {
        for (PlayerData dataLoop : Collections.synchronizedList(players)) {
            if (dataLoop.getPlayer() == player) {
                players.remove(dataLoop);
                break;
            }
        }
    }

    public void addCheck(Check check) {
        if (!checks.contains(check)) checks.add(check);
    }

    public List<Check> getChecks() {
        return checks;
    }
}
