package ac.firefly.managers;

import ac.firefly.command.*;
import ac.firefly.util.command.CommandFramework;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {

    public static CommandManager instance;

    static {
        instance = new CommandManager();
    }

    CommandFramework framework;

    public void setupCommands(JavaPlugin plugin) {
        framework = new CommandFramework(plugin);
        framework.registerCommands(new FireFlyCommand());
        framework.registerCommands(new GuiCommand());
        framework.registerCommands(new AlertsCommand());
        framework.registerCommands(new BanCommand());
        framework.registerCommands(new CrashCommand());
        framework.registerHelp();
    }
}