package rip.firefly.compat.reflect;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import rip.firefly.compat.wrapper.asm.QuadFunction;
import rip.firefly.compat.wrapper.asm.TriFunction;
import rip.firefly.compat.wrapper.asm.WrappedClass;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class Reflections {
    private static final String craftBukkitString;
    @Getter  private static final String netMinecraftServerString;
    private static MethodHandles.Lookup lookup = MethodHandles.lookup();

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        craftBukkitString = "org.bukkit.craftbukkit." + version + ".";
        netMinecraftServerString = "net.minecraft.server." + version + ".";

    }

    public static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static WrappedClass getCBClass(String name) {
        return getClass(craftBukkitString + name);
    }

    @SneakyThrows
    public static WrappedClass getNMSClass(String name) {
        return getClass(netMinecraftServerString + name);
    }


    public static WrappedClass getClass(String name) {
        try {
            return new WrappedClass(Class.forName(name));
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new NullPointerException("Class" + name + " could not be found!");
        }
    }

   // public static WrappedClass getUtilClass(String name) {
    //    return getClass((ProtocolVersion.getGameVersion().isBelow(ProtocolVersion.V1_8)
    //            ? "net.minecraft.util." : "") + name.replace("cc.funkemunky.api.utils.", ""));
   // }

    @SneakyThrows
    public static <T> T createMethodLambda(Method method) {
        if(!method.isAccessible()) return null;
        val handle = lookup.unreflect(method);
        Class<?> functionType;
        switch(method.getParameterCount()) {
            case 0:
                functionType = Function.class;
                break;
            case 1:
                functionType = BiFunction.class;
                break;
            case 2:
                functionType = TriFunction.class;
                break;
            case 3:
                functionType = QuadFunction.class;
            default:
                functionType = null;
                break;
        }

        if(functionType != null) {
            return (T) LambdaMetafactory.metafactory(lookup, "apply",
                    MethodType.methodType(functionType),
                    MethodType.methodType(method.getReturnType(), handle.type().parameterArray()),
                    handle, handle.type()).getTarget().invoke();
        }

        return null;
    }

    public static WrappedClass getClass(Class clazz) {
        return new WrappedClass(clazz);
    }
}