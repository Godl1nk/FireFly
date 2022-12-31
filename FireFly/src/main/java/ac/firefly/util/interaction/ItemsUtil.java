package ac.firefly.util.interaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class ItemsUtil {
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