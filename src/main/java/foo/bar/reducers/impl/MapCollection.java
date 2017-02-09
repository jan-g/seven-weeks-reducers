package foo.bar.reducers.impl;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;

import java.util.function.Function;

/**
 * Created by jang on 07/02/2017.
 */
public class MapCollection<From, To> implements Reducible<To> {
    private Reducible<From> underlying;
    private Function<From, To> mapf;

    public MapCollection(Function<From, To> mapf, Reducible<From> underlying) {
        this.mapf = mapf;
        this.underlying = underlying;
    }

    @Override
    public <A> A runReduction(ReducingOperation<A, To> op, A init) {
        return underlying.runReduction(reducingOperation(mapf, op), init);
    }

    /**
     * Construct a mapping ReducingOperation from another
     */
    public static <A, F, T> ReducingOperation<A, F> reducingOperation(Function<F, T> mapf, ReducingOperation<A, T> op) {
        return (acc, from) -> op.apply(acc, mapf.apply(from));
    }

    /**
     * The 'mapping' of Clojure is a partial application of @reducingOperation
     */
    public static <A, F, T> Function<ReducingOperation<A, T>, ReducingOperation<A, F>> mapping(Function<F, T> mapf) {
        return (ReducingOperation<A, T> op) -> reducingOperation(mapf, op);
    }

    /**
     * Function to wrap the constructor
     */
    public static <F, T> MapCollection<F, T> map(Function<F, T> mapf, Reducible<F> items) {
        return new MapCollection<>(mapf, items);
    }
}
