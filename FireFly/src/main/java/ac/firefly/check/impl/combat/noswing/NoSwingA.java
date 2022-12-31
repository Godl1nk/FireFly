package ac.firefly.check.impl.combat.noswing;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.events.packet.PacketAttackEvent;
import ac.firefly.events.packet.PacketSwingArmEvent;
import ac.firefly.managers.PluginManager;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

public class NoSwingA extends Check {
    public NoSwingA() {
        super("NoSwing (A)", CheckType.COMBAT, true);
    }

    @EventHandler
    public void onAttack(PacketAttackEvent e) {
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());
        if (Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
            if(com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(e.getPlayer()) > 47) {
                return;
            } else if(com.viaversion.viaversion.api.Via.getAPI().getPlayerVersion(e.getPlayer()) < 47) {
                return;
            }
        }
        if(ProtocolLibrary.getProtocolManager().getProtocolVersion(e.getPlayer()) > 47) {
            return;
        } else if(ProtocolLibrary.getProtocolManager().getProtocolVersion(e.getPlayer()) < 47) {
            return;
        }

        if(System.currentTimeMillis() - data.noSwingADelay > 90) {
            flag(e.getPlayer(), "No Swing Packet", "No Swing Packet For: " + (System.currentTimeMillis() - data.noSwingADelay));
        }
    }

    @EventHandler
    public void onSwing(PacketSwingArmEvent e) {
        PlayerData data = PluginManager.instance.getDataManager().getData(e.getPlayer());
        data.setLastSwing(System.currentTimeMillis());
        data.noSwingADelay = System.currentTimeMillis();
    }
}
