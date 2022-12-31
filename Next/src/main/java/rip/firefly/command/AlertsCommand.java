package rip.firefly.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;
import rip.firefly.util.command.Command;
import rip.firefly.util.command.CommandArgs;

public class AlertsCommand {
    @Command(name = "$ffalerts", aliases = "$alertsalias", inGameOnly = true, permission = "firefly.alerts")
    public void AlertsCommand(CommandArgs args) {
        Player player = args.getPlayer();

        PlayerData playerData = DataManager.getData(player);

        playerData.setAlerts(!playerData.isAlerts());

        if(playerData.isAlerts()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("enableAlerts").replace("$prefix", ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("prefix")))));
        } else {

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("disableAlerts").replace("$prefix", ChatColor.translateAlternateColorCodes('&', ConfigManager.messages.getConfiguration().getString("prefix")))));
        }
    }
}
