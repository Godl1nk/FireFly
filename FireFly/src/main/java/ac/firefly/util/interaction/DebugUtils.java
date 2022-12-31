package ac.firefly.util.interaction;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DebugUtils {
    public static boolean enabled = true;

    public static void Send(Player p, String string) {
        if (enabled) {
            p.sendMessage(ChatColor.AQUA + "DEBUG: " + ChatColor.GRAY + string);
        }
    }
}
