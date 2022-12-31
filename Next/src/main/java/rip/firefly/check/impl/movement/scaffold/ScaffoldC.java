package rip.firefly.check.impl.movement.scaffold;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Scaffold", subType = "C", description = "Checks for invalid deceleration when rotating", threshold = 8, type = CheckType.MOVEMENT)
public class ScaffoldC extends MovementCheck {

    public ScaffoldC(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate packet) {
        if(!packet.isRotation()) return;
    //    if(packet.getObject() instanceof WrappedInFlyingPacket) {
      //      WrappedInFlyingPacket wrapped = (WrappedInFlyingPacket) packet.getObject();
        //    if(wrapped.isLook()) {
                final float deltaYaw = playerData.getMovement().getDeltaYaw();

                final double deltaXZ = playerData.getMovement().getDeltaH();
                final double lastDeltaXZ = playerData.getMovement().getLastDeltaH();

                final double accel = Math.abs(deltaXZ - lastDeltaXZ);

                final double squaredAccel = accel * 100;

                final boolean exempt = Bukkit.getPlayer(playerData.getUuid()).getVehicle() == null || playerData.isOnLadder() || playerData.isTeleporting() || (System.currentTimeMillis() - playerData.getLastJoin()) < 5000;

                final boolean declaring = deltaYaw < 1.5F && deltaXZ < .150 && squaredAccel > 1.0E-5;

                if(!declaring && !exempt && incrementBuffer() > 3) {
                    flag(playerData, String.format("DY: %s DH: %s ACC: %s", deltaYaw, deltaXZ, accel));
                } else {
                    decreaseBuffer(0.2);
                }
          //  }
       // }
    }
}
