package ac.firefly.managers;

import ac.firefly.Firefly;
import ac.firefly.api.event.PreBanEvent;
import ac.firefly.util.formatting.Color;
import ac.firefly.util.interaction.Crash;
import ac.firefly.util.webhook.WebhookUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PunishManager {

    public static HashMap<Player,Boolean> banWave = new HashMap<>();

    public static void punishPlayer(Player p) {
        banWave.put(p, true);
        FileConfiguration sttng = ConfigManager.settings.getConfiguration();
        if (sttng.getBoolean("instantbans")) {
            final PreBanEvent preBanEvent = new PreBanEvent(p);
            Bukkit.getPluginManager().callEvent(preBanEvent);
            if(ConfigManager.settings.getConfiguration().getBoolean("ban.crash")) {
                Crash.crashPlayer(p);
            }

            Firefly.banPlayer(p);

            for (String string : ConfigManager.messages.getConfiguration().getStringList("ban.broadcast")) {
                Bukkit.broadcastMessage(Color.translate(string).replace("$player", p.getName()));
            }
            if(sttng.getBoolean("webhook.enabled")) {
                WebhookUtil.sendMessage(ConfigManager.messages.getConfiguration().getString("webhook.instantban").replace("$player", p.getName()));
            }
            Firefly.banCount++;
        } else {
            for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                FileConfiguration msg = ConfigManager.messages.getConfiguration();
                if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                    staff.sendMessage(Color.translate(msg.getString("punish.addToBanWave")
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", p.getName())
                    ));
                }
            }
        }
    }

    public static void removePunish(Player p) {
        banWave.remove(p);
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            FileConfiguration msg = ConfigManager.messages.getConfiguration();
            if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                staff.sendMessage(Color.translate(msg.getString("punish.removeFromBanWave")
                        .replace("$prefix", Color.translate(msg.getString("prefix")))
                        .replace("$player", p.getName())
                ));
            }
        }
    }
}
