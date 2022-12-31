package rip.firefly.log;

import lombok.AllArgsConstructor;

import java.util.UUID;

public class Log {
    public final String check;
    public final String subtype;
    public String logId;
    public String[] data;
    public final UUID uuid;
    
    public Log(String check, String subtype, UUID uuid) {
        this.check = check;
        this.subtype = subtype;
        this.uuid = uuid;
    }
    
    public Log(String check, String subtype, UUID uuid, String[] data) {
        this.check = check;
        this.subtype = subtype;
        this.data = data;
        this.uuid = uuid;
    }
    
    public void save() {
        // Saving function
    }
    
    
    public Log load() {
        // loading function
        return this;
    }
}
