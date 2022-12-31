package rip.firefly.compat.wrapper.asm;

@FunctionalInterface
public interface TriFunction<R, T, U, V> {

    R apply(T t, U u, V v);
}