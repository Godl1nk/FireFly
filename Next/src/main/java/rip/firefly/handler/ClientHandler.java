package rip.firefly.handler;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import rip.firefly.check.impl.misc.payload.PayloadB;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;
import rip.firefly.util.misc.ColorUtil;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements PluginMessageListener, Listener {

    public ClientHandler(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "MC|Brand", this);
    }

    private static final String[] INVALID_BRANDS = {"Vanilla", "\u0007Vanilla", "Synergy", "\u0007Synergy", "Created By ", "\u0007Created By "};

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] msg) {
        String brand = new String(msg, StandardCharsets.UTF_8).substring(1);

        PlayerData data = DataManager.getData(p);
        for (String s : INVALID_BRANDS) {
            if(s.equals(brand)) {
                data.getCheckManager().getCheck(PayloadB.class).flag(data, String.format("B: %s", brand));
            }
        }
        if(brand.contains("lunarclient")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "Lunar Client"));
                }
            }
        } else if(brand.equals("vanilla") || brand.equals("\u0007vanilla")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "Vanilla"));
                }
            }
        } else if(brand.equals("PLC18") || brand.equals("PLC17")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "PvPLounge Client"));
                }
            }
        } else if(brand.equals("Feather Forge") || brand.equals("Feather Fabric")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "Feather Client"));
                }
            }
        } else if(brand.contains("lunarclient:")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "Lunar Client"));
                }
            }
        } else if(brand.contains("LiteLoader")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "LiteLoader"));
                }
            }
        } else if(brand.contains("fml,forge")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$client", "ForgeModLoader"));
                }
            }
        }


    }

    private static void addChannel(Player p, String channel) {
        try {
            p.getClass().getMethod("addChannel", String.class).invoke(p, channel);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                | SecurityException e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        addChannel(e.getPlayer(), "MC|BRAND");
        addChannel(e.getPlayer(), "minecraft:brand");
        addChannel(e.getPlayer(), "fml,forge");
        addChannel(e.getPlayer(), "LiteLoader");
    }

    @EventHandler
    public void onRegister(PlayerRegisterChannelEvent e) {
        if(e.getChannel().contains("CB-C")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", e.getPlayer().getName())
                            .replace("$client", "CheatBreaker"));
                }
            }
        } if(e.getChannel().contains("lunarclient") || e.getChannel().contains("LC")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("join"))
                            .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                            .replace("$player", e.getPlayer().getName())
                            .replace("$client", "Lunar Client"));
                }
            }
        }
    }
}
