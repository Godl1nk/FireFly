package rip.firefly.check.impl.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import rip.firefly.util.lag.LagUtil;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.player.PlayerUtil;
import rip.firefly.util.velocity.VelocityUtil;

import java.text.DecimalFormat;

@CheckData(name = "Speed", subType = "E", description = "Checks If A Player Is Going Over The Speed Limit", type = CheckType.MOVEMENT, threshold = 7)
public class SpeedE extends PacketCheck {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final double[] bhopValues = {0.313D, 0.612D, 0.36D, 0.353D, 0.347D, 0.341D, 0.336D, 0.331D, 0.327D, 0.323D, 0.32D, 0.316D};

    public SpeedE(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject fpacket) {
        if (fpacket instanceof WrappedInFlyingPacket) {
            WrappedInFlyingPacket packet = (WrappedInFlyingPacket) fpacket;

            if((System.currentTimeMillis() - playerData.getLastJoin()) < 5000 || ((System.currentTimeMillis() -  playerData.getLastTeleport()) < 300L)) return;

            if(Bukkit.getPlayer(playerData.getUuid()).isFlying() || Bukkit.getPlayer(playerData.getUuid()).getGameMode().equals(GameMode.CREATIVE)) return;
            Player p = Bukkit.getPlayer(playerData.getUuid());

            if (VelocityUtil.didTakeVel(p) || p.getGameMode().equals(GameMode.CREATIVE)) return;

            if (PlayerUtil.isOnGround(playerData.getTo().toBukkit(p.getWorld()))) {
                playerData.setBhopDelay(0);
            } else {
                playerData.setBhopDelay(playerData.getBhopDelay() + 1);
            }
            if (p.getVehicle() == null && !p.getAllowFlight()) {
                double maxSpeed = calculateMaxSpeed(playerData, playerData.getFrom(), playerData.getTo());
                double speed = PlayerUtil.getHDistance(playerData.getFrom(), playerData.getTo());

                double lagThreshold = 10.0D;
                if (speed > (Bukkit.getPlayer(playerData.getUuid()).isFlying() ? maxSpeed * (Bukkit.getPlayer(playerData.getUuid()).getFlySpeed() * 10) : maxSpeed * (Bukkit.getPlayer(playerData.getUuid()).getWalkSpeed()*10)) && speed < lagThreshold) { // < 10 in case of lag
                    final double beforeSpeed = speed;
                    speed = recalculateFail(playerData, speed);

                    if (speed > 0.0D) {
                        int alertThreshold = 3;
                        if (incrementBuffer() >= alertThreshold) {
                            flag(playerData, String.format("D: %s", (speed-maxSpeed)), String.format("AT: %s", alertThreshold));
                            resetBuffer();
                        }
                    }
                } else if (speed > (Bukkit.getPlayer(playerData.getUuid()).isFlying() ? maxSpeed * (Bukkit.getPlayer(playerData.getUuid()).getFlySpeed() * 10) : maxSpeed * (Bukkit.getPlayer(playerData.getUuid()).getWalkSpeed()*10)) && speed > lagThreshold) {
                    // Above lag threshold, need to account for this in case it is NOT lag and they are
                    // Just moving very fast
                    double tps = LagUtil.getTPS();
                    double minTpsIgnoreLagThreshold = 17;
                    if (tps >= minTpsIgnoreLagThreshold) {
                        int lagThresholdExceedAttempts = 5;
                        if (incrementBuffer() >= lagThresholdExceedAttempts) {
                            flag(playerData, String.format("D: %s", (speed-maxSpeed)), String.format("LT: %s", lagThresholdExceedAttempts));
                            resetBuffer();
                        }
                    }
                }
            }
        } else if(fpacket instanceof WrappedOutVelocityPacket) {
            WrappedOutVelocityPacket velPacket = (WrappedOutVelocityPacket)fpacket;

            Player p = velPacket.getPlayer();
            // Explosion;
            double x = Math.abs(velPacket.getX()) * 5.0D;
            double z = Math.abs(velPacket.getZ()) * 5.0D;
            if (x + z > playerData.getHFreedom()) {
                playerData.setHFreedom(x + z);
            }
        }
    }

    private double calculateMaxSpeed(PlayerData p, PlayerLocation from, PlayerLocation to) {

        double movementSpeed = 0.287D;
        double speed = movementSpeed * (Bukkit.getPlayer(p.getUuid()).getWalkSpeed() / 0.2D);

        for (PotionEffect effect : Bukkit.getPlayer(p.getUuid()).getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                int amp = effect.getAmplifier() + 1;
                double speedAmplifier = 0.125D;
                speed += speed * speedAmplifier * amp;
                double speedMultiplier = 2.5D;
                speed *= speedMultiplier;
            }
        }

        if (playerData.underBlock) {
            if (playerData.getHFreedom() < 1.7175D) {
                playerData.setHFreedom(1.7175D);
            }
        }

        if (playerData.onIce) {
            if (playerData.getHFreedom() < 2.175D) {
                playerData.setHFreedom(2.175D);
            }
        }

        double deltaY = to.getY() - from.getY();

        if (deltaY > 0.0D && (deltaY < 0.6D) && (playerData.getHFreedom() < (deltaY * 1.3))) {
            playerData.setHFreedom(deltaY * 1.3D);
        }

        if (playerData.getHFreedom() > 10.0D) {
            playerData.setHFreedom(10.0D);
        }
        return speed;
    }

    private double recalculateFail(PlayerData rp, double hDistance) {
        double bhop = getBunnyHop(rp);
        hDistance -= bhop;
        if (hDistance > 0.0D) {
            rp.setHFreedom(rp.getHFreedom() - (hDistance / 2.0D));
            if (rp.getHFreedom() > 0.0D) {
                hDistance = 0.0D;
            }
        }
        return hDistance;
    }

    private double getBunnyHop(PlayerData rp) {
        if (rp.getBhopDelay() > (bhopValues.length - 1)) {
            double defaultBhop = 0.33D;
            return defaultBhop;
        }
        return bhopValues[rp.getBhopDelay()];
    }
}
