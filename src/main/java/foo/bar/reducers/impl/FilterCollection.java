package foo.bar.reducers.impl;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;

import java.util.function.Predicate;

/**
 * Created by jasgrant on 07/02/2017.
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
        return underlying.runReduction((acc, x) -> test.test(x) ? op.apply(acc, x) : acc, init);
    }
}
