package ac.firefly.util.interaction;


import ac.firefly.data.PlayerData;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ReflectionUtil
{

    private static Logger log = Logger.getLogger("BoxUtils");

    private static HashMap<String, Class<?>> classCache = new HashMap('?');
    private static HashMap<String, Field> fieldCache = new HashMap('?');
    private static HashMap<String, Method> methodCache = new HashMap('?');
    private static HashMap<String, Constructor> constructorCache = new HashMap('?');

    private static String obcPrefix = null;
    private static String nmsPrefix = null;

    static
    {
        try
        {
            nmsPrefix = "net.minecraft.server." +
                    Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";

            obcPrefix = "org.bukkit.craftbukkit." +
                    Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
        }
        catch (Exception exception)
        {
            nmsPrefix = "net.minecraft.server.";

            obcPrefix = "org.bukkit.craftbukkit.";
        }
    }

    public static Class<?> getCraftBukkitClass(String paramString)
    {
        return getClass(String.valueOf(obcPrefix) + paramString);
    }


    public static Class<?> getNMSClass(String paramString)
    {
        return getClass(String.valueOf(nmsPrefix) + paramString);
    }


    public static Class<?> getClass(String paramString)
    {
        Validate.notNull(paramString);

        if (classCache.containsKey(paramString))
        {
            return (Class) classCache.get(paramString);
        }
        Class clazz = null;

        try
        {
            clazz = Class.forName(paramString);
        }
        catch (ClassNotFoundException classNotFoundException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the class " + paramString);
        }

        if (clazz != null)
        {
            classCache.put(paramString, clazz);
        }

        return clazz;
    }


    public static Field getField(String paramString, Class<?> paramClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (fieldCache.containsKey(str))
        {
            return (Field) fieldCache.get(str);
        }
        Field field = null;

        try
        {
            field = paramClass.getField(paramString);
        }
        catch (NoSuchFieldException noSuchFieldException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the field " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (field != null)
        {
            fieldCache.put(str, field);
        }

        return field;
    }


    public static Method getMethod(Class<?> paramClass, String paramString, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, paramArrayOfClass);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Method getMethod(String paramString, Class<?> paramClass, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, paramArrayOfClass);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Method getMethod(String paramString, Class<?> paramClass)
    {
        Validate.notNull(paramString);
        Validate.notNull(paramClass);

        String str = String.valueOf(paramClass.getCanonicalName()) + "@" + paramString;

        if (methodCache.containsKey(str))
        {
            return (Method) methodCache.get(str);
        }
        Method method = null;

        try
        {
            method = paramClass.getMethod(paramString, new Class[0]);
        }
        catch (NoSuchMethodException noSuchMethodException)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to find the the method " +
                    paramString + " in class " + paramClass.getSimpleName());
        }

        if (method != null)
        {
            methodCache.put(str, method);
        }

        return method;
    }


    public static Constructor<?> getConstructor(Class<?> paramClass, Class<?>[] paramArrayOfClass)
    {
        Validate.notNull(paramClass);
        Validate.notNull(paramArrayOfClass);

        String str = String.valueOf(paramClass.getSimpleName()) + "#";
        int i;
        Class<?>[] arrayOfClass;
        /*for (int b; i = paramArrayOfClass.length, b = 0; b < i; ) { Class<?> clazz = arrayOfClass[b];
            str = String.valueOf(str) + clazz.getSimpleName() + "_";
            b++; }*/

        str = str.substring(0, str.length() - 1);

        if (constructorCache.containsKey(str))
        {
            return (Constructor) constructorCache.get(str);
        }
        Constructor constructor = null;

        try
        {
            constructor = paramClass.getConstructor(paramArrayOfClass);
        }
        catch (NoSuchMethodException b)
        {
            NoSuchMethodException noSuchMethodException;
            StringBuilder stringBuilder = new StringBuilder();
            byte b1;
            int j;
            Class<?>[] arrayOfClass1 = null;
            for (j = paramArrayOfClass.length, b1 = 0; b1 < j; )
            {
                Class<?> clazz = arrayOfClass1[b1];
                stringBuilder.append(clazz.getCanonicalName()).append(", ");
                b1++;
            }

            stringBuilder.setLength(stringBuilder.length() - 2);

            log.log(Level.SEVERE, "[Reflection] Unable to find the the constructor  with the params [" +

                    stringBuilder.toString() + "] in class " + paramClass.getSimpleName());
        }

        if (constructor != null)
        {
            constructorCache.put(str, constructor);
        }

        return constructor;
    }


    public static Object getEntityHandle(Entity paramEntity)
    {
        try
        {
            Method method = getMethod("getHandle", paramEntity.getClass());
            return method.invoke(paramEntity, new Object[0]);
        }
        catch (Exception exception)
        {
            log.log(Level.SEVERE, "[Reflection] Unable to getHandle of " +
                    paramEntity.getType());
            return null;
        }
    }

    public static void sendPacket(Player paramPlayer, Object paramObject)
    {
        Object object = null;
        try
        {
            object = getEntityHandle(paramPlayer);
            Object object1 = object.getClass().getField("playerConnection").get(object);
            getMethod("sendPacket", object1.getClass()).invoke(object1, new Object[]{paramObject});
        }
        catch (IllegalAccessException illegalAccessException)
        {
            illegalAccessException.printStackTrace();
        }
        catch (InvocationTargetException invocationTargetException)
        {
            invocationTargetException.printStackTrace();
        }
        catch (NoSuchFieldException noSuchFieldException)
        {
            noSuchFieldException.printStackTrace();
        }
    }

    private static Field getField(final Class<?> clazz, final String name, final Class<?> type) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            if (field.getType() != type) {
                throw new IllegalStateException("Invalid action for field '" + name + "' (expected " + type.getName() + ", got " + field.getType().getName() + ")");
            }
            return field;
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to get field '" + name + "'");
        }
    }

    /**
     * Gets a field value and casts it to the class specified generic in {@param action}
     *
     * @param clazz - The class with the field to retrieve in
     * @param fieldName - The field name
     * @param type - The field action, int, double etc
     * @param instance - The instance to use to retrieve the specified field value
     * @param <T> - The action generic
     * @return The field value for {@param fieldName} in the {@param clazz} class
     */
    public static <T> T getFieldValue(Class<?> clazz, String fieldName, Class<?> type, Object instance) {
        Field field = getField(clazz, fieldName, type);
        field.setAccessible(true);
        try {
            //noinspection unchecked
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to get value of field '" + field.getName() + "'");
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */

    @SuppressWarnings("all")
    private static Class[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration resources = classLoader.getResources(path);
        List dirs = new ArrayList();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList classes = new ArrayList();
        for (Object directory : dirs) {
            classes.addAll(findClasses((File) directory, packageName));
        }

        return (Class[]) classes.toArray(new Class[classes.size()]);
    }
    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("all")
    private static List findClasses(File directory, String packageName) throws ClassNotFoundException {
        List classes = new ArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    public static boolean onGround(final PlayerData playerData) {
        final Player player = playerData.getPlayer();

        boolean onGround = false;

        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);

            onGround = handle.getClass().getField("onGround").getBoolean(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return onGround;
    }

    public static double getMotionY(final PlayerData playerData) {
        final Player player = playerData.getPlayer();

        double motionY = 1.0;

        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);

            motionY = handle.getClass().getField("motY").getDouble(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return motionY;
    }

    public static double getMotionX(final PlayerData playerData) {
        final Player player = playerData.getPlayer();

        double motionX = 1.0;

        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);

            motionX = handle.getClass().getField("motX").getDouble(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return motionX;
    }

    public static double getMotionZ(final PlayerData playerData) {
        final Player player = playerData.getPlayer();

        double motionZ = 1.0;

        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);

            motionZ = handle.getClass().getField("motZ").getDouble(handle);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return motionZ;
    }
}