package rip.firefly.type;

public enum FFPackage {
    STANDARD("Standard"),
    ENTERPRISE("Enterprise");

    public final String name;

    FFPackage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
