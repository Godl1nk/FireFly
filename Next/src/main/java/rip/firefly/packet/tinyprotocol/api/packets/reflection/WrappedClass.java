package rip.firefly.packet.tinyprotocol.api.packets.reflection;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

@Getter
public class WrappedClass {
    private final Class parent;

    public WrappedClass(Class parent) {
        this.parent = parent;
    }

    public WrappedField getFieldByName(String name) {
        Field tempField = null;
        for (Field field : this.parent.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                tempField = field;
                break;
            }
        }
        if (tempField != null) {
            tempField.setAccessible(true);
            return new WrappedField(this, tempField);
        }
        return null;
    }

    public WrappedConstructor getConstructor(Class... types) {
        try {
            return new WrappedConstructor(this, this.parent.getDeclaredConstructor(types));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public WrappedConstructor getConstructor() {
        if(Arrays.stream(this.parent.getConstructors()).anyMatch(cons -> cons.getParameterCount() == 0)) {
            return new WrappedConstructor(this);
        }
        return null;
    }

    public WrappedConstructor getConstructorAtIndex(int index) {
        return new WrappedConstructor(this, this.parent.getConstructors()[index]);
    }

    private WrappedField getFieldByType(Class<?> type) {
        WrappedField tempField = null;
        for (Field field : this.parent.getDeclaredFields()) {
            if (field.getType().equals(type)) {
                tempField = new WrappedField(this, field);
                break;
            }
        }
        return tempField;
    }

    public WrappedField getFirstFieldByType(Class<?> type) {
        return this.getFieldByType(type);
    }

    public WrappedMethod getMethod(String name, Class... parameters) {
        for (Method method : this.parent.getDeclaredMethods()) {
            if (!method.getName().equals(name) || parameters.length != method.getParameterTypes().length) {
                continue;
            }
            boolean same = true;
            for (int x = 0; x < method.getParameterTypes().length; x++) {
                if (method.getParameterTypes()[x] != parameters[x]) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return new WrappedMethod(this, method);
            }
        }
        for (Method method : this.parent.getMethods()) {
            if (!method.getName().equals(name) || parameters.length != method.getParameterTypes().length) {
                continue;
            }
            boolean same = true;
            for (int x = 0; x < method.getParameterTypes().length; x++) {
                if (method.getParameterTypes()[x] != parameters[x]) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return new WrappedMethod(this, method);
            }
        }
        return null;
    }

    public Enum getEnum(String name) {
        return Enum.valueOf(this.parent, name);
    }
}
