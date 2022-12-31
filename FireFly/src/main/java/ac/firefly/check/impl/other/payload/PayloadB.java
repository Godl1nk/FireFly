package ac.firefly.check.impl.other.payload;

import ac.firefly.managers.ConfigManager;
import ac.firefly.managers.PunishManager;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class PayloadB extends Check implements PluginMessageListener {

    private static final ImmutableMap map = new ImmutableMap.Builder().put("LOLIMAHCKER", "Cracked Vape").put("CPS_BAN_THIS_NIGGER", "Cracked Vape").put("EROUAXWASHERE", "Cracked Vape").put("EARWAXWASHERE", "Cracked Vape").put("#unbanearwax", "Cracked Vape").put("1946203560", "Vape v3").put("cock", "Reach Mod").put("lmaohax", "Reach Mod").put("reach", "Reach Mod").put("gg", "Reach Mod").put("customGuiOpenBspkrs", "Bspkrs Client").put("0SO1Lk2KASxzsd", "Bspkrs Client").put("MCnetHandler", "Misplace").put("n", "Misplace").put("CRYSTAL|KZ1LM9TO", "CrystalWare").put("CRYSTAL|6LAKS0TRIES", "CrystalWare").put("BLC|M", "Remix").build();

    public PayloadB() {
        super("Payload (B)", CheckType.MISC, true);
    }

    FileConfiguration msg = ConfigManager.messages.getConfiguration();
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {

        String string = channel;
        String string2 = (String)map.get(string);
        if (string2 != null) {
            flag(player, string2, string2);
            PunishManager.punishPlayer(player);
        } else if (string.startsWith("CRYSTAL|")) {
            flag(player, "CrystalWare", "CrystalWare");
            PunishManager.punishPlayer(player);
        }
    }
}
