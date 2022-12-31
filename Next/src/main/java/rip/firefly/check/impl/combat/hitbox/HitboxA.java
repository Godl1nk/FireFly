package rip.firefly.check.impl.combat.hitbox;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInUseEntityPacket;
import rip.firefly.packet.tinyprotocol.packet.types.Vec3D;


@CheckData(name = "HitBox", subType = "A", description = "Checks For HitBox expansion using Vectors", type = CheckType.COMBAT, threshold = 10, autoban = true)
public class HitboxA extends PacketCheck {

    //not compatible with things such as mSpigot
    public HitboxA(PlayerData data) {
        super(data);
    }


    public static String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().substring(23);

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if (packet instanceof WrappedInUseEntityPacket && ((WrappedInUseEntityPacket)packet).getEntity() instanceof Player) {
            Player player = (Player)((WrappedInUseEntityPacket)packet).getEntity();
            if (player.getGameMode() == GameMode.CREATIVE) {
                return;
            }
            Vec3D vec = ((WrappedInUseEntityPacket)packet).getBody();
            double x = vec.a;
            double y = vec.b;
            double z = vec.c;
            if (Math.max(Math.abs(x), Math.abs(z)) > 0.401 || Math.abs(y) > 1.91) {
                flag(playerData, String.format("X: %s", x), String.format("Y: %s", y), String.format("Z: %s", z));
            }
        }
    }
}
