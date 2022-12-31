package rip.firefly.webapi.socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileLoader {
    ArrayList<String> hwids;

    public FileLoader(ArrayList<String> hwids) {
        this.hwids = hwids;
    }

    public synchronized void start() {
        hwids.clear();
        File file = new File("Whitelist.webdat");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (sc.hasNextLine()) {
            hwids.add(sc.nextLine());
        }
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        start();
    }
}
