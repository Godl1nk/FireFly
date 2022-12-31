package ac.firefly.events;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.etc.EvictingList;
import ac.firefly.util.math.GraphUtil;
import ac.firefly.util.math.MathUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

public class PerdictionListener implements Listener {
    private float lastDeltaYaw, lastDeltaPitch;
    public static double sensitivityX;
    public static double sensitivityY;
    public static double deltaX;
    public static double deltaY;

    private final EvictingList<Double> yawSamples = new EvictingList<>(50);
    private final EvictingList<Double> pitchSamples = new EvictingList<>(50);

    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        PlayerData playerData = PluginManager.instance.getDataManager().getData(e.getPlayer());

        final Location from = e.getFrom();
        final Location to = e.getTo();

        final float deltaYaw = Math.abs(to.getYaw() - from.getYaw());
        final float deltaPitch = Math.abs(to.getPitch() - from.getPitch());

        final double gcdYaw = MathUtils.getGcd((long) (deltaYaw * MathUtils.EXPANDER), (long) (lastDeltaYaw * MathUtils.EXPANDER));
        final double gcdPitch = MathUtils.getGcd((long) (deltaPitch * MathUtils.EXPANDER), (long) (lastDeltaPitch * MathUtils.EXPANDER));

        final double dividedYawGcd = gcdYaw / MathUtils.EXPANDER;
        final double dividedPitchGcd = gcdPitch / MathUtils.EXPANDER;

        if (gcdYaw > 90000 && gcdYaw < 2E7 && dividedYawGcd > 0.01f && deltaYaw < 8) {
            yawSamples.add(dividedYawGcd);
        }

        if (gcdPitch > 90000 && gcdPitch < 2E7 && deltaPitch < 8) {
            pitchSamples.add(dividedPitchGcd);
        }

        double modeYaw = 0.0;
        double modePitch = 0.0;

        if (pitchSamples.size() > 3 && yawSamples.size() > 3) {
            modeYaw = MathUtils.getMode(yawSamples);
            modePitch = MathUtils.getMode(pitchSamples);
        }

        final double deltaX = deltaYaw / modeYaw;
        final double deltaY = deltaPitch / modePitch;

        final double sensitivityX = getSensitivityFromYawGCD(modeYaw);
        final double sensitivityY = getSensitivityFromPitchGCD(modePitch);

        playerData.setSensitivityX(sensitivityX);
        playerData.setSensitivityY(sensitivityY);

        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.sensitivityX = sensitivityX;
        this.sensitivityY = sensitivityY;
        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }

    private static double yawToF2(double yawDelta) {
        return yawDelta / .15;
    }

    private static double pitchToF3(double pitchDelta) {
        int b0 = pitchDelta >= 0 ? 1 : -1; //Checking for inverted mouse.
        return pitchDelta / .15 / b0;
    }

    private static double getSensitivityFromPitchGCD(double gcd) {
        double stepOne = pitchToF3(gcd) / 8;
        double stepTwo = Math.cbrt(stepOne);
        double stepThree = stepTwo - .2f;
        return stepThree / .6f;
    }

    private static double getSensitivityFromYawGCD(double gcd) {
        double stepOne = yawToF2(gcd) / 8;
        double stepTwo = Math.cbrt(stepOne);
        double stepThree = stepTwo - .2f;
        return stepThree / .6f;
    }

    private static double getFFromYaw(double gcd) {
        double sens = getSensitivityFromYawGCD(gcd);
        return sens * .6f + .2;
    }
}
