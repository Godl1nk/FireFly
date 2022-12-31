package rip.firefly.check.types;

import rip.firefly.check.AbstractCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.update.MovementUpdate;

public abstract class MovementCheck extends AbstractCheck<MovementUpdate> {

    public MovementCheck(PlayerData data) {
        super(data);
    }

  //  protected boolean falling = new WrappedPlayer(playerData.getPlayer()).isFalling();
//    protected PlayerLocation to = playerData.getTo();
//    protected PlayerLocation from = playerData.getFrom();
    //protected double deltaH = playerData.getMovement().deltaH;
    //protected double deltaV = playerData.getMovement().deltaV;
    //protected double lastDeltaH = playerData.getMovement().lastDeltaH;
    //protected double lastDeltaV = playerData.getMovement().lastDeltaV;


    /**
     * The method to handle checks via {@link MovementUpdate}
     * @param playerData The player to check
     * @param movementUpdate The {@link MovementUpdate} to pass to the check
     */
    public abstract void handle(PlayerData playerData, MovementUpdate movementUpdate);
}