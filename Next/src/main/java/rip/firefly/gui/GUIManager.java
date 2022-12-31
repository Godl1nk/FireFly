package rip.firefly.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import rip.firefly.FireFly;
import rip.firefly.gui.impl.ActionsGUI;
import rip.firefly.gui.impl.ChecksGUI;
import rip.firefly.gui.impl.MainGUI;
import rip.firefly.gui.impl.TypeGUI;

import java.util.ArrayList;

public class GUIManager {
    private static ArrayList<GUI> guis = new ArrayList<>();

    static {
        guis.add(new MainGUI());
        guis.add(new ActionsGUI());
        guis.add(new TypeGUI());
        guis.add(new ChecksGUI());
    }

    public GUIManager() {
        for(Listener l : guis) {
            Bukkit.getPluginManager().registerEvents(l, FireFly.getInstance().getPlugin());
        }
    }
}
