package rip.firefly.webapi.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class FileUtil {
    public static ArrayList<String> getWhitelistedHwids() {
        ArrayList<String> hwidss = new ArrayList<>();
        ArrayList<String> hwids = new ArrayList<>();
        new Thread(() -> {
            hwidss.clear();
            File file = new File("Whitelist.webdat");
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            while (sc.hasNextLine()) {
                hwidss.add(sc.nextLine());
            }

            for(String s : hwidss) {
                if(s.contains("//") || !isValidMD5(s)) {
                    continue;
                }
                hwids.add(s);
            }
        }).start();
        return hwids;
    }

    public static boolean isValidMD5(String s) {
        return s.matches("^[a-fA-F0-9]{32}$");
    }

    public static void writeLine(String s) {
        try {
            Files.write(new File("Whitelist.webdat").toPath(), (s + "\n").getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeLine(String lineContent)
    {
        File file = new File("Whitelist.webdat");
        List<String> out = null;
        try {
            out = Files.lines(file.toPath())
                    .filter(line -> !line.contains(lineContent))
                    .collect(Collectors.toList());
            Files.write(file.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
