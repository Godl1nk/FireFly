package ac.firefly.check.impl.movement.phase;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.PlayerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import java.util.*;


public class PhaseB extends Check {
	public static List<Material> allowed = new ArrayList<>();
	public static ArrayList<Player> teleported = new ArrayList<>();
	public static HashMap<Player, Location> lastLocation = new HashMap<>();

	static {
		allowed.add(Material.PISTON_EXTENSION);
		allowed.add(Material.PISTON_STICKY_BASE);
		allowed.add(Material.PISTON_BASE);
		allowed.add(Material.SIGN_POST);
		allowed.add(Material.WALL_SIGN);
		allowed.add(Material.STRING);
		allowed.add(Material.AIR);
		allowed.add(Material.FENCE_GATE);
		allowed.add(Material.CHEST);
	}

	public PhaseB() {
        super("Phase (B)", CheckType.MOVEMENT, true);
    }

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {
		if (e.getCause() != TeleportCause.UNKNOWN) return;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();

		Location to = e.getTo().clone();
		Location from = e.getFrom().clone();

		if (from.getY() == to.getY()
				|| p.getAllowFlight()
				|| p.getVehicle() != null
				|| teleported.remove(e.getPlayer())
				|| e.getTo().getY() <= 0 || e.getTo().getY() >= p.getWorld().getMaxHeight()
				|| !PlayerUtils.blocksNear(p)
				|| (p.getLocation().getY() < 0.0D)
				|| (p.getLocation().getY() > p.getWorld().getMaxHeight())) return;

		double yDist = from.getBlockY() - to.getBlockY();
		for (double y = 0; y < Math.abs(yDist); y++) {
			Location l = yDist < -0.2 ? from.getBlock().getLocation().clone().add(0.0D, y, 0.0D) : to.getBlock().getLocation().clone().add(0.0D, y, 0.0D);
			if ((yDist > 20 || yDist < -20) && l.getBlock().getType() != Material.AIR
					&& l.getBlock().getType().isSolid() && !allowed.contains(l.getBlock().getType())) {
				flag(p, "B", "+20 Block VClip");
				p.teleport(from);
				return;
			}
			if (l.getBlock().getType() != Material.AIR && Math.abs(yDist) > 1.0 && l.getBlock().getType().isSolid()
					&& !allowed.contains(l.getBlock().getType())) {
				flag(p, "B", y + " Block VClip");
				p.teleport(lastLocation.get(p));
			} else {
				lastLocation.put(p, p.getLocation());
			}
		}
	}
}