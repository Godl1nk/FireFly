package rip.firefly.packet;

import org.bukkit.entity.Player;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.api.Packet;
import rip.firefly.packet.tinyprotocol.packet.in.*;
import rip.firefly.packet.tinyprotocol.packet.out.*;

/**
 * @author Ghast
 * @since 27-Oct-19
 * Ghast CC © 2019
 */
public class PacketUtil {

    public static NMSObject findInboundPacket(Player player, String type, Object packet){
        switch (type){
            case Packet.Client.ABILITIES: return new WrappedInAbilitiesPacket(packet, player);
            case Packet.Client.ARM_ANIMATION: return new WrappedInArmAnimationPacket(packet, player);
            case Packet.Client.BLOCK_DIG: return new WrappedInBlockDigPacket(packet, player);
            case Packet.Client.BLOCK_PLACE: return new WrappedInBlockPlacePacket(packet, player);
            case Packet.Client.CHAT: return new WrappedInChatPacket(packet, player);
            case Packet.Client.CLOSE_WINDOW: return new WrappedInCloseWindowPacket(packet, player);
            case Packet.Client.CUSTOM_PAYLOAD: return new WrappedInCustomPayload(packet, player);
            case Packet.Client.ENTITY_ACTION: return new WrappedInEntityActionPacket(packet, player);
            case Packet.Client.FLYING: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.POSITION: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.POSITION_LOOK: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.LEGACY_POSITION: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.LEGACY_POSITION_LOOK: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.LEGACY_LOOK: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.LOOK: return new WrappedInFlyingPacket(packet, player);
            case Packet.Client.HELD_ITEM_SLOT: return new WrappedInHeldItemSlotPacket(packet, player);
            case Packet.Client.KEEP_ALIVE: return new WrappedInKeepAlivePacket(packet, player);
            case NMSObject.Client.STEER_VEHICLE: return new WrappedInSteerVehiclePacket(packet, player);
            case Packet.Client.TAB_COMPLETE: return new WrappedInTabComplete(packet, player);
            case Packet.Client.TRANSACTION: return new WrappedInTransactionPacket(packet, player);
            case Packet.Client.USE_ENTITY: return new WrappedInUseEntityPacket(packet, player);
            default: return null;
        }
    }

    public static NMSObject findOutboundPacket(Player player, String type, Object packet){
        switch (type){
            case Packet.Server.ABILITIES: return new WrappedOutAbilitiesPacket(packet, player);
            case Packet.Server.CUSTOM_PAYLOAD: return new WrappedOutPayloadPacket(packet, player);
            case Packet.Server.BLOCK_CHANGE: return new WrappedOutBlockChange(packet);
            case Packet.Server.ENTITY_METADATA: return new WrappedOutEntityMetadata(packet, player);
            case Packet.Server.ENTITY_VELOCITY: return new WrappedOutVelocityPacket(packet, player);
            case Packet.Server.HELD_ITEM: return new WrappedOutHeldItemSlot(packet, player);
            case Packet.Server.POSITION: return new WrappedOutPositionPacket(packet, player);
            case Packet.Server.TAB_COMPLETE: return new WrappedOutTabComplete(packet, player);
            case Packet.Server.TRANSACTION: return new WrappedOutTransaction(packet, player);
            case Packet.Server.KEEP_ALIVE: return new WrappedOutKeepAlivePacket(packet, player);
            default: return null;
        }
    }
}
