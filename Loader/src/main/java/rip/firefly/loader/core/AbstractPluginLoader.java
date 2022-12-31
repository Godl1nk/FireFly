package rip.firefly.loader.core;

import rip.firefly.loader.jar.EncryptedJarFile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author AkramL
 * 21/06/2022 12:56 GMT
 */
public abstract class AbstractPluginLoader extends JavaPlugin {

    @Getter private ExternalLoader loader;
    @Getter @Setter
    private EncryptedJarFile jarFile;

    public void onEnable() {
        try (Socket socket = new Socket("127.0.0.1", 7096)) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            if (getHWID().equals("Error")) {
                getLogger().log(Level.SEVERE, "[FireFly] There Was An Issue Authenticating Your Server.");
                setEnabled(false);
                return;
            }
            outputStream.writeUTF(getHWID());
            String action = inputStream.readUTF();
            if (action.equals("false")) {
                getLogger().log(Level.SEVERE, "[FireFly] Invalid License!");
                getLogger().log(Level.SEVERE, "[FireFly] License: "+ getHWID());
                setEnabled(false);
                return;
            }
            String encryption = inputStream.readUTF();
            byte[] byteArray = readFrom(inputStream);
            inputStream.close();
            outputStream.close();
            setJarFile(new EncryptedJarFile(byteArray, encryption));
            this.loader = (ExternalLoader) jarFile.getClassLoader().loadClass("rip.firefly.Initializer", false)
                    .getConstructor(AbstractPluginLoader.class).newInstance(this);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private byte[] readFrom(DataInputStream inputStream) {
        List<Byte> bytes = new ArrayList<>();
        while (true) {
            try {
                byte b = inputStream.readByte();
                bytes.add(b);
            } catch (IOException exception) {
                break;
            }
        }
        byte[] byteArray = new byte[bytes.size()];
        int i = 0;
        for (Byte b : bytes) {
            byteArray[i++] = b;
        }
        return byteArray;
    }

    public abstract String getHWID();

    public void forceDisable() {
        setEnabled(false);
    }

    public void update() {
        loader.shutdown();
        try (Socket socket = new Socket("127.0.0.1", 7096)) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            if (getHWID().equals("Error")) {
                getLogger().log(Level.SEVERE, "[FireFly] There Was An Issue Authenticating Your Server.");
                setEnabled(false);
                return;
            }
            outputStream.writeUTF(getHWID());
            String action = inputStream.readUTF();
            if (action.equals("false")) {
                getLogger().log(Level.SEVERE, "[FireFly] Invalid License!");
                getLogger().log(Level.SEVERE, "[FireFly] License: "+ getHWID());
                setEnabled(false);
                return;
            }
            String encryption = inputStream.readUTF();
            byte[] byteArray = readFrom(inputStream);
            inputStream.close();
            outputStream.close();
            setJarFile(new EncryptedJarFile(byteArray, encryption));
//            this.loader = (ExternalLoader) jarFile.getClassLoader().loadClass("rip.firefly.Verifier", false)
//                    .getConstructor(AbstractPluginLoader.class).newInstance(this);

            this.loader = (ExternalLoader) jarFile.getClassLoader().loadClass("rip.firefly.Initializer", false)
                    .getConstructor(AbstractPluginLoader.class).newInstance(this);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        loader.load();
    }
}
