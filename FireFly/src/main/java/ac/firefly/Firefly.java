package ac.firefly;

import ac.firefly.check.Check;
import ac.firefly.events.update.UpdateEvent;
import ac.firefly.handlers.UpdateHandler;
import ac.firefly.tasks.WaveTask;
import ac.firefly.check.impl.other.payload.PayloadB;
import ac.firefly.client.ClientListener;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.etc.APIUtil;
import ac.firefly.util.interaction.WorldUtil;
import ac.firefly.util.webhook.WebhookUtil;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import ac.firefly.tasks.DataTask;
import ac.firefly.managers.PluginManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Firefly extends JavaPlugin {

    public static int banCount = 0;
    public static String version;
    public static JavaPlugin instance;
    public static BukkitRunnable dataSys;
    public static BukkitRunnable banWave;
    public static ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        //antiAgent();
        if(!Bukkit.getPluginManager().isPluginEnabled("ProtocolLib")) {
            Bukkit.getLogger().info("[FireFly] ProtocolLib Is Required To Use FireFly");
            Bukkit.getPluginManager().disablePlugin(this);
        }
        banWave = new WaveTask();
        protocolManager = ProtocolLibrary.getProtocolManager();
        dataSys = new DataTask(); // Anti Piracy
        instance = this;
        new UpdateHandler(this);
        APIUtil.startup();
        version  = getDescription().getVersion();
        ConfigManager.instance.setup();
        PluginManager.instance.setup(this);
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        banCount = config.getInt("ban.bans");
        registerChannels();
        Bukkit.getLogger().info("[FireFly] Enabled FireFly");
        if(config.getBoolean("webhook.enabled")) {
            WebhookUtil.sendMessage(ConfigManager.messages.getConfiguration().getString("webhook.startup"));
        }
        if (!config.getBoolean("instantbans")) {
            banWave.runTaskTimerAsynchronously(this, 0, 20 * (config.getInt("time.banwavetime") * 60));
        }
        dataSys.runTaskTimerAsynchronously(this, 0, 20 * 10 * 60);
    }

    @Override
    public void onDisable() {
        APIUtil.submitTotalVL(Check.getTotalVL());
        APIUtil.shutdown();
        ConfigManager.settings.getConfiguration().set("ban.bans", banCount);
        PluginManager.instance.saveChecks();
        if(ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
            WebhookUtil.sendMessage(ConfigManager.messages.getConfiguration().getString("webhook.shutdown"));
        }
        Bukkit.getLogger().info("[FireFly] Disabled FireFly");
    }

    public static String getVersion() {
        return version;
    }

    public static void banPlayer(Player p) {
        Bukkit.getScheduler().runTask(Firefly.instance, new Runnable() {
            @Override
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.settings.getConfiguration().getString("ban.command").replace("$player", p.getName()));
            }
        });
    }

    private void registerChannels() {
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "MC|Brand", new ClientListener());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "minecraft:brand", new ClientListener());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "CB-Client", new ClientListener());
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "LiteLoader", new ClientListener());

        String[] clientList = {"LOLIMAHCKER", "CPS_BAN_THIS_NIGGER", "EROUAXWASHERE", "EARWAXWASHERE", "#unbanearwax", "1946203560", "cock", "lmaohax", "reach", "gg", "customGuiOpenBspkrs", "0SO1Lk2KASxzsd", "MCnetHandler", "n", "CRYSTAL|KZ1LM9TO", "CRYSTAL|6LAKS0TRIES", "BLC|M"};

        for (String s : clientList) {
            getServer().getMessenger().registerIncomingPluginChannel(this, s, new PayloadB());
        }
    }

    /*public void antiAgent() {*/
        /*final List<String> BAD_INPUT_FLAGS = Arrays.asList(
                "-javaagent",
                "-agentlib"
        );

        final byte[] EMPTY_CLASS_BYTES =
                {
                        -54, -2, -70, -66, 0, 0, 0, 49, 0, 5, 1, 0, 34, 115, 117, 110,
                        47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 73,
                        110, 115, 116, 114, 117, 109, 101, 110, 116, 97, 116, 105, 111,
                        110, 73, 109, 112, 108, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47,
                        108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 0, 1,
                        0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0
                };

        Optional<String> inputFlag = ManagementFactory.getRuntimeMXBean().getInputArguments().stream()
                .filter(input -> BAD_INPUT_FLAGS.stream().anyMatch(input::contains))
                .findFirst();

        // if there's a bad input flag present in the vm options
        // then InstrumentationImpl will already have been loaded
        if (inputFlag.isPresent()) {
            throw new IllegalArgumentException(String.format("Bad VM option \"%s\"", inputFlag.get()));
        }

        Unsafe unsafe = Firefly.getUnsafe();
        
        unsafe.defineClass("sun.instrument.InstrumentationImpl", EMPTY_CLASS_BYTES, 0, EMPTY_CLASS_BYTES.length, null, null);

        // this is for testing purposes to make sure it's actually loaded
        try {
            Class.forName("sun.instrument.InstrumentationImpl");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static Unsafe getUnsafe() {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");

            unsafeField.setAccessible(true);

            return (Unsafe) unsafeField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
