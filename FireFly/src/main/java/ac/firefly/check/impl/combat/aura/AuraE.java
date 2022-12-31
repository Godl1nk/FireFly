package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketUseEntityEvent;
import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.TimerUtils;
import ac.firefly.util.interaction.VelocityUtils;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ac.firefly.util.math.MathUtils.*;

public class AuraE extends Check {

    public static Map<UUID, Map.Entry<Integer, Long>> AuraTicks;

    public AuraE() {
        super("Aura (E)", CheckType.COMBAT, true);
        AuraTicks = new HashMap<>();
    }

    @EventHandler
    public void onLog(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();

        AuraTicks.remove(uuid);
    }

    @EventHandler
    public void UseEntity(PacketUseEntityEvent e) {
        if (e.getAction() != EnumWrappers.EntityUseAction.ATTACK
                || !((e.getEntity()) instanceof Player)) return;

        Player damager = e.getPlayer();
        Player player = (Player) e.getEntity();

        if (damager.getAllowFlight()
                || player.getAllowFlight()) return;

        int Count = 0;
        long Time = System.currentTimeMillis();
        if (AuraTicks.containsKey(damager.getUniqueId())) {
            Count = AuraTicks.get(damager.getUniqueId()).getKey();
            Time = AuraTicks.get(damager.getUniqueId()).getValue();
        }
        double OffsetXZ = getAimbotoffset(damager.getLocation(), damager.getEyeHeight(),
                player);
        double LimitOffset = 200.0;
        if (damager.getVelocity().length() > 0.08
                || NEW_Velocity_Utils.didTakeVel(damager)
                || VelocityUtils.didTakeVelocity(damager)) {
            LimitOffset += 200.0;
        }
        int Ping = getPing(damager);
        if (Ping >= 100 && Ping < 200) {
            LimitOffset += 50.0;
        } else if (Ping >= 200 && Ping < 250 || Ping >= 130 && Ping < 250) {
            LimitOffset += 75.0;
        } else if (Ping >= 250 && Ping < 300) {
            LimitOffset += 150.0;
        } else if (Ping >= 300 && Ping < 350) {
            LimitOffset += 300.0;
        } else if (Ping >= 350 && Ping < 400) {
            LimitOffset += 400.0;
        } else if (Ping > 400) return;
        if (OffsetXZ > LimitOffset * 4.0) {
            Count += 12;
        } else if (OffsetXZ > LimitOffset * 3.0) {
            Count += 10;
        } else if (OffsetXZ > LimitOffset * 2.0) {
            Count += 8;
        } else if (OffsetXZ > LimitOffset) {
            Count += 4;
        }
        if (AuraTicks.containsKey(damager.getUniqueId()) && TimerUtils.elapsed(Time, 60000L)) {
            Count = 0;
            Time = TimerUtils.nowlong();
        }
        if (Count >= 16) {
            Count = 0;
            flag(damager, null, null);
        }
        AuraTicks.put(damager.getUniqueId(), new AbstractMap.SimpleEntry<>(Count, Time));
    }

    public static double getAimbotoffset(Location playerLocLoc, double playerEyeHeight, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0D, entity.getEyeHeight(), 0.0D);
        Location playerLoc = playerLocLoc.add(0.0D, playerEyeHeight, 0.0D);

        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0F);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);

        double deltaYaw = clamp180(playerRotation.getX() - expectedRotation.getX());

        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);

        double offsetX = deltaYaw * horizontalDistance * distance;

        return offsetX;
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