package ac.firefly.events;

import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.events.packet.PacketPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PacketListener implements Listener {
    @EventHandler
    public void onPacketPlayerEvent(PacketPlayerEvent e) {
        Player p = e.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(p);
        if (data != null) {
            if (data.getLastPlayerPacketDiff() > 200) {
                data.setLastDelayedPacket(System.currentTimeMillis());
            }
            data.setLastPlayerPacket(System.currentTimeMillis());
        }
    }
}
