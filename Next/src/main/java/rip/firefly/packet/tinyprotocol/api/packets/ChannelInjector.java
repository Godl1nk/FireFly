package rip.firefly.packet.tinyprotocol.api.packets;

import lombok.Getter;
import rip.firefly.packet.tinyprotocol.api.packets.handler.ChannelHandler1_7;
import rip.firefly.packet.tinyprotocol.api.packets.handler.ChannelHandler1_8;
import rip.firefly.packet.tinyprotocol.api.packets.handler.ChannelHandlerAbstract;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflections;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class ChannelInjector implements Listener {
    private static ChannelHandlerAbstract channel;

    public ChannelInjector() {
        /*if(ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)) {
            this.channel = new ChannelHandler1_7();
        } else if(ProtocolVersion.getGameVersion().isAbove(ProtocolVersion.V1_7_10) && ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.v1_17)) {
            this.channel = new ChannelHandler1_8();
        }*/
        /*if(Reflections.classExists("net.minecraft.util.io.netty.channel.Channel")) {
            this.channel = new ChannelHandler1_8();
        } else if(Reflections.classExists("net.minecraft.server.network.LoginListener")) {
            this.channel = new ChannelHandler1_17();
        } else {
            this.channel = new ChannelHandler1_7();
        }*/
        this.channel = Reflections.classExists("net.minecraft.util.io.netty.channel.Channel")
                ? new ChannelHandler1_8() : new ChannelHandler1_7();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        //System.out.println("Injecting channel to user " + event.getPlayer().getName());
        this.channel.addChannel(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        this.channel.removeChannel(event.getPlayer());
    }

    public static ChannelHandlerAbstract getChannel() {
        return channel;
    }
}
