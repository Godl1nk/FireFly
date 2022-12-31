package ac.firefly.data;

public enum WaveType {
    CHEATING("Unfair Advantage"),
    BOOSTING("Boosting"),
    COMPROMISED("Compromised Account"),
    ABUSE("Abuse");
    private final String type;

    WaveType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
