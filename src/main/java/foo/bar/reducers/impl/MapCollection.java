package foo.bar.reducers.impl;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;

import java.util.function.Function;

/**
 * Created by jasgrant on 07/02/2017.
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
        return underlying.runReduction((acc, from) -> op.apply(acc, mapf.apply(from)), init);
    }
}
