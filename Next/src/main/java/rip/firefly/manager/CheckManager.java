package rip.firefly.manager;

import com.google.common.collect.ImmutableClassToInstanceMap;
import org.bukkit.plugin.java.JavaPlugin;
import rip.firefly.FireFly;
import rip.firefly.check.AbstractCheck;
import rip.firefly.check.impl.combat.aim.*;
import rip.firefly.check.impl.combat.aim.sub.*;
import rip.firefly.check.impl.combat.aura.AuraA;
import rip.firefly.check.impl.combat.autoclicker.AutoClickerA;
import rip.firefly.check.impl.combat.autoclicker.AutoClickerB;
import rip.firefly.check.impl.combat.autoclicker.AutoClickerC;
import rip.firefly.check.impl.combat.hitbox.HitboxA;
import rip.firefly.check.impl.combat.reach.ReachA;
import rip.firefly.check.impl.combat.reach.ReachB;
import rip.firefly.check.impl.misc.baritone.BaritoneA;
import rip.firefly.check.impl.misc.invalid.*;
import rip.firefly.check.impl.misc.payload.PayloadA;
import rip.firefly.check.impl.misc.payload.PayloadB;
import rip.firefly.check.impl.misc.payload.PayloadC;
import rip.firefly.check.impl.misc.ping.PingA;
import rip.firefly.check.impl.misc.ping.PingB;
import rip.firefly.check.impl.misc.ping.PingC;
import rip.firefly.check.impl.movement.fly.FlyA;
import rip.firefly.check.impl.movement.fly.FlyB;
import rip.firefly.check.impl.movement.motion.MotionA;
import rip.firefly.check.impl.movement.scaffold.ScaffoldC;
import rip.firefly.check.impl.movement.speed.*;
import rip.firefly.check.impl.movement.velocity.VelocityA;
import rip.firefly.data.PlayerData;
import rip.firefly.type.FFPackage;

import java.util.Collection;

public class CheckManager {
 //   private static ClassToInstanceMap<AbstractCheck> checks;
    private static PlayerData data;

    public CheckManager(final PlayerData playerData) {

        data = playerData;
        playerData.checks = new ImmutableClassToInstanceMap.Builder<AbstractCheck>()
                .put(AimA.class, new AimA(playerData))
                .put(AimB.class, new AimB(playerData))
                .put(AimC.class, new AimC(playerData))
                .put(AimD.class, new AimD(playerData))
                .put(AimE.class, new AimE(playerData))
                .put(AimF.class, new AimF(playerData))
                .put(AimG.class, new AimG(playerData))
                .put(AimH.class, new AimH(playerData))
                .put(AimI.class, new AimI(playerData))
                .put(AimJ.class, new AimJ(playerData))
                .put(AimK.class, new AimK(playerData))
                .put(AimL.class, new AimL(playerData))
                .put(AimM.class, new AimM(playerData))
                .put(AimN.class, new AimN(playerData))
                .put(AimO.class, new AimO(playerData))
                .put(AimP.class, new AimP(playerData))
                .put(AimQ.class, new AimQ(playerData))
                .put(AimR.class, new AimR(playerData))
                .put(AimS.class, new AimS(playerData))
                .put(AimT.class, new AimT(playerData))
                .put(AimU.class, new AimU(playerData))
                .put(AimV.class, new AimV(playerData))
                .put(AimX.class, new AimX(playerData))
                .put(AimY.class, new AimY(playerData))
                .put(AimZ.class, new AimZ(playerData))

                .put(AimAA.class, new AimAA(playerData))
                .put(AimAB.class, new AimAB(playerData))
                .put(AimAC.class, new AimAC(playerData))
                .put(AimAD.class, new AimAD(playerData))
                .put(AimAE.class, new AimAE(playerData))

                .put(AuraA.class, new AuraA(playerData))

                .put(HitboxA.class, new HitboxA(playerData))

            //    .put(NoSlowA.class, new NoSlowA(playerData))

                .put(AutoClickerA.class, new AutoClickerA(playerData))
                .put(AutoClickerB.class, new AutoClickerB(playerData))
                .put(AutoClickerC.class, new AutoClickerC(playerData))

                .put(InvalidA.class, new InvalidA(playerData))
                .put(InvalidB.class, new InvalidB(playerData))
                .put(InvalidC.class, new InvalidC(playerData))
                .put(InvalidD.class, new InvalidD(playerData))
                .put(InvalidE.class, new InvalidE(playerData))

                .put(FlyA.class, new FlyA(playerData))
                .put(FlyB.class, new FlyB(playerData))

                .put(BaritoneA.class, new BaritoneA(playerData))

                .put(SpeedA.class, new SpeedA(playerData))
                .put(SpeedB.class, new SpeedB(playerData))
                .put(SpeedC.class, new SpeedC(playerData))
                .put(SpeedD.class, new SpeedD(playerData))
                .put(SpeedE.class, new SpeedE(playerData))

                .put(ReachA.class, new ReachA(playerData))
                .put(ReachB.class, new ReachB(playerData))

                .put(PingA.class, new PingA(playerData))
                .put(PingB.class, new PingB(playerData))
                .put(PingC.class, new PingC(playerData))

           //     .put(ScaffoldA.class, new ScaffoldA(playerData))
         //       .put(ScaffoldB.class, new ScaffoldB(playerData))
               // .put(ScaffoldC.class, new ScaffoldC(playerData)) // Could flag speed too (needs more experimenting)
              //  .put(ScaffoldD.class, new ScaffoldD(playerData))

                .put(PayloadA.class, new PayloadA(playerData))
                .put(PayloadB.class, new PayloadB(playerData))
                .put(PayloadC.class, new PayloadC(playerData))

                .put(MotionA.class, new MotionA(playerData))

                .put(VelocityA.class, new VelocityA(playerData))

                .build();

    }

    public CheckManager(JavaPlugin plugin) {

    }

    public void newLoad(PlayerData playerData) {
        for(Class<? extends AbstractCheck> c : playerData.checks.keySet()) {
            if(playerData.checks.get(c).isEnterprise() && FireFly.getType() != FFPackage.ENTERPRISE) {
                playerData.checks.remove(c);
            }
        }
    }

    // Get a specific check for pushing a packet/event, or do whatever action possible within said check class
    public final <T extends AbstractCheck> T getCheck(final Class<T> clazz) {
        return data.checks.getInstance(clazz);
    }

    // Get all checks we have stored
    public static Collection<AbstractCheck> getChecks() {
        return data.checks.values();
    }
}
