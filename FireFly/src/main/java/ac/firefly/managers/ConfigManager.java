package ac.firefly.managers;

import ac.firefly.Firefly;
import ac.firefly.util.file.Config;

public class ConfigManager {

    public static ConfigManager instance;

    static {
        instance = new ConfigManager();
    }

    public static Config settings;
    public static Config messages;

    public void setup() {
        settings = new Config(Firefly.instance, "settings.yml", null);
        messages = new Config(Firefly.instance, "messages.yml", null);
        settings.saveDefaultConfig();
        messages.saveDefaultConfig();
    }
}
