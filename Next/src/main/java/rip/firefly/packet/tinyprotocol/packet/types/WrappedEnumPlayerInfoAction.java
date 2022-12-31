package rip.firefly.packet.tinyprotocol.packet.types;

public enum WrappedEnumPlayerInfoAction {
    ADD_PLAYER("addPlayer"),
    UPDATE_GAME_MODE("updateGamemode"),
    UPDATE_LATENCY("updatePing"),
    UPDATE_DISPLAY_NAME("updateDisplayName"),
    REMOVE_PLAYER("removePlayer");

    public String legacyMethodName;

    WrappedEnumPlayerInfoAction(String legacyMethodName) {
        this.legacyMethodName = legacyMethodName;
    }
}
