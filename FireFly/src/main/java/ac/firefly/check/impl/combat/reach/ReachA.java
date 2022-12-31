package ac.firefly.check.impl.combat.reach;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.events.packet.PacketClientKeepAliveEvent;
import ac.firefly.events.packet.PacketServerKeepAliveEvent;
import ac.firefly.events.packet.PacketUseEntityEvent;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.VelocityUtils;
import ac.firefly.util.location.CustomLocation;
import com.comphenix.protocol.wrappers.EnumWrappers;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReachA extends Check {
    public ReachA() {
        super("Reach (A)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onServerKeepAlive(PacketServerKeepAliveEvent e) {
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());

        data.pingD = System.currentTimeMillis() - data.lastServerKP;
    }

    @EventHandler
    public void onClientKeepAlive(PacketClientKeepAliveEvent e) {
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());

        data.lastServerKP = System.currentTimeMillis();
    }

    @EventHandler
    public void onUsePacket(PacketUseEntityEvent e) {
        Optional<Entity> entityOp = e.getPlayer().getWorld().getEntities().stream().filter(entity -> entity.getEntityId() == e.getPacket().getIntegers().read(0)).findFirst();

        if(Lag.getPing(e.getPlayer()) > 100) {
            return;
        }

        if (entityOp.isPresent()) {
            if(e.getPlayer().getGameMode() == GameMode.CREATIVE
                    || VelocityUtils.didTakeVelocity(e.getPlayer())
                    || NEW_Velocity_Utils.didTakeVel(e.getPlayer())) {
                return;
            }
            Entity entity = entityOp.get();

            EnumWrappers.EntityUseAction action = e.getPacket().getEntityUseActions().read(0);

            if (action.equals(EnumWrappers.EntityUseAction.ATTACK) && entity instanceof LivingEntity) {
                PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());

                if (data != null) {
                    data.lastHitEntity = (LivingEntity) entity;
                    Location origin = data.getPlayer().getLocation();

                    List<Vector> pastLocation = data.entityPastLocations.getEstimatedLocation(data.pingD, 150).stream().map(CustomLocation::toVector).collect(Collectors.toList());

                    float distance = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(origin.toVector().clone().setY(0)) - 0.3f).min().orElse(0);

                    if (distance > 3.1f) {
                        if (data.reachThreshold++ > 10) {
                            flag(e.getPlayer(), "distance=" + distance, "Reach: " + distance);
                        }
                    } else data.reachThreshold -= data.reachThreshold > 0 ? 1 : 0;
                }
            }
        }
    }
}

