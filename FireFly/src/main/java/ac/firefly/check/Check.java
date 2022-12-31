package ac.firefly.check;

import ac.firefly.api.event.PostBanEvent;
import ac.firefly.managers.PunishManager;
import ac.firefly.Firefly;
import ac.firefly.util.formatting.Color;
import ac.firefly.managers.ConfigManager;
import ac.firefly.util.interaction.Lag;
import ac.firefly.util.interaction.ServerUtils;
import ac.firefly.managers.PluginManager;
import ac.firefly.api.event.FlagEvent;
import ac.firefly.util.webhook.WebhookUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.WeakHashMap;

public class Check implements Listener {
    public Map<Player, Integer> violations = new WeakHashMap<>();

    private String name;
    private CheckType type;
    private boolean enabled;
    private static int totalVl;

    String bukkitversion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

    private Player p;

    public Check(String name, CheckType type, boolean enabled) {
        this.name = name;
        this.type = type;
        this.enabled = enabled;

        if (enabled) Bukkit.getPluginManager().registerEvents(this, Firefly.instance);
    }

    public static int getTotalVL() {
        return totalVl;
    }

    protected void flag(Player p, String data, String debugData) {
        PluginManager.instance.getDataManager().addViolation(p, this);
        Integer vl = PluginManager.instance.getDataManager().getViolatonsPlayer(p, this);
        String checkOut;

        totalVl++;

        if(p.hasPermission("firefly.bypass")) {
            return;
        }
        final FlagEvent flagevent = new FlagEvent(p, getName(), data, debugData, vl);
        Bukkit.getPluginManager().callEvent(flagevent);


        if (ConfigManager.settings.getConfiguration().getBoolean("severitycolor")) {
            if (vl <= 5) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity1")) + getName();
            } else if (vl <= 10) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity2")) + getName();
            } else if (vl >= 15) {
                checkOut = Color.translate(ConfigManager.messages.getConfiguration().getString("severity3")) + getName();
            } else {
                checkOut = getName();
            }
        } else {
            checkOut = getName();
        }

        if (ConfigManager.settings.getConfiguration().getBoolean("webhook.enabled")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    WebhookUtil.sendMessage(ConfigManager.messages.getConfiguration().getString("webhook.alert")
                            .replace("$player", p.getName())
                            .replace("$check", getName())
                            .replace("$data", data != null ? data : "")
                            .replace("$vl", "" + vl)
                            .replace("$ping", String.valueOf(Lag.getPing(p)))
                            .replace("\\n", "\n"));
                }
            }.runTaskAsynchronously(Firefly.instance);

        }

        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
            FileConfiguration msg = ConfigManager.messages.getConfiguration();
            if (staff.hasPermission("firefly.alerts") || staff.hasPermission("firefly.admin")) {
                if (PluginManager.alertsToggled.containsKey(staff)) {
                    staff.sendMessage(Color.translate(msg.getString("alert"))
                            .replace("$prefix", Color.translate(msg.getString("prefix")))
                            .replace("$player", p.getName())
                            .replace("$check", checkOut)
                            .replace("$data", data != null ? data : "")
                            .replace("$vl", "" + vl)
                            .replace("$ping", String.valueOf(Lag.getPing(p)))
                            .replace("\\n", "\n"));
                }
            }
        }
        ServerUtils.logDebug(null, p.getName() + " flagged " + this.name + " (" + debugData != null ? debugData : "NO DATA" + ") [x" + vl + "]");

        if (vl >= ConfigManager.settings.getConfiguration().getInt("ban.banvl")) {
            if (PunishManager.banWave.containsKey(p)) {
                return;
            } else {
                PunishManager.punishPlayer(p);
                final PostBanEvent postBanEvent = new PostBanEvent(p, getName());
                Bukkit.getPluginManager().callEvent(postBanEvent);
            }
            ServerUtils.logDebug(null, p.getName() + " has been banned. Check: " + this.name + " (" + data + ") [x" + vl + "]");
        }
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        if (this.enabled) {
            Bukkit.getPluginManager().registerEvents(this, Firefly.instance);
        } else {
            HandlerList.unregisterAll(this);
        }
    }

    public CheckType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    protected void onEvent(Event event) {
        // TODO Auto-generated method stub

    }
}
