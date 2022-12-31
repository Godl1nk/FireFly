package ac.firefly.check.impl.player.timer;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketPlayerEvent;
import ac.firefly.handlers.PacketHandler;
import ac.firefly.util.interaction.TimerUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;

public class TimerA extends Check {
    private Map<UUID, Map.Entry<Integer, Long>> packets;
    private Map<UUID, Integer> verbose;
    private Map<UUID, Long> lastPacket;
    private List<Player> toCancel;

    public TimerA() {
        super("Timer (A)", CheckType.PLAYER, true);

        packets = new HashMap<>();
        verbose = new HashMap<>();
        toCancel = new ArrayList<>();
        lastPacket = new HashMap<>();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        packets.remove(e.getPlayer().getUniqueId());
        verbose.remove(e.getPlayer().getUniqueId());
        lastPacket.remove(e.getPlayer().getUniqueId());
        toCancel.remove(e.getPlayer());
    }

    @EventHandler
    public void PacketPlayer(PacketPlayerEvent event) {
        Player player = event.getPlayer();
        if(
                player.getLocation().getBlock().getType() != Material.AIR
                || player.getLocation().getBlock().getType() == Material.TRAP_DOOR) {
            return;
        }


        /*if (Lag.getTPS() < 17) return;*/

        long lastPacket = this.lastPacket.getOrDefault(player.getUniqueId(), 0L);
        int packets = 0;
        long Time = System.currentTimeMillis();
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);

        if (this.packets.containsKey(player.getUniqueId())) {
            packets = this.packets.get(player.getUniqueId()).getKey();
            Time = this.packets.get(player.getUniqueId()).getValue();
        }

        if (System.currentTimeMillis() - lastPacket < 5) {
            this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
            return;
        }
        double threshold = 21;
        if (TimerUtils.elapsed(Time, 1000L)) {
            if (toCancel.remove(player) && packets <= 13) {
                return;
            }
            if (packets > threshold + PacketHandler.movePackets.getOrDefault(player.getUniqueId(), 0) && PacketHandler.movePackets.getOrDefault(player.getUniqueId(), 0) < 5) {
                verbose = (packets - threshold) > 10 ? verbose + 2 : verbose + 1;
            } else {
                verbose = 0;
            }

            if (verbose > 2) {
                flag(player, "Packets: " + packets, "Packets: " + packets);
            }
            packets = 0;
            Time = TimerUtils.nowlong();
            PacketHandler.movePackets.remove(player.getUniqueId());
        }
        packets++;

        this.lastPacket.put(player.getUniqueId(), System.currentTimeMillis());
        this.packets.put(player.getUniqueId(), new SimpleEntry<>(packets, Time));
        this.verbose.put(player.getUniqueId(), verbose);
    }
}
