package ac.firefly.tasks;

import ac.firefly.Firefly;
import ac.firefly.util.interaction.ItemsUtil;
import ac.firefly.util.math.SecMath;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.*;
import org.json.simple.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class DataTask extends BukkitRunnable {
    public static ArrayList<String> HWIDList = new ArrayList<>();

    int i = 0;

    @Override
    public void run() {
        try {
            URL auth = new URL("http://ffapi.dashpvp.net:4205/api/auth?hwid=" + getHWID());
            URLConnection conn = auth.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            conn.getInputStream()));
            String inputLine;
            ArrayList<String> array = new ArrayList<>();
            while ((inputLine = in.readLine()) != null)
                array.add(inputLine);
            in.close();

            boolean bool = false;
            for(String s : array) {
                if (s.toLowerCase().contains("true")) {
                    bool = true;
                }
            }
            if(bool != true) {
                Bukkit.getLogger().info("[mGuard] Invalid HWID!");
                Bukkit.getLogger().info("[mGuard] HWID: " + getHWID());
                Firefly.instance.getServer().getPluginManager().disablePlugin(Firefly.instance);
            }
        } catch (IOException exception) {
            Bukkit.getLogger().info("[mGuard] No Internet Access!");
            Firefly.instance.getServer().getPluginManager().disablePlugin(Firefly.instance);
        }

    }
   /*@Override
    public void run() {
        if(i <= 1) {
            try {
                runStartup();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        i++;

        try
        {
            //URL list = new URL("https://raw.githubusercontent.com/FireflyAC/Whitelist/main/Whitelist.txt");
            URL list = new URL("http://ff.dashpvp.net:8080");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(list.openStream()));
            String userLine;
            while ((userLine = bufferedReader.readLine()) != null)
            {
                HWIDList.add(userLine);
            }
        } catch(Exception e)
        {
            //e.printStackTrace();
            Bukkit.getLogger().info("[mGuard] No Internet Access!");
            Firefly.instance.getServer().getPluginManager().disablePlugin(Firefly.instance);
        }

        if(HWIDList.contains(getHWID())) {

        } else {
            Bukkit.getLogger().info("[mGuard] Invalid HWID!");
            Bukkit.getLogger().info("[mGuard] HWID: " + getHWID());
            Firefly.instance.getServer().getPluginManager().disablePlugin(Firefly.instance);
        }
    }*/
    // OLD AUTH STUFF //

    public static String getHWID() {
        String hwid = System.getenv("COMPUTERNAME") + System.getProperty("user.name") + System.getProperty("os.name") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL");
        return SecMath.hashMD5(hwid);
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

    public static void runStartup() {
        ItemsUtil.sendMessage(
                "~~----------------------------------------------------------------------~~" +
                        "\n**FireFly Has Been Started On A Server**" +
                        "**Version:** " + Firefly.version +
                        "\n**IP:** " + getIP() +
                        "\n**Port:** " + Bukkit.getServer().getPort() +
                        "\n**HWID:** " + getHWID() +
                        "\n~~----------------------------------------------------------------------~~", "https://discord.com/api/webhooks/927352129683865621/vg6MTaCYB0F0FhaAEEgfn7Txel-UqlDRrLjX6xfakHonfiradCnzk_PLacXO_KiOfiOn");
    }

}
