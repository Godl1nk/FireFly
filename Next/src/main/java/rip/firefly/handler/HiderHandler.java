package rip.firefly.handler;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.manager.ConfigManager;

public class HiderHandler implements Listener {

    public HiderHandler(JavaPlugin javaPlugin) {
        Bukkit.getPluginManager().registerEvents(this , javaPlugin);
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent e) {
        if(ConfigManager.settings.getConfiguration().getBoolean("pluginHider.spoofName")) {
            if ((e.getMessage().toLowerCase().startsWith("/pl") || e.getMessage().toLowerCase().startsWith("/plugins") || e.getMessage().toLowerCase().startsWith("/bukkit:pl") || e.getMessage().toLowerCase().startsWith("/bukkit:plugins")) && !e.getMessage().toLowerCase().startsWith("/plugman")) {
                if (e.getPlayer().hasPermission("bukkit.command.plugins")) {
                    boolean notFlase = false;
                    for (String string : e.getMessage().split(" ")) {
                        if (string.toLowerCase().contains("plugins") || string.toLowerCase().contains("/pl")) notFlase = true;
                    }
                    if (!notFlase) return;
                    e.setCancelled(true);
                    String pluginMessage = "Plugins (%s)";
                    StringBuilder append = new StringBuilder();
                    int plugins = 0;
                    for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                        String name = plugin.getName();
                        if (name.equalsIgnoreCase("FireFly")) name = ConfigManager.settings.getConfiguration().getString("pluginHider.spoofPluginName");
                        append.append(plugins < 1 ? ChatColor.GREEN + name + ChatColor.WHITE + ", " : ChatColor.WHITE + (plugins > 1 ? ", " : "") + ChatColor.GREEN + name + ChatColor.WHITE);
                        plugins++;
                    }
                    if (append.length() > 0) {
                        append.setLength(append.length() - 2);
                    }

                    e.getPlayer().sendMessage(String.format(pluginMessage, plugins) + ": " + append);
                } else {
                    e.setCancelled(true);
                    e.getPlayer().sendMessage(ChatColor.RED + "I'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.");
                }
            }
        }
    }
}
