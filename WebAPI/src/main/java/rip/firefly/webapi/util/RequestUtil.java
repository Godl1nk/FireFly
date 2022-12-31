package rip.firefly.webapi.util;

import com.sun.net.httpserver.HttpExchange;

import java.math.BigDecimal;

public class RequestUtil {
    public static String getValue(String param, HttpExchange httpExchange) {

        return httpExchange.getRequestURI()
                .toString()
                .split("\\?")[1]
                .split("=")[1];
    }
}
