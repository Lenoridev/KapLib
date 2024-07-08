package net.kapitencraft.kap_lib.stream;

import java.util.function.BiFunction;

/**
 * {@link java.util.function.UnaryOperator} for 2 inputs
 */
@FunctionalInterface
public interface UnaryBiOperator<T> extends BiFunction<T, T, T> {
}
