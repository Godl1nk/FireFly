package ac.firefly.check.impl.movement.fly;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.SetBackSystem;
import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.VelocityUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class FlyA extends Check {
    public FlyA() {
        super("Fly (A)", CheckType.MOVEMENT, true);
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
                || NEW_Velocity_Utils.didTakeVel(p)
                || VelocityUtils.didTakeVelocity(p)) {
            return;
        }

        PlayerData data = PluginManager.instance.getDataManager().getData(p);

        if (data == null) {
            return;
        }
        //Ascension Check
        if (!NEW_Velocity_Utils.didTakeVel(p) && !PlayerUtils.wasOnSlime(p)) {
            Vector vec = new Vector(to.getX(), to.getY(), to.getZ());
            double Distance = vec.distance(new Vector(from.getX(), from.getY(), from.getZ()));
            if (p.getFallDistance() == 0.0f && p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.AIR && p.getLocation().getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
                if (Distance > 0.50 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ() && !NEW_Velocity_Utils.didTakeVel(p)) {
                    flag(p, "(A)", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 0.90 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "(A)", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 1.0 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "(A)", "Distance: " + Distance);
                    setBackPlayer(p);
                } else if (Distance > 3.24 && !PlayerUtils.isOnGround(p) && e.getTo().getY() > e.getFrom().getY() && e.getTo().getX() == e.getFrom().getX() && e.getTo().getZ() == e.getFrom().getZ()) {
                    flag(p, "(A)", "Distance: " + Distance);
                    setBackPlayer(p);
                }
            }
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
}
