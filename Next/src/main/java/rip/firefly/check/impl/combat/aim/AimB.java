package rip.firefly.check.impl.combat.aim;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.MathUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CheckData(name = "Aim", subType = "B", description = "Checks If The Player's Rotation GCD Is Lower Than Possible", type = CheckType.COMBAT, threshold = 7)
public class AimB extends PacketCheck {

    private float previous;
    private List<Float> previousYaws = new ArrayList<Float>();
    private float previousPitchChange = 0.0f;
    private float previousYawChange = 0.0f;

    public AimB(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInFlyingPacket && ((WrappedInFlyingPacket) packet).isLook()) {
            if (hasExpired(playerData.getLastAttack(), 3L) || Bukkit.getPlayer(playerData.getUuid()).isSneaking()) {
                return;
            }
            Location to = playerData.getTo().toBukkit(Bukkit.getPlayer(playerData.getUuid()).getWorld());
            Location from = playerData.getFrom().toBukkit(Bukkit.getPlayer(playerData.getUuid()).getWorld());
            float pitchChange = Math.abs(to.getPitch() - from.getPitch());
            float yawChange = Math.abs(to.getYaw() - from.getYaw());
            float pitchChangeDelta = Math.abs(pitchChange - this.previousPitchChange);
            float yawChangeDelta = Math.abs(yawChange - this.previousYawChange);
            float pitchYawCoef = Math.abs(pitchChange % yawChange);
            float previousPitchYawCoef = Math.abs(this.previousPitchChange % this.previousYawChange);
            float pycDelta = Math.abs(pitchYawCoef - previousPitchYawCoef);
            long a = (long) ((double) pitchChange * MathUtil.EXPANDER);
            long b = (long) ((double) this.previousPitchChange * MathUtil.EXPANDER);
            long pGCD = MathUtil.gcd(16384L, a, b);
            long c = (long) ((double) yawChange * MathUtil.EXPANDER);
            long d = (long) ((double) this.previousYawChange * MathUtil.EXPANDER);
            long yGCD = MathUtil.gcd(16384L, c, d);
            if ((double) yawChange > 0.9 && (double) pitchChange < 15.0 && (double) pitchChangeDelta > 0.5 && yGCD < 131072L && pGCD < 131072L && pycDelta > 1.0f && Math.abs(to.getPitch()) < 30.0f) {
                if (incrementBuffer() < 20) {
                    flag(playerData, String.format("PG: %s", pGCD), String.format("PD: %s", pycDelta), String.format("YG: %s", yGCD));
                } else {
                    decreaseBuffer(5);
                }
            }
            this.previous = pitchChange;
            this.previousPitchChange = pitchChange;
            this.previousYawChange = yawChange;
        }
    }

    public static boolean hasExpired (long timestamp, long seconds){
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(seconds);
    }
}
