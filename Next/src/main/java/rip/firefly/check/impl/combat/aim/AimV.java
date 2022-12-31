package rip.firefly.check.impl.combat.aim;

import lombok.val;
import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "V", description = "Checks If The Player's Rotation Change Is Invalid", type = CheckType.COMBAT, threshold = 5)
public class AimV extends MovementCheck {

    private float lastPitchDelta, lastYawDelta;
    private double vl;
    private double lastGCD;

    public AimV(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(playerData.isCinematic()) {
            return;
        }
        if (update.isRotation()) {
            float deltaYaw = Math.abs(playerData.getTo().getYaw() - playerData.getFrom().getYaw());
            float deltaPitch = Math.abs(playerData.getTo().getPitch() - playerData.getFrom().getPitch());
            boolean flagged = false;
            if (deltaYaw != 0.0F && (float)Math.round(deltaYaw) == deltaYaw && increaseBuffer(2) >= 0) {
                flag(playerData, new String[]{"D: " + deltaYaw, "VL: " + getBuffer()});
                flagged = true;
            }

            if (deltaPitch != 0.0F && (float)Math.round(deltaPitch) == deltaPitch && increaseBuffer(2) >= 0) {
                flag(playerData, new String[]{"D: " + deltaPitch, "VL: " + getBuffer()});
                flagged = true;
            }

            if (flagged) {
                if (getBuffer() >= 10) {
                    punish(Bukkit.getPlayer(playerData.getUuid()));
                }
            } else {
                decrementBuffer();
            }
        }

    }
}