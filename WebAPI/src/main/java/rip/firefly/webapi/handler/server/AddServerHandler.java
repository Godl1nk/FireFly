package rip.firefly.webapi.handler.server;

import rip.firefly.webapi.manager.DataManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AddServerHandler implements HttpHandler {
    public static String response = "";
    public static ArrayList<String> hwids = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

        DataManager.addServer(1);
        response = "success";

        httpExchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
