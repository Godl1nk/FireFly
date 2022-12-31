package rip.firefly.manager;

import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.util.misc.Configuration;

public class ConfigManager {

    public static ConfigManager instance;

    public static Configuration settings;
    public static Configuration messages;

    public ConfigManager(JavaPlugin fireFly) {
        instance = this;

        settings = new Configuration(fireFly, "firefly.yml", null);
        messages = new Configuration(fireFly, "messages.yml", null);
        settings.saveDefaultConfig();
        messages.saveDefaultConfig();
    }

    public ConfigManager getInstance() {
        return instance;
    }
}
