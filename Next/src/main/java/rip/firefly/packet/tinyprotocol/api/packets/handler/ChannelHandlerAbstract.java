package rip.firefly.packet.tinyprotocol.api.packets.handler;

import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflections;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.WrappedField;
import org.bukkit.entity.Player;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ChannelHandlerAbstract {
    static final WrappedField networkManagerField = Reflections.getNMSClass("PlayerConnection").getFieldByName("networkManager");
    static final WrappedField playerConnectionField = Reflections.getNMSClass("EntityPlayer").getFieldByName("playerConnection");
    final Executor addChannelHandlerExecutor;
    final Executor removeChannelHandlerExecutor;
    final String handlerKey;
    final String playerKey;

    ChannelHandlerAbstract() {
        this.addChannelHandlerExecutor = Executors.newSingleThreadExecutor();
        this.removeChannelHandlerExecutor = Executors.newSingleThreadExecutor();
        this.handlerKey = "packet_handler";
        this.playerKey = "trijes";
    }

    public abstract void addChannel(Player player);

    public abstract void removeChannel(Player player);

    public abstract void sendPacket(Player player, Object packet);
}
