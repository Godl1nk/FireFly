package ac.firefly.util.etc;

import ac.firefly.Firefly;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class APIUtil {
    public static void startup() {
        new BukkitRunnable(){
            @Override
            public void run() {
                try {
                    URL auth = new URL("http://ffapi.dashpvp.net:4205/api/server/startup");
                    URLConnection conn = auth.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    String inputLine;
                    ArrayList<String> array = new ArrayList<>();
                    while ((inputLine = in.readLine()) != null)
                        array.add(inputLine);
                    in.close();
                } catch (IOException exception) {

                }

            }
        }.runTaskAsynchronously(Firefly.instance);
    }

    public static void shutdown() {
        // new BukkitRunnable(){
          //  @Override
            //public void run() {
                try {
                    URL auth = new URL("http://ffapi.dashpvp.net:4205/api/server/shutdown");
                    URLConnection conn = auth.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    String inputLine;
                    ArrayList<String> array = new ArrayList<>();
                    while ((inputLine = in.readLine()) != null)
                        array.add(inputLine);
                    in.close();
                } catch (IOException exception) {

                }

            //}
        //}.runTaskAsynchronously(Firefly.instance);
    }

    public static void submitTotalVL(int vl) {
        //new BukkitRunnable(){
          //  @Override
            //public void run() {
                try {
                    URL auth = new URL("http://ffapi.dashpvp.net:4205/api/vl/addvl?vl=" + vl);
                    URLConnection conn = auth.openConnection();
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    String inputLine;
                    ArrayList<String> array = new ArrayList<>();
                    while ((inputLine = in.readLine()) != null)
                        array.add(inputLine);
                    in.close();
                } catch (IOException exception) {

                }

            //}
        //}.runTaskAsynchronously(Firefly.instance);
    }
}
