package ac.firefly.check.impl.movement.fly;

import ac.firefly.Firefly;
import ac.firefly.check.Check;
import ac.firefly.check.CheckType;
import ac.firefly.managers.PluginManager;
import ac.firefly.util.interaction.CompatUtil;
import net.minecraft.server.v1_8_R3.AxisAlignedBB;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class FlyG extends Check {

    private Material slime;
    private Map<UUID, Long> slimeTicks = new HashMap<UUID, Long>();
    private Map<UUID, Double> slimeHeight = new HashMap<UUID, Double>();
    private Map<UUID, Integer> jumpBoostLevel = new HashMap<UUID, Integer>();
    private Map<UUID, Long> jumpBoostTicks = new HashMap<UUID, Long>();
    private Map<UUID, Map<Long, Double>> ascensionTicks = new HashMap<UUID, Map<Long, Double>>();
    private Map<UUID, Location> ground = new HashMap<UUID, Location>();
    private Map<UUID, Vector> playerVelocity = new HashMap<UUID, Vector>();
    private Map<UUID, Long> velocityUpdate = new HashMap<UUID, Long>();
    private Map<UUID, Long> lastAttack = new HashMap<UUID, Long>();
    private long velocityDeacyTime = 2500L;
    private double maxVelocityBeforeDeacy = 0.03;
    private double baseHeight = 3.0;

    public FlyG() {
        super("Fly (G)", CheckType.MOVEMENT, true);

        try {
            this.slime = Material.SLIME_BLOCK;
        }
        catch (NoSuchFieldError noSuchFieldError) {
            // empty catch block
        }
        new BukkitRunnable(){

            public void run() {
                for (Player op : Bukkit.getOnlinePlayers()) {
                    long lastUpdate;
                    Vector v = op.getVelocity();
                    UUID uuid = op.getUniqueId();
                    if (!FlyG.this.velocityUpdate.containsKey(op.getUniqueId()) || (lastUpdate = System.currentTimeMillis() - (Long)FlyG.this.velocityUpdate.get(op.getUniqueId())) <= FlyG.this.velocityDeacyTime || !(v.getX() < FlyG.this.maxVelocityBeforeDeacy && v.getY() < FlyG.this.maxVelocityBeforeDeacy && v.getZ() < FlyG.this.maxVelocityBeforeDeacy) && (!(v.getX() > -FlyG.this.maxVelocityBeforeDeacy) || !(v.getY() > -FlyG.this.maxVelocityBeforeDeacy) || !(v.getZ() > -FlyG.this.maxVelocityBeforeDeacy))) continue;
                    FlyG.this.playerVelocity.remove(uuid);
                }
            }
        }.runTaskTimer(Firefly.instance, 0L, 1L);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!player.getAllowFlight()) {
            if (player.getVehicle() == null) {
                long jumpBoostTicksOffset;
                double groundDiff;
                if (player.isOnGround()) {
                    return;
                }
                long lastAttackOffset = Long.MAX_VALUE;
                if (this.lastAttack.containsKey(uuid)) {
                    lastAttackOffset = System.currentTimeMillis() - this.lastAttack.get(uuid);
                }
                if (boundingBoxOnGround(player)) {
                    if (lastAttackOffset > 1000L) {
                        this.ground.put(uuid, player.getLocation());
                    }
                } else if (!this.ground.containsKey(uuid)) {
                    this.ground.put(uuid, player.getLocation());
                }
                Location lastGround = this.ground.get(uuid);
                if (lastAttackOffset < 5000L) {
                    lastGround = lastGround.clone().add(0.0, 1.0, 0.0);
                }
                double maxHeight = this.baseHeight + this.calcSlimeOffset(player);
                if (this.playerVelocity.containsKey(uuid)) {
                    maxHeight += this.playerVelocity.get(uuid).getY() + 1.0;
                }
                if (player.hasPotionEffect(PotionEffectType.JUMP)) {
                    for (PotionEffect effect : player.getActivePotionEffects()) {
                        if (!effect.getType().equals((Object)PotionEffectType.JUMP)) continue;
                        int level = effect.getAmplifier() + 1;
                        maxHeight += Math.pow((double)level + 4.2, 2.0) / 16.0;
                        this.jumpBoostLevel.put(uuid, level);
                        this.jumpBoostTicks.put(uuid, System.currentTimeMillis());
                        break;
                    }
                } else if (this.jumpBoostTicks.containsKey(uuid) && (jumpBoostTicksOffset = System.currentTimeMillis() - this.jumpBoostTicks.get(uuid)) < 5000L) {
                    int level = this.jumpBoostLevel.get(uuid);
                    maxHeight += Math.pow((double)level + 4.2, 2.0) / 16.0;
                }
                if ((groundDiff = e.getTo().getY() - lastGround.getY()) > maxHeight + 0.005 && hasExpired(PluginManager.instance.getDataManager().getData(player).getLastExplosion(), 5L)) {
                    this.flag(player, new String(), new String());
                }
            } else {
                this.ground.put(uuid, player.getLocation());
            }
        } else {
            this.ground.put(uuid, player.getLocation());
        }
    }

    public static boolean hasExpired(long timestamp, long seconds) {
        return System.currentTimeMillis() - timestamp > TimeUnit.SECONDS.toMillis(seconds);
    }

    private double calcSlimeOffset(Player player) {
        if (this.slime != null) {
            UUID uuid = player.getUniqueId();
            if (!this.slimeTicks.containsKey(uuid)) {
                this.slimeTicks.put(uuid, 0L);
                this.slimeHeight.put(uuid, 0.0);
            } else {
                long timeOffset = System.currentTimeMillis() - this.slimeTicks.get(uuid);
                if (timeOffset < 15000L) {
                    return this.slimeHeight.get(uuid) + 2.0;
                }
            }
            Location playerLoc = player.getLocation();
            for (int yOffset = 0; yOffset <= 4; ++yOffset) {
                playerLoc.setY(playerLoc.getY() - 1.0);
                Block block = playerLoc.getBlock();
                if (block.getType() != this.slime) continue;
                double fallDistance = Math.max((double)(player.getFallDistance() + (float)yOffset), 4.0);
                if (fallDistance <= 128.0) {
                    double estBounceHeight = -0.0011 * Math.pow(fallDistance, 2.0) + 0.43529 * fallDistance + 1.8;
                    this.slimeTicks.put(uuid, System.currentTimeMillis());
                    this.slimeHeight.put(uuid, estBounceHeight);
                    return estBounceHeight + 2.0;
                }
                return 17.625;
            }
            return 0.0;
        }
        return 0.0;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player && !(e.getDamager() instanceof Arrow)) {
            Player player = (Player)e.getDamager();
            this.ground.put(player.getUniqueId(), player.getLocation().add(0.0, 0.5, 0.0));
            this.lastAttack.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        this.ground.put(e.getPlayer().getUniqueId(), e.getTo());
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        this.playerVelocity.put(e.getPlayer().getUniqueId(), e.getVelocity());
        if (!this.velocityUpdate.containsKey(e.getPlayer().getUniqueId())) {
            this.velocityUpdate.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        } else {
            this.velocityUpdate.put(e.getPlayer().getUniqueId(), System.currentTimeMillis());
        }
    }

    public static boolean boundingBoxOnGround(Player player) {
        CompatUtil.detectVersion();
        if (!CompatUtil.is17()) {
            AxisAlignedBB axisalignedbb = ((CraftPlayer)player).getHandle().getBoundingBox().grow(0.0625, 0.0625, 0.0625).a(0.0, -0.2, 0.0);
            CraftWorld cw = (CraftWorld)player.getWorld();
            return cw.getHandle().c(axisalignedbb);
        }
        return true;
    }



}
