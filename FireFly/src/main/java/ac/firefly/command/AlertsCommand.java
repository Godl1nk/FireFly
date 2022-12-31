package ac.firefly.command;

import ac.firefly.managers.PluginManager;
import ac.firefly.util.command.Command;
import ac.firefly.util.command.CommandArgs;
import ac.firefly.util.formatting.Color;
import ac.firefly.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class AlertsCommand {

    @Command(name = "alerts", inGameOnly = true, permission = "firefly.alerts")
    public void AlertsCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        if (args.length < 1) {
            if (!PluginManager.alertsToggled.containsKey(p)) {
                PluginManager.alertsToggled.put(p, true);
                p.sendMessage(Color.translate(msg.getString("commands.alerts.toggled")
                        .replace("$state", (PluginManager.alertsToggled.containsKey(p) ? ChatColor.GREEN.toString() : ChatColor.RED.toString()) + PluginManager.alertsToggled.containsKey(p))));
                return;
            }
            PluginManager.alertsToggled.remove(p);
            p.sendMessage(Color.translate(msg.getString("commands.alerts.toggled")
                    .replace("$state", (PluginManager.alertsToggled.containsKey(p) ? ChatColor.GREEN.toString() : ChatColor.RED.toString()) + PluginManager.alertsToggled.containsKey(p))));
        }
    }
}