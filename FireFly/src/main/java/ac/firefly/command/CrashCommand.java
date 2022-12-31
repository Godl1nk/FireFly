package ac.firefly.command;

import ac.firefly.managers.ConfigManager;
import ac.firefly.util.command.Command;
import ac.firefly.util.command.CommandArgs;
import ac.firefly.util.formatting.Color;
import ac.firefly.util.interaction.Crash;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class CrashCommand {

    @Command(name = "ffcrash", inGameOnly = false, permission = "firefly.crash")
    public void CrashCommand(CommandArgs ca){
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        if (args.length < 1) {
            p.sendMessage(ChatColor.RED + "Usage: /ffcrash <player>");
            return;
        } else if(Bukkit.getPlayer(args[0]).isOnline()){
            Crash.crashPlayer(Bukkit.getPlayer(args[0]));
        } else {
            p.sendMessage(Color.translate(msg.getString("not_online").replace("$player", args[0])));
            return;
        }
    }

}
