package ac.firefly.command;

import ac.firefly.Firefly;
import ac.firefly.api.event.BanWaveEvent;
import ac.firefly.managers.PunishManager;
import ac.firefly.gui.MainGUI;
import ac.firefly.managers.PluginManager;
import ac.firefly.tasks.WaveTask;
import ac.firefly.util.command.Command;
import ac.firefly.util.command.CommandArgs;
import ac.firefly.check.Check;
import ac.firefly.util.formatting.Color;
import ac.firefly.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class FireFlyCommand {

    @Command(name = "firefly", inGameOnly = true, aliases = "ff", permission = "firefly.admin")
    public void FireFlyCommand(CommandArgs ca) {
        String c = ca.getCommand().getName();
        Player p = ca.getPlayer();
        String[] args = ca.getArgs();
        FileConfiguration config = ConfigManager.settings.getConfiguration();
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        if (args.length < 1) {
                sendHelp(p);
                return;
            }
            if (args[0].equalsIgnoreCase("help") && args.length == 1) {
                sendHelp(p);
                return;
            }
            if (args[0].equalsIgnoreCase("gui") && args.length == 1) {
                MainGUI.openGUI(p);
                return;
            }
            if (args[0].equalsIgnoreCase("list") && args.length == 1) {
                List<String> checkNames = new ArrayList<>();

                for (Check checkLoop : PluginManager.instance.getDataManager().getChecks()) {
                    checkNames.add((checkLoop.isEnabled() ? "§a" + checkLoop.getName() : "§c" + checkLoop.getName()) + "§7");
                }
                for (String msg2 : msg.getStringList("commands.firefly.list")) {
                    p.sendMessage(Color.translate(msg2).replace("$checks", checkNames.toString()));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
                p.sendMessage(Color.translate(msg.getString("commands.firefly.reloading")));
                long before;
                long after;
                before = System.currentTimeMillis();
                try {
                    ConfigManager.settings.reloadConfig();
                    ConfigManager.messages.reloadConfig();
                    after = System.currentTimeMillis();
                    long reloadTime = after - before;
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.reload_success").replace("$time", "" + reloadTime)));
                } catch (Exception ex) {
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.reload_failed")));
                    ex.printStackTrace();
                }
                return;
            }
            if (args[0].equalsIgnoreCase("toggle") && args.length == 2) {
                Check check = PluginManager.instance.getDataManager().getCheckByName(args[1]);
                if (check != null) {
                    check.setEnabled(!check.isEnabled());
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.toggled_check")
                            .replace("$check", args[1])
                            .replace("$state", (check.isEnabled() ? "§a" : "§c") + check.isEnabled())));
                    return;
                }
                p.sendMessage(Color.translate(msg.getString("commands.firefly.invalid_check").replace("$check", args[1])));
                return;
            }
            if (args[0].equalsIgnoreCase("debug") && args.length == 1) {
                if (config.getBoolean("debug")) {
                    config.set("debug", false);
                    ConfigManager.settings.saveConfig();
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.toggled_debug").replace("$state", (config.getBoolean("debug") ? "§a" : "§c") + config.getBoolean("debug"))));
                    return;
                }
                config.set("debug", true);
                ConfigManager.settings.saveConfig();
                p.sendMessage(Color.translate(msg.getString("commands.firefly.toggled_debug").replace("$state", (config.getBoolean("debug") ? "§a" : "§c") + config.getBoolean("debug"))));
                return;
            }
            if (args[0].equalsIgnoreCase("resetvio") && args.length == 2) {
                Player pp = Bukkit.getPlayer(args[1]);
                if (pp != null) {
                    try {
                        PluginManager.instance.getDataManager().resetViolation(pp);
                        p.sendMessage(Color.translate(msg.getString("commands.firefly.resetviolations_success").replace("$player", pp.getName())));
                    } catch (Exception ex) {
                        p.sendMessage(Color.translate(msg.getString("commands.firefly.resetviolations_failed").replace("$player", pp.getName())));
                        ex.printStackTrace();
                    }
                    return;
                }
                p.sendMessage(Color.translate(msg.getString("not_online").replace("$player", args[1])));
                return;
            }
            if (args[0].equalsIgnoreCase("banwave") && args.length == 1) {
                if(config.getBoolean("instantbans")) {
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.notEnabled")));
                    return;
                } else {
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.startingWave")));
                }
                BukkitRunnable wave = new WaveTask();
                wave.runTaskLaterAsynchronously(Firefly.instance, 1500);
                return;
            }
            if (args[0].equalsIgnoreCase("banqueue") && args.length == 1) {
                if(config.getBoolean("instantbans")) {
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.notEnabled")));
                    return;
                }
                for (String msg2 : msg.getStringList("commands.firefly.list")) {
                    p.sendMessage(Color.translate(msg2).replace("$banqueue", PunishManager.banWave.keySet().toString()));
                }
                return;
            }
            if (args[0].equalsIgnoreCase("cancelban") && args.length == 2) {
                if(config.getBoolean("instantbans")) {
                    p.sendMessage(Color.translate(msg.getString("commands.firefly.notEnabled")));
                    return;
                }
                Player pp = Bukkit.getPlayer(args[1]);
                if (pp != null) {
                    if (PunishManager.banWave.keySet().contains(pp)) {
                        PunishManager.removePunish(pp);
                        return;
                    } else {
                        p.sendMessage(Color.translate("&cCould Not Find A Player By The Name Of '" + pp.getName() + "' In The Ban Wave."));
                        return;
                    }
                }
                p.sendMessage(Color.translate(msg.getString("not_online").replace("$player", args[1])));
                return;
            }
            sendHelp(p);
    }

    private void sendHelp(Player p) {
        FileConfiguration msg = ConfigManager.messages.getConfiguration();
        for (String msg2 : msg.getStringList("commands.firefly.help")) {
            p.sendMessage(Color.translate(msg2));
        }
    }
}
