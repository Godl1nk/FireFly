package ac.firefly.data;

import ac.firefly.Firefly;
import ac.firefly.events.PerdictionListener;
import ac.firefly.util.location.PastLocation;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import ac.firefly.client.ClientType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;

public class PlayerData {

    public long lastReachFlag;
    private long lastExplosion;
    public long ping;
    public boolean cinematic;
    public int reachThreshold;
    public LivingEntity lastHitEntity;
    public PastLocation entityPastLocations = new PastLocation();
    public long lastServerKP;
    public long pingD;
    public long lastSwing;
    public long lastAttack;
    public long noSwingADelay;
    public long noSwingBDelay;
    public boolean digging;
    public  long lastPlace;
    public boolean instantDigging;
    /**
     * Data fields
     **/
    private ClientType client = ClientType.UNKNOWN;
    private Player player;
    private boolean alerts = false;
    private double fallDistance = 0D;
    private int aboveBlockTicks = 0;
    private int waterTicks = 0;
    private long LastBlockPlacedTicks = 0;
    private boolean LastBlockPlaced_GroundSpoof = false;
    private int airTicks = 0;
    private int groundTicks = 0;
    private boolean ShouldSetBack = false;
    private int setBackTicks = 0;
    private long LastVelMS = 0;
    private boolean DidTakeVelocity = false;
    private long lastDelayedPacket;
    private long lastPlayerPacket;
    private Location setbackLocation;
    private double GoingUp_Blocks;
    private double LastY_Gravity;
    private int Gravity_VL;
    private int AntiCactus_VL;
    private double lastVelocityFlyY = 0;
    private double lastKillauraPitch = 0;
    private double lastKillauraYaw = 0;
    private long lastPacket = 0;
    private long lastAimTime = System.currentTimeMillis();
    private long Speed_Ticks = 0;
    private boolean Speed_TicksSet = false;
    private boolean isNearIce = false;
    private long isNearIceTicks = 0;
    private long LastVelUpdate = 0;
    private boolean LastVelUpdateBoolean = false;
    private double lastKillauraYawDif = 0;
    private long lastPacketTimer = 0;
    private long LastTimeTimer = 0;
    private int LastPACKETSTimer = 0;
    private long WebFloatMS = 0;
    private boolean WebFloatMS_Set = false;
    private int WebFloat_BlockCount = 0;
    private long AboveSpeedTicks = 0;
    private boolean AboveSpeedSet = false;
    private long HalfBlocks_MS = 0;
    private boolean HalfBlocks_MS_Set = false;
    private boolean Speed_C_2_Set = false;
    private long Speed_C_2_MS = 0;
    private long GlideTicks = 0;
    private long Speed_PistonExpand_MS = 0;
    private boolean Speed_PistonExpand_Set = false;
    private long BlockAbove = 0;
    private boolean BlockAbove_Set = false;
    private long Speed_YPORT_MS = 0;
    private boolean Speed_YPORT_Set = false;
    private int iceTicks = 0;
    private long Speed_YPort2_MS = 0;
    private boolean Speed_YPort2_Set = false;
    private long speedGroundReset = 0;
    private int slimeTicks = 0;
    private long Speed_MS_Yport = 0;
    private boolean Speed_MS_Set = false;
    private List<Double> averageScaffoldPitches;
    private float lastYaw;
    private float lastYawDiff;
    private long patternMS;
    private float lastYawOffset;
    private HashMap<Float, Integer> samples = new HashMap<Float, Integer>();

    private long lastPlacePacket = 0;
    //For CPS Lmao Python coded this.
    private long lastAttackPacket = 0;
    /**
     * Violation fields
     **/
    private int criticalsVerbose = 0;
    private int flyHoverVerbose = 0;
    private int patternVerbose = 0;
    private int flyVelocityVerbose = 0;
    private int GroundSpoofVL = 0;
    private int killauraAVerbose = 0;
    private int Speed2Verbose = 0;
    private int Speed_OnGround_Verbose = 0;
    private int TimerVerbose = 0;
    private int SpeedAC2_Verbose = 0;
    private int SpeedC_Verbose = 0;
    private int Speed_C_3_Verbose = 0;
    private int Speed_YPORT_Verbose = 0;
    private int Speed_YPort2_Verbose = 0;
    private int NEWSpeed_Verbose = 0;
    private int speedAVerbose = 0;
    private int Speed_C3_Verbose = 0;
    private int Jesus_Verbose = 0;
    private float lastRotationYaw = 0;
    private double sensitivityX = PerdictionListener.sensitivityX;
    private double sensitivityY = PerdictionListener.sensitivityY;

    private double mouseDeltaX = PerdictionListener.deltaX;
    private double mouseDeltaY = PerdictionListener.deltaY;

    private PacketContainer lastPacketClass = null; // SethPython

    public void setDigging(boolean digging) {
        this.digging = digging;
    }

    public boolean isDigging() {
        return digging;
    }

    public void setInstantBreakDigging(boolean instantDigging) {
        this.instantDigging = instantDigging;
    }

    public boolean isInstantDigging() {
        return instantDigging;
    }

    public void setLastAttack(long lastAttack) {
        this.lastAttack = lastAttack;
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public void setLastSwing(long lastSwing) {
        this.lastSwing = lastSwing;
    }

    public long getLastSwing() {
        return lastSwing;
    }

    public void setCinematic(boolean cinematic) {
        this.cinematic = cinematic;
    }

    public boolean isCinematic() {
        return cinematic;
    }

    public double getMouseDeltaX() {
        return mouseDeltaX;
    }

    public double getMouseDeltaY() {
        return mouseDeltaY;
    }

    public double getSensitivityX() {
        return sensitivityX;
    }

    public double getSensitivityY() {
        return sensitivityY;
    }

    public void setMouseDeltaX(double mouseDeltaX) {
        this.mouseDeltaX = mouseDeltaX;
    }

    public void setMouseDeltaY(double mouseDeltaY) {
        this.mouseDeltaY = mouseDeltaY;
    }

    public void setSensitivityX(double sensitivityX) {
        this.sensitivityX = sensitivityX;
    }

    public void setSensitivityY(double sensitivityY) {
        this.sensitivityY = sensitivityY;
    }

    public PlayerData(Player player) {
        this.player = player;
        new BukkitRunnable() {
            public void run() {
                if(lastHitEntity != null) {
                    entityPastLocations.addLocation(lastHitEntity.getLocation());
                }
            }
        }.runTaskTimer(Firefly.instance, 0L, 1L);
    }
    
    public ClientType getClient() {
        return client;
    }

    public void setClient(ClientType client) {
        this.client = client;
    }

    public int getJesus_Verbose() {
        return Jesus_Verbose;
    }

    public void setJesus_Verbose(int jesus_Verbose) {
        Jesus_Verbose = jesus_Verbose;
    }

    public int getSpeed_C3_Verbose() {
        return Speed_C3_Verbose;
    }

    public void setSpeed_C3_Verbose(int speed_C3_Verbose) {
        Speed_C3_Verbose = speed_C3_Verbose;
    }

    public int getNEWSpeed_Verbose() {
        return NEWSpeed_Verbose;
    }

    public void setNEWSpeed_Verbose(int NEWSpeed_Verbose) {
        this.NEWSpeed_Verbose = NEWSpeed_Verbose;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isAlerts() {
        return alerts;
    }

    public void setAlerts(boolean alerts) {
        this.alerts = alerts;
    }

    public double getFallDistance() {
        return fallDistance;
    }

    public List<Double> getAverageScaffoldPitches()
    {
        return averageScaffoldPitches;
    }

    public int getPatternVerbose()
    {
        return patternVerbose;
    }

    public void setPatternVerbose(int patternVerbose)
    {
        this.patternVerbose = patternVerbose;
    }

    public long getPatternMS()
    {
        return patternMS;
    }

    public void setPatternMS(long patternMS)
    {
        this.patternMS = patternMS;
    }

    public void resetPatternMS()
    {
        this.setPatternMS(System.currentTimeMillis());
    }

    public void setAverageScaffoldPitches(List<Double> averageScaffoldPitches)
    {
        this.averageScaffoldPitches = averageScaffoldPitches;
    }

    public void setFallDistance(double fallDistance) {
        this.fallDistance = fallDistance;
    }

    public int getIceTicks() {
        return iceTicks;
    }

    public void setIceTicks(int iceTicks) {
        this.iceTicks = iceTicks;
    }

    public int getAboveBlockTicks() {
        return aboveBlockTicks;
    }

    public HashMap<Float, Integer> getSamples()
    {
        return samples;
    }

    public void setSamples(HashMap<Float, Integer> samples)
    {
        this.samples = samples;
    }

    public void setAboveBlockTicks(int aboveBlockTicks) {
        this.aboveBlockTicks = aboveBlockTicks;
    }

    public int getWaterTicks() {
        return waterTicks;
    }

    public void setWaterTicks(int waterTicks) {
        this.waterTicks = waterTicks;
    }

    public int getSpeedAVerbose() {
        return speedAVerbose;
    }

    public void setSpeedAVerbose(int speedAVerbose) {
        this.speedAVerbose = speedAVerbose;
    }

    public int getSlimeTicks() {
        return slimeTicks;
    }

    public void setSlimeTicks(int slimeTicks) {
        this.slimeTicks = slimeTicks;
    }

    public long getSpeedGroundReset() {
        return speedGroundReset;
    }

    public void setSpeedGroundReset(long speedGroundReset) {
        this.speedGroundReset = speedGroundReset;
    }

    public int getCriticalsVerbose() {
        return criticalsVerbose;
    }

    public void setCriticalsVerbose(int criticalsVerbose) {
        this.criticalsVerbose = criticalsVerbose;
    }

    public double getLastKillauraYawDif() {
        return lastKillauraYawDif;
    }

    public void setLastKillauraYawDif(double lastKillauraYawDif) {
        this.lastKillauraYawDif = lastKillauraYawDif;
    }

    public double getLastKillauraPitch() {
        return lastKillauraPitch;
    }

    public void setLastKillauraPitch(double lastKillauraPitch) {
        this.lastKillauraPitch = lastKillauraPitch;
    }

    public double getLastKillauraYaw() {
        return lastKillauraYaw;
    }

    public void setLastKillauraYaw(double lastKillauraYaw) {
        this.lastKillauraYaw = lastKillauraYaw;
    }

    public int getKillauraAVerbose() {
        return killauraAVerbose;
    }

    public void setKillauraAVerbose(int killauraAVerbose) {
        this.killauraAVerbose = killauraAVerbose;
    }

    public long getLastPacket() {
        return lastPacket;
    }

    public void setLastPacket(long lastPacket) {
        this.lastPacket = lastPacket;
    }

    public long getLastAimTime() {
        return lastAimTime;
    }

    public void setLastAimTime(long lastAimTime) {
        this.lastAimTime = lastAimTime;
    }

    public long getLastBlockPlacedTicks() {
        return LastBlockPlacedTicks;
    }

    public void setLastBlockPlacedTicks(long lastBlockPlacedTicks) {
        LastBlockPlacedTicks = lastBlockPlacedTicks;
    }

    public boolean isLastBlockPlaced_GroundSpoof() {
        return LastBlockPlaced_GroundSpoof;
    }

    public void setLastBlockPlaced_GroundSpoof(boolean lastBlockPlaced_GroundSpoof) {
        LastBlockPlaced_GroundSpoof = lastBlockPlaced_GroundSpoof;
    }

    public int getAirTicks() {
        return airTicks;
    }

    public void setAirTicks(int airTicks) {
        this.airTicks = airTicks;
    }

    public int getGroundTicks() {
        return groundTicks;
    }

    public void setGroundTicks(int groundTicks) {
        this.groundTicks = groundTicks;
    }

    public int getFlyHoverVerbose() {
        return flyHoverVerbose;
    }

    public void setFlyHoverVerbose(int flyHoverVerbose) {
        this.flyHoverVerbose = flyHoverVerbose;
    }

    public int getGroundSpoofVL() {
        return GroundSpoofVL;
    }

    public void setGroundSpoofVL(int groundSpoofVL) {
        GroundSpoofVL = groundSpoofVL;
    }

    public boolean isShouldSetBack() {
        return ShouldSetBack;
    }

    public void setShouldSetBack(boolean shouldSetBack) {
        ShouldSetBack = shouldSetBack;
    }

    public double getLastVelocityFlyY() {
        return lastVelocityFlyY;
    }

    public void setLastVelocityFlyY(double lastVelocityFlyY) {
        this.lastVelocityFlyY = lastVelocityFlyY;
    }

    public int getSetBackTicks() {
        return setBackTicks;
    }

    public void setSetBackTicks(int setBackTicks) {
        this.setBackTicks = setBackTicks;
    }

    public long getLastVelMS() {
        return LastVelMS;
    }

    public void setLastVelMS(long lastVelMS) {
        LastVelMS = lastVelMS;
    }

    public boolean isDidTakeVelocity() {
        return DidTakeVelocity;
    }

    public void setDidTakeVelocity(boolean didTakeVelocity) {
        DidTakeVelocity = didTakeVelocity;
    }

    public int getFlyVelocityVerbose() {
        return flyVelocityVerbose;
    }

    public void setFlyVelocityVerbose(int flyVelocityVerbose) {
        this.flyVelocityVerbose = flyVelocityVerbose;
    }

    public long getLastDelayedPacket() {
        return this.lastDelayedPacket;
    }

    public void setLastDelayedPacket(long l) {
        this.lastDelayedPacket = l;
    }

    public long getLastPlayerPacketDiff() {
        return System.currentTimeMillis() - this.getLastPlayerPacket();
    }

    public long getLastPlayerPacket() {
        return this.lastPlayerPacket;
    }

    public void setLastPlayerPacket(long l) {
        this.lastPlayerPacket = l;
    }

    public Location getSetbackLocation() {
        return setbackLocation;
    }

    public void setSetbackLocation(Location setbackLocation) {
        this.setbackLocation = setbackLocation;
    }

    public double getGoingUp_Blocks() {
        return GoingUp_Blocks;
    }

    public void setGoingUp_Blocks(double goingUp_Blocks) {
        GoingUp_Blocks = goingUp_Blocks;
    }

    public double getLastY_Gravity() {
        return LastY_Gravity;
    }

    public void setLastY_Gravity(double lastY_Gravity) {
        LastY_Gravity = lastY_Gravity;
    }

    public int getGravity_VL() {
        return Gravity_VL;
    }

    public void setGravity_VL(int gravity_VL) {
        Gravity_VL = gravity_VL;
    }

    public int getAntiCactus_VL() {
        return AntiCactus_VL;
    }

    public void setAntiCactus_VL(int antiCactus_VL) {
        AntiCactus_VL = antiCactus_VL;
    }

    public long getSpeed_Ticks() {
        return Speed_Ticks;
    }

    public boolean isSpeed_TicksSet() {
        return Speed_TicksSet;
    }

    public void setSpeed_TicksSet(boolean speed_TicksSet) {
        Speed_TicksSet = speed_TicksSet;
    }

    public boolean isNearIce() {
        return isNearIce;
    }

    public void setNearIce(boolean nearIce) {
        isNearIce = nearIce;
    }

    public long getIsNearIceTicks() {
        return isNearIceTicks;
    }

    public void setIsNearIceTicks(long isNearIceTicks) {
        this.isNearIceTicks = isNearIceTicks;
    }

    public long getLastVelUpdate() {
        return LastVelUpdate;
    }

    public void setLastVelUpdate(long lastVelUpdate) {
        LastVelUpdate = lastVelUpdate;
    }

    public boolean isLastVelUpdateBoolean() {
        return LastVelUpdateBoolean;
    }

    public void setLastVelUpdateBoolean(boolean lastVelUpdateBoolean) {
        LastVelUpdateBoolean = lastVelUpdateBoolean;
    }

    public int getSpeed2Verbose() {
        return Speed2Verbose;
    }

    public void setSpeed2Verbose(int speed2Verbose) {
        Speed2Verbose = speed2Verbose;
    }


    public int getSpeed_OnGround_Verbose() {
        return Speed_OnGround_Verbose;
    }

    public void setSpeed_OnGround_Verbose(int speed_OnGround_Verbose) {
        Speed_OnGround_Verbose = speed_OnGround_Verbose;
    }

    public long getLastPacketTimer() {
        return lastPacketTimer;
    }

    public void setLastPacketTimer(long lastPacketTimer) {
        this.lastPacketTimer = lastPacketTimer;
    }

    public long getLastTimeTimer() {
        return LastTimeTimer;
    }

    public void setLastTimeTimer(long lastTimeTimer) {
        LastTimeTimer = lastTimeTimer;
    }

    public int getTimerVerbose() {
        return TimerVerbose;
    }

    public void setTimerVerbose(int timerVerbose) {
        TimerVerbose = timerVerbose;
    }

    public int getLastPACKETSTimer() {
        return LastPACKETSTimer;
    }

    public void setLastPACKETSTimer(int lastPACKETSTimer) {
        LastPACKETSTimer = lastPACKETSTimer;
    }

    public long getWebFloatMS() {
        return WebFloatMS;
    }

    public void setWebFloatMS(long webFloatMS) {
        WebFloatMS = webFloatMS;
    }

    public boolean isWebFloatMS_Set() {
        return WebFloatMS_Set;
    }

    public void setWebFloatMS_Set(boolean webFloatMS_Set) {
        WebFloatMS_Set = webFloatMS_Set;
    }

    public int getWebFloat_BlockCount() {
        return WebFloat_BlockCount;
    }

    public void setWebFloat_BlockCount(int webFloat_BlockCount) {
        WebFloat_BlockCount = webFloat_BlockCount;
    }

    public long getAboveSpeedTicks() {
        return AboveSpeedTicks;
    }

    public void setAboveSpeedTicks(long aboveSpeedTicks) {
        AboveSpeedTicks = aboveSpeedTicks;
    }

    public boolean isAboveSpeedSet() {
        return AboveSpeedSet;
    }

    public void setAboveSpeedSet(boolean aboveSpeedSet) {
        AboveSpeedSet = aboveSpeedSet;
    }

    public int getSpeedAC2_Verbose() {
        return SpeedAC2_Verbose;
    }

    public void setSpeedAC2_Verbose(int speedAC2_Verbose) {
        SpeedAC2_Verbose = speedAC2_Verbose;
    }

    public long getHalfBlocks_MS() {
        return HalfBlocks_MS;
    }

    public void setHalfBlocks_MS(long halfBlocks_MS) {
        HalfBlocks_MS = halfBlocks_MS;
    }

    public boolean isHalfBlocks_MS_Set() {
        return HalfBlocks_MS_Set;
    }

    public void setHalfBlocks_MS_Set(boolean halfBlocks_MS_Set) {
        HalfBlocks_MS_Set = halfBlocks_MS_Set;
    }

    public float getLastYaw()
    {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw)
    {
        this.lastYaw = lastYaw;
    }

    public float getLastYawDiff()
    {
        return lastYawDiff;
    }

    public void setLastYawDiff(float lastYawDiff)
    {
        this.lastYawDiff = lastYawDiff;
    }

    public float getLastYawOffset()
    {
        return lastYawOffset;
    }

    public void setLastYawOffset(float lastYawOffset)
    {
        this.lastYawOffset = lastYawOffset;
    }

    public float getLastRotationYaw()
    {
        return lastRotationYaw;
    }

    public void setLastRotationYaw(float lastRotationYaw)
    {
        this.lastRotationYaw = lastRotationYaw;
    }

    public boolean isSpeed_C_2_Set() {
        return Speed_C_2_Set;
    }

    public void setSpeed_C_2_Set(boolean speed_C_2_Set) {
        Speed_C_2_Set = speed_C_2_Set;
    }

    public long getSpeed_C_2_MS() {
        return Speed_C_2_MS;
    }

    public void setSpeed_C_2_MS(long speed_C_2_MS) {
        Speed_C_2_MS = speed_C_2_MS;
    }

    public int getSpeedC_Verbose() {
        return SpeedC_Verbose;
    }

    public void setSpeedC_Verbose(int speedC_Verbose) {
        SpeedC_Verbose = speedC_Verbose;
    }

    public long getGlideTicks() {
        return GlideTicks;
    }

    public void setGlideTicks(long glideTicks) {
        GlideTicks = glideTicks;
    }

    public int getSpeed_C_3_Verbose() {
        return Speed_C_3_Verbose;
    }

    public void setSpeed_C_3_Verbose(int speed_C_3_Verbose) {
        Speed_C_3_Verbose = speed_C_3_Verbose;
    }

    public long getSpeed_PistonExpand_MS() {
        return Speed_PistonExpand_MS;
    }

    public void setSpeed_PistonExpand_MS(long speed_PistonExpand_MS) {
        Speed_PistonExpand_MS = speed_PistonExpand_MS;
    }

    public boolean isSpeed_PistonExpand_Set() {
        return Speed_PistonExpand_Set;
    }

    public void setSpeed_PistonExpand_Set(boolean speed_PistonExpand_Set) {
        Speed_PistonExpand_Set = speed_PistonExpand_Set;
    }

    public long getBlockAbove() {
        return BlockAbove;
    }

    public void setBlockAbove(long blockAbove) {
        BlockAbove = blockAbove;
    }

    public boolean isBlockAbove_Set() {
        return BlockAbove_Set;
    }

    public void setBlockAbove_Set(boolean blockAbove_Set) {
        BlockAbove_Set = blockAbove_Set;
    }

    public int getSpeed_YPORT_Verbose() {
        return Speed_YPORT_Verbose;
    }

    public void setSpeed_YPORT_Verbose(int speed_YPORT_Verbose) {
        Speed_YPORT_Verbose = speed_YPORT_Verbose;
    }

    public long getSpeed_YPORT_MS() {
        return Speed_YPORT_MS;
    }

    public void setSpeed_YPORT_MS(long speed_YPORT_MS) {
        Speed_YPORT_MS = speed_YPORT_MS;
    }

    public boolean isSpeed_YPORT_Set() {
        return Speed_YPORT_Set;
    }

    public void setSpeed_YPORT_Set(boolean speed_YPORT_Set) {
        Speed_YPORT_Set = speed_YPORT_Set;
    }

    public long getSpeed_YPort2_MS() {
        return Speed_YPort2_MS;
    }

    public void setSpeed_YPort2_MS(long speed_YPort2_MS) {
        Speed_YPort2_MS = speed_YPort2_MS;
    }

    public boolean isSpeed_YPort2_Set() {
        return Speed_YPort2_Set;
    }

    public void setSpeed_YPort2_Set(boolean speed_YPort2_Set) {
        Speed_YPort2_Set = speed_YPort2_Set;
    }

    public int getSpeed_YPort2_Verbose() {
        return Speed_YPort2_Verbose;
    }

    public void setSpeed_YPort2_Verbose(int speed_YPort2_Verbose) {
        Speed_YPort2_Verbose = speed_YPort2_Verbose;
    }

    public long getLastPlacePacket() {
        return lastPlacePacket;
    }

    public void setLastPlacePacket(long lastPlacePacket) {
        this.lastPlacePacket = lastPlacePacket;
    }


    public long getLastAttackPacket() {
        return lastAttackPacket;
    }

    public void setLastAttackPacket(long lastAttackPacket) {
        this.lastAttackPacket = lastAttackPacket;
    }

    public void setLastPacketClass(PacketContainer packet) {
        this.lastPacketClass = packet;
    }

    public PacketContainer getLastPacketClass() {
        return lastPacketClass;
    }

    public long getLastPlace() {
        return lastPlace;
    }

    public void setLastPlace(long lastPlace) {
        this.lastPlace = lastPlace;
    }

    public long getLastExplosion() {
        return this.lastExplosion;
    }

    public void setLastExplosion(long lastExplosion) {
        this.lastExplosion = lastExplosion;
    }
}
