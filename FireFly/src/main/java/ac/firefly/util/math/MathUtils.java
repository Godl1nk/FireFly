package ac.firefly.util.math;

import ac.firefly.util.interaction.Tuple2;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class  MathUtils {

    private static final double HITBOX_NORMAL = 0.4;
    private static final double HITBOX_DIAGONAL = Math.sqrt(Math.pow(HITBOX_NORMAL, 2) + Math.pow(HITBOX_NORMAL, 2));
    public static final double EXPANDER = Math.pow(2, 24);

    public static <T extends Number> T getMode(Collection<T> collect) {
        Map<T, Integer> repeated = new HashMap<>();

        //Sorting each value by how to repeat into a map.
        collect.forEach(val -> {
            int number = repeated.getOrDefault(val, 0);

            repeated.put(val, number + 1);
        });

        //Calculating the largest value to the key, which would be the mode.
        return repeated.keySet().stream()
                .map(key -> new Tuple2<>(key, repeated.get(key))) //We map it into a Tuple for easier sorting.
                .max(Comparator.comparing(Tuple2::b, Comparator.naturalOrder()))
                .orElseThrow(NullPointerException::new).a();
    }

    public static boolean elapsed(long from, long required) {
        return System.currentTimeMillis() - from > required;
    }

    public static long elapsed(long starttime) {
        return System.currentTimeMillis() - starttime;
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double offset(final double a, final double b)
    {
        return a > b ? (a - b) : (b - a);
    }

    public static long gcd1(long a, long b) {
        return b <= 0x4000 ? a : gcd(b, a % b);
    }

    @Deprecated
    public static long getGcdOld(long current, long previous) {
        return (double) previous <= 16384.0D ? current : (long) getGcd(previous, (long) Math.abs(current - previous));
    }

    public static long getGcd(long a, long b) {
        return b == 0.0D ? a : gcd(b, a % b);
    }

    public static double clamp180(double theta)
    {
        theta %= 360.0D;

        if (theta >= 180.0D)
        {
            theta -= 360.0D;
        }

        if (theta < -180.0D)
        {
            theta += 360.0D;
        }

        return theta;
    }

    public static long gcd(long a, long b) {
        while(b > 0L) {
            long temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static String decrypt(String strEncrypted) {
        String strData = "";

        try {
            byte[] decoded = Base64.getDecoder().decode(strEncrypted);
            strData = (new String(decoded, "UTF-8") + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strData;
    }

    public static double offset(final Vector one, final Vector two) {
        return one.subtract(two).length();
    }

    public static double getHorizontalDistance(Location to, Location from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }

    public static float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = (two.getY() + 2.0D - 0.4D) - (
                one.getY() + 2.0D);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D);

        return new float[]{yaw, pitch};
    }

    public static float getYawChangeToEntity(Player player, Entity entity, Location from, Location to) {
        double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double yawToEntity;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
            if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
                yawToEntity = -90.0D +
                        Math.toDegrees(Math.atan(deltaZ / deltaX));
            } else {
                yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
            }
        }
        return Float.valueOf((float) (-(getYawDifference(from, to)) - yawToEntity));
    }

    public static float getPitchChangeToEntity(Player player, Entity entity, Location from, Location to) {
        double deltaX = entity.getLocation().getX() - player.getLocation().getX();
        double deltaZ = entity.getLocation().getZ() - player.getLocation().getZ();
        double deltaY = player.getLocation().getY() - 1.6D + 2 - 0.4D -
                entity.getLocation().getY();
        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ *
                deltaZ);

        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));

        return yawTo180F((float) (-(getYawDifference(from, to) -
                (float) pitchToEntity)));
    }

    public static Vector getVerticalVector(final Vector v) {
        v.setX(0);
        v.setZ(0);
        return v;
    }

    public static double[] cursor(Player player, LivingEntity entity) {
        Location entityLoc = entity.getLocation().add(0.0, entity.getEyeHeight(), 0.0);
        Location playerLoc = player.getLocation().add(0.0, player.getEyeHeight(), 0.0);
        Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0.0f);
        Vector expectedRotation = getRotation(playerLoc, entityLoc);
        double deltaYaw = yawTo180D(playerRotation.getX() - expectedRotation.getX());
        double deltaPitch = yawTo180D(playerRotation.getY() - expectedRotation.getY());
        double horizontalDistance = getHorizontalDistance(playerLoc, entityLoc);
        double distance = getDistance3D(playerLoc, entityLoc);
        double offsetX = deltaYaw * horizontalDistance * distance;
        double offsetY = deltaPitch * Math.abs(Math.sqrt(entityLoc.getY() - playerLoc.getY())) * distance;
        return new double[]{Math.abs(offsetX), Math.abs(offsetY)};
    }

    public static double fixedXAxis(double x) {
        double touchedX = x;
        double rem = touchedX - Math.round(touchedX) + 0.01D;
        if (rem < 0.3D) {
            touchedX = NumberConversions.floor(x) - 1;
        }
        return touchedX;
    }

    public static Vector calculateAngle(Vector player, Vector otherPlayer) {
        Vector delta = new Vector((player.getX() - otherPlayer.getX()), (player.getY() - otherPlayer.getY()), (player.getZ() - otherPlayer.getZ()));
        double hyp = Math.sqrt(delta.getX() * delta.getX() + delta.getY() * delta.getY());

        Vector angle = new Vector(Math.atan((delta.getZ() + 64.06) / hyp) * 57.295779513082, Math.atan((delta.getY() / delta.getX())) * 57.295779513082, 0.0D);
        if (delta.getX() >= 0.0) {
            angle.setY(angle.getY() + 180.0);
        }
        return angle;
    }

    public static double getDistance3D(Location one, Location two) {
        double toReturn = 0.0;
        double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
        double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
        double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
        double sqrt = Math.sqrt(xSqr + ySqr + zSqr);
        toReturn = Math.abs(sqrt);
        return toReturn;
    }

    public static Vector getRotation(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(dy, distanceXZ) * 180.0 / 3.141592653589793);
        return new Vector(yaw, pitch, 0.0f);
    }

    public static float[] getAngles(Player player, Entity e, Location from, Location to) {
        return new float[]{(float) (getYawChangeToEntity(player, e, from, to) + getYawDifference(from, to)), (float) (getPitchChangeToEntity(player, e, from, to) + getYawDifference(from, to))};
    }

    public static double getYawDifference(Location one, Location two) {
        return Math.abs(one.getYaw() - two.getYaw());
    }

    public static double getVerticalDistance(Location to, Location from) {
        double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

        return Math.sqrt(y * y);
    }

    public static double getYDifference(Location to, Location from) {
        return Math.abs(to.getY() - from.getY());
    }

    public static Vector getHorizontalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setY(0);
        return vec;
    }

    public static Vector getVerticalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setX(0);
        vec.setZ(0);
        return vec;
    }

    public static float[] getRotation2(Location one, Location two) {
        double dx = two.getX() - one.getX();
        double dy = two.getY() - one.getY();
        double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
        dy /= distanceXZ;
        float yaw = (float) (-(Math.atan2(dx, dz) * 57.29577951308232));
        float pitch = (float) (-(Math.asin(dy) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    public static float yawTo180F(float flub) {
        flub %= 360.0F;
        if (flub >= 180.0F) {
            flub -= 360.0F;
        }
        if (flub < -180.0F) {
            flub += 360.0F;
        }
        return flub;
    }

    public static double yawTo180D(double dub) {
        dub %= 360.0D;
        if (dub >= 180.0D) {
            dub -= 360.0D;
        }
        if (dub < -180.0D) {
            dub += 360.0D;
        }
        return dub;
    }

    public static double[] getOffsetFromEntity(Player player, LivingEntity entity) {
        double yawOffset = Math.abs(MathUtils.yawTo180F(player.getEyeLocation().getYaw()) - MathUtils.yawTo180F(MathUtils.getRotations(player.getLocation(), entity.getLocation())[0]));
        double pitchOffset = Math.abs(Math.abs(player.getEyeLocation().getPitch()) - Math.abs(MathUtils.getRotations(player.getLocation(), entity.getLocation())[1]));
        return new double[]{yawOffset, pitchOffset};
    }

    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }




    private static Random rand = new Random();
    private static DecimalFormat decimalFormat = new DecimalFormat("0.000");
    public static Random random = new Random();

    public static long gcd(long limit, long a, long b) {
        return b <= limit ? a : MathUtils.gcd(limit, b, a % b);
    }

    public static int gcd(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    public static boolean getRandomBoolean() {
        return rand.nextBoolean();
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float distance = Math.abs(angle1 - angle2) % 360.0f;
        if (distance > 180.0f) {
            distance = 360.0f - distance;
        }
        return distance;
    }

    public static float getRandomFloat() {
        boolean negative = rand.nextBoolean();
        if (!negative) {
            return rand.nextFloat();
        }
        return -rand.nextFloat();
    }

    public static double distanceXZ(Location from, Location to) {
        double deltaX = from.getX() - to.getX();
        double deltaZ = from.getZ() - to.getZ();
        return Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
    }

    public static double distanceXZSquared(Location from, Location to) {
        double deltaX = from.getX() - to.getX();
        double deltaZ = from.getZ() - to.getZ();
        return deltaX * deltaX + deltaZ * deltaZ;
    }

    public static String getPrettyDecimal(double d) {
        return decimalFormat.format(d);
    }
    public static int floor(double var0) {
        int var2 = (int)var0;
        return var0 < (double)var2 ? var2 - 1 : var2;
    }

    public static int r(int i) {
        return random.nextInt(i);
    }

    public static double abs(double a) {
        return a <= 0.0 ? 0.0 - a : a;
    }

    public static String ArrayToString(String[] list) {
        String string = "";
        for (String key : list) {
            string = string + key + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String ArrayToString(List<String> list) {
        String string = "";
        for (String key : list) {
            string = string + key + ",";
        }
        if (string.length() != 0) {
            return string.substring(0, string.length() - 1);
        }
        return null;
    }

    public static String[] StringToArray(String string, String split) {
        return string.split(split);
    }

    public static double offset2d(Entity a, Entity b) {
        return MathUtils.offset2d(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset2d(Location a, Location b) {
        return MathUtils.offset2d(a.toVector(), b.toVector());
    }

    public static double offset2d(Vector a, Vector b) {
        a.setY(0);
        b.setY(0);
        return a.subtract(b).length();
    }

    public static double offset(Entity a, Entity b) {
        return MathUtils.offset(a.getLocation().toVector(), b.getLocation().toVector());
    }

    public static double offset(Location a, Location b) {
        return MathUtils.offset(a.toVector(), b.toVector());
    }

    public static double offsetSquared(Vector a, Vector b) {
        return a.subtract(b).lengthSquared();
    }

    public static String serializeLocation(Location location) {
        int X = (int)location.getX();
        int Y = (int)location.getY();
        int Z = (int)location.getZ();
        int P = (int)location.getPitch();
        int Yaw = (int)location.getYaw();
        return location.getWorld().getName() + "," + X + "," + Y + "," + Z + "," + P + "," + Yaw;
    }

    public static Location deserializeLocation(String string) {
        String[] parts = string.split(",");
        World world = Bukkit.getServer().getWorld(parts[0]);
        Double LX = Double.parseDouble(parts[1]);
        Double LY = Double.parseDouble(parts[2]);
        Double LZ = Double.parseDouble(parts[3]);
        Float P = Float.valueOf(Float.parseFloat(parts[4]));
        Float Y = Float.valueOf(Float.parseFloat(parts[5]));
        Location result = new Location(world, LX.doubleValue(), LY.doubleValue(), LZ.doubleValue());
        result.setPitch(P.floatValue());
        result.setYaw(Y.floatValue());
        return result;
    }

    public static float averageFloat(List<Float> list) {
        float avg = 0.0f;
        for (float value : list) {
            avg += value;
        }
        if (list.size() > 0) {
            return avg / (float)list.size();
        }
        return 0.0f;
    }

    public static long averageLong(List<Long> list) {
        long avg = 0L;
        for (long value : list) {
            avg += value;
        }
        if (list.size() > 0) {
            return avg / (long)list.size();
        }
        return 0L;
    }

    public static double averageDouble(List<Double> list) {
        Double add = 0.0;
        for (Double listlist : list) {
            add = add + listlist;
        }
        return add / (double)list.size();
    }

    public static double averageIDouble(List<Integer> list) {
        Double add = 0.0;
        for (Integer listlist : list) {
            add = add + (double)listlist.intValue();
        }
        return add / (double)list.size();
    }

    public static int getMode(int[] a) {
        int maxValue = 0;
        int maxCount = 0;
        for (int i = 0; i < a.length; ++i) {
            int count = 0;
            for (int j = 0; j < a.length; ++j) {
                if (a[j] != a[i]) continue;
                ++count;
            }
            if (count <= maxCount) continue;
            maxCount = count;
            maxValue = a[i];
        }
        return maxValue;
    }

    public static long getRange(List<Long> a) {
        int size = a.size();
        long first = a.get(0);
        long last = a.get(size - 1);
        return last - first;
    }

    public static double getDistanceBetweenAngles360(double alpha, double beta) {
        double abs = Math.abs(alpha % 360.0 - beta % 360.0);
        return Math.abs(Math.min(360.0 - abs, abs));
    }

    public static int pingFormula(long ping) {
        return (int)Math.ceil((double)ping / 50.0);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isBoolean(String str) {
        switch (str.toLowerCase()) {
            case "true":
            case "false": {
                return true;
            }
        }
        return false;
    }

    public static double getMagnitude(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
}
