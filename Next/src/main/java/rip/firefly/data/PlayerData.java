package rip.firefly.data;

import com.google.common.collect.ClassToInstanceMap;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import rip.firefly.FireFly;
import rip.firefly.check.AbstractCheck;
import rip.firefly.compat.wrapper.player.WrappedPlayer;
import rip.firefly.manager.CheckManager;
import rip.firefly.manager.TransactionManager;
import rip.firefly.manager.VelocityManager;
import rip.firefly.packet.TinyProtocolHandler;
import rip.firefly.packet.tinyprotocol.api.ProtocolVersion;
import rip.firefly.packet.tinyprotocol.packet.out.WrappedOutTransaction;
import rip.firefly.util.lag.LagUtil;
import rip.firefly.util.location.PastLocation;
import rip.firefly.util.location.PlayerLocation;
import rip.firefly.util.location.PlayerPosition;
import rip.firefly.util.misc.*;

import java.util.*;

@Data
public class PlayerData {

    @Setter(AccessLevel.NONE)
    private final CheckManager checkManager;

    @Setter(AccessLevel.NONE)
    private final VelocityManager velocityManager;


    @Setter(AccessLevel.NONE)
    private final TransactionManager transactionManager;

    public ClassToInstanceMap<AbstractCheck> checks;
    public int hitTicks, ticks, bhopDelay, teleportTicks, altTeleportTicks;
  //  private Player player;
    public int currentTick;
    private boolean verifyingSensitivity, didTakeVelocity, touchingAir, velocity, attacking, belowBlocks;
    private double sensitivity, velocityX, velocityY, velocityZ;
    private boolean alerts, exempt, velUpdate, onGround, digging, instantBreakDigging, teleporting;
    private WrappedOutTransaction lastOutTransaction;
    private EvictingList<PlayerPosition> locations = new EvictingList<>(10);
    private PlayerPosition playerPosition;
    private UUID uuid;
    private List<Double> graph = new ArrayList<>();
    private final TickTimer lagTick = new TickTimer();
    private long lastVelMS, lastJoin, ping, lastServerKeepAlive, lastClientKeepAlive, velUpdateTime, lastAttack, lastPlace, lastSwing, lastFlying, lastDelayedFlyingPacket, lastDelta, transactionPing, lastRecivedTransaction, lastSentTransaction;
    private LivingEntity lastHitEntity;
    private float lastYaw;
    private double distance, hFreedom;
    public Entity target;
    public Entity lastTarget;
    private float lastPitch;
    private final ConcurrentEvictingList<Pair<Location, Integer>> targetLocations = new ConcurrentEvictingList<>(40);
    private float lastTeleport;
    private int x, y, z, airTicks, groundTicks, gsTicks, cticks, lastTransactionId, transactionId;
    private PastLocation entityPastLocations = new PastLocation();
    private Map<AbstractCheck, Integer> violations;
    private int vl;
    public ProtocolVersion playerVersion;

    private final Map<Short, Long> connectionMap = new EvictingMap<>(100);
    private PlayerLocation to, from;
    public Movement movement;
    private final List<Vector> teleports = new ArrayList<>();
    private boolean cinematic, sentBanMessage;
    private double lastFlag;
    private int flagStreak;
    private PlayerLocation lagback;
    public boolean playerOnGround, onLadder, inWater, inLava,onIce, underBlock, onPiston, inWeb;

    /**
     * The constructor to create PlayerData for
     * @param player The player to create PlayerData for
     */
    public PlayerData(Player player) {
        this.movement = new Movement();
        //this.player = player;
        this.alerts = false;
        this.to = new PlayerLocation();
        this.from = new PlayerLocation();
        this.uuid = player.getUniqueId();
        this.violations = new HashMap<>();
        this.lastJoin = System.currentTimeMillis();
        this.playerVersion = ProtocolVersion.getVersion(new WrappedPlayer(player).getProtocolVersion());
        this.checkManager = new CheckManager(this);
        this.velocityManager = new VelocityManager();
        this.transactionManager = new TransactionManager(this);
    }


    public int getViolatons(AbstractCheck abstractCheck) {
        return violations.getOrDefault(abstractCheck, 0);
    }

    public void addViolation(AbstractCheck abstractCheck) {
        violations.put(abstractCheck, violations.getOrDefault(abstractCheck, 0) + 1);
    }

    public void resetVl() {
        for (AbstractCheck abstractCheck : CheckManager.getChecks()) {
            violations.put(abstractCheck, 0);
        }
    }

    public boolean hasAttacked() {
        return System.currentTimeMillis() - this.getLastAttack() < 200L;
    }

    public boolean isLagging() {
        long now = System.currentTimeMillis();
        return now - this.getLastDelayedFlyingPacket() < 220L || this.getTeleportTicks() > 0;
    }


    public void sendPacket(Object packet) {
        if(Bukkit.getPlayer(this.getUuid()) != null && Bukkit.getPlayer(this.getUuid()).isOnline()) {
            TinyProtocolHandler.sendPacket(Bukkit.getPlayer(this.getUuid()), packet);
        }
    }

    public boolean shouldCancel() {
        return !Bukkit.getPlayer(this.getUuid()).getLocation().getChunk().isLoaded() || LagUtil.getTPS() <= 19.0 || Bukkit.getPlayer(this.getUuid()).getAllowFlight()
                || Bukkit.getPlayer(this.getUuid()).isFlying() || Bukkit.getPlayer(this.getUuid()).getGameMode() == GameMode.CREATIVE
                || Bukkit.getPlayer(this.getUuid()).getGameMode() == GameMode.SPECTATOR;
    }

    @Getter
    public class Movement {
        public double fx, fy, fz; // Last location
        public double tx, ty, tz; // Current location
        public float fyaw, fpitch, tyaw, tpitch;
        public double deltaH;
        public double deltaV;

        public double lastDeltaH;
        public double lastDeltaV;

        public boolean hasJumped, inAir;

        public float deltaYaw, deltaPitch;
        public float lastDeltaYaw, lastDeltaPitch;
        public float yawDifference, pitchDifference;


        public Vector tPos() {
            return new Vector(tx, ty, tz);
        }

        public Vector fPos() {
            return new Vector(fx, fy, fz);
        }

        public Vector tEyePos() {
            return new Vector(tx, ty + Bukkit.getPlayer(uuid).getEyeHeight(), tz);
        }

        public Vector fEyePos() {
            return new Vector(fx, fy + Bukkit.getPlayer(uuid).getEyeHeight(), fz);
        }

        public Vector tDir() {
            return vector(tyaw, tpitch);
        }

        public Vector fDir() {
            return vector(fyaw, fpitch);
        }

        public Vector vector(double yaw, double pitch) {
            Vector vector = new Vector();
            vector.setY(-Math.sin(Math.toRadians(pitch)));
            double xz = Math.cos(Math.toRadians(pitch));
            vector.setX(-xz * Math.sin(Math.toRadians(yaw)));
            vector.setZ(xz * Math.cos(Math.toRadians(yaw)));
            return vector;
        }
    }
}
