package rip.firefly.check;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.firefly.FireFly;
import rip.firefly.api.event.PunishEvent;
import rip.firefly.api.event.FlagEvent;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.data.PlayerData;
import rip.firefly.manager.ConfigManager;
import rip.firefly.manager.DataManager;
import rip.firefly.util.draw.GraphUtil;
import rip.firefly.util.lag.LagUtil;
import rip.firefly.util.misc.ColorUtil;
import rip.firefly.util.webhook.DiscordWebhook;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Getter
public abstract class AbstractCheck<T> {
    protected final PlayerData playerData;

    private String checkName, description, subType, verusId;
    private int threshold;
    protected double buffer;
    private CheckType type;

    private boolean enabled, autobans, expermimental, enterprise;

    /**
     * The constructor to create an AbstractCheck class
     * @param playerData The player to register the check to.
     */
    public AbstractCheck(PlayerData playerData) {
        this.playerData = playerData;

        final Class clazz = this.getClass();

        if (clazz.isAnnotationPresent(CheckData.class)) {
            final CheckData checkData = (CheckData) clazz.getAnnotation(CheckData.class);

            this.checkName = checkData.name();

            this.subType = checkData.subType();

            this.threshold = checkData.threshold();

            this.enterprise = checkData.enterprise();

            if(this.checkName.toLowerCase().contains("aim")) {
                this.verusId = "1";
            } else if(this.checkName.toLowerCase().contains("clicker")) {
                this.verusId = "2";
            } else if(this.checkName.toLowerCase().contains("invalid")) {
                this.verusId = "3";
            } else if(this.checkName.toLowerCase().contains("fly")) {
                this.verusId = "4";
            } else if(this.checkName.toLowerCase().contains("inv")) {
                this.verusId = "5";
            }  else if(this.checkName.toLowerCase().contains("aura")) {
                this.verusId = "6";
            } else if(this.checkName.toLowerCase().contains("payload")) {
                this.verusId = "7";
            } else if(this.checkName.toLowerCase().contains("phase")) {
                this.verusId = "8";
            }  else if(this.checkName.toLowerCase().contains("reach")) {
                this.verusId = "9";
            } else if(this.checkName.toLowerCase().contains("speed")) {
                this.verusId = "10";
            } else if(this.checkName.toLowerCase().contains("timer")) {
                this.verusId = "11";
            } else if(this.checkName.toLowerCase().contains("velocity")) {
                this.verusId = "12";
            } else if(this.checkName.toLowerCase().contains("scaffold")) {
                this.verusId = "14";
            } else if(this.checkName.toLowerCase().contains("crasher")) {
                this.verusId = "15";
            } else {
                this.verusId = "13";
            }


            if(!checkData.experimental()) {
                this.autobans = checkData.autoban();
            } else {
                this.autobans = false;
            }

            this.expermimental = checkData.experimental();

            this.type = checkData.type();

            this.enabled = checkData.enabled();

            this.description = checkData.description();
        }
    }



//    protected void flag(Object... data) {
//        if(!isEnabled()) {
//            return;
//        }
//
//        final FlagEvent flagevent = new FlagEvent(Bukkit.getPlayer(playerData.getUuid()), getCheckName(), playerData.getViolatons(this));
//        Bukkit.getPluginManager().callEvent(flagevent);
//        if(flagevent.isCancelled()) return;
//
//        playerData.addViolation(this);
//
//        playerData.setLastFlag(System.currentTimeMillis());
//        for (Player p : Bukkit.getOnlinePlayers()) {
//            PlayerData alertPlayerData = DataManager.getData(p);
//
//            if (alertPlayerData.isAlerts()) {
//                //String graph = GraphUtil.drawGraph(20, (int) (((double)playerData.getViolatons(this) / (double)threshold) * 10)*2);
//                String graph = GraphUtil.drawGraph(ConfigManager.messages.getConfiguration().getInt("graph.maxLength"),  (((double)playerData.getViolatons(this) / (double)threshold) * 10)*(ConfigManager.messages.getConfiguration().getInt("graph.maxLength")/10));
//
//                final TextComponent mainComponent = new TextComponent(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("alert"))
//                        .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
//                        .replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())
//                        .replace("$check", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("check.format").replace("$name", checkName).replace("$verusid", verusId).replace("$subtype", subType)))
//                        .replace("$vl", Integer.toString(playerData.getViolatons(this)))
//                        .replace("$ping", Long.toString(playerData.getPing()))
//                        .replace("$graph", graph)
//                        .replace("$maxvl", Integer.toString(threshold))
//                        .replace("$tps", Double.toString(round(LagUtil.getTPS(), 2)))
//                );
//
//                StringBuilder s = new StringBuilder();
//                for(String str : Arrays.stream(data).map(Object::toString).toArray(String[]::new)) {
//                    s.append(ChatColor.YELLOW).append(str.split(":")[0]).append(":").append(ChatColor.GOLD).append(str.split(":")[1]).append("\n");
//                }
//                final String hoverComponent = ColorUtil.translate(
//                            ColorUtil.translate(
//                                    "&7&m-------------------------------------------\n" +
//                                    "&6Description: \n&e" + getDescription() + "\n\n" +
//                                    s +
//                                    "\n&eClick To Teleport\n" +
//                                    "\n&7&m-------------------------------------------"
//                            )
//                );
//
//                mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverComponent).create()));
//                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teleport " + Bukkit.getPlayer(playerData.getUuid()).getName()));
//
//                p.spigot().sendMessage( mainComponent );
//
//                if(ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
//                    DiscordWebhook webhook = new DiscordWebhook(ConfigManager.settings.getConfiguration().getString("webhook.url"));
//                    webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png");
//                    webhook.setUsername("FireFly");
//                    String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
//                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
//                            .setTitle("FireFly > Alert")
//                            .setDescription(
//                                    "```" + Bukkit.getPlayer(playerData.getUuid()).getName() + " failed " + checkName + " (" + subType + ") (" + playerData.getViolatons(this) + "/" + threshold + ")```\\n" +
//                                            "```fix\\n" +
//                                            "Description: " + description + "\\n" +
//                                            "Ping: " + playerData.getPing() + "\\n" +
//                                            "Player's Version: " + playerData.getPlayerVersion().name() + "\\n" +
//                                            "TPS: " + LagUtil.getTPS() + "\\n" +
//                                            "Location: " + Bukkit.getPlayer(playerData.getUuid()).getWorld().getName() + " | X: " + round(playerData.getTo().getX(), 1) + " Y: " + round(playerData.getTo().getY(), 1) + " Z: " + round(playerData.getTo().getX(), 1) + "```")
//                            .setColor(new Color(0xf4a45a))
//                            .setThumbnail(String.format("https://minotar.net/helm/%s/100.png", Bukkit.getPlayer(playerData.getUuid()).getName()))
//                            .setFooter("https://firefly.rip | " + time, "https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png"));
//                    new BukkitRunnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                webhook.execute();
//                            } catch (IOException ignored) {
//                            }
//                        }
//                    }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
//                }
//
//            }
//        }
//
//        BridgeData.totalFlags++;
//        if(playerData.getViolatons(this) == threshold && autobans) {
//            punish(Bukkit.getPlayer(playerData.getUuid()));
//        }
//    }

    /**
     * The method to flag a player for failing a check
     * @param data The data used to conclude that the player may be cheating
     */
    protected void flag(PlayerData playerData, Object... data) {
        if(!isEnabled()) {
            return;
        }

        final FlagEvent flagevent = new FlagEvent(Bukkit.getPlayer(playerData.getUuid()), getCheckName(), playerData.getViolatons(this));
        Bukkit.getPluginManager().callEvent(flagevent);
        if(flagevent.isCancelled()) return;

        playerData.addViolation(this);

        playerData.setLastFlag(System.currentTimeMillis());
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData alertPlayerData = DataManager.getData(p);

            if (alertPlayerData.isAlerts()) {
                //String graph = GraphUtil.drawGraph(20, (int) (((double)playerData.getViolatons(this) / (double)threshold) * 10)*2);
                String graph = GraphUtil.drawGraph(ConfigManager.messages.getConfiguration().getInt("graph.maxLength"),  (((double)playerData.getViolatons(this) / (double)threshold) * 10)*(ConfigManager.messages.getConfiguration().getInt("graph.maxLength")/10));

                final TextComponent mainComponent = new TextComponent(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("alert"))
                        .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                        .replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())
                        .replace("$check", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("check.format").replace("$name", checkName).replace("$verusid", verusId).replace("$subtype", subType)))
                        .replace("$vl", Integer.toString(playerData.getViolatons(this)))
                        .replace("$ping", Long.toString(playerData.getPing()))
                        .replace("$graph", graph)
                        .replace("$maxvl", Integer.toString(threshold))
                        .replace("$tps", Double.toString(round(LagUtil.getTPS(), 2)))
                );

                StringBuilder s = new StringBuilder();
                for(String str : Arrays.stream(data).map(Object::toString).toArray(String[]::new)) {
                    s.append(ChatColor.YELLOW).append(str.split(":")[0]).append(":").append(ChatColor.GOLD).append(str.split(":")[1]).append("\n");
                }

                StringBuilder hoverComponent = new StringBuilder();

                for (String str : ConfigManager.messages.getConfiguration().getStringList("check.hover")) {
                    hoverComponent.append(str.replace("$description", getDescription()).replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName()).replace("$data", s)).append("\n");
                }

                hoverComponent.setLength(hoverComponent.length() - 1);

                mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(hoverComponent.toString())).create()));
                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ConfigManager.messages.getConfiguration().getString("check.command").replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())));

                p.spigot().sendMessage( mainComponent );

                if(ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
                    DiscordWebhook webhook = new DiscordWebhook(ConfigManager.settings.getConfiguration().getString("webhook.url"));
                    webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png");
                    webhook.setUsername("FireFly");
                    String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setTitle("FireFly > Alert")
                            .setDescription(
                                    "```" + Bukkit.getPlayer(playerData.getUuid()).getName() + " failed " + checkName + " (" + subType + ") (" + playerData.getViolatons(this) + "/" + threshold + ")```\\n" +
                                            "```fix\\n" +
                                            "Description: " + description + "\\n" +
                                            "Ping: " + playerData.getPing() + "\\n" +
                                            "Player's Version: " + playerData.getPlayerVersion().name() + "\\n" +
                                            "TPS: " + LagUtil.getTPS() + "\\n" +
                                            "Location: " + Bukkit.getPlayer(playerData.getUuid()).getWorld().getName() + " | X: " + round(playerData.getTo().getX(), 1) + " Y: " + round(playerData.getTo().getY(), 1) + " Z: " + round(playerData.getTo().getX(), 1) + "```")
                            .setColor(new Color(0xf4a45a))
                            .setThumbnail(String.format("https://minotar.net/helm/%s/100.png", Bukkit.getPlayer(playerData.getUuid()).getName()))
                            .setFooter("https://firefly.rip | " + time, "https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                webhook.execute();
                            } catch (IOException ignored) {
                            }
                        }
                    }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
                }

            }
        }

        BridgeData.totalFlags++;
        if(playerData.getViolatons(this) == threshold && autobans) {
            punish(Bukkit.getPlayer(playerData.getUuid()));
        }
    }

    /**
     * The method to flag a player for failing a check
     * @param data The data used to conclude that the player may be cheating
     */
    protected void flag(PlayerData playerData, String[] data) {
        if(!isEnabled()) {
            return;
        }

        final FlagEvent flagevent = new FlagEvent(Bukkit.getPlayer(playerData.getUuid()), getCheckName(), playerData.getViolatons(this));
        Bukkit.getPluginManager().callEvent(flagevent);
        if(flagevent.isCancelled()) return;

        playerData.addViolation(this);

        playerData.setLastFlag(System.currentTimeMillis());
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData alertPlayerData = DataManager.getData(p);

            if (alertPlayerData.isAlerts()) {
                //String graph = GraphUtil.drawGraph(20, (int) (((double)playerData.getViolatons(this) / (double)threshold) * 10)*2);
                String graph = GraphUtil.drawGraph(ConfigManager.messages.getConfiguration().getInt("graph.maxLength"),  (((double)playerData.getViolatons(this) / (double)threshold) * 10)*(ConfigManager.messages.getConfiguration().getInt("graph.maxLength")/10));

                final TextComponent mainComponent = new TextComponent(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("alert"))
                        .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                        .replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())
                        .replace("$check", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("check.format").replace("$name", checkName).replace("$verusid", verusId).replace("$subtype", subType)))
                        .replace("$vl", Integer.toString(playerData.getViolatons(this)))
                        .replace("$ping", Long.toString(playerData.getPing()))
                        .replace("$graph", graph)
                        .replace("$maxvl", Integer.toString(threshold))
                        .replace("$tps", Double.toString(round(LagUtil.getTPS(), 2)))
                );

                StringBuilder s = new StringBuilder();
                for(String str : Arrays.stream(data).map(Object::toString).toArray(String[]::new)) {
                    s.append(ChatColor.YELLOW).append(str.split(":")[0]).append(":").append(ChatColor.GOLD).append(str.split(":")[1]).append("\n");
                }

                StringBuilder hoverComponent = new StringBuilder();

                for (String str : ConfigManager.messages.getConfiguration().getStringList("check.hover")) {
                    hoverComponent.append(str.replace("$description", getDescription()).replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName()).replace("$data", s)).append("\n");
                }


                hoverComponent.setLength(hoverComponent.length() - 1);

                mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(hoverComponent.toString())).create()));
                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ConfigManager.messages.getConfiguration().getString("check.command").replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())));

                p.spigot().sendMessage( mainComponent );

                if(ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
                    DiscordWebhook webhook = new DiscordWebhook(ConfigManager.settings.getConfiguration().getString("webhook.url"));
                    webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png");
                    webhook.setUsername("FireFly");
                    String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setTitle("FireFly > Alert")
                            .setDescription(
                                    "```" + Bukkit.getPlayer(playerData.getUuid()).getName() + " failed " + checkName + " (" + subType + ") (" + playerData.getViolatons(this) + "/" + threshold + ")```\\n" +
                                            "```fix\\n" +
                                            "Description: " + description + "\\n" +
                                            "Ping: " + playerData.getPing() + "\\n" +
                                            "Player's Version: " + playerData.getPlayerVersion().name() + "\\n" +
                                            "TPS: " + LagUtil.getTPS() + "\\n" +
                                            "Location: " + Bukkit.getPlayer(playerData.getUuid()).getWorld().getName() + " | X: " + round(playerData.getTo().getX(), 1) + " Y: " + round(playerData.getTo().getY(), 1) + " Z: " + round(playerData.getTo().getX(), 1) + "```")
                            .setColor(new Color(0xf4a45a))
                            .setThumbnail(String.format("https://minotar.net/helm/%s/100.png", Bukkit.getPlayer(playerData.getUuid()).getName()))
                            .setFooter("https://firefly.rip | " + time, "https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                webhook.execute();
                            } catch (IOException ignored) {
                            }
                        }
                    }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
                }

            }
        }

        BridgeData.totalFlags++;
        if(playerData.getViolatons(this) == threshold && autobans) {
            punish(Bukkit.getPlayer(playerData.getUuid()));
        }
    }

    /**
     * The method to flag a player for failing a check
     */
    protected void flag(PlayerData playerData) {
        if(!isEnabled()) {
            return;
        }

        final FlagEvent flagevent = new FlagEvent(Bukkit.getPlayer(playerData.getUuid()), getCheckName(), playerData.getViolatons(this));
        Bukkit.getPluginManager().callEvent(flagevent);
        if(flagevent.isCancelled()) return;

        playerData.addViolation(this);

        playerData.setLastFlag(System.currentTimeMillis());
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerData alertPlayerData = DataManager.getData(p);

            if (alertPlayerData.isAlerts()) {
                //String graph = GraphUtil.drawGraph(20, (int) (((double)playerData.getViolatons(this) / (double)threshold) * 10)*2);
                String graph = GraphUtil.drawGraph(ConfigManager.messages.getConfiguration().getInt("graph.maxLength"),  (((double)playerData.getViolatons(this) / (double)threshold) * 10)*(ConfigManager.messages.getConfiguration().getInt("graph.maxLength")/10));

                final TextComponent mainComponent = new TextComponent(ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("alert"))
                        .replace("$prefix", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("prefix")))
                        .replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())
                        .replace("$check", ColorUtil.translate(ConfigManager.messages.getConfiguration().getString("check.format").replace("$name", checkName).replace("$verusid", verusId).replace("$subtype", subType)))
                        .replace("$vl", Integer.toString(playerData.getViolatons(this)))
                        .replace("$ping", Long.toString(playerData.getPing()))
                        .replace("$graph", graph)
                        .replace("$maxvl", Integer.toString(threshold))
                        .replace("$tps", Double.toString(round(LagUtil.getTPS(), 2)))
                );

                StringBuilder s = new StringBuilder();

//                final String hoverComponent = ColorUtil.translate(
//                        ColorUtil.translate(
//                                "&7&m-------------------------------------------\n" +
//                                        "&6Description: \n&e" + getDescription() + "\n\n" +
//                                        s +
//                                        "\n&eClick To Teleport\n" +
//                                        "\n&7&m-------------------------------------------"
//                        )
//                );

                StringBuilder hoverComponent = new StringBuilder();

                for (String str : ConfigManager.messages.getConfiguration().getStringList("check.hover")) {
                    hoverComponent.append(str.replace("$description", getDescription()).replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName()).replace("$data", s)).append("\n");
                }


                hoverComponent.setLength(hoverComponent.length() - 1);

                mainComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ColorUtil.translate(hoverComponent.toString())).create()));
                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + ConfigManager.messages.getConfiguration().getString("check.command").replace("$player", Bukkit.getPlayer(playerData.getUuid()).getName())));

                p.spigot().sendMessage( mainComponent );

                if(ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
                    DiscordWebhook webhook = new DiscordWebhook(ConfigManager.settings.getConfiguration().getString("webhook.url"));
                    webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png");
                    webhook.setUsername("FireFly");
                    String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
                    webhook.addEmbed(new DiscordWebhook.EmbedObject()
                            .setTitle("FireFly > Alert")
                            .setDescription(
                                    "```" + Bukkit.getPlayer(playerData.getUuid()).getName() + " failed " + checkName + " (" + subType + ") (" + playerData.getViolatons(this) + "/" + threshold + ")```\\n" +
                                            "```fix\\n" +
                                            "Description: " + description + "\\n" +
                                            "Ping: " + playerData.getPing() + "\\n" +
                                            "Player's Version: " + playerData.getPlayerVersion().name() + "\\n" +
                                            "TPS: " + LagUtil.getTPS() + "\\n" +
                                            "Location: " + Bukkit.getPlayer(playerData.getUuid()).getWorld().getName() + " | X: " + round(playerData.getTo().getX(), 1) + " Y: " + round(playerData.getTo().getY(), 1) + " Z: " + round(playerData.getTo().getX(), 1) + "```")
                            .setColor(new Color(0xf4a45a))
                            .setThumbnail(String.format("https://minotar.net/helm/%s/100.png", Bukkit.getPlayer(playerData.getUuid()).getName()))
                            .setFooter("https://firefly.rip | " + time, "https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png"));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            try {
                                webhook.execute();
                            } catch (IOException ignored) {
                            }
                        }
                    }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
                }

            }
        }

        BridgeData.totalFlags++;
        if(playerData.getViolatons(this) >= threshold && autobans) {
            punish(Bukkit.getPlayer(playerData.getUuid()));
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * The method to punish a player
     */
    protected void punish(Player p) {


        if(ConfigManager.settings.getConfiguration().getBoolean("ban.enabled")) {
            final PunishEvent event = new PunishEvent(Bukkit.getPlayer(playerData.getUuid()), getCheckName(), playerData.getViolatons(this));
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) return;

            if (ConfigManager.settings.getConfiguration().getBoolean("ban.lightningEffect")) {
                p.getWorld().strikeLightning(p.getLocation());
            }

            Bukkit.getScheduler().runTask(FireFly.getInstance().getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ConfigManager.settings.getConfiguration().getString("ban.command").replace("$player", p.getName())));

            //   if(!playerData.isSentBanMessage()) {
            Bukkit.getScheduler().callSyncMethod(FireFly.getInstance().getPlugin(), () -> {
                for (String string : ConfigManager.messages.getConfiguration().getStringList("banBroadcast")) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.sendMessage(ChatColor.translateAlternateColorCodes('&', (string.replace("$player", p.getName()))).replace(":X:", "✘"));
                        //                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', string.replace("$player", p.getName())));
                    }
                }
                return new Object();
            });
//                    for (String string : ConfigManager.messages.getConfiguration().getStringList("banBroadcast")) {
//                        for(Player pl : Bukkit.getOnlinePlayers()) {
//                            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', (string.replace("$player", p.getName()))).replace(":X:", "✘"));
//                            FireFly.getInstance().getPlugin().getLogger().info(p.getName() + " Was Removed For Failing " + checkName + " (" + subType +") " + playerData.getViolatons(this) + " Times.");
//                            //                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', string.replace("$player", p.getName())));
//                        }
//                    }
            //playerData.setSentBanMessage(true);
            // }

            if (ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
                DiscordWebhook webhook = new DiscordWebhook(ConfigManager.settings.getConfiguration().getString("webhook.url"));
                webhook.setAvatarUrl("https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png");
                webhook.setUsername("FireFly");
                String time = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date());
                webhook.addEmbed(new DiscordWebhook.EmbedObject()
                        .setTitle("FireFly > Punishment")
                        .setDescription(
                                "```" + Bukkit.getPlayer(playerData.getUuid()).getName() + " failed " + checkName + " (" + subType + ") (" + playerData.getViolatons(this) + "/" + threshold + ")```\\n" +
                                        "```fix\\n" +
                                        "Description: " + description + "\\n" +
                                        "Ping: " + playerData.getPing() + "\\n" +
                                        "TPS: " + LagUtil.getTPS() + "\\n" +
                                        "Player's Version: " + playerData.getPlayerVersion().name() + "\\n```")
                        .setColor(new Color(0xff6e69))
                        .setThumbnail(String.format("https://minotar.net/helm/%s/100.png", Bukkit.getPlayer(playerData.getUuid()).getName()))
                        .setFooter("https://firefly.rip | " + time, "https://cdn.discordapp.com/attachments/1014166710691041352/1016814271209943070/ff_logo.png"));
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        try {
                            webhook.execute();
                        } catch (IOException ignored) {
                        }
                    }
                }.runTaskAsynchronously(FireFly.getInstance().getPlugin());
            }

            playerData.resetVl();
            BridgeData.totalBans++;
        }
    }

    protected double increaseBuffer(double amount) {
        return buffer = Math.min(buffer + amount, 20);
    }

    protected double incrementBuffer() {
        return increaseBuffer(1.0);
    }

    protected double decreaseBuffer(double amount) {
        return buffer = Math.max(buffer - amount, 0);
    }

    protected double decrementBuffer() {
        return decreaseBuffer(1.0);
    }

    protected void resetBuffer() {
        buffer = 0.0;
    }

    protected void setBuffer(double buffer) {this.buffer = buffer; }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAutobans(boolean autobans) {
        this.autobans = autobans;
    }

    public boolean isEnabled() {
        return enabled;
    }
}