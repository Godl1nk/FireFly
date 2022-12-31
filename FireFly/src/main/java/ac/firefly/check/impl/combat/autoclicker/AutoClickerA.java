package ac.firefly.check.impl.combat.autoclicker;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.events.packet.PacketSwingArmEvent;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.ServerUtils;
import ac.firefly.util.interaction.TimerUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class AutoClickerA extends Check {
    public static Map<UUID, Long> LastMS;
    public static Map<UUID, List<Long>> Clicks;
    public static Map<UUID, Map.Entry<Integer, Long>> ClickTicks;

    public AutoClickerA() {
        super("AutoClicker (A)", CheckType.COMBAT, true);

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
        if (ClickTicks.containsKey(uuid)) {
            Clicks.remove(uuid);
        }
    }

    /*@EventHandler
    public void onUse(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK || !((e.getEntity()) instanceof Player)) return;
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
                List<Long> Clicks = AutoClickerA.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 3) {
                    AutoClickerA.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range >= 0 && Range <= 2) {
                        ++Count;
                        Time = System.currentTimeMillis();
                    }
                } else {
                    Clicks.add(MS);
                    AutoClickerA.Clicks.put(damager.getUniqueId(), Clicks);
                }
            } else {
                List<Long> Clicks = new ArrayList<>();
                Clicks.add(MS);
                AutoClickerA.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (ClickTicks.containsKey(damager.getUniqueId()) && TimerUtils.elapsed(Time, 5000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if ((Count > 4 && getPing(damager) < 100) || (Count > 6 && getPing(damager) < 200)) {
            Count = 0;
            flag(damager, "Continuous/Repeating Patterns", null);
            ClickTicks.remove(damager.getUniqueId());
        } else if (getPing(damager) > 250) {
            ServerUtils.logDebug(damager, "Would set off Autoclicker (Constant) but latency is too high!");
            //Just for fun: I usually get 230-270 ping on USA servers so WHAT IF I CAN BYPASS :O "SethPython"
        }
        LastMS.put(damager.getUniqueId(), TimerUtils.nowlong());
        ClickTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }*/

    @EventHandler
    public void onSwing(PacketSwingArmEvent e) {
        Player damager = e.getPlayer();
        int Count = 0;
        long Time = System.currentTimeMillis();
        PlayerData data = PluginManager.instance.getDataManager().getData(damager);
        if(data.isDigging()) {
            return;
        }
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
                List<Long> Clicks = AutoClickerA.Clicks.get(damager.getUniqueId());
                if (Clicks.size() == 3) {
                    AutoClickerA.Clicks.remove(damager.getUniqueId());
                    Collections.sort(Clicks);
                    long Range = Clicks.get(Clicks.size() - 1) - Clicks.get(0);
                    if (Range >= 0 && Range <= 2) {
                        ++Count;
                        Time = System.currentTimeMillis();
                    }
                } else {
                    Clicks.add(MS);
                    AutoClickerA.Clicks.put(damager.getUniqueId(), Clicks);
                }
            } else {
                List<Long> Clicks = new ArrayList<>();
                Clicks.add(MS);
                AutoClickerA.Clicks.put(damager.getUniqueId(), Clicks);
            }
        }
        if (ClickTicks.containsKey(damager.getUniqueId()) && TimerUtils.elapsed(Time, 5000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if ((Count > 6 && getPing(damager) < 100) || (Count > 8 && getPing(damager) < 200)) {
            Count = 0;
            flag(damager, "Continuous/Repeating Patterns", null);
            ClickTicks.remove(damager.getUniqueId());
        } else if (getPing(damager) > 250) {
            ServerUtils.logDebug(damager, "Would set off Autoclicker (Constant) but latency is too high!");
            //Just for fun: I usually get 230-270 ping on USA servers so WHAT IF I CAN BYPASS :O "SethPython"
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
