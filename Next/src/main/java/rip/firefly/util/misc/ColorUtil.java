package rip.firefly.util.misc;

import net.md_5.bungee.api.ChatColor;

public class ColorUtil {

    /**
     * @param string The string you want to translate.
     * @return The string formatted with color codes.
     */

    public static String translate(final String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
