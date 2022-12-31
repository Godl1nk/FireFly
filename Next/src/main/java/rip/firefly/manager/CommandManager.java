package rip.firefly.manager;

import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.command.AlertsCommand;
import rip.firefly.command.FireFlyCommand;
import rip.firefly.util.command.CommandFramework;

public class CommandManager {

    public static CommandManager instance;

    CommandFramework framework;

    public CommandManager(JavaPlugin fireFly) {
        instance = this;

        framework = new CommandFramework(fireFly);
        framework.registerCommands(new FireFlyCommand());
        framework.registerCommands(new AlertsCommand());
        framework.registerHelp();
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public CommandFramework getFramework() {
        return framework;
    }
}
