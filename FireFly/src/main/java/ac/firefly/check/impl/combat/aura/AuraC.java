package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class AuraC extends Check {
    public static Map<Player, Map.Entry<Integer, Long>> lastAttack;

    public AuraC() {
        super("Aura (C)", CheckType.COMBAT, true);
        lastAttack = new HashMap<>();
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        lastAttack.remove(e.getPlayer());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onAttack(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                || !((e.getEntity()) instanceof Player)
                || !(e.getDamager() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getDamager();
        if (lastAttack.containsKey(player)) {
            Integer entityid = lastAttack.get(player).getKey();
            Long time = lastAttack.get(player).getValue();
            if (entityid != e.getEntity().getEntityId() && System.currentTimeMillis() - time < 6L) {
                flag(player, null, null);
            }
            lastAttack.remove(player);
        } else {
            lastAttack.put(player, new AbstractMap.SimpleEntry<>(e.getEntity().getEntityId(),
                    System.currentTimeMillis()));
        }
    }
}