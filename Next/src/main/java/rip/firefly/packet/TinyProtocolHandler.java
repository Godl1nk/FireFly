package rip.firefly.packet;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.FireFly;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.manager.DataManager;
import rip.firefly.packet.event.PacketReceiveEvent;
import rip.firefly.packet.event.PacketSendEvent;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.Packet;
import rip.firefly.packet.tinyprotocol.api.packets.ChannelInjector;
import rip.firefly.packet.tinyprotocol.listeners.PacketListener;

public class TinyProtocolHandler {

    @Getter
    private static ChannelInjector instance;
    @Getter private PacketListener packetListener;
    @Getter private static JavaPlugin plugin;
    public TinyProtocolHandler(JavaPlugin javaPlugin) {
        init(javaPlugin);
    }

    public static void sendPacket(Player player, Object packet) {
        instance.getChannel().sendPacket(player, packet);
    }
    public synchronized void init(JavaPlugin javaPlugin){
        plugin = javaPlugin;
        packetListener = new PacketListener();
        instance = new ChannelInjector();
        Bukkit.getPluginManager().registerEvents(instance, javaPlugin);
    }

    public static Object onPacketOutAsync(Player sender, Object packet) {
        String name = packet.getClass().getName();
        int index = name.lastIndexOf(".");
        String packetName = name.substring(index + 1).replace("PacketPlayInUseItem", "PacketPlayInBlockPlace")
                .replace(Packet.Client.LEGACY_LOOK, Packet.Client.LOOK)
                .replace(Packet.Client.LEGACY_POSITION, Packet.Client.POSITION)
                .replace(Packet.Client.LEGACY_POSITION_LOOK, Packet.Client.POSITION_LOOK);

        PacketReceiveEvent event = new PacketReceiveEvent(sender, packet, packetName);
        FireFly.getProtocolHandler().getPacketListener().onPacket(event);
        Bukkit.getPluginManager().callEvent(event);

        NMSObject payload = PacketUtil.findInboundPacket(sender, packetName, packet);

//        FireFly.getCheckService().execute(() -> {
//            for(AbstractCheck check : DataManager.getData(sender).getCheckManager().getChecks()) {
//                if(check instanceof PacketCheck) {
//                    PacketCheck packetCheck = (PacketCheck)check;
//
//                    packetCheck.handle(DataManager.getData(sender), payload);
//                }
//            }
//        });



        return !event.isCancelled() ? event.getPacket() : null;
    }

    public static Object onPacketInAsync(Player sender, Object packet) {
        String name = packet.getClass().getName();
        int index = name.lastIndexOf(".");
        String packetName = name.substring(index + 1);


        PacketSendEvent event = new PacketSendEvent(sender, packet, packetName);
        FireFly.getProtocolHandler().getPacketListener().onSend(event);
        Bukkit.getPluginManager().callEvent(event);


        return !event.isCancelled() ? event.getPacket() : null;
    }
}

