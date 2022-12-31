package ac.firefly.check.impl.player;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.events.packet.PacketPlayerEvent;
import ac.firefly.handlers.PacketHandler;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.util.interaction.SetBackSystem;
import ac.firefly.util.interaction.TimerUtils;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class BadPackets extends Check {
    private Map<UUID, Integer> packets;
    private Map<UUID, Integer> verbose;
    private Map<UUID, Long> lastPacket;
    private List<Player> toCancel;

    public BadPackets() {
        super("BadPackets (A)", CheckType.MISC, true);
        packets = new HashMap<>();
        verbose = new HashMap<>();
        toCancel = new ArrayList<>();
        lastPacket = new HashMap<>();
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e) {
        if(packets.containsKey(e.getPlayer().getUniqueId())) {
            packets.remove(e.getPlayer().getUniqueId());
        }
        if(verbose.containsKey(e.getPlayer().getUniqueId())) {
            verbose.remove(e.getPlayer().getUniqueId());
        }
        if(lastPacket.containsKey(e.getPlayer().getUniqueId())) {
            lastPacket.remove(e.getPlayer().getUniqueId());
        }
        if(toCancel.contains(e.getPlayer())) {
            toCancel.remove(e.getPlayer());
        }
    }
    @EventHandler
    public void packetPlayer(PacketPlayerEvent event) {
        Player player = event.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(player);

        int packets = this.packets.getOrDefault(player.getUniqueId(), 0);
        long Time = this.lastPacket.getOrDefault(player.getUniqueId(), System.currentTimeMillis());
        int verbose = this.verbose.getOrDefault(player.getUniqueId(), 0);

        if((System.currentTimeMillis() - data.getLastPacket()) > 100L) {
            toCancel.add(player);
        }
        double threshold = 42;
        if(TimerUtils.elapsed(Time, 1000L)) {
            if(toCancel.remove(player) && packets <= 67) {
                this.packets.put(player.getUniqueId(), 0);
                return;
            }
            if(packets > threshold + PacketHandler.movePackets.getOrDefault(player.getUniqueId(), 0)) {
                verbose++;
            } else {
                verbose = 0;
            }

            //Bukkit.broadcastMessage(packets + ", " + verbose);

            if(verbose > 2  && !isPartiallyStuck(player) || !isFullyStuck(player)) {
                flag(player, "High Packets", "Verbose: " + verbose);
                SetBackSystem.setBack(player);
            }
            packets = 0;
            Time = System.currentTimeMillis();
            PacketHandler.movePackets.remove(player.getUniqueId());
        }
        packets++;

        this.packets.put(player.getUniqueId(), packets);
        this.verbose.put(player.getUniqueId(), verbose);
        this.lastPacket.put(player.getUniqueId(), Time);
    }

    public static boolean isPartiallyStuck(Player player) {
        if (player.getLocation().clone().getBlock() == null) {
            return false;
        }
        Block block = player.getLocation().clone().getBlock();
        if (PlayerUtils.isSlab(block) || PlayerUtils.isStair(block)) {
            return false;
        }
        if (player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isSolid()
                || player.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()) {
            return true;
        }
        if (player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().getRelative(BlockFace.DOWN).getType()
                .isSolid()
                || player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().getRelative(BlockFace.UP).getType()
                .isSolid()) {
            return true;
        }
        return block.getType().isSolid();
    }

    public static boolean isFullyStuck(Player player) {
        Block block1 = player.getLocation().clone().getBlock();
        Block block2 = player.getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock();
        if (block1.getType().isSolid() && block2.getType().isSolid()) {
            return true;
        }
        return block1.getRelative(BlockFace.DOWN).getType().isSolid()
                || block1.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid()
                && block2.getRelative(BlockFace.DOWN).getType().isSolid()
                || block2.getLocation().getBlock().getRelative(BlockFace.UP).getType().isSolid();
    }
}
