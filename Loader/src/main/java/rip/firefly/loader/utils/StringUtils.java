package rip.firefly.loader.utils;

import java.util.Random;

/**
 * @author AkramL
 * @since 1.0-BETA
 */
public class StringUtils {

    public static String generateRandomName() {
        String alpha = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        int size = random.nextInt(8) + 8;
        for(int i = 0; i < size; i++) {
            builder.append(alpha.charAt(random.nextInt(alpha.length())));
        }
        return builder.toString();
    }


}
