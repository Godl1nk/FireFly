package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.Distance;
import ac.firefly.util.math.MathUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class AuraB extends Check {

    public AuraB() {
        super("Aura (B)", CheckType.COMBAT, true);
    }

    // Herustic Check //

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        PlayerData playerData = PluginManager.instance.getDataManager().getData(event.getPlayer());
        Distance distance = new Distance(event);
        float yawDiff = (float) MathUtils.round((double)Math.abs(MathUtils.clamp180(distance.getFrom().getYaw() - distance.getTo().getYaw())), 3);

        if (yawDiff > 0.0F)
        {
            int vb = 0;

            if (playerData.getSamples().containsKey(yawDiff))
            {
                vb = playerData.getSamples().get(yawDiff);
                playerData.getSamples().put(yawDiff, vb + 1);
                playerData.setPatternVerbose(playerData.getPatternVerbose() + 1);
            }
            else
            {
                playerData.getSamples().put(yawDiff, 1);

                if (System.currentTimeMillis() - playerData.getPatternMS() >= 5500L)
                {
                    vb = playerData.getPatternVerbose();
                    int samples = playerData.getSamples().size();
                    playerData.setPatternVerbose(0);
                    playerData.getSamples().clear();
                    playerData.resetPatternMS();
                    int samplesNeeded = 55;

                    if (vb == 0 && samples > samplesNeeded)
                    {
                        flag(event.getPlayer(), "B", null);
                    }
                }
            }
        }
    }
}