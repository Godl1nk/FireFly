package rip.firefly.webapi.handler;

import rip.firefly.webapi.util.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class TestHandler implements HttpHandler {
    public static String response = "";
    public static ArrayList<String> hwids = new ArrayList<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestParamValue=null;

        if("GET".equals(httpExchange.getRequestMethod())) {
            requestParamValue = handleGetRequest(httpExchange);
        }else if("POST".equals(httpExchange)) {
            requestParamValue = handlePostRequest(httpExchange);
        }

        handleResponse(httpExchange,requestParamValue);
    }

    private String handleGetRequest(HttpExchange httpExchange) {

        return RequestUtil.getValue("test", httpExchange);
    }

    private String handlePostRequest(HttpExchange httpExchange) {

            return RequestUtil.getValue("test", httpExchange);
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

       response = requestParamValue;

        // this line is a must
        httpExchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
