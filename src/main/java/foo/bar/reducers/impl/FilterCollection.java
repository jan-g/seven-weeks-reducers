package foo.bar.reducers.impl;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by jang on 07/02/2017.
 */
public class FilterCollection<X> implements Reducible<X> {

    private Reducible<X> underlying;
    private Predicate<X> test;

    public FilterCollection(Predicate<X> test, Reducible<X> underlying) {
        this.test = test;
        this.underlying = underlying;
    }

    @Override
    public <A> A runReduction(ReducingOperation<A, X> op, A init) {
        // If the current element passes the filter, then we let the underlying
        // reducible handle it. Otherwise, we just skip it.
        return underlying.runReduction(reducingOperation(test, op), init);
    }

    /**
     * Construct a mapping ReducingOperation from another
     */
    public static <A, T> ReducingOperation<A, T> reducingOperation(Predicate<T> test, ReducingOperation<A, T> op) {
        return (acc, x) -> test.test(x) ? op.apply(acc, x) : acc;
    }

    /**
     * The 'mapping' of Clojure is a partial application of @reducingOperation
     */
    public static <A, T> Function<ReducingOperation<A, T>, ReducingOperation<A, T>> filtering(Predicate<T> test) {
        return (ReducingOperation<A, T> op) -> reducingOperation(test, op);
    }

    /**
     * Function to wrap the constructor
     */
    public static <T> FilterCollection<T> filter(Predicate<T> test, Reducible<T> items) {
        return new FilterCollection<>(test, items);
    }
}
