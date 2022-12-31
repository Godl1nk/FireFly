package rip.firefly.check.impl.misc.payload;

import rip.firefly.check.CheckType;
import rip.firefly.check.annotation.CheckData;
import rip.firefly.check.types.PacketCheck;
import rip.firefly.data.PlayerData;
import rip.firefly.packet.tinyprotocol.api.NMSObject;
import rip.firefly.packet.tinyprotocol.packet.in.WrappedInCustomPayload;

@CheckData(name = "Payload", subType = "A", description = "Checks for invalid messaging channels", type = CheckType.MISC, threshold = 1)
public class PayloadA extends PacketCheck {
    private static final String[] INVALID_CHANNELS = {"LOLIMAHCKER", "CPS_BAN_THIS_NIGGER",  "EROUAXWASHERE", "EARWAXWASHERE", "#unbanearwax", "1946203560", "cock", "lmaohax",  "reach", "gg", "customGuiOpenBspkrs", "0SO1Lk2KASxzsd", "MCnetHandler", "n", "CRYSTAL|KZ1LM9TO", "CRYSTAL|6LAKS0TRIES", "BLC|M", "XDSMKDKFDKSDAKDFkEJF"};

    private boolean flag;

    public PayloadA(PlayerData data) {
        super(data);
    }

    @Override
    public void handle(PlayerData playerData, NMSObject packet) {
        if(packet instanceof WrappedInCustomPayload) {
            for(String s : INVALID_CHANNELS) {
                if (((WrappedInCustomPayload) packet).getChannel().equalsIgnoreCase(s)) {
                    flag = true;
                    break;
                }
            }

            if(flag) {
                flag(playerData, String.format("C: %s", ((WrappedInCustomPayload) packet).getChannel()));
                flag = false;
            }
        }
    }
}