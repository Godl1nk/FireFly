package rip.firefly.check.impl.combat.autoclicker;

import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.bukkit.entity.Player;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInArmAnimationPacket;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInFlyingPacket;
import rip.firefly.util.math.EvictingLinkedList;

@CheckData(name = "AutoClicker", subType = "C", type = CheckType.COMBAT, threshold = 10, description = "Checks For Vape Client's AutoClicker")
public class AutoClickerC extends PacketCheck {
    public AutoClickerC(PlayerData data) {
        super(data);
    }
    ClickProfile clickProfile = null;

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInArmAnimationPacket) {
            if(playerData.isDigging()) {
                return;
            }
            if(clickProfile ==  null) {
                clickProfile = new ClickProfile();
            }
            clickProfile.issueClick(playerData);
        }
    }

    public class ClickProfile
    {

        public double clicks = 0.0;
        private long clickSprint = 0L;
        private double lastCPS = 0.0;
        private double twoSecondsAgoCPS = 0.0;
        private double threeSecondsAgoCPS = 0.0;
        private int violations = 0;

        public void issueClick(PlayerData player)
        {
            long l = System.currentTimeMillis();
            if (l - this.clickSprint >= 1000L)
            {
                this.shuffleDown();
                this.clickSprint = l;
                this.clicks = 0.0;
                double d = this.lastCPS;
                double d2 = this.twoSecondsAgoCPS;
                double d3 = this.threeSecondsAgoCPS;
                if (d == 9.0 && d2 == 11.0 && d3 == 10.0 || d == 9.0 && d2 == 8.0 && d3 == 10.0)
                {
                    ++this.violations;
                    if (this.violations >= 1)
                    {
                        flag(player, new String[]{"CPS: " + this.lastCPS + ", " + this.twoSecondsAgoCPS + ", " + this.threeSecondsAgoCPS});
                    }
                }
            }
            this.clicks += 1.0;
        }

        private void shuffleDown()
        {
            this.threeSecondsAgoCPS = this.twoSecondsAgoCPS;
            this.twoSecondsAgoCPS = this.lastCPS;
            this.lastCPS = this.clicks;
        }
    }

}
