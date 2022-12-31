package ac.firefly.check.impl.movement.phase;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PhaseC extends Check {
    public PhaseC() {
        super("Phase (C)", CheckType.MOVEMENT, true);
    }

    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        PlayerData data = PluginManager.instance.getDataManager().getData(player);
        if(data == null)
            return;
        if(!isEnabled())
            return;
        if(player.getFallDistance() > 3){
            return;
            //Basically prevents flagging when jumping from high places
        }

        if(event.getFrom().getBlockY()-event.getTo().getBlockY() > 2){
            flag(player, "Phase (C)", null);
        }else if(event.getTo().getBlockY()-event.getFrom().getBlockY() > 2){
            flag(player, "Phase (C)", null);
        }
    }
}
