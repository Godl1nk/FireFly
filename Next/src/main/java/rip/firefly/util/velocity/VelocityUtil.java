package rip.firefly.util.velocity;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.DataManager;
import rip.firefly.util.math.TimerUtil;

public class VelocityUtil implements Listener {

    public static boolean didTakeVel(Player p) {
        boolean out = false;
        PlayerData data = DataManager.getData(p);
        if (data != null && data.isDidTakeVelocity()) {
            out = true;
        }
        return out;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data =  DataManager.getData(p);
        if (data != null) {
            if (data.isDidTakeVelocity()) {
                if (TimerUtil.elapsed(data.getLastVelMS(), 300L)) {
                    data.setDidTakeVelocity(false);
                }
            }
        }
    }

    @EventHandler
    public void onVelEvent(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        PlayerData data =  DataManager.getData(p);
        if (data != null) {
            data.setDidTakeVelocity(true);
            data.setLastVelMS(TimerUtil.nowlong());
        }
    }

    /*  public static boolean didTakeVel(Player p) {
        PlayerData data = DataManager.getData(p);
        if (data != null) {
            return data.isVelUpdate();
        } else {
            return false;
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        PlayerData data = DataManager.getData(p);
        if (data != null) {
            if (data.isVelUpdate()) {
                if (TimerUtil.elapsed(data.getVelUpdateTime(), 750L)) {
                    //FORCE Reset
                    data.setVelUpdate(false);
                }
                if (TimerUtil.elapsed(data.getVelUpdateTime(), 750L)) {
                    if (!data.isOnGround()) {
                        data.setVelUpdateTime(TimerUtil.nowlong());
                    } else {
                        data.setVelUpdate(false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onVelChange(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        PlayerData data = DataManager.getData(p);
        if (data != null) {
            if (p.getNoDamageTicks() <= 0) {
                if (!data.isVelUpdate()) {
                    data.setVelUpdate(true);
                    data.setVelUpdateTime(System.currentTimeMillis());
                }
            }
        }
    }*/
}
