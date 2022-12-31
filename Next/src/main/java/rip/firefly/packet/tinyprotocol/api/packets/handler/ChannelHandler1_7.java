package rip.firefly.packet.tinyprotocol.api.packets.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import rip.firefly.packet.TinyProtocolHandler;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflections;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.ReflectionsUtil;
import org.bukkit.entity.Player;

public class ChannelHandler1_7 extends ChannelHandlerAbstract {

    @Override
    public void addChannel(Player player) {
        Channel channel = getChannel(player);
        this.addChannelHandlerExecutor.execute(() -> {
            if (channel != null && channel.pipeline().get(this.playerKey) == null) {
                channel.pipeline().addBefore(this.handlerKey, this.playerKey, new ChannelHandler(player, this));
            }
        });
    }

    @Override
    public void removeChannel(Player player) {
        Channel channel = getChannel(player);
        this.removeChannelHandlerExecutor.execute(() -> {
            if (channel != null && channel.pipeline().get(this.playerKey) != null) {
                channel.pipeline().remove(this.playerKey);
            }
        });
    }

    private Channel getChannel(Player player) {
        return (Channel) Reflections.getNMSClass("NetworkManager").getFirstFieldByType(Channel.class).get(networkManagerField.get(playerConnectionField.get(ReflectionsUtil.getEntityPlayer(player))));
    }

    private static class ChannelHandler extends ChannelDuplexHandler {
        private final Player player;
        private final ChannelHandlerAbstract channelHandlerAbstract;

        ChannelHandler(Player player, ChannelHandlerAbstract channelHandlerAbstract) {
            this.player = player;
            this.channelHandlerAbstract = channelHandlerAbstract;
        }

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Object packet = TinyProtocolHandler.onPacketInAsync(player, msg);
            if (packet != null) {
                super.write(ctx, packet, promise);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            Object packet = TinyProtocolHandler.onPacketOutAsync(player, msg);
            if (packet != null) {
                super.channelRead(ctx, packet);
            }
        }
    }

    public void sendPacket(Player player, Object packet) {
        getChannel(player).pipeline().writeAndFlush(packet);
    }
}