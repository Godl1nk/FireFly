package ac.firefly.check.impl.combat.velocity;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.ServerUtils;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class VelocityA extends Check {
    public VelocityA() {
        super("Velocity (A)", CheckType.COMBAT, true);
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketListenerVelocity());
    }

    private class PacketListenerVelocity extends PacketAdapter {

        public PacketListenerVelocity() {
            super(Firefly.instance, ListenerPriority.MONITOR, PacketType.Play.Server.ENTITY_VELOCITY);
        }

        @Override
        public void onPacketSending(PacketEvent packetEvent) {
            Player player = packetEvent.getPlayer();
            PlayerData data = PluginManager.instance.getDataManager().getData(player);
            Location location = player.getLocation();
            if(data == null)
                return;
            if(!isEnabled())
                return;
            if(getPing(player) > 600){
                return; //PingSpoof can disable this but at least we did good :)
                // @author SethTheDev
            }

            if(packetEvent.getPacketType() == PacketType.Play.Server.ENTITY_VELOCITY){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(location.getX() != player.getLocation().getX() || location.getZ() != player.getLocation().getZ()){

                        }else{
                      //      flag(player, "Velocity (A)", null);
                        }
                    }
                }.runTaskLater(Firefly.instance, 600);
            }
        }
    }

    public int getPing(Player who) {
        try {
            String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + bukkitversion + ".entity.CraftPlayer");
            Object handle = craftPlayer.getMethod("getHandle").invoke(who);
            return (Integer) handle.getClass().getDeclaredField("ping").get(handle);
        } catch (Exception e) {
            return 404;
        }
    }
}
