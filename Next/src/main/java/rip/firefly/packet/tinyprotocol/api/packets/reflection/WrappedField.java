package rip.firefly.packet.tinyprotocol.api.packets.reflection;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Getter
public class WrappedField {
    private final WrappedClass parent;
    private final Field field;
    private final Class<?> type;

    public WrappedField(WrappedClass parent, Field field) {
        this.parent = parent;
        this.field = field;
        this.type = field.getType();
        this.field.setAccessible(true);
    }

    public <T> T get(Object parent) {
        try {
            return (T) this.field.get(parent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(Object parent, Object value) {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(this.field, this.field.getModifiers() & ~Modifier.FINAL);
            this.field.setAccessible(true);
            this.field.set(parent, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
