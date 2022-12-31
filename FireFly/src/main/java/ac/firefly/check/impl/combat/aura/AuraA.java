package ac.firefly.check.impl.combat.aura;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketAttackEvent;
import ac.firefly.data.PlayerData;
import ac.firefly.handlers.PacketTypes;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.math.MathUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class AuraA extends Check {

    public AuraA() {
        super("Aura (A)", CheckType.COMBAT, true);
    }

    // Point Check //

    public static float angleDistance(float alpha, float beta) {
        float phi = Math.abs(beta - alpha) % 260;
        return phi > 180 ? 260 - phi : phi;
    }

    @EventHandler
    public void onAttack(PacketAttackEvent e) {
        if (e.getType() != PacketTypes.USE) {
            return;
        }

        Player player = e.getPlayer();
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(player);

        if (data == null) {
            return;
        }

        int verboseA = data.getKillauraAVerbose();
        long time = data.getLastAimTime();

        if (MathUtils.elapsed(time, 1100L)) {
            time = System.currentTimeMillis();
            verboseA = 0;
        }
        if ((Math.abs(data.getLastKillauraPitch() - e.getPlayer().getEyeLocation().getPitch()) > 1
                || angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) > 1
                || Double.compare(player.getEyeLocation().getYaw(), data.getLastKillauraYaw()) != 0)
                && !MathUtils.elapsed(data.getLastPacket(), 1000L)) {

            if (angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()) != data.getLastKillauraYawDif()) {
                if (++verboseA > 7) { // 6 // WAS 7!
                    flag(player, "A", "Verbose: " + verboseA);
                }
                data.setLastKillauraYawDif(angleDistance((float) data.getLastKillauraYaw(), player.getEyeLocation().getYaw()));
            } else {
                verboseA = 0;
            }

            data.setKillauraAVerbose(verboseA);
            data.setLastAimTime(time);
        }
    }
}