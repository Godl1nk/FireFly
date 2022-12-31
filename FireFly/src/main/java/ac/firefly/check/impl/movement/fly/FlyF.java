package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.WeakHashMap;

public class FlyF extends Check {

    private Map<Player, Integer> verbose = new WeakHashMap<>();
    private Map<Player, Float> lastYMovement = new WeakHashMap<>();

    public FlyF() {
        super("Fly (F)", CheckType.MOVEMENT, true);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        int verbose = this.verbose.getOrDefault(player, 0);
        float yDelta = (float) (e.getTo().getY() - e.getFrom().getY());
        if (player.getAllowFlight()
                || !lastYMovement.containsKey(player)
                || Math.abs(yDelta - lastYMovement.get(player)) > 0.002) return;
        if (verbose++ > 5) {
            flag(player, Math.abs(yDelta - lastYMovement.get(player)) + "<-" + 0.002, Math.abs(yDelta - lastYMovement.get(player)) + "<-" + 0.002);
        }
        lastYMovement.put(player, yDelta);
        this.verbose.put(player, verbose);
    }
}
