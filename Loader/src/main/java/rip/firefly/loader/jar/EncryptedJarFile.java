package rip.firefly.loader.jar;

import rip.firefly.loader.utils.CryptoUtils;
import rip.firefly.loader.utils.StringUtils;
import lombok.Getter;

import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author AkramL
 * 21/06/2022 13:16 GMT
 */
public class EncryptedJarFile {

    private File file, decryptedFile;
    @Getter private final Map<String, Class<?>> classMap = new HashMap<>();
    @Getter private RemoteClassLoader classLoader;

    public EncryptedJarFile(byte[] bytes, String key) {
        try {
            file = Files.createTempFile(StringUtils.generateRandomName(), "").toFile();
            try (OutputStream outputStream = Files.newOutputStream(file.toPath())) {
                outputStream.write(bytes);
            }
            decryptedFile = Files.createTempFile(StringUtils.generateRandomName(), "").toFile();
            CryptoUtils.decrypt(key, file, decryptedFile);
            file.delete();
            /*
             * Loading YAML configurations
             */
            try (JarFile jarFile = new JarFile(decryptedFile.getPath())) {
                Enumeration<JarEntry> e = jarFile.entries();
                URL[] urls = { new URL("jar:file:" + decryptedFile.getPath() +"!/") };
                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();
                    if(je.isDirectory()) {
                        continue;
                    }
                    if (je.getName().endsWith(".yml")) {
                        File ymlFile = new File(je.getName());
                        if (!ymlFile.exists())
                            Files.copy(jarFile.getInputStream(je), ymlFile.toPath());
                    }
                }
            }
            ByteFile byteFile = new ByteFile(decryptedFile);
            byte[] array = byteFile.toByteArray();
            decryptedFile.delete();
            this.classLoader = new RemoteClassLoader(array);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (file != null)
            file.delete();
        if (decryptedFile != null)
            decryptedFile.deleteOnExit();
    }

}
