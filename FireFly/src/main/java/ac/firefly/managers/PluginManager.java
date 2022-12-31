package ac.firefly.managers;

import ac.firefly.check.Check;
import ac.firefly.events.*;
import ac.firefly.gui.CheckGUI;
import ac.firefly.gui.MainGUI;
import ac.firefly.gui.MenuGUI;
import ac.firefly.gui.checks.*;
import ac.firefly.handlers.PacketHandler;
import ac.firefly.util.interaction.NEW_Velocity_Utils;
import ac.firefly.util.interaction.ServerUtils;
import ac.firefly.util.interaction.SetBackSystem;
import ac.firefly.util.interaction.VelocityUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class PluginManager {

    public static PluginManager instance;

    private DataManager dataManager;
    public PacketHandler packet;
    public static HashMap<Player,Boolean> alertsToggled = new HashMap<>();

    static {
        instance = new PluginManager();
    }

    public void setup(JavaPlugin plugin) {

        dataManager = new DataManager();
        packet = new PacketHandler();
        PacketHandler.init();
        loadChecks();
        addDataPlayers();
        CommandManager.instance.setupCommands(plugin);

        // Registering events
        getServer().getPluginManager().registerEvents(new MainGUI(), plugin);
        getServer().getPluginManager().registerEvents(new CheckGUI(), plugin);

        getServer().getPluginManager().registerEvents(new CombatGUI(), plugin);
        getServer().getPluginManager().registerEvents(new MovementGUI(), plugin);
        getServer().getPluginManager().registerEvents(new PlayerGUI(), plugin);
        getServer().getPluginManager().registerEvents(new MiscellaneousGUI(), plugin);
        getServer().getPluginManager().registerEvents(new WorldGUI(), plugin);

        getServer().getPluginManager().registerEvents(new MenuGUI(), plugin);
        getServer().getPluginManager().registerEvents(new UtilityMoveEvent(), plugin);
        getServer().getPluginManager().registerEvents(new UtilityJoinQuitEvent(), plugin);
        getServer().getPluginManager().registerEvents(new SetBackSystem(), plugin);
        getServer().getPluginManager().registerEvents(new VelocityUtils(), plugin);
        getServer().getPluginManager().registerEvents(new NEW_Velocity_Utils(), plugin);
        getServer().getPluginManager().registerEvents(new CinemeaticListener(), plugin);
        getServer().getPluginManager().registerEvents(new PerdictionListener(), plugin);

        //getServer().getPluginManager().registerEvents(new DevListener(), plugin);
    }

    public void loadChecks() {
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        for (Check check : getDataManager().getChecks()) {
            if (config.get("checks." + check.getName() + ".enabled") != null) {
                check.setEnabled(config.getBoolean("checks." + check.getName() + ".enabled"));
            } else {
                config.set("checks." + check.getName() + ".enabled", check.isEnabled());
                ConfigManager.settings.saveConfig();
            }
            ServerUtils.logDebug(null,"Loaded checks: " + check);
        }
    }

    public void saveChecks() {
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        for (Check check : getDataManager().getChecks()) {
            config.set("checks." + check.getName() + ".enabled", check.isEnabled());
            ConfigManager.settings.saveConfig();
            ServerUtils.logDebug(null,"Saved checks: " + check);
        }
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public void addDataPlayers() {
        for (Player playerLoop : getServer().getOnlinePlayers()) {
            instance.getDataManager().addPlayerData(playerLoop);
            ServerUtils.logDebug(null,"Added data players: " + playerLoop);
        }
    }
}
