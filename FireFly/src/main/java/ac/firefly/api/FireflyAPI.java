package ac.firefly.api;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.managers.ConfigManager;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.formatting.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class FireflyAPI {
    public static void silentBan(Player player) {
        Firefly.banPlayer(player);
    }

    public static void banPlayer(Player player) {
        for (String string : ConfigManager.messages.getConfiguration().getStringList("ban.broadcast")) {
            Bukkit.broadcastMessage(Color.translate(string).replace("$player", player.getName()));
        }
        Firefly.banPlayer(player);
    }

    public static void resetVL(Player player) {
        PluginManager.instance.getDataManager().resetViolation(player);
    }

    public static Integer getCheckVL(Player player, Check check) {
        Integer vl;

        vl = PluginManager.instance.getDataManager().getViolatonsPlayer(player, check);

        return vl;
    }

    public static Integer getTotalVL(Player player) {
        Integer playerVL = 0;
        for (Check check : PluginManager.instance.getDataManager().getChecks()) {
            playerVL = playerVL + PluginManager.instance.getDataManager().getViolatonsPlayer(player, check);
        }
        return playerVL;
    }

    public static Check getCheck(String checkName) {
        return PluginManager.instance.getDataManager().getCheckByName(checkName);
    }
}
