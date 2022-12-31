package rip.firefly.check.impl.combat.noslow;

import org.bukkit.Bukkit;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.MovementCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.update.MovementUpdate;

@CheckData(name = "NoSlow", subType = "A", threshold = 10, type = CheckType.COMBAT, description = "Checks If A Player Is Sprinting While Blocking With A Sword")
public class NoSlowA extends MovementCheck {
    public NoSlowA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, MovementUpdate movementUpdate) {
        if(Bukkit.getPlayer(playerData.getUuid()).isInsideVehicle()) return;

        if(Bukkit.getPlayer(playerData.getUuid()).isSprinting() && Bukkit.getPlayer(playerData.getUuid()).isBlocking()) {
            flag(playerData, String.format("SP: %s", Bukkit.getPlayer(playerData.getUuid()).isSprinting()), String.format("BK: %s", Bukkit.getPlayer(playerData.getUuid()).isBlocking()));
        }
    }
}
