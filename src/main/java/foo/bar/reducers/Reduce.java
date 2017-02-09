package foo.bar.reducers;

/**
 * Created by jang on 09/02/2017.
 */
public class Reduce {
    public static <A, X> A reduce(ReducingOperation<A, X> op, A init, Reducible<X> collection) {
        return collection.runReduction(op, init);
    }
}
