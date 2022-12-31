package rip.firefly.util.math;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;
import rip.firefly.util.location.PlayerLocation;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;

public class MathUtil {
    public static final double EXPANDER = Math.pow(2, 24);
    public final DecimalFormat FORMATTER = new DecimalFormat("##.#");

    public static double hypot(final double x, final double z) {
        return Math.sqrt(x * x + z * z);
    }

    public static double offset(final Vector one, final Vector two) {
        return one.subtract(two).length();
    }

    public static Vector getHorizontalVector(final Vector v) {
        v.setY(0);
        return v;
    }

    public static Vector getHorizontalVector(Location loc) {
        Vector vec = loc.toVector();
        vec.setY(0);
        return vec;
    }

    public static float averageFloat(List<Float> list) {
        float avg = 0.0F;

        float value;
        for(Iterator var2 = list.iterator(); var2.hasNext(); avg += value) {
            value = (Float)var2.next();
        }

        return list.size() > 0 ? avg / (float)list.size() : 0.0F;
    }

    public static float getDistanceBetweenAngles(float yaw, float pitch) {
        return (float) distanceBetweenAngles(yaw, pitch);
    }

    public static double distanceBetweenAngles(float alpha, float beta) {
        float alphax = alpha % 360, betax = beta % 360;
        float delta = Math.abs(alphax - betax);
        return Math.abs(Math.min(360.0 - delta, delta));
    }

    public static double trim(int degree, double d) {
        String format = "#.#";
        for (int i = 1; i < degree; ++i) {
            format = String.valueOf(format) + "#";
        }
        DecimalFormat twoDForm = new DecimalFormat(format);
        return Double.valueOf(twoDForm.format(d).replaceAll(",", "."));
    }

    public static double magnitude(final double... points) {
        double sum = 0.0;

        for (final double point : points) {
            sum += point * point;
        }

        return Math.sqrt(sum);
    }

    public double except(double num, double min, double max) {
        return num > min ? min : Math.max(num, max);
    }

    public static double clamp(double val, double min, double max) {
        return Math.max(min, Math.min(max, val));
    }

    public boolean onGround(double position) {
        return position % (1.0 / 64.0) == 0.0;
    }

    public float yawTo180F(float flub) {
        if ((flub %= 360.0f) >= 180.0f) {
            flub -= 360.0f;
        }
        if (flub < -180.0f) {
            flub += 360.0f;
        }
        return flub;
    }

    public double getVariance(final Collection<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        double average;

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }

    public int getPingTicks(final long ping, final int extraTicks) {
        return NumberConversions.floor(ping / 50.0D) + extraTicks;
    }

    public double getStandardDeviation(final Collection<? extends Number> data) {
        final double variance = getVariance(data);

        return Math.sqrt(variance);
    }

    public double getSkewness(final Collection<? extends Number> data) {
        double sum = 0;
        int count = 0;

        final List<Double> numbers = Lists.newArrayList();

        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }

        Collections.sort(numbers);

        final double mean = sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double dev = getStandardDeviation(data);

        return (mean - median) / dev;
    }

    public static double getAverage(final Collection<? extends Number> data) {
        return data.stream().mapToDouble(Number::doubleValue).average().orElse(0D);
    }

    public double getAverage2(final List<Integer> data) {
        double average = 0d;

        for (int i : data) {
            average += i;
        }

        return average / data.size();
    }

    public double getKurtosis(final Collection<? extends Number> data) {
        double sum = 0.0;
        int count = 0;

        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        if (count < 3.0) {
            return 0.0;
        }

        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;

        double variance = 0.0;
        double varianceSquared = 0.0;

        for (final Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }

        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    public static int getMode(Collection<? extends Number> array) {
        int mode = (int) array.toArray()[0];
        int maxCount = 0;
        for (Number value : array) {
            int count = 1;
            for (Number i : array) {
                if (i.equals(value))
                    count++;
                if (count > maxCount) {
                    mode = (int) value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }

    public static double getModeD(Collection<? extends Double> array) {
        double mode = (double) array.toArray()[0];
        double maxCount = 0;
        for (Double value : array) {
            int count = 1;
            for (Double i : array) {
                if (i.equals(value))
                    count++;
                if (count > maxCount) {
                    mode =  value;
                    maxCount = count;
                }
            }
        }
        return mode;
    }

    private double getMedian(final List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }

    public boolean isExponentiallySmall(final Number number) {
        return number.doubleValue() < 1 && Double.toString(number.doubleValue()).contains("E");
    }

    public boolean isExponentiallyLarge(final Number number) {
        return number.doubleValue() > 10000 && Double.toString(number.doubleValue()).contains("E");
    }

    public static long getGcd(final long current, final long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }

    public static long gcd(long limit, long a, long b) {
        return b <= limit ? a : MathUtil.gcd(limit, b, a % b);
    }

    public static int gcd(int a, int b) {
        return BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
    }

    public static double getVerticalDistance(Location to, Location from) {
        double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

        return Math.sqrt(y * y);
    }

    public static double getVerticalDistance(PlayerLocation to, PlayerLocation from) {
        double y = Math.abs(Math.abs(to.getY()) - Math.abs(from.getY()));

        return Math.sqrt(y * y);
    }

    public static double gcd(final double current, final double previous) {
        long curr = (long) (current * EXPANDER);
        long prev = (long) (previous * EXPANDER);

        return getGcd(curr, prev) / EXPANDER;
    }

    public static double getHorizontalDistance(Location to, Location from) {
        double x = Math.abs(Math.abs(to.getX()) - Math.abs(from.getX()));
        double z = Math.abs(Math.abs(to.getZ()) - Math.abs(from.getZ()));

        return Math.sqrt(x * x + z * z);
    }

    public int getOutliers2(final Collection<? extends Number> samples) {
        final double avg = getAverage(samples);

        int outliers = 0;

        for (Number n : samples) {
            final double n2 = n.doubleValue();

            if (Math.abs(avg - n2) > 2) ++outliers;
        }

        return outliers;
    }

    public static double getGcd(final double a, final double b) {
        if (a < b) {
            return getGcd(b, a);
        }

        if (Math.abs(b) < 0.001) {
            return a;
        } else {
            return getGcd(b, a - Math.floor(a / b) * b);
        }
    }

    public double getCps(final List<Integer> data) {
        return 20 / getAverage2(data);
    }

    public int getDuplicates(final Collection<? extends Number> data) {
        return (int) (data.size() - data.stream().distinct().count());
    }

    public int getDistinct(final Collection<? extends Number> data) {
        return (int) data.stream().distinct().count();
    }

    public List<Float> skipValues(double count, double min, double max) {
        List<Float> floats = new ArrayList<>();
        for (float x = (float) min; x <= max; x += count) {
            floats.add(x);
        }
        return floats;
    }

    public double getFluctuation(double[] array) {
        double max = 0;
        double min = Double.MAX_VALUE;
        double sum = 0;

        for (double i : array) {
            sum += i;
            if (i > max) max = i;
            if (i < min) min = i;
        }

        double average = sum / array.length;
        // example: 75 - ((75 - 35) / 2) = 75 - (40 / 2) = 75 - 20 = 55
        double median = max - ((max - min) / 2);
        double range = max - min;
        return (average / 50) / (median / 50);
    }

    public double roundToPlace(double value, int places) {
        double multiplier = Math.pow(10, places);
        return Math.round(value * multiplier) / multiplier;
    }
}
