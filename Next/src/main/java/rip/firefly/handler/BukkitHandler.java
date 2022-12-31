package rip.firefly.handler;

import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import rip.firefly.FireFly;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.types.BukkitCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.DataManager;
import rip.firefly.util.velocity.VelocityUtil;

import java.util.Set;
import java.util.UUID;

public class BukkitHandler implements Listener {

    public BukkitHandler(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getPluginManager().registerEvents(new VelocityUtil(), plugin);
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent e) {

        e.getPlayer().sendMessage("\u00A7r\u00A71\u00A70\u00A73\u00A73\u00A73\u00A75\u00A76");

    }

    @EventHandler
    public void onEvent(EntityDamageEvent e) {

        if(e instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                int ticks = DataManager.getData((Player) e.getEntity()).getCticks();
                if (e.isCancelled()) {
                    ticks += (ticks < 20 ? 1 : 0);
                } else {
                    ticks -= (ticks > 0 ? 5 : 0);
                }
                DataManager.getData((Player) e.getEntity()).setCticks(ticks);
            }
        }

    }

  /*  @EventHandler
    public void onEvent(PlayerQuitEvent e) {
        PlayerData data = DataManager.getData(e.getPlayer());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (AbstractCheck check : data.getCheckManager().getChecks()) {
                    if (check instanceof BukkitCheck) {
                        BukkitCheck bukkitCheck = (BukkitCheck) check;

                        bukkitCheck.handle(data, e);
                    }
                }
            }
        }.runTaskAsynchronously(FireFly.getInstance());
    }*/

    /*@EventHandler
    public void onEvent(PlayerInteractEvent e) {
        PlayerData data = DataManager.getData(e.getPlayer());

        new BukkitRunnable() {
            @Override
            public void run() {
                for (AbstractCheck check : data.getCheckManager().getChecks()) {
                    if (check instanceof BukkitCheck) {
                        BukkitCheck bukkitCheck = (BukkitCheck) check;

                        bukkitCheck.handle(data, e);
                    }
                }
            }
        }.runTaskAsynchronously(FireFly.getInstance());
    }*/

    @EventHandler
    public void onEvent(PlayerTeleportEvent e) {
        PlayerData data = DataManager.getData(e.getPlayer());
        if(data != null) {
            data.setLastTeleport(System.currentTimeMillis());
        }
    }

//    @EventHandler
//    public void onEvent(PlayerRespawnEvent e) {
//        PlayerData data = DataManager.getData(e.getPlayer());
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (AbstractCheck check : data.getCheckManager().getChecks()) {
//                    if (check instanceof BukkitCheck) {
//                        BukkitCheck bukkitCheck = (BukkitCheck) check;
//
//                        bukkitCheck.handle(data, e);
//                    }
//                }
//            }
//        }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
//    }
//
//    @EventHandler
//    public void onEvent(PlayerGameModeChangeEvent e) {
//        PlayerData data = DataManager.getData(e.getPlayer());
//
//        new BukkitRunnable() {
//            @Override
//            public void run() {
//                for (AbstractCheck check : data.getCheckManager().getChecks()) {
//                    if (check instanceof BukkitCheck) {
//                        BukkitCheck bukkitCheck = (BukkitCheck) check;
//
//                        bukkitCheck.handle(data, e);
//                    }
//                }
//            }
//        }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
//    }
}

