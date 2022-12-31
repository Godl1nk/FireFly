package ac.firefly.check.impl.movement.inventory;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

public class InventoryA extends Check {
    public InventoryA() {
        super("Inventory (A)", CheckType.MOVEMENT, true);

        Firefly.protocolManager.addPacketListener(new PacketAdapter(Firefly.instance, PacketType.Play.Client.CLOSE_WINDOW) {
            @Override
            public void onPacketReceiving(PacketEvent e) {
                if(e.getPacket().getType() == PacketType.Play.Client.CLOSE_WINDOW) {
                    if(e.getPlayer().isSprinting()) {
                        flag(e.getPlayer(), "Sprinting And Closed Window", "Packet: " + e.getPacket());
                    }
                }
            }
        });
    }
}
