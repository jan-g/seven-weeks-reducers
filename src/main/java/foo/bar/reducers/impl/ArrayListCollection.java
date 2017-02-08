package foo.bar.reducers.impl;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasgrant on 07/02/2017.
 */
public class ArrayListCollection<X> extends ArrayList<X> implements Reducible<X> {

    public ArrayListCollection(List<X> items) {
        super(items);
    }

    @Override
    public <A> A runReduction(ReducingOperation<A, X> op, A init) {
        A value = init;
        for (X item: this)
            value = op.apply(value, item);
        return value;
    }
}
