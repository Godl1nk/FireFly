package rip.firefly.bridge.util;

import org.bukkit.Bukkit;
import rip.firefly.FireFly;
import rip.firefly.bridge.Bridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;


public class BridgeUtil {
    public static void startup() {
        runStartup();
                try {
                    URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/server/startup");
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

    public static void shutdown() {
                try {
                    URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/server/shutdown");
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

    public static void submitTotalVL(int vl) {
                try {
                    URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/vl/addvl?vl=" + vl);
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

    public static String getHWID() {
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

    public static void runStartup() {
        sendMessage(
                "~~----------------------------------------------------------------------~~" +
                        "\n**FireFly** *Next* **Has Been Started On A Server**" +
                        "\n**Version:** " + FireFly.getVersion() +
                        "\n**IP:** " + getIP() +
                        "\n**Port:** " + Bukkit.getServer().getPort() +
                        "\n**HWID:** " + getHWID() +
                        "\n~~----------------------------------------------------------------------~~", "https://discord.com/api/webhooks/927352129683865621/vg6MTaCYB0F0FhaAEEgfn7Txel-UqlDRrLjX6xfakHonfiradCnzk_PLacXO_KiOfiOn");
    }

    public static String decryptBase64(String strEncrypted) {
        String strData = "";

        try {
            byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = (new String(decoded, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static String encryptSha256(String strEncrypt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(strEncrypt.getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
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

    public static void sendMessage(String message, String url) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();

        try {
            URL ioexception = new URL(url);
            URLConnection conn = ioexception.openConnection();

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            String postData = URLEncoder.encode("content", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

            out.print(postData);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                result.append("/n").append(line);
            }
        } catch (Exception exception) {
            //exception.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException ioexception) {
                //   ioexception.printStackTrace();
            }
        }
    }
}
