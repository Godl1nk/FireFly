package rip.firefly.webapi.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class BaseHandler implements HttpHandler {
    public static String response = "";

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        OutputStream outputStream = httpExchange.getResponseBody();

        response = "          _____                    _____                    _____                    _____                    _____                    _____          \n" +
                "         /\\    \\                  /\\    \\                  /\\    \\                  /\\    \\                  /\\    \\                  /\\    \\         \n" +
                "        /::\\____\\                /::\\    \\                /::\\    \\                /::\\    \\                /::\\    \\                /::\\    \\        \n" +
                "       /:::/    /               /::::\\    \\              /::::\\    \\              /::::\\    \\              /::::\\    \\               \\:::\\    \\       \n" +
                "      /:::/   _/___            /::::::\\    \\            /::::::\\    \\            /::::::\\    \\            /::::::\\    \\               \\:::\\    \\      \n" +
                "     /:::/   /\\    \\          /:::/\\:::\\    \\          /:::/\\:::\\    \\          /:::/\\:::\\    \\          /:::/\\:::\\    \\               \\:::\\    \\     \n" +
                "    /:::/   /::\\____\\        /:::/__\\:::\\    \\        /:::/__\\:::\\    \\        /:::/__\\:::\\    \\        /:::/__\\:::\\    \\               \\:::\\    \\    \n" +
                "   /:::/   /:::/    /       /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\      /::::\\   \\:::\\    \\              /::::\\    \\   \n" +
                "  /:::/   /:::/   _/___    /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\    /::::::\\   \\:::\\    \\    ____    /::::::\\    \\  \n" +
                " /:::/___/:::/   /\\    \\  /:::/\\:::\\   \\:::\\    \\  /:::/\\:::\\   \\:::\\ ___\\  /:::/\\:::\\   \\:::\\    \\  /:::/\\:::\\   \\:::\\____\\  /\\   \\  /:::/\\:::\\    \\ \n" +
                "|:::|   /:::/   /::\\____\\/:::/__\\:::\\   \\:::\\____\\/:::/__\\:::\\   \\:::|    |/:::/  \\:::\\   \\:::\\____\\/:::/  \\:::\\   \\:::|    |/::\\   \\/:::/  \\:::\\____\\\n" +
                "|:::|__/:::/   /:::/    /\\:::\\   \\:::\\   \\::/    /\\:::\\   \\:::\\  /:::|____|\\::/    \\:::\\  /:::/    /\\::/    \\:::\\  /:::|____|\\:::\\  /:::/    \\::/    /\n" +
                " \\:::\\/:::/   /:::/    /  \\:::\\   \\:::\\   \\/____/  \\:::\\   \\:::\\/:::/    /  \\/____/ \\:::\\/:::/    /  \\/_____/\\:::\\/:::/    /  \\:::\\/:::/    / \\/____/ \n" +
                "  \\::::::/   /:::/    /    \\:::\\   \\:::\\    \\       \\:::\\   \\::::::/    /            \\::::::/    /            \\::::::/    /    \\::::::/    /          \n" +
                "   \\::::/___/:::/    /      \\:::\\   \\:::\\____\\       \\:::\\   \\::::/    /              \\::::/    /              \\::::/    /      \\::::/____/           \n" +
                "    \\:::\\__/:::/    /        \\:::\\   \\::/    /        \\:::\\  /:::/    /               /:::/    /                \\::/____/        \\:::\\    \\           \n" +
                "     \\::::::::/    /          \\:::\\   \\/____/          \\:::\\/:::/    /               /:::/    /                                   \\:::\\    \\          \n" +
                "      \\::::::/    /            \\:::\\    \\               \\::::::/    /               /:::/    /                                     \\:::\\    \\         \n" +
                "       \\::::/    /              \\:::\\____\\               \\::::/    /               /:::/    /                                       \\:::\\____\\        \n" +
                "        \\::/____/                \\::/    /                \\::/____/                \\::/    /                                         \\::/    /        \n" +
                "                                  \\/____/                                           \\/____/                                           \\/____/         \n" +
                "                                                                                                                                                      \n" +
                "You Found Me! :D\n" +
                "\n" +
                "web:      https://firefly.rip\n" +
                "discord:  https://discord.gg/tns6abuHUs\n"+
                "apiver:   v3.21 (\"Not FoxAC\" Edition)";

        httpExchange.sendResponseHeaders(200, response.length());
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}