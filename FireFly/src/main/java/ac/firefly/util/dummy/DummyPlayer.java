package ac.firefly.util.dummy;

import ac.firefly.Firefly;
import com.comphenix.packetwrapper.WrapperPlayServerEntityDestroy;
import com.comphenix.packetwrapper.WrapperPlayServerNamedEntitySpawn;
import com.comphenix.packetwrapper.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DummyPlayer {
    private static Random random = new Random();
    private static Location pos;
    private static UUID id;
    private static int entityId;
    private static String username;
    private static EnumWrappers.NativeGameMode gameMode;

    public DummyPlayer() {
        id = UUID.randomUUID();
        entityId = random.nextInt(1000);
        gameMode = EnumWrappers.NativeGameMode.SURVIVAL;
        username = "Steve";
    }

    public void spawn(Player target) {
        WrapperPlayServerNamedEntitySpawn body = new WrapperPlayServerNamedEntitySpawn();


        body.setEntityID(entityId);
        body.setPlayerUUID(id);

        body.setX(pos.getX() * 32);
        body.setY(pos.getY() * 32);
        body.setZ(pos.getZ() * 32);
        body.setYaw(pos.getYaw());
        body.setPitch(pos.getPitch());

        WrapperPlayServerPlayerInfo info = new WrapperPlayServerPlayerInfo();

        info.setAction(EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        PlayerInfoData data = new PlayerInfoData(new WrappedGameProfile(id, username), 1, gameMode, WrappedChatComponent.fromText(username));
        List<PlayerInfoData> dataList = new ArrayList<PlayerInfoData>();
        dataList.add(data);
        info.setData(dataList);

        try {
            Firefly.protocolManager.sendServerPacket(target, info.getHandle());
            Firefly.protocolManager.sendServerPacket(target, body.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void despawn(Player target) {
        WrapperPlayServerEntityDestroy packet = new WrapperPlayServerEntityDestroy();
        packet.setEntityIds(new int[]{entityId});
        try {
            Firefly.protocolManager.sendServerPacket(target, packet.getHandle());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setEntityId(int entityId) {
        DummyPlayer.entityId = DummyPlayer.entityId;
    }

    public void setUuid(UUID uuid) {
        DummyPlayer.id = id;
    }

    public void setGameMode(EnumWrappers.NativeGameMode gameMode) {
        DummyPlayer.gameMode = gameMode;
    }

    public void setPos(Location pos) {
        DummyPlayer.pos = pos;
    }

    public void setUsername(String username) {
        DummyPlayer.username = username;
    }

    public Location getPos() {
        return pos;
    }

    public UUID getUuid() {
        return id;
    }

    public String getUsername() {
        return username;
    }


}
