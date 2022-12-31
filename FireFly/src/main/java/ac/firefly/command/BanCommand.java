package ac.firefly.command;

import ac.firefly.Firefly;
import ac.firefly.util.command.Command;
import ac.firefly.util.command.CommandArgs;
import ac.firefly.util.formatting.Color;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.interaction.Crash;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class BanCommand {
    @Command(name = "ffban", inGameOnly = false, permission = "firefly.ban")
    public void BanCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        if (args.length < 1) {
            p.sendMessage(Color.translate(msg.getString("not_online")));
            return;
        } else {
            Firefly.banPlayer(Bukkit.getPlayer(args[0]));
            for (String string : ConfigManager.messages.getConfiguration().getStringList("ban.staffban")) {
                if (p.isOnline()) {
                    if(ConfigManager.settings.getConfiguration().getBoolean("ban.crash")) {
                        Crash.crashPlayer(Bukkit.getPlayer(args[0]));
                    }
                    Bukkit.broadcastMessage(Color.translate(string).replace("$player", args[0]));
                    Firefly.banCount++;
                } else {
                    Bukkit.broadcastMessage(Color.translate(string).replace("$player", args[0]));
                    Firefly.banCount++;
                }
            }
        }
    }
}
