package rip.firefly.webapi.handler.vl;

import rip.firefly.webapi.manager.DataManager;
import rip.firefly.webapi.util.RequestUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class AddVlHandler implements HttpHandler {
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

        /*return httpExchange.getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];*/
        return RequestUtil.getValue("vl", httpExchange);
    }

    private String handlePostRequest(HttpExchange httpExchange) {

        /*return httpExchange.getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];*/
        return RequestUtil.getValue("vl", httpExchange);
    }

    private void handleResponse(HttpExchange httpExchange, String requestParamValue)  throws  IOException {
        OutputStream outputStream = httpExchange.getResponseBody();
        StringBuilder htmlBuilder = new StringBuilder();

       if(isInteger(requestParamValue)) {
           if(Integer.parseInt(requestParamValue) >= 200000) {
               response = "false";
               httpExchange.sendResponseHeaders(400, response.length());
           } else {
               DataManager.addVl(Integer.parseInt(requestParamValue));
               response = "true";
               httpExchange.sendResponseHeaders(200, response.length());
           }
       } else {
           response = "false";
           httpExchange.sendResponseHeaders(400, response.length());
       }

        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '-') {
                return false;
            }
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
