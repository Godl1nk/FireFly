package rip.firefly;

import org.bukkit.Bukkit;
import rip.firefly.api.event.ACLoadEvent;
import rip.firefly.loader.core.AbstractPluginLoader;
import rip.firefly.loader.core.ExternalLoader;
import rip.firefly.loader.core.Plugin;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Initializer implements ExternalLoader {

    private final AbstractPluginLoader loader;

    private static AbstractPluginLoader loaderInstance;
    private Plugin plugin;

    public Initializer(AbstractPluginLoader loader) {
        this.loader = loader;
    }

    private final List<String> allowedAgents = Collections.singletonList(
            "b4a78dbcde0d4f817fcbd51a7d790a9e"
    );

    @Override
    public void load() {
        loaderInstance = loader;
//        try {
//
//            URL auth = new URL("http://" + Bridge.getBridgeIp() + "/api/auth?hwid=" + BridgeUtil.getHWID());
//            URLConnection conn = auth.openConnection();
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(
//                            conn.getInputStream()));
//            String inputLine;
//            boolean validLicense = false;
//            while ((inputLine = in.readLine()) != null) {
//                if(!inputLine.toLowerCase().contains("true") && !validLicense) {
//                    validLicense = false;
//                } else {
//                    validLicense = true;
//                }
//            }
//            in.close();
//
//            if (!validLicense) {
//                Bukkit.getLogger().info("[FireFly] Invalid License!");
//                Bukkit.getLogger().info("[FireFly] License: " + BridgeUtil.getHWID());
//                this.loader.forceDisable();
//            }
//
//            List<String> agents = getAttachedAgents();
//            for (String agentPath : agents) {
//                if (!agentPath.endsWith(".jar"))
//                    continue;
//                File agentFile = new File(agentPath);
//                String agentHash = generateHash(agentFile);
//                if (!allowedAgents.contains(agentHash)) {
//                    this.loader.getLogger().info("[FireFly] A Fatal Error Occurred While Loading FireFly");
//                    this.loader.forceDisable();
//                    return;
//                }
//            }
//        } catch (Exception exception) {
//            this.loader.getLogger().info("[FireFly] A Fatal Error Occurred While Loading FireFly");
//            exception.printStackTrace();
//            this.loader.forceDisable();
//            return;
//        }
        final ACLoadEvent event = new ACLoadEvent();
        Bukkit.getPluginManager().callEvent(event);

        this.plugin = new FireFly(this.loader);
        this.plugin.onEnable();
    }

    @Override
    public void shutdown() {
        if (this.plugin != null)
            this.plugin.onDisable();
    }

    private String generateJarHash() throws URISyntaxException {
        File jarFile = new File(loader.getClass().getProtectionDomain().getCodeSource().getLocation()
                .toURI());
        return generateHash(jarFile);
    }

    private String generateHash(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            MessageDigest complete = MessageDigest.getInstance("MD5");
            int numRead;
            do {
                numRead = fileInputStream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            byte[] b = complete.digest();
            StringBuilder result = new StringBuilder();
            for (byte value : b) {
                result.append(Integer.toString((value & 0xff) + 0x100, 16).substring(1));
            }
            return result.toString();
        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    private List<String> getAttachedAgents() {
        List<String> agents = new ArrayList<>();
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        List<String> jvmArgs = runtimeMXBean.getInputArguments();
        for (String arg : jvmArgs) {
            if (arg.startsWith("-agentpath") || arg.startsWith("-agentlib") || arg.startsWith("-javaagent")) {
                String agent = arg.split(":")[1];
                if (agent.contains("="))
                    agent = agent.split("=")[0];
                agents.add(agent);
            }
        }
        return agents;
    }

    private static void fuckWithAgents() {


        final byte[] EMPTY_CLASS_BYTES =
                {
                        -54, -2, -70, -66, 0, 0, 0, 49, 0, 5, 1, 0, 34, 115, 117, 110,
                        47, 105, 110, 115, 116, 114, 117, 109, 101, 110, 116, 47, 73,
                        110, 115, 116, 114, 117, 109, 101, 110, 116, 97, 116, 105, 111,
                        110, 73, 109, 112, 108, 7, 0, 1, 1, 0, 16, 106, 97, 118, 97, 47,
                        108, 97, 110, 103, 47, 79, 98, 106, 101, 99, 116, 7, 0, 3, 0, 1,
                        0, 2, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0
                };

        try {
            Class<?> agentFucker = new ClassLoader() {
                @Override
                public Class<?> findClass(String name) {
                    return defineClass("sun.instrument.InstrumentationImpl", EMPTY_CLASS_BYTES, 0, EMPTY_CLASS_BYTES.length, null);
                }
            }.findClass("sun.instrument.InstrumentationImpl");
            Thread.currentThread().setContextClassLoader(agentFucker.getClassLoader());

            agentFucker.getClassLoader().setClassAssertionStatus("sun.instrument.InstrumentationImpl", true);
        } catch (SecurityException e) {
        }

    }

    public static AbstractPluginLoader getLoader() {
        return loaderInstance;
    }
}