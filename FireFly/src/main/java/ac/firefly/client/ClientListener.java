package ac.firefly.client;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.Bukkit;
import ac.firefly.data.PlayerData;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.formatting.Color;
import ac.firefly.managers.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;


public class ClientListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PlayerData pData = PluginManager.instance.getDataManager().getData(player);

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String brand = in.readLine();
        if(brand.contains("lunarclient")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                        .replace("$prefix", Color.translate(msg.getString("prefix")))
                        .replace("$player", player.getName())
                        .replace("$client", "Lunar Client"));
                }
            }
        } else if(brand.contains("fml,forge")) {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "Forge"));
                }
            }
        } else if(brand.contains("vanilla")) {
           // pData.setClient(ClientType.VANILLA);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "Vanilla"));
                }
            }
        } else if(brand.contains("PLC18") || brand.contains("PLC17")) {
           // pData.setClient(ClientType.PVPLOUNGE);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "PvPLounge"));
                }
            }
        } else if(brand.contains("fabric")) {
            //pData.setClient(ClientType.FABRIC);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "Fabric"));
                }
            }
        } else if(brand.contains("LiteLoader") || brand.contains("liteloader")) {
            //pData.setClient(ClientType.LITELOADER);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "LiteLoader"));
                }
            }
        } else {
            //pData.setClient(ClientType.UNKNOWN);
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("client_message"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", player.getName())
                            .replace("$client", "Unknown"));
                }
            }
        }
        Bukkit.getLogger().info("[FireFly] [DEBUG] " + brand);
    }
}
