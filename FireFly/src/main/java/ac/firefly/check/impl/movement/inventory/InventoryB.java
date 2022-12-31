package ac.firefly.check.impl.movement.inventory;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryB extends Check {
    public InventoryB() {
        super("Inventory (B)", CheckType.MOVEMENT, true);
    }

    public void onClick(InventoryClickEvent e) {
        Player player;
        if (e.getWhoClicked() instanceof Player && (player = (Player)e.getWhoClicked()).isSprinting()) {
            this.flag(player, "", "");
        }
    }
}
