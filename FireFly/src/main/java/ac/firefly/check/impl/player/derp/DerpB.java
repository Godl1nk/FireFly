package ac.firefly.check.impl.player.derp;

import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

public class DerpB extends Check {
    public DerpB() {
        super("Derp (B)", CheckType.PLAYER, true);
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event){
        double lastPitch = event.getFrom().getPitch();
        double currentPitch = event.getTo().getPitch();
        double diff = currentPitch - lastPitch;
        //Change these values idk Im checking for quick movement in pitch which is unlike normal minecraft.
        //So basically minecraft sends a packet for every pitch change while hacked clients just send a different pitch.
        //So this can easily flag many blatant hacked clients.
        if(diff <= -170 || diff >= 170){
            flag(event.getPlayer(), "Derp/Head Roll", "Last Pitch: " + lastPitch + ", Current Pitch: " + currentPitch + ", Diff: " + diff);
        }

        //SethPython: Firstly as soon as somebody joins usually the yaw and pitch is reset by the server and this can cause an unncecessary flag. Secondly sometimes when joining a minigame or something for the first time the yaw and pitch is usually set to 0 so a huge change in pitch/yaw might be bad.
    }
}
