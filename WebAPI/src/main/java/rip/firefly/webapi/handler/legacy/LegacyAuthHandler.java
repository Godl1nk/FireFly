package rip.firefly.webapi.handler.legacy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class LegacyAuthHandler implements HttpHandler {
    public static String response = "";
    public static ArrayList<String> hwids = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        Thread fileLoader = new Thread(() -> {
            hwids.clear();
            File file = new File("Whitelist.webdat");
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (sc.hasNextLine()) {
                hwids.add(sc.nextLine());
            }

            response = "";
            for(String s : hwids) {
                if(s.contains("//")) {
                    continue;
                }
                response = response + s + "\n";
            }
        });

        fileLoader.setName("Thread-Legacy");
        fileLoader.start();

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        httpExchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
