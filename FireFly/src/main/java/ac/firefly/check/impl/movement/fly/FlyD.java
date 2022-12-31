package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.*;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
 
public class FlyD extends Check {
    public FlyD() {
        super("Fly (D)", CheckType.MOVEMENT, true);
    }

    private static void setBackPlayer(Player p) {
        SetBackSystem.setBack(p);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Location from = e.getFrom();
        Location to = e.getTo();
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.CREATIVE)
                || p.getAllowFlight()
                || e.getPlayer().getVehicle() != null
                || p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SPONGE
                || PlayerUtils.isOnClimbable(p, 0)
                || PlayerUtils.isOnClimbable(p, 1)
                || NEW_Velocity_Utils.didTakeVel(p)) {
            return;
        }

        PlayerData data = PluginManager.instance.getDataManager().getData(p);

        if (data == null) {
            return;
        }

        //Velocity Diff check
        double diffY = Math.abs(from.getY() - to.getY());
        double lastDiffY = data.getLastVelocityFlyY();
        int verboseC = data.getFlyVelocityVerbose();

        double finalDifference = Math.abs(diffY - lastDiffY);

        //Bukkit.broadcastMessage(Math.abs(diffY - lastDiffY) + ", " + PlayerUtils.isOnGround(p));

        if (finalDifference < 0.08
                && e.getFrom().getY() < e.getTo().getY()
                && !PlayerUtils.isOnGround(p) && !p.getLocation().getBlock().isLiquid() && !BlockUtils.isNearLiquid(p)
                && !NEW_Velocity_Utils.didTakeVel(p) && !VelocityUtils.didTakeVelocity(p)) {
            if (++verboseC > 2) {
                flag(p, "D", "Verbose: " + verboseC);
                SetBackSystem.setBack(p);
                verboseC = 0;
            }
        } else {
            verboseC = verboseC > 0 ? verboseC - 1 : 0;
        }
        data.setLastVelocityFlyY(diffY);
        data.setFlyVelocityVerbose(verboseC);
    }

    private int getDistanceToGround(Player p) {
        Location loc = p.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0; i--) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid() || loc.getBlock().isLiquid()) break;
            distance++;
        }
        return distance;
    }
}
