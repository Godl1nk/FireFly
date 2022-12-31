package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketUseEntityEvent;
import ac.firefly.util.interaction.ServerUtils;
import ac.firefly.util.interaction.TimerUtils;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class AuraF extends Check {

    public static Map<UUID, Long> LastMS;
    public static Map<UUID, List<Long>> Clicks;
    public static Map<UUID, Map.Entry<Integer, Long>> ClickTicks;

    public AuraF() {
        super("Aura (F)", CheckType.COMBAT, true);
        LastMS = new HashMap<>();
        Clicks = new HashMap<>();
        ClickTicks = new HashMap<>();
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        LastMS.remove(uuid);
        Clicks.remove(uuid);
        ClickTicks.remove(uuid);
    }

    @EventHandler
    public void onUseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK
                || !((e.getEntity()) instanceof Player)) return;

        Player damager = e.getPlayer();
        int Count = 0;
        long Time = System.currentTimeMillis();
        if (ClickTicks.containsKey(damager.getUniqueId())) {
            Count = ClickTicks.get(damager.getUniqueId()).getKey();
            Time = ClickTicks.get(damager.getUniqueId()).getValue();
        }
        if (LastMS.containsKey(damager.getUniqueId())) {
            long MS = TimerUtils.nowlong() - LastMS.get(damager.getUniqueId());
            if (MS > 500L || MS < 5L) {
                LastMS.put(damager.getUniqueId(), TimerUtils.nowlong());
                return;
            }
            if (Clicks.containsKey(damager.getUniqueId())) {
                List<Long> Clicks = AuraF.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 10) {
                    AuraF.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    final long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range < 30L) {
                        ++Count;
                        Time = System.currentTimeMillis();
                    }
                } else {
                    Clicks.add(MS);
                    AuraF.Clicks.put(damager.getUniqueId(), Clicks);
                }
            } else {
                final List<Long> Clicks = new ArrayList<>();
                Clicks.add(MS);
                AuraF.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (ClickTicks.containsKey(damager.getUniqueId()) && TimerUtils.elapsed(Time, 5000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if ((Count > 2 && getPing(damager) < 100)
                || (Count > 4 && getPing(damager) <= 400)) {
            Count = 0;
            flag(damager, null, null);
            ClickTicks.remove(damager.getUniqueId());
        } else if (getPing(damager) > 400) {
            ServerUtils.logDebug(damager, "Would set off Killaura (Click Pattern) but latency is too high!");
        }
        LastMS.put(damager.getUniqueId(), TimerUtils.nowlong());
        ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }

    public int getPing(Player who) {
        try {
            String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle").invoke(who);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return 404;
        }
    }
}