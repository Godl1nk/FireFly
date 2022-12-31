package rip.firefly.util.update;

import lombok.Data;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;

@Data
public class MovementUpdate {

    private PlayerData playerData;
    private PlayerLocation to;
    private PlayerLocation from;
    private boolean positionUpdate;
    private boolean onGround;

    /**
     * The constructor to create a MovementUpdate
     * @param playerData The player that the MovementUpdate is for
     * @param to The location the player is going to in the form of {@link PlayerLocation}
     * @param from The location the player was at in the form of {@link PlayerLocation}
     * @param positionUpdate If the MovementUpdate is a position update
     * @param onGround If the player is on the ground (according to the flying packet)
     */
    public MovementUpdate(PlayerData playerData, PlayerLocation to, PlayerLocation from, boolean positionUpdate, boolean onGround) {
        this.playerData = playerData;
        this.to = to;
        this.from = from;
        this.positionUpdate = positionUpdate;
        this.onGround = onGround;
    }

    public boolean isRotation() {
        if(!positionUpdate) {
            return true;
        } else {
            return false;
        }
    }
}
