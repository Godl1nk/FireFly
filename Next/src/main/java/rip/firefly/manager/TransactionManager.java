package rip.firefly.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import rip.firefly.FireFly;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.TinyProtocolHandler;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInTransactionPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutPositionPacket;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutRelativePosition;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutTransaction;
import rip.firefly.util.location.PlayerLocation;

import java.util.HashMap;

public class TransactionManager {
    
    private PlayerData user;
    
    public HashMap<Short, ReachData> reachTestMap = new HashMap();

    private ReachData reachData;

    public TransactionManager(PlayerData data) {
        this.user = data;
    }

    
    public void handle(PlayerData data, NMSObject fpacket) {
        this.user = data;
        if(fpacket instanceof WrappedInTransactionPacket) {
            user.setTransactionPing(System.currentTimeMillis() - user.getLastSentTransaction());
            WrappedInTransactionPacket transactionPacket = (WrappedInTransactionPacket)fpacket;
            short action = transactionPacket.getAction();

            if (user.getTransactionManager().reachTestMap.containsKey(action)) {

               // user.getTransactionManager().reachData = user.getTransactionManager().reachTestMap.get(action);

                user.getTransactionManager().reachData = user.getTransactionManager().reachTestMap.get(action);
                user.getTransactionManager().reachTestMap.remove(action);
            }
        } else if(fpacket instanceof WrappedOutRelativePosition) {
            WrappedOutRelativePosition relativePosition =
                    (WrappedOutRelativePosition)fpacket;


            double x = relativePosition.getX() / 32D;
            double y = relativePosition.getY() / 32D;
            double z = relativePosition.getZ() / 32D;

            float f = relativePosition.isLook() ? (float) (relativePosition.getYaw() * 360) / 256.0F :
                    relativePosition.getPlayer().getLocation().getYaw();

            float f1 = relativePosition.isLook() ? (float) (relativePosition.getPitch() * 360) / 256.0F :
                    relativePosition.getPlayer().getLocation().getPitch();

            queueTransaction(new ReachData(user, System.currentTimeMillis(),
                    new PlayerLocation(x,y,z, f, f1, System.currentTimeMillis())));
        } else if(fpacket instanceof WrappedOutPositionPacket) {
            WrappedOutPositionPacket wrappedOutEntityTeleport = (WrappedOutPositionPacket)fpacket;
            double x = wrappedOutEntityTeleport.getX() / 32.0D;
            double y = wrappedOutEntityTeleport.getY() / 32.0D;
            double z = wrappedOutEntityTeleport.getZ() / 32.0D;

            float f = (float) (wrappedOutEntityTeleport.getYaw() * 360) / 256.0F;
            float f1 = (float) (wrappedOutEntityTeleport.getPitch() * 360) / 256.0F;

            queueTransaction(new ReachData(user, System.currentTimeMillis(),
                    new PlayerLocation(x,y,z, f, f1, System.currentTimeMillis())));
        }
    }

    private void queueTransaction(ReachData reachData) {
        short random = (short) (FireFly.getInstance().getTransactionTask().getTime() - 3);

        user.getTransactionManager().reachTestMap.put(random, reachData);

        TinyProtocolHandler.sendPacket(Bukkit.getPlayer(user.getUuid()),
                new WrappedOutTransaction(0, random, false).getObject());
        user.setLastSentTransaction(System.currentTimeMillis());

    }


    public ReachData getReachData() {
        return reachData;
    }
    @Getter
    @AllArgsConstructor
    public static class ReachData {
        private final PlayerData user;
        private final long time;
        private final PlayerLocation customLocation;
    }

    @Getter
    @Setter
    public static class TransactionTask implements Runnable {
        public TransactionTask() {
            this.start();
        }

        private short time = 32000;
        private BukkitTask bukkitTask;

        public void start() {
            if (this.bukkitTask == null) {
                this.bukkitTask = Bukkit.getScheduler().runTaskTimer(FireFly.getInstance().getPlugin(), this, 1L, 1L);
//                this.bukkitTask = taskTimerAsync(
//                        this, 0L, 0L);
            }
        }
        public void stop() {
            if (this.bukkitTask != null) {
                this.bukkitTask.cancel();
            }
        }

        @Override
        public void run() {

            if (this.time-- < 1) {
                this.time = 32000;
            }

//            if (ProtocolVersion.getGameVersion().isAbove(ProtocolVersion.V1_9_4)) {
//                //Fix for 1.9+ servers, because keepalives are broken for some reason...?
//                this.processTransaction();
//            } else {
                this.processTransaction();
            //}
        }

        public static BukkitTask taskTimerAsync(Runnable runnable, Plugin plugin, long delay, long interval) {
            return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay, interval);
        }

        public static BukkitTask taskTimerAsync(Runnable runnable, long delay, long interval) {
            return taskTimerAsync(runnable, FireFly.getInstance().getPlugin(), delay, interval);
        }

   /* void processKeepAlive() {
        WrappedOutKeepAlivePacket wrappedOutKeepAlivePacket = new WrappedOutKeepAlivePacket(this.time);
        Anticheat.getInstance().getUserManager().getUserMap().forEach((uuid, user) -> {
            user.getConnectionMap2().put(this.time, System.currentTimeMillis());
            user.sendPacket(wrappedOutKeepAlivePacket.getObject());
        });
    } */

        void processTransaction() {
            WrappedOutTransaction wrappedOutTransaction = new WrappedOutTransaction(0, this.time,
                    false);
            DataManager.getDataMap().forEach((uuid, user) -> {
                if(Bukkit.getPlayer(user.getUuid()) != null && Bukkit.getPlayer(user.getUuid()).isOnline()) {
                    user.getConnectionMap().put(this.time, System.currentTimeMillis());
                    user.sendPacket(wrappedOutTransaction.getObject());
                    user.setLastSentTransaction(System.currentTimeMillis());
                }
            });
        }
    }

}

