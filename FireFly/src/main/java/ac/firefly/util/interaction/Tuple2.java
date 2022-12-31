package ac.firefly.util.interaction;

public final class Tuple2<A, B>
{
    private A a;
    private B b;

    public Tuple2(A aIn, B bIn)
    {
        this.a = aIn;
        this.b = bIn;
    }

    /**
     * Get the first Object in the Tuple
     */
    public A a() {
        return this.a;
    }

    /**
     * Get the second Object in the Tuple
     */
    public B b()
    {
        return this.b;
    }
}