package rip.firefly.check.impl.combat.aim;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.util.math.MathUtil;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Aim", subType = "Z", description = "Checks If The Player's Rotation GCD Is Lower Than Possible", type = CheckType.COMBAT, threshold = 10)
public class AimZ extends PacketCheck {

    private int ticks, lastTicks, attacks, streak, buffer;

    public AimZ(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInUseEntityPacket) {
            final WrappedInUseEntityPacket wrapper = (WrappedInUseEntityPacket) packet;

            if (wrapper.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                final boolean duplicate = ticks < 8 && ticks == lastTicks;

                if (duplicate) {
                    ++buffer;
                }

                if (++attacks == 25) {
                    final boolean invalid = buffer > 22;
                    final boolean suspicious = buffer > 15;

                    if (invalid) {
                        flag(playerData, new String[]{"B: " + buffer, "S: " + streak, "T: " + ticks});
                    }

                    if (!suspicious) {
                        streak = 0;
                    }

                    attacks = 0;
                    buffer = 0;
                }

                lastTicks = ticks;
                ticks = 0;
            }
        } else if (packet instanceof WrappedInFlyingPacket) {
            ++ticks;
        }
    }
}