package rip.firefly.util.draw;

import org.bukkit.ChatColor;
import rip.firefly.manager.ConfigManager;

public class GraphUtil {
    public static String drawGraph(double max, double value) {
        StringBuilder graph = new StringBuilder();
        
        graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.borderColor")) + "[");
        for(double i = 0; i <= max; i++) {
            if(i > value) {
                graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.unfilledColor")) + ConfigManager.messages.getConfiguration().getString("graph.lineChar"));
            } else {
                graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.filledColor")) + ConfigManager.messages.getConfiguration().getString("graph.lineChar"));
            }
        }
        graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.borderColor")) + "]");
        return graph.toString();
    }

    public static String drawGraph(int value) {
        StringBuilder graph = new StringBuilder();
        int barSize = ConfigManager.messages.getConfiguration().getInt("graph.maxLength");
        int usedLength = barSize/value*10;
        int remainLength = barSize - usedLength;
        for(int i = 0; i < usedLength; i++) {
            graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.unfilledColor")) + ConfigManager.messages.getConfiguration().getString("graph.lineChar"));
        }
        for (int i = 0; i < remainLength; i++) {
            graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.filledColor")) + ConfigManager.messages.getConfiguration().getString("graph.lineChar"));
        }
        graph.append(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("graph.borderColor")) + "]");
        return graph.toString();
    }
}
