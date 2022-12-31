package rip.firefly.check;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CheckType {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    WORLD("World"),
    MISC("Miscellaneous");

    private final String displayName;
}