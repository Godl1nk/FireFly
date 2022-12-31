package rip.firefly;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.api.event.ACStartEvent;
import rip.firefly.bridge.Bridge;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.bridge.util.BridgeUtil;
import rip.firefly.data.PlayerData;
import rip.firefly.gui.GUIManager;
import rip.firefly.loader.LoaderPlugin;
import rip.firefly.loader.core.Plugin;
import rip.firefly.manager.*;
import rip.firefly.packet.TinyProtocolHandler;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.api.packets.reflection.Reflections;
import rip.firefly.type.FFPackage;
import rip.firefly.util.command.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FireFly implements Plugin {

    @Getter private static final String version = "b9242";
    @Getter private JavaPlugin plugin;
    private static FireFly instance;
    public ConfigManager configManager;
    public CheckManager checkManager;
    public DataManager dataManager;
    public CommandManager commandManager;
    public static TinyProtocolHandler protocolHandler;

    private TransactionManager.TransactionTask transactionTask;
    private HandlerManager handlerManager;
    private GUIManager guiManager;
    private static FFPackage type;

    @Getter private static ScheduledExecutorService protocolService = Executors.newSingleThreadScheduledExecutor();
    @Getter private static ScheduledExecutorService checkService = Executors.newSingleThreadScheduledExecutor();
    @Getter private static ScheduledExecutorService webhookService = Executors.newSingleThreadScheduledExecutor();

    public FireFly(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public void onEnable() {
        instance = this;


        boolean bypassValidityChecks = Objects.equals(System.getProperty("ffNoVerify"), "true");

        if(!Reflections.classExists("org.spigotmc.SpigotConfig") && !bypassValidityChecks) {
            plugin.getLogger().info("-------------------------------------------------------------------");
            plugin.getLogger().info("FireFly Was Unloaded");
            plugin.getLogger().info("Reason: To Use FireFly You Need Spigot.");
            plugin.getLogger().info("-------------------------------------------------------------------");

            Initializer.getLoader().forceDisable();
        }
        if(Reflections.classExists("net.frozenorb.chunksnapshot.CraftChunkSnapshot") && !bypassValidityChecks) {
            plugin.getLogger().info("-------------------------------------------------------------------");
            plugin.getLogger().info("FireFly Was Unloaded");
            plugin.getLogger().info("Reason: mSpigot Is Not Supported By FireFly.");
            plugin.getLogger().info("-------------------------------------------------------------------");

            Initializer.getLoader().forceDisable();
        }


        transactionTask = new TransactionManager.TransactionTask();
        protocolHandler = new TinyProtocolHandler(plugin);
        configManager = new ConfigManager(plugin);
        dataManager = new DataManager(plugin);
        handlerManager = new HandlerManager(plugin);
        checkManager = new CheckManager(plugin);
        commandManager = new CommandManager(plugin);
        guiManager = new GUIManager();

        if(ProtocolVersion.getGameVersion().isAbove(ProtocolVersion.V1_12_2)) {
            plugin.getLogger().info("-------------------------------------------------------------------");
            plugin.getLogger().info("FireFly Is In Experimental Mode!");
            plugin.getLogger().info("Reason: 1.12.2+ Support Is Experimental.");
            plugin.getLogger().info("-------------------------------------------------------------------");
        }

        type = FFPackage.ENTERPRISE;

        Bridge.loadBridge(plugin);

        final ACStartEvent event = new ACStartEvent(version, BridgeUtil.getHWID(), type.getName());
        Bukkit.getPluginManager().callEvent(event);

        switch (type) {
            case ENTERPRISE:
                plugin.getLogger().info("FireFly " + version + " (Enterprise) Has Been Loaded");
                break;
            case STANDARD:
                plugin.getLogger().info("FireFly " + version + " Has Been Loaded");
        }

    }

    @Override
    public void onDisable() {
//        try {
//            URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/vl/addvl?vl=" + BridgeData.totalFlags);
//            URLConnection conn = auth.openConnection();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(
//                            conn.getInputStream()));
//            String inputLine;
//            ArrayList<String> array = new ArrayList<>();
//            while ((inputLine = in.readLine()) != null)
//                array.add(inputLine);
//            in.close();
//        } catch (IOException exception) {
//
//        }
//
//        try {
//            URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/server/shutdown");
//            URLConnection conn = auth.openConnection();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(
//                            conn.getInputStream()));
//            String inputLine;
//            ArrayList<String> array = new ArrayList<>();
//            while ((inputLine = in.readLine()) != null)
//                array.add(inputLine);
//            in.close();
//        } catch (IOException exception) {
//
//        }

        DataManager.getData().clear();

        transactionTask.stop();
        switch (type) {
            case ENTERPRISE:
                plugin.getLogger().info("FireFly " + version + " (Enterprise) Has Been Unloaded");
                break;
            case STANDARD:
                plugin.getLogger().info("FireFly " + version + " Has Been Unloaded");
        }
    }

    public static FireFly getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }

    public CheckManager getCheckManager() {
        return checkManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public TransactionManager.TransactionTask getTransactionTask() {
        return transactionTask;
    }

    public static TinyProtocolHandler getProtocolHandler() {
        return protocolHandler;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static FFPackage getType() {
        return type;
    }
}
