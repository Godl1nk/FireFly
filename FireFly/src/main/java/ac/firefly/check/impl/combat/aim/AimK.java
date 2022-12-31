package ac.firefly.check.impl.combat.aim;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class AimK extends Check {

    private float previousPitch, previousYaw, pitch, yaw;

    public AimK() {
        super("Aim (K)", CheckType.COMBAT, true);
    }


    public static HashMap<Player, Integer> AimAssistCount = new HashMap<>();
    public static HashMap<Player, Integer> AimAssistTimer = new HashMap<>();
    public static HashMap<Player, Float> AimAssistLastYaw = new HashMap<>();
    public static HashMap<Player, Float> AimAssistLastPitch = new HashMap<>();
    public static HashMap<Player, Integer> AimAssistHitTimer = new HashMap<>();

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent event)
    {
        PlayerData playerData = PluginManager.instance.getDataManager().getData(event.getPlayer());

        boolean cinematic = playerData.isCinematic();

        Player player = event.getPlayer();
        float yaw_diff = clampYaw(event.getTo().getYaw() - event.getFrom().getYaw());
        float pitch_diff = event.getTo().getPitch() - event.getFrom().getPitch();

        if(cinematic) {
            return;
        }

        if(AimAssistHitTimer.get(player) != null)
        {
            if(AimAssistHitTimer.get(player) > 0)
            {
                if(AimAssistLastPitch.get(player) != null && AimAssistLastYaw.get(player) != null)
                {
                    float yaw_diff_2 = clampYaw(AimAssistLastYaw.get(player) - yaw_diff);
                    float pitch_diff_2 = AimAssistLastPitch.get(player) - pitch_diff;

                    if(Math.abs(AimAssistLastYaw.get(player)) > 1.f) {
                        if (Math.abs(yaw_diff_2) > 0.0 && Math.abs(yaw_diff_2) < 0.3 && Math.abs(pitch_diff_2) > 0.0 && Math.abs(pitch_diff_2) < 0.3) {
                            AimAssistCount.put(player, AimAssistCount.get(player) != null ? AimAssistCount.get(player) + 1 : 1);
                        }
                    }
                }

                AimAssistLastYaw.put(player, yaw_diff);
                AimAssistLastPitch.put(player, pitch_diff);
                AimAssistTimer.put(player, AimAssistTimer.get(player) != null ? AimAssistTimer.get(player) + 1 : 1);

                if(AimAssistTimer.get(player) > 20)
                {
                    if(AimAssistCount.get(player) != null) {
                        if(AimAssistCount.get(player) > 3)
                        {
                            flag(player, "Smoothness: " + AimAssistCount.get(player), "Smoothness: " + AimAssistCount.get(player));
                        }

                        AimAssistCount.put(player,0);
                    }
                    AimAssistTimer.put(player, 0);
                }

                AimAssistHitTimer.put(player, AimAssistHitTimer.get(player) - 1);
            }
        }
    }

    @EventHandler
    public void onEnityDamageByEntity(EntityDamageByEntityEvent event)
    {
        if(event.getDamager() instanceof Player)
        {
            Player player = (Player)event.getDamager();
            AimAssistHitTimer.put(player, 10);
        }
    }

    public static float clampYaw(float value) {
        value = value % 360.0F;

        if (value >= 180.0F) {
            value -= 360.0F;
        }

        if (value < -180.0F) {
            value += 360.0F;
        }

        return value;
    }
}
