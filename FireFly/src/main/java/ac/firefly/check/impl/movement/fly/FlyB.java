package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.SetBackSystem;
import ac.firefly.util.interaction.VelocityUtils;
import ac.firefly.util.math.MathUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class FlyB extends Check {
    public FlyB() {
        super("Fly (B)", CheckType.MOVEMENT, true);
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
                || PlayerUtils.isOnCarpet(p)
                || NEW_Velocity_Utils.didTakeVel(p)
                || VelocityUtils.didTakeVelocity(p)
                || PlayerUtils.isInWeb(p)) {
            return;
        }

        PlayerData data = PluginManager.instance.getDataManager().getData(p);

        if (data == null) {
            return;
        }

        //Hover check
        if (!isReallyOnGround(p) || !PlayerUtils.isInWater(p)) {
            double distanceToGround = getDistanceToGround(p);
            double yDiff = MathUtils.getVerticalDistance(e.getFrom(), e.getTo());
            int verbose = data.getFlyHoverVerbose();

            if (distanceToGround > 2) {
                verbose = yDiff == 0 ? verbose + 6 : yDiff < 0.06 ? verbose + 4 : 0;
            } else if (data.getAirTicks() > 7
                    && yDiff < 0.001) {
                verbose += 2;
            } else {
                verbose = 0;
            }


            if (verbose > 24 && !PlayerUtils.isOnGround4(p) && !PlayerUtils.isOnGround(p.getLocation())) {
                flag(p, "(B)", "Verbose: " + verbose);
                setBackPlayer(p);
                verbose = 0;
            }
            data.setFlyHoverVerbose(verbose);
        }
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

    public static boolean isReallyOnGround(Player p) {
        Location l = p.getLocation();
        int x = l.getBlockX();
        int y = l.getBlockY();
        int z = l.getBlockZ();
        Location b = new Location(p.getWorld(), x, y - 1, z);

        return p.isOnGround() && b.getBlock().getType() != Material.AIR && b.getBlock().getType() != Material.WEB
                && !b.getBlock().isLiquid();
    }
}