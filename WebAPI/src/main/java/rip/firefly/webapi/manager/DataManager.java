package rip.firefly.webapi.manager;

import rip.firefly.webapi.user.User;

import java.util.ArrayList;

public class DataManager {
    private static int vl = 0;
    private static int servers = 0;
    private static ArrayList<User> bannedUsers = new ArrayList<>();

    public static int getVl() {
        return vl;
    }

    public static void addVl(int vlToAdd) {
        vl += vlToAdd;
    }

    public static int getServers() {
        return servers;
    }

    public static void addServer(int add) {
        servers += add;
    }

    public static void removeServer(int remove) {
        servers -= remove;
    }

    public static ArrayList<User> getBannedUsers() {
        return bannedUsers;
    }

    public static void addPlayer(User user) {
        new Thread(() -> {
            for (User u : getBannedUsers()) {
                if(u.uuid == user.uuid) {
                    return;
                } else {
                    continue;
                }
            }
        }).start();

        getBannedUsers().add(user);
    }
}
