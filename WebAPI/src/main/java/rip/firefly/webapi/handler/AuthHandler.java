package rip.firefly.webapi.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class AuthHandler implements HttpHandler {
    public static String response = "";
    public static ArrayList<String> hwids = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;

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

        fileLoader.setName("Thread-FileLoader");
        fileLoader.start();

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }else if("POST".equals(httpExchange)) {
            requestParamValue = handlePostRequest(httpExchange);
        }

        handleResponse(httpExchange,requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {

        return getValue(httpExchange);
    }

    private String handlePostRequest(HttpExchange httpExchange) {

        return getValue(httpExchange);
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        if(requestParamValue == null || requestParamValue == "") {
            response = "invalid";
            httpExchange.sendResponseHeaders(400, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }

        if(hwids.contains(requestParamValue)) {
            response = "true";
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        } else {
            response = "false";
            httpExchange.sendResponseHeaders(200, response.length());
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }

      //  httpExchange.sendResponseHeaders(200, response.length());

    }

    public static String getValue(HttpExchange httpExchange) {
        return httpExchange.getRequestURI()
                .toString()
                .split( "\\?")[1]
                .split("=")[1];
    }
}
