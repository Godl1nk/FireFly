package rip.firefly.check.types;

import rip.firefly.check.AbstractCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;

public abstract class PacketCheck extends AbstractCheck<NMSObject> {

    public PacketCheck(PlayerData data) {
        super(data);
    }

    /**
     * The method to handle checks with packets
     * @param playerData The player the check
     * @param packet The {@link NMSObject} to pass to the check
     */
    public abstract void handle(PlayerData playerData, NMSObject packet);
}