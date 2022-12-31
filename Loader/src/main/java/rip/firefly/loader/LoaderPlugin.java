package rip.firefly.loader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import rip.firefly.loader.core.AbstractPluginLoader;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.security.MessageDigest;

/**
 * @author AkramL
 * 21/06/2022 12:53 GMT
 */
public class LoaderPlugin extends AbstractPluginLoader {
    @Override
    public void onEnable() {
        if (!isEnabled())
            return;
        super.onEnable();
        if (getLoader() != null)
            getLoader().load();
    }

    @Override
    public void onDisable() {
        if (getLoader() != null)
            getLoader().shutdown();
    }

    @Override
    public String getHWID() {
        String hwid = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getProperty("os.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
        return hashMD5(hwid);
    }

    public static String getIP() {
        try {
            URL ip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    ip.openStream()));
            return bufferedReader.readLine();
        } catch (Exception e) {
            //e.printStackTrace();
            return "Unknown";
        }
    }

    public static String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

}
