package ac.firefly.command;

import ac.firefly.gui.MainGUI;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.command.Command;
import ac.firefly.util.command.CommandArgs;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GuiCommand {

    @Command(name = "ffgui", inGameOnly = true, permission = "firefly.admin")
    public void GuiCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
       // if (!p.hasPermission("firefly.admin")) {
       //     p.sendMessage(ChatColor.RED + "You haven't got permission to run this command.");
       //     return;
       // }
        MainGUI.openGUI(p);
    }
}