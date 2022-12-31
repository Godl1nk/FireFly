package rip.firefly.handler;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;

public class CombatHandler {

    public CombatHandler(JavaPlugin plugin) {

    }

    public static void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)packet).getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
            playerData.setLastAttack(System.currentTimeMillis());

            WrappedInUseEntityPacket wrappedPacket = (WrappedInUseEntityPacket)packet;

            if(wrappedPacket.getEntity() instanceof LivingEntity) {
                playerData.setLastHitEntity((LivingEntity) wrappedPacket.getEntity());
            }
            playerData.lastTarget = playerData.target == null ? wrappedPacket.getEntity() : playerData.target;
            playerData.target = wrappedPacket.getEntity();
            if(Bukkit.getPlayer(playerData.getUuid()).getLocation() != null && playerData.target != null && playerData.target.getLocation() != null) {
                playerData.setDistance(Bukkit.getPlayer(playerData.getUuid()).getLocation().toVector().setY(0).distance(playerData.target.getLocation().toVector().setY(0)) - .42);
            }

            playerData.hitTicks = 0;
        } else if(packet instanceof WrappedInArmAnimationPacket) {
            playerData.setLastSwing(System.currentTimeMillis());
        } else if(packet instanceof WrappedInFlyingPacket) {
            ++playerData.hitTicks;
        }
    }
}

