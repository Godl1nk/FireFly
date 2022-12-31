package rip.firefly.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import rip.firefly.FireFly;
import rip.firefly.Initializer;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.check.AbstractCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.gui.impl.MainGUI;
import rip.firefly.manager.CheckManager;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;
import rip.firefly.util.command.Command;
import rip.firefly.util.command.CommandArgs;
import rip.firefly.util.misc.ColorUtil;

public class FireFlyCommand {
    @Command(name = "$ffcommand", aliases = "$mainalias", permission = "firefly.admin")
    public void FireFlyCommand(CommandArgs commandArgs) {
        Player p = commandArgs.getPlayer();


        String[] args = commandArgs.getArgs();

        if (args.length < 1) {
            sendHelp(p);
            return;
        } else if (args[0].equalsIgnoreCase("help") && args.length == 1) {
            sendHelp(p);
            return;
        } else if (args[0].equalsIgnoreCase("gui") && args.length == 1) {
            MainGUI.openGUI(p);
            return;
        } else if (args[0].equalsIgnoreCase("info") && args.length == 1) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eFire&6&lFly &7> &eInformation"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eVersion &7- &6" + FireFly.getVersion()));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePackage &7- &6" + FireFly.getType().getName()));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTotal Flags &7- &6" + BridgeData.totalFlags));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eTotal Bans &7- &6" + BridgeData.totalBans));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eChecks &7- &6" + CheckManager.getChecks().size()));
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7&m-----------------------------------------------------"));
            return;
        } else if (args[0].contains("inspect")) {

            if (args.length < 2) {
                p.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&7&m-----------------------------------------------------\n" +
                                        "&eFire&6&lFly &7> &eInspect \n"+
                                        "&7&m-----------------------------------------------------\n" +
                                        "&eArgs: &6/firefly inspect <player>" +
                                        "&7&m-----------------------------------------------------\n"

                        )
                );
            } else if(DataManager.getData(Bukkit.getPlayer(args[1])) == null || Bukkit.getPlayer(args[1]) == null) {
                p.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&cThat Player Has Never Joined!"
                        )
                );
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                PlayerData targetData = DataManager.getData(Bukkit.getPlayer(args[1]));
                if(Math.round(targetData.getSensitivity() * 200.0) == 99) {

                    for(String s : ConfigManager.messages.getConfiguration().getStringList("command.inspectCommand")) {
                        p.sendMessage(ColorUtil.translate(s
                                .replace("$player", target.getName())
                                .replace("$sensitivity", "100"))
                                .replace("$cinematic", Boolean.toString(targetData.isCinematic()))
                                .replace("$ping", Long.toString(targetData.getPing()))
                                .replace("$version", targetData.getPlayerVersion().getServerVersion())
                                .replace("$uuid", targetData.getUuid().toString())
                        );
                    }
//                    p.sendMessage(
//                            ChatColor.translateAlternateColorCodes('&',
//                                    "&7&m-----------------------------------------------------\n" +
//                                            "&eFire&6&lFly &7> &eInspect \n"+
//                                            "&7&m-----------------------------------------------------\n" +
//                                            "&6Inspection Of &e" + target.getName() + "\n\n" +
//                                            "&6Mouse Sensativity: &e100%\n" +
//                                            "&6Cinematic Camera: &e" + targetData.isCinematic() + "\n" +
//                                            "&6Ping: &e" + targetData.getPing() + "\n" +
//                                            "&6Version: &e" + targetData.getPlayerVersion().getServerVersion() + "\n" +
//                                            "&6UUID: &e" + targetData.getUuid().toString() + "\n" +
//                                            "&7&m-----------------------------------------------------\n"
//
//                            )
//                    );
                } else {

                    for(String s : ConfigManager.messages.getConfiguration().getStringList("command.inspectCommand")) {
                        p.sendMessage(ColorUtil.translate(s
                                .replace("$player", target.getName())
                                .replace("$sensitivity", (Long.toString(Math.round(targetData.getSensitivity() * 200.0))))
                                .replace("$cinematic", Boolean.toString(targetData.isCinematic()))
                                .replace("$ping", Long.toString(targetData.getPing()))
                                .replace("$version", targetData.getPlayerVersion().getServerVersion())
                                .replace("$uuid", targetData.getUuid().toString())
                        ));
                    }
//                    p.sendMessage(
//                            ChatColor.translateAlternateColorCodes('&',
//                                    "&7&m-----------------------------------------------------\n" +
//                                            "&eFire&6&lFly &7> &eInspect \n"+
//                                            "&7&m-----------------------------------------------------\n" +
//                                            "&6Inspection Of &e" + target.getName() + "\n\n" +
//                                            "&6Mouse Sensitivity: &e" + Math.round(targetData.getSensitivity() * 200.0) + "%\n" +
//                                            "&6Cinematic Camera: &e" + targetData.isCinematic() + "\n" +
//                                            "&6Ping: &e" + targetData.getPing() + "\n" +
//                                            "&6Version: &e" + targetData.getPlayerVersion().getServerVersion() + "\n" +
//                                            "&6UUID: &e" + targetData.getUuid().toString() + "\n" +
//                                            "&7&m-----------------------------------------------------\n"
//
//                            )
//                    );
                }


            }

            return;
        } else if (args[0].equalsIgnoreCase("reload")) {
            long now = System.currentTimeMillis();
            ConfigManager.messages.reloadConfig();
            ConfigManager.settings.reloadConfig();
            System.gc();
            Runtime.getRuntime().gc();
            for(String s : ConfigManager.messages.getConfiguration().getStringList("command.reloadCommand")) {
                p.sendMessage(
                        ColorUtil.translate(s
                            .replace("$time", Long.toString((System.currentTimeMillis() - now)))
                            .replace("$version", FireFly.getVersion())
                        )
                );
            }
            return;
        } else if (args[0].equalsIgnoreCase("update")) {
            Initializer.getLoader().update();
        } else if (args[0].contains("resetvl")) {

            if (args.length < 2) {
                p.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&7&m-----------------------------------------------------\n" +
                                        "&eFire&6&lFly &7> &eReset VL \n"+
                                        "&7&m-----------------------------------------------------\n" +
                                        "&eArgs: &6/firefly resetvl <player>" +
                                        "&7&m-----------------------------------------------------\n"

                        )
                );
            } else if(DataManager.getData(Bukkit.getPlayer(args[1])) == null || Bukkit.getPlayer(args[1]) == null) {
                p.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&cThat Player Has Never Joined!"
                        )
                );
            } else {
                Player target = Bukkit.getPlayer(args[1]);
                PlayerData targetData = DataManager.getData(Bukkit.getPlayer(args[1]));
                targetData.resetVl();
                p.sendMessage(
                        ChatColor.translateAlternateColorCodes('&',
                                "&7&m-----------------------------------------------------\n" +
                                        "&eFire&6&lFly &7> &eReset VL \n"+
                                        "&7&m-----------------------------------------------------\n" +
                                        "&eReset &6" + target.getName() + "'s&e VL\n" +
                                        "&7&m-----------------------------------------------------\n"

                        )
                );


            }

        } else {
            sendHelp(p);
        }
    }

    private void sendHelp(Player p) {
        for(String s : ConfigManager.messages.getConfiguration().getStringList("command.helpCommand")) {
            p.sendMessage(ColorUtil.translate(s));
        }

        //            p.sendMessage(
//                    ChatColor.translateAlternateColorCodes('&',
//                            "&7&m-----------------------------------------------------\n" +
//                                    "&eFire&6&lFly &7> &eHelp \n" +
//                                    "&7&m-----------------------------------------------------\n" +
//                                    "&e/firefly checks &8&m-&6 Lists the enabled checks\n" +
//                                    "&e/firefly info &8&m-&6 Lists information about FireFly\n" +
//                                    "&e/firefly gui &8&m-&6 Opens FireFly's GUI\n" +
//                                    "&e/firefly inspect <player> &8&m-&6 Inspect A Player\n" +
//                                    "&7&m-----------------------------------------------------\n"
//
//                    )
//            );

    }
}
