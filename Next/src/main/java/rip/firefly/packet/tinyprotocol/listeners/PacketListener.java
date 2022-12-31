package rip.firefly.packet.tinyprotocol.listeners;

import org.bukkit.entity.Player;
import rip.firefly.FireFly;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.handler.CombatHandler;
import rip.firefly.handler.MovementHandler;
import rip.firefly.handler.PacketHandler;
import rip.firefly.manager.DataManager;
import rip.firefly.packet.PacketUtil;
import rip.firefly.packet.event.PacketReceiveEvent;
import rip.firefly.packet.event.PacketSendEvent;
import rip.firefly.packet.tinyprotocol.api.NMSObject;

;

/**
 * @author Ghast
 * @since 11-Oct-19
 * Ghast CC © 2019
 */
public class PacketListener {

    public void onPacket(PacketReceiveEvent event){
        try {
            Player player = event.getPlayer();
            PlayerData data = FireFly.getInstance().getDataManager().getData(player);
            if (player == null || data == null || event.getPacket() == null) return;
            Object packet = event.getPacket();
            long now = System.currentTimeMillis();
            // Packet Position
            NMSObject payload = PacketUtil.findInboundPacket(player, event.getType(), event.getPacket());

            PacketHandler.handle(data, payload);
            MovementHandler.handle(data, payload);
            CombatHandler.handle(data, payload);

            FireFly.getCheckService().execute(() -> {
                for (AbstractCheck check : data.getCheckManager().getChecks()) {
                    if (check instanceof PacketCheck) {
                        PacketCheck packetCheck = (PacketCheck) check;

                        if(check.isEnabled()) {
                            packetCheck.handle(data, payload);
                        }
                    }
                }
            });

        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void onSend(PacketSendEvent e){
        Player player = e.getPlayer();
        PlayerData data = FireFly.getInstance().getDataManager().getData(player);
        Object packet = e.getPacket();
        if (player == null || data == null || e.getPacket() == null) return;

        NMSObject payload = PacketUtil.findInboundPacket(player, e.getType(), e.getPacket());

        PacketHandler.handle(data, payload);
        MovementHandler.handle(data, payload);

//        FireFly.getCheckService().execute(() -> {
//            for(AbstractCheck check : data.getCheckManager().getChecks()) {
//                if(check instanceof PacketCheck) {
//                    PacketCheck packetCheck = (PacketCheck)check;
//
//                    packetCheck.handle(data, payload);
//                }
//            }
//        });


        FireFly.getCheckService().execute(() -> {
            for (AbstractCheck check : data.getCheckManager().getChecks()) {
                if (check instanceof PacketCheck) {
                    PacketCheck packetCheck = (PacketCheck) check;

                    if(check.isEnabled()) {
                        packetCheck.handle(data, payload);
                    }
                }
            }
        });

    }

}
