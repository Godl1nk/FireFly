package rip.firefly.webapi.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbedUtil {
    private static final EmbedBuilder eb = new EmbedBuilder();
    private static final Color infoColor = new Color(245, 165, 90);
    private static final Color successColor = new Color(116, 250, 44);
    private static final Color errorColor = new Color(250, 44, 44);

    public static MessageEmbed info(String Title, String Content) {
        eb.clear();
        eb.setColor(infoColor);
        eb.addField(Title, Content, false);
        eb.build();
        return eb.build();
    }

    public static MessageEmbed error(String Title, String Content) {
        eb.clear();
        eb.setColor(errorColor);
        eb.addField(Title, Content, false);
        eb.build();
        return eb.build();
    }

    public static MessageEmbed success(String Title, String Content) {
        eb.clear();
        eb.setColor(successColor);
        eb.addField(Title, Content, false);
        eb.build();
        return eb.build();
    }
}