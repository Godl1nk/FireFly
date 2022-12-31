package rip.firefly.util.player;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import rip.firefly.FireFly;
import rip.firefly.packet.TinyProtocolHandler;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutVelocityPacket;

import java.util.Random;

public class CrashUtil {
    public void crashPlayer(Player target) {
        target.playSound(target.getLocation(), Sound.GHAST_SCREAM, 10f, 1f);
        target.playSound(target.getLocation(), Sound.GHAST_SCREAM2, 10f, 1f);
        target.playSound(target.getLocation(), Sound.GHAST_MOAN, 10f, 1f);
        target.playSound(target.getLocation(), Sound.GHAST_DEATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.BAT_DEATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.ANVIL_BREAK, 10f, 1f);
        target.playSound(target.getLocation(), Sound.BLAZE_DEATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.CAT_HISS, 10f, 1f);
        target.playSound(target.getLocation(), Sound.CREEPER_DEATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.CREEPER_HISS, 10f, 1f);
        target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 10f, 1f);
        target.playSound(target.getLocation(), Sound.ENDERDRAGON_DEATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.EXPLODE, 10f, 1f);
        target.playSound(target.getLocation(), Sound.ENDERMAN_SCREAM, 10f, 1f);
        target.playSound(target.getLocation(), Sound.SILVERFISH_KILL, 10f, 1f);
        target.playSound(target.getLocation(), Sound.BLAZE_BREATH, 10f, 1f);
        target.playSound(target.getLocation(), Sound.WOLF_GROWL, 10f, 1f);
        target.playSound(target.getLocation(), Sound.WOLF_BARK, 10f, 1f);
        target.playSound(target.getLocation(), Sound.WOLF_HURT, 10f, 1f);
        Random r = new Random();

        new BukkitRunnable() {


            @Override
            public void run() {
                TinyProtocolHandler.sendPacket(target, new WrappedOutVelocityPacket(target.getEntityId(), Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE).getObject());
/*                Random random = new Random(System.currentTimeMillis());
                for (int i = 0; i < 10; i++) {
                    target.sendBlockChange(target.getLocation(), random.nextInt(2674) - 1337, (byte) 0);
                }

                target.sendBlockChange(target.getLocation(), Integer.MIN_VALUE, (byte) 0);
                target.sendBlockChange(target.getLocation(), Integer.MAX_VALUE, (byte) 0);*/
            }
        }.runTaskLaterAsynchronously(FireFly.getInstance().getPlugin(), 2000);

        new BukkitRunnable() {


            @Override
            public void run() {
                if(target.isOnline()) {
                    Random random = new Random(System.currentTimeMillis());
                    for (int i = 0; i < 10; i++) {
                        target.sendBlockChange(target.getLocation(), random.nextInt(2674) - 1337, (byte) 0);
                    }

                    target.sendBlockChange(target.getLocation(), Integer.MIN_VALUE, (byte) 0);
                    target.sendBlockChange(target.getLocation(), Integer.MAX_VALUE, (byte) 0);
                }
            }
        }.runTaskLaterAsynchronously(FireFly.getInstance().getPlugin(), 3000);

    }
}
