package rip.firefly.handler;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.CheckManager;
import rip.firefly.manager.TransactionManager;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.*;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutKeepAlivePacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutVelocityPacket;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;

import java.util.Arrays;
import java.util.List;

public class PacketHandler {

    private static List<String> instantBreakBlocks;

    public PacketHandler(JavaPlugin plugin) {
        instantBreakBlocks = Arrays.asList("reeds", "waterlily", "deadbush", "doubleplant", "flower", "tallgrass");
    }

    public static void handle(PlayerData playerData, NMSObject packet) {
        playerData.getTransactionManager().handle(playerData, packet);
        if (packet instanceof WrappedInFlyingPacket) {
            playerData.getLagTick().tick();

            playerData.setAttacking(false);
            long now = System.currentTimeMillis();
            long delta = now - playerData.getLastFlying();

            if (playerData.getLastDelta() > 100L && delta < 5L) playerData.getLagTick().reset();

            WrappedInFlyingPacket packetFlying = (WrappedInFlyingPacket) packet;

            PlayerLocation tov = new PlayerLocation(packetFlying.getX(), packetFlying.getY(), packetFlying.getZ(), packetFlying.getYaw(), packetFlying.getPitch(), packetFlying.isGround());
            PlayerLocation fromv = playerData.getFrom();
            if (!tov.equals(fromv)) {
                MovementUpdate update = new MovementUpdate(playerData, tov, fromv, packetFlying.isPos(), packetFlying.isGround());
                VelocityHandler.handle(playerData, update);
            }

            for (AbstractCheck c : CheckManager.getChecks()) {
                if (c instanceof MovementCheck) {
                    MovementCheck check = (MovementCheck) c;
                    PlayerLocation to = new PlayerLocation(packetFlying.getX(), packetFlying.getY(), packetFlying.getZ(), packetFlying.getYaw(), packetFlying.getPitch(), packetFlying.isGround());
                    PlayerLocation from = playerData.getFrom();
                    if (to != from) {
                        MovementUpdate update = new MovementUpdate(playerData, to, from, packetFlying.isPos(), packetFlying.isGround());
                        if(check.isEnabled()) {
                            check.handle(playerData, update);
                        }
                    }

                }
            }

            playerData.setLastDelta(delta);
            playerData.setLastFlying(now);
        } else if (packet instanceof WrappedInBlockDigPacket) {
            WrappedInBlockDigPacket wPacket = (WrappedInBlockDigPacket) packet;
            if (wPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.START_DESTROY_BLOCK) {
                Block block = new Location(wPacket.getPlayer().getWorld(), wPacket.getPosition().getX(), wPacket.getPosition().getY(), wPacket.getPosition().getZ()).getBlock();
                String tileName = block.getType().toString().replace("tile.", "");

                if (instantBreakBlocks.contains(tileName)) {
                    playerData.setInstantBreakDigging(true);
                } else {
                    playerData.setInstantBreakDigging(false);
                }

                playerData.setDigging(true);
            } else if (wPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.ABORT_DESTROY_BLOCK || wPacket.getAction() == WrappedInBlockDigPacket.EnumPlayerDigType.STOP_DESTROY_BLOCK) {
                playerData.setInstantBreakDigging(false);
                playerData.setDigging(false);
            }
        } else if (packet instanceof WrappedOutKeepAlivePacket) {
            playerData.setLastServerKeepAlive(System.currentTimeMillis());
        } else if (packet instanceof WrappedInKeepAlivePacket || packet instanceof WrappedIn13KeepAlive) {
            playerData.setLastClientKeepAlive(System.currentTimeMillis());
        } else if (packet instanceof WrappedInBlockPlacePacket) {
            playerData.setLastPlace(System.currentTimeMillis());
        } else if (packet instanceof WrappedOutVelocityPacket) {
            final WrappedOutVelocityPacket wrapper = (WrappedOutVelocityPacket) packet;

            if (wrapper.getPlayer().getEntityId() == Bukkit.getPlayer(playerData.getUuid()).getEntityId()) {
                final double posX = wrapper.getX();
                final double posY = wrapper.getY();
                final double posZ = wrapper.getZ();

                playerData.setVelocityX(posX);
                playerData.setVelocityY(posY);
                playerData.setVelocityZ(posZ);

                playerData.getVelocityManager().addVelocityEntry(posX, posY, posZ);
                playerData.setVelocity(true);
            }
        } else if(packet instanceof WrappedInUseEntityPacket) {
            final WrappedInUseEntityPacket wrapper = (WrappedInUseEntityPacket) packet;

            if (wrapper.getAction() == WrappedInUseEntityPacket.EnumEntityUseAction.ATTACK) {
                playerData.setAttacking(true);
            }
        }
    }
}

