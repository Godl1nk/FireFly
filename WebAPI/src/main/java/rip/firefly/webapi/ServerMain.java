package rip.firefly.webapi;

import rip.firefly.webapi.bot.DiscordBot;
import rip.firefly.webapi.handler.AuthHandler;
import rip.firefly.webapi.handler.BaseHandler;
import rip.firefly.webapi.handler.server.AddServerHandler;
import rip.firefly.webapi.handler.server.GetServersHandler;
import rip.firefly.webapi.handler.server.RemoveServerHandler;
import rip.firefly.webapi.handler.vl.AddVlHandler;
import rip.firefly.webapi.handler.vl.GetVlHandler;
import rip.firefly.webapi.socket.LoaderSocket;
import rip.firefly.webapi.util.SimpleLogger;
import com.sun.net.httpserver.HttpServer;
import lombok.Getter;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class ServerMain {

    private static int loaderPort = 7096;

    private static int webPort = 4205;
    public static SimpleLogger logger = new SimpleLogger("Server");
    @Getter
    private static String loaderKey = "BaUbmLxUaNcOwPcU";

    private static Key getSecureRandomKey(String cipher, int keySize) {

        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, cipher);
    }

    public static void main(String[] args) throws Exception {
//        String r = new String(getSecureRandomKey("AES", 256).getEncoded());
//        System.out.println(r);

        LoaderSocket socket = new LoaderSocket(loaderPort);
        HttpServer server = HttpServer.create(new InetSocketAddress(webPort), 0);

        server.createContext("/api/auth").setHandler(new AuthHandler());
        server.createContext("/api/vl/addvl").setHandler(new AddVlHandler());
        server.createContext("/api/vl/getvl").setHandler(new GetVlHandler());
        server.createContext("/api/server/shutdown").setHandler(new RemoveServerHandler());
        server.createContext("/api/server/startup").setHandler(new AddServerHandler());
        server.createContext("/api/server/getservers").setHandler(new GetServersHandler());
        server.createContext("/").setHandler(new BaseHandler());

        server.start();
        socket.start();
//        DiscordBot.startBot();
        logger.info("Server Started");

//        throw new StackOverflowError("go to stackoverflow.com to make your code work");
    }


}