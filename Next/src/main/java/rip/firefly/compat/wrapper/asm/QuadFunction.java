package rip.firefly.compat.wrapper.asm;

@FunctionalInterface
public interface QuadFunction<R, T, U, V, G>{

    R apply(T t, U u, V v, G g);
}