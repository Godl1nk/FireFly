package rip.firefly.webapi.util;

/*
* A Simple Logger Framework For Java
* Free For Personal Use
* Made By: OmenDoesStuff
*/

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimpleLogger {

    String prefix;

    public SimpleLogger(String prefix) {
        this.prefix = "[" + prefix + "] ";
    }

    public void info(String s) {
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + formatter.format(date) + "] " + prefix + "[INFO] " + s);
    }

    public void warn(String s) {
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + formatter.format(date) + "] " + prefix + "[WARN] " + s);
    }

    public void err(String s) {
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.out.println("[" + formatter.format(date) + "] " + prefix + "[ERROR] " + s);
    }

    public void error(String s) {
        SimpleDateFormat formatter= new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        System.err.println("[" + formatter.format(date) + "] " + prefix + "[ERROR] " + s);
    }
}
