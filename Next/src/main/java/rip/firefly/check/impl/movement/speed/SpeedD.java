package rip.firefly.check.impl.movement.speed;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Speed", subType = "D", description = "Checks For A Player Going Over The Motion Limit", type = CheckType.MOVEMENT, experimental = true, threshold = 7)
public class SpeedD extends MovementCheck {

    private boolean lastGround;
    private double speed;
    public SpeedD(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        Player player = Bukkit.getPlayer(playerData.getUuid());
        if (player.getAllowFlight() || !player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }
        final double x = Math.abs(Math.abs(update.getTo().getX()) - Math.abs(update.getFrom().getX()));
        final double z = Math.abs(Math.abs(update.getTo().getZ()) - Math.abs(update.getFrom().getZ()));
        this.speed = Math.sqrt(x * x + z * z);
        double max = 0.6399999856948853;
        for (final PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                max += effect.getAmplifier() + 1;
            }
        }
        final boolean ground = this.playerData.isOnGround();
        if (ground && !this.lastGround && this.speed > max) {
            flag(playerData, String.format("S: %s", speed), String.format("MAX: %s", max));
        }
        this.lastGround = ground;
    }

}
