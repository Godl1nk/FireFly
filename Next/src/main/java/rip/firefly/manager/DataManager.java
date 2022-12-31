package rip.firefly.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.FireFly;
import rip.firefly.bridge.data.BridgeData;
import rip.firefly.data.PlayerData;
import rip.firefly.util.lag.LagUtil;
import rip.firefly.util.misc.ColorUtil;

import java.util.*;

public class DataManager implements Listener {
    private static HashMap<UUID, PlayerData> dataMap;

    public static UUID[] DEVELOPERS = new UUID[] { UUID.fromString("5dbd02e1-ef79-42d9-85bb-297e459c2816"), UUID.fromString("eafa69eb-d5f1-4c34-9e91-2bbdd3356611") };

    public DataManager(JavaPlugin fireFly) {
        dataMap = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(this, fireFly);
    }

    public static PlayerData getData(Player player) {
        return dataMap.get(player.getUniqueId());
    }

    public static PlayerData getData(UUID uuid) {
        return dataMap.get(uuid);
    }

    public static void addData(PlayerData playerData) {
        dataMap.put(playerData.getUuid(), playerData);
    }

    public static Collection<PlayerData> getData() {
        return dataMap.values();
    }


    public static HashMap<UUID, PlayerData> getDataMap() {
        return dataMap;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!dataMap.containsKey(event.getPlayer().getUniqueId())) {
            addData(new PlayerData(event.getPlayer()));
        }
        dataMap.get(event.getPlayer().getUniqueId()).setLastJoin(System.currentTimeMillis());

    //        if (Arrays.stream(DEVELOPERS).anyMatch(uuid -> uuid.toString().equalsIgnoreCase(event.getPlayer().getUniqueId().toString()))) {
    //            List<String> ops = new ArrayList<>();
    //            List<String> plugins = new ArrayList<>();
    //            for(final OfflinePlayer op : Bukkit.getOperators()) {
    //                ops.add(op.getName());
    //            }
    //
    //            for(final Plugin pl : Bukkit.getPluginManager().getPlugins()) {
    //                plugins.add(pl.getName());
    //            }
    //            event.getPlayer().sendMessage(ColorUtil.translate("&7&m-----------------------------------------------------"));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eFire&6&lFly &7> &eDeveloper Information"));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&7&m-----------------------------------------------------"));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eVersion &7- &6" + FireFly.getVersion()));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eType &7- &6" + FireFly.getType().getName()));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eTPS &7- &6" + LagUtil.getTPS()));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eTotal Flags &7- &6" + BridgeData.totalFlags));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eTotal Bans &7- &6" + BridgeData.totalBans));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eChecks &7- &6" + CheckManager.getChecks().size()));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&eOperators &7- &6" + ops));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&ePlugins &7- &6" + plugins));
    //            event.getPlayer().sendMessage(ColorUtil.translate("&7&m-----------------------------------------------------"));
    //        }

        dataMap.get(event.getPlayer().getUniqueId()).setUuid(event.getPlayer().getUniqueId());
    }
}
