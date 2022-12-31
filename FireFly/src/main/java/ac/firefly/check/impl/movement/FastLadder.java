package ac.firefly.check.impl.movement;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.util.interaction.PlayerUtils;
import ac.firefly.util.math.MathUtils;
import ac.firefly.util.interaction.ServerUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.WeakHashMap;

public class FastLadder extends Check {

    public Map<Player, Integer> count;

    public FastLadder() {
        super("FastLadder (A)", CheckType.MOVEMENT, true);

        count = new WeakHashMap<Player, Integer>();
    }

    @EventHandler
    public void checkFastLadder(PlayerMoveEvent e) {
        Player player = e.getPlayer();

        /** False flag check **/
        if(e.isCancelled()
                || (e.getFrom().getY() == e.getTo().getY())
                || player.getAllowFlight()
                || !onLadder(player)
                || PlayerUtils.isOnGround(player)
               // || !PlayerUtils.isOnClimbable(player, 1)
                //|| !PlayerUtils.isOnClimbable(player, 0)
                || player.hasPotionEffect(PotionEffectType.JUMP)) {
            return;
        }

        if(player.isSprinting()) {
            return;
        }

        int Count = count.getOrDefault(player, 0);
        double OffsetY = MathUtils.offset(MathUtils.getVerticalVector(e.getFrom().toVector()),
                MathUtils.getVerticalVector(e.getTo().toVector()));
        double Limit = 0.13;

        double updown = e.getTo().getY() - e.getFrom().getY();
        if (updown <= 0) {
            return;
        }


        /** Checks if Y Delta is greater than Limit **/

        if (OffsetY > Limit) {
            Count++;
            ServerUtils.logDebug(null, "[Illegitmate] New Count: " + Count + " (+1); Speed: " + OffsetY + "; Max: " + Limit);
        } else {
            Count = Count > -2 ? Count - 1 : 0;
        }

        long percent = Math.round((OffsetY - Limit) * 120);

        /**If verbose count is greater than 11, flag **/
        if (Count > 11) {
            Count = 0;
            ServerUtils.logDebug(null, "FastLadder; Speed: " + OffsetY + "; Max: " + Limit + "; New Count: " + Count);
            flag(player, "Ladder", percent + "% faster than normal");
        }
        count.put(player, Count);
    }

    public boolean onLadder(Player p) {
        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();
        Location loc = new Location(p.getWorld(), x, y, z);
        Material type = loc.getBlock().getType();

        if(type.equals(Material.LADDER)) {
            return true;
        } else {
            return false;
        }
    }
}