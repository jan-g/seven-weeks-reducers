package foo.bar.reducers.impl;

import foo.bar.reducers.ReducingOperation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasgrant on 07/02/2017.
 */
public class Conj<X> implements ReducingOperation<List<X>, X> {
    public List<X> apply(List<X> acc, X item) {
        // XXX This makes the underlying ArrayList look immutable,
        // but it's unbelievably inefficient.
        ArrayList<X> result = new ArrayList<>(acc);
        result.add(item);
        return result;
    }
}
