package foo.bar.reducers;

import java.util.function.BiFunction;

/**
 * Created by jasgrant on 07/02/2017.
 */
public interface ReducingOperation<A, X> extends BiFunction<A, X, A> {}