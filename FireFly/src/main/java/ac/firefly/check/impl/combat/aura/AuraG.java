package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.check.impl.movement.phase.PhaseA;
import ac.firefly.util.interaction.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.Bukkit;

import java.util.*;

public class AuraG extends Check {

    public static HashMap<Player, Integer> counts = new HashMap<>();
    private ArrayList<Player> blockGlitched = new ArrayList<>();


    public AuraG() {
        super("Aura (G)", CheckType.COMBAT, true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogout(PlayerQuitEvent e) {
        counts.remove(e.getPlayer());
        blockGlitched.remove(e.getPlayer());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.isCancelled()) {
            blockGlitched.add(e.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void checkKillaura(EntityDamageByEntityEvent e) {
        if (e.getCause() != DamageCause.ENTITY_ATTACK || !(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player))
            return;
        Player p = (Player) e.getDamager();
        if (PlayerUtils.slabsNear(p.getEyeLocation()) || PlayerUtils.slabsNear(p.getEyeLocation().clone().add(0.0D, 0.5D, 0.0D)))
            return;
        int Count = 0;
        if (counts.containsKey(p)) {
            Count = counts.get(p);
        }
        Player attacked = (Player) e.getEntity();
        Location dloc = p.getLocation();
        Location aloc = attacked.getLocation();
        double zdif = Math.abs(dloc.getZ() - aloc.getZ());
        double xdif = Math.abs(dloc.getX() - aloc.getX());
        if (xdif == 0 || zdif == 0 || PlayerUtils.getOffsetOffCursor(p, attacked) > 20) return;
        for (int y = 0; y < 1; y += 1) {
            Location zBlock = zdif < -0.2 ? dloc.clone().add(0.0D, y, zdif) : aloc.clone().add(0.0D, y, zdif);
            if (!PhaseA.allowed.contains(zBlock.getBlock().getType()) && zBlock.getBlock().getType().isSolid() && !p.hasLineOfSight(attacked) && !PlayerUtils.isSlab(zBlock.getBlock())) {
                Count++;
            }
            Location xBlock = xdif < -0.2 ? dloc.clone().add(xdif, y, 0.0D) : aloc.clone().add(xdif, y, 0.0D);
            if (!PhaseA.allowed.contains(xBlock.getBlock().getType()) && xBlock.getBlock().getType().isSolid() && !p.hasLineOfSight(attacked) && !PlayerUtils.isSlab(xBlock.getBlock())) {
                Count++;
            }
        }
        if (Count > 3) {
            flag(p, "Experimental", "Experimental");
            Count = 0;
        }
        counts.put(p, Count);
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