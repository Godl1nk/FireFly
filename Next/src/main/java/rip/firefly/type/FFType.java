package rip.firefly.type;

public enum FFType {
    LIFETIME("Lifetime"),
    MONTHLY("Monthly");

    public final String name;

    FFType(String name) {
        this.name = name;
    }
}
