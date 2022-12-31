package rip.firefly.check.impl.misc.invalid;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "Invalid", subType = "E", description = "Checks If A Player Is Sneaking And Sprinting", type = CheckType.MISC, threshold = 1)
public class InvalidE extends MovementCheck {

    public InvalidE(PlayerData data) {
        super(data);
    }

    private boolean sent;
    public long lastFlying, lastPacket;

    // Totally doesnt desync bro
    @Override
    public void handle(PlayerData playerData, MovementUpdate update) {
        if(Bukkit.getPlayer(playerData.getUuid()).isSprinting() && Bukkit.getPlayer(playerData.getUuid()).isSneaking()) flag(playerData, new String[]{String.format("SN: %s", Bukkit.getPlayer(playerData.getUuid()).isSneaking()), String.format("SP: %s", Bukkit.getPlayer(playerData.getUuid()).isSprinting())});
    }
}
