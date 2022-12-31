package ac.firefly.util.formatting;

public class RandomString {
    public enum Mode {
        ALPHA, NUMERIC, ALPHANUMERIC
    }

    public static String randomString(int length, Mode type) {
        StringBuilder bldr = new StringBuilder();
        String s = "";
        switch (type) {
            case ALPHA:
                s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
            case NUMERIC:
                s = "123456789";
            case ALPHANUMERIC:
                s = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";
        }
        for(int i =0; i<length; i++) {
            double index = Math.random() * s.length();
            bldr.append(s.charAt((int) index));
        }
        return bldr.toString();
    }
}
