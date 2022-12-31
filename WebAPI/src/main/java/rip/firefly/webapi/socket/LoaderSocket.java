package rip.firefly.webapi.socket;


import rip.firefly.webapi.ServerMain;
import rip.firefly.webapi.util.CryptoUtils;

import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;


public class LoaderSocket {
    ArrayList<String> hwids = new ArrayList<>();
    private final ServerSocket serverSocket;
    private final ExecutorService loaderPool = Executors.newCachedThreadPool();

    public LoaderSocket(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }


    private static Key getSecureRandomKey(String cipher, int keySize) {
        byte[] secureRandomKeyBytes = new byte[keySize / 8];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(secureRandomKeyBytes);
        return new SecretKeySpec(secureRandomKeyBytes, cipher);
    }


    public void start() {
        new Thread(() -> {

            new FileLoader(hwids).start();
        }).start();
        loaderPool.execute(() -> {
            while (!serverSocket.isClosed()) {
                try (Socket socket = this.serverSocket.accept()) {


                    ServerMain.logger.info("Loader \"" + socket.getInetAddress().getHostAddress() + "\" connected");
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                    String license = input.readUTF();
                    File file = new File("FF.jar"),
                            encryptedFile = new File("FireFly.jar");
                    if (!encryptedFile.exists())
                        encryptedFile.createNewFile();

                    int leftLimit = 48; // numeral '0'
                    int rightLimit = 122; // letter 'z'
                    int targetStringLength = 32;
                    Random random = new Random();

                    String s = random.ints(leftLimit, rightLimit + 1)
                            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                            .limit(targetStringLength)
                            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                            .toString();

//                    CryptoUtils.encrypt(ServerMain.getLoaderKey(), file, encryptedFile);
                    CryptoUtils.encrypt(s, file, encryptedFile);
                    if (!hwids.contains(license) || !file.exists()) {
                        ServerMain.logger.info("Loader \"" + socket.getInetAddress().getHostAddress() + "\" disconnected (Denied Access)");
                        output.writeUTF("false");
                        output.flush();
                        input.close();
                        output.close();
                        continue;
                    } else {
                        output.writeUTF("true");
                    }
                //    output.writeUTF(ServerMain.getLoaderKey());
                    output.writeUTF(s);


                    ServerMain.logger.info("Loader \"" + socket.getInetAddress().getHostAddress() + "\" authenticated, File Key: \"" + s + "\", Server HWID: \"" + license + "\"");
                    ByteFile byteFile = new ByteFile(encryptedFile);
                    output.write(byteFile.toByteArray());
                    ServerMain.logger.info("Loader \"" + socket.getInetAddress().getHostAddress() + "\" disconnected (Completed Request)");
                    encryptedFile.delete();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void shutdown() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loaderPool.shutdown();
    }

}
