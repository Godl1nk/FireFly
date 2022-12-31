package ac.firefly.check.impl.other.payload;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PayloadA extends Check implements PluginMessageListener {

    public static Set<UUID> vapers = new HashSet<UUID>();

    public PayloadA() {
        super("Payload (A)", CheckType.MISC, true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().sendMessage("§8 §8 §1 §3 §3 §7 §8 ");
        //e.getPlayer().sendMessage("\u00a78 \u00a78 \u00a71 \u00a73 \u00a73 \u00a77 \u00a78 ");
    }

    public void onPluginMessageReceived(String string, Player player, byte[] arrby) {
        try {
            String string2 = new String(arrby);
        }
        catch (Exception exception) {
            String string3 = "";
        }
        flag(player, "Vape", "Packet: " + arrby);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        vapers.remove(playerQuitEvent.getPlayer().getUniqueId());
    }
}
