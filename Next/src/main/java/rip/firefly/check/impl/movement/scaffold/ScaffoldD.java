package rip.firefly.check.impl.movement.scaffold;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInBlockPlacePacket;
import rip.firefly.packet.tinyprotocol.packet.types.EnumDirection;
import rip.firefly.util.player.PlayerUtil;

@CheckData(name = "Scaffold", subType = "D", description = "Checks if player is sprinting and bridging at the same time ", threshold = 8, type = CheckType.MOVEMENT)
public class ScaffoldD extends PacketCheck {

    public ScaffoldD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet.getObject() instanceof WrappedInBlockPlacePacket) {
            WrappedInBlockPlacePacket wrapped = (WrappedInBlockPlacePacket) packet.getObject();
            if(wrapped.getFace() == EnumDirection.UP || wrapped.getFace() == EnumDirection.DOWN) {
                return;
            }

            double limit = Bukkit.getPlayer(playerData.getUuid()).hasPotionEffect(PotionEffectType.SPEED) ? 0.270 + ((PlayerUtil.getPotionEffectLevel(Bukkit.getPlayer(playerData.getUuid()), PotionEffectType.SPEED) * 0.05)) : 0.270;

            final boolean invalid = playerData.getMovement().getDeltaH() > limit && isBridging();
            if(invalid) {
                flag(playerData, String.format("DH: %s", playerData.getMovement().getDeltaH()));
            }
        }
    }

    public boolean isBridging() {
        return Bukkit.getPlayer(playerData.getUuid()).getLocation().clone().subtract(0, 2, 0).getBlock().getType() == Material.AIR && Bukkit.getPlayer(playerData.getUuid()).getLocation().clone().subtract(0, 1, 0).getBlock().getType().isSolid() && playerData.getMovement().getTpitch() > 70 && playerData.getMovement().getTpitch() < 81;
    }
}
