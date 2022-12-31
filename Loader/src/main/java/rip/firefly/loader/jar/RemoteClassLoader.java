package rip.firefly.loader.jar;

import rip.firefly.loader.core.ExternalLoader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * I used some codes from StackOverflow so yea,
 * but it should work fine.
 *
 * @author AkramL
 * @since 1.0-BETA
 */
public class RemoteClassLoader extends SecureClassLoader {

    private final byte[] jarBytes;
    private final Set<String> names;

    public RemoteClassLoader(byte[] jarBytes) throws IOException {
        super(ExternalLoader.class.getClassLoader());
        this.jarBytes = jarBytes;
        this.names = RemoteClassLoader.loadNames(jarBytes);
    }

    /**
     * This will put all the entries into a thread-safe Set
     */
    private static Set<String> loadNames(byte[] jarBytes) throws IOException {
        Set<String> set = new HashSet<>();
        try (ZipInputStream jis =
                     new ZipInputStream(new ByteArrayInputStream(jarBytes))) {
            ZipEntry entry;
            while ((entry = jis.getNextEntry()) != null) {
                set.add(entry.getName());
            }
        }
        return Collections.unmodifiableSet(set);
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        if (!names.contains(name)) {
            return null;
        }
        boolean found = false;
        ZipInputStream jis = null;
        try {
            jis = new ZipInputStream(new ByteArrayInputStream(jarBytes));
            ZipEntry entry;
            while ((entry = jis.getNextEntry()) != null) {
                if (entry.getName().equals(name)) {
                    found = true;
                    return jis;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jis != null && !found) {
                try {
                    jis.close();
                } catch (IOException e) {
                }
            }
        }
        return null;
    }

    @Override
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(name);
        if (clazz == null) {
            try {
                InputStream in = getResourceAsStream(name.replace('.', '/') + ".class");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                int i = 0;
                while ((i = in.read()) >= 0) {
                    out.write(i);
                }
                byte[] bytes = out.toByteArray();
                clazz = defineClass(name, bytes, 0, bytes.length);
                if (resolve) {
                    resolveClass(clazz);
                }
            } catch (Exception e) {
                clazz = super.loadClass(name, resolve);
            }
        }
        return clazz;
    }
}