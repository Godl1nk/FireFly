package ac.firefly.check.impl.combat;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.ServerUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Regen extends Check {
    public Regen() {
        super("Regen (A)", CheckType.COMBAT, true);
    }

    public static Map<UUID, Long> LastHeal = new HashMap<UUID, Long>();
    public static Map<UUID, Map.Entry<Integer, Long>> FastHealTicks = new HashMap<UUID, Map.Entry<Integer, Long>>();

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        if (LastHeal.containsKey(uuid)) {
            LastHeal.remove(uuid);
        }
        if (FastHealTicks.containsKey(uuid)) {
            FastHealTicks.remove(uuid);
        }
    }

    public boolean checkFastHeal(Player player) {
        if (LastHeal.containsKey(player.getUniqueId())) {
            long l = LastHeal.get(player.getUniqueId()).longValue();
            LastHeal.remove(player.getUniqueId());
            if (System.currentTimeMillis() - l < 3000L) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onHeal(EntityRegainHealthEvent event) {
        if (!event.getRegainReason().equals(EntityRegainHealthEvent.RegainReason.SATIATED)
                || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        int Count = 0;
        long Time = System.currentTimeMillis();
        if (FastHealTicks.containsKey(player.getUniqueId())) {
            Count = FastHealTicks.get(player.getUniqueId()).getKey().intValue();
            Time = FastHealTicks.get(player.getUniqueId()).getValue().longValue();
        }
        if (checkFastHeal(player) && !ServerUtils.isFullyStuck(player) && !ServerUtils.isPartiallyStuck(player)) {
            Count++;
        } else {
            Count = Count > 0 ? Count - 1 : Count;
        }

        if(Count > 2) {
            flag(player, "Experimental", "Count: " + Count);
        }
        if (FastHealTicks.containsKey(player.getUniqueId()) && ServerUtils.elapsed(Time, 60000L)) {
            Count = 0;
            Time = ServerUtils.nowlong();
        }
        LastHeal.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
        FastHealTicks.put(player.getUniqueId(),
                new AbstractMap.SimpleEntry<Integer, Long>(Count, Time));
    }
}