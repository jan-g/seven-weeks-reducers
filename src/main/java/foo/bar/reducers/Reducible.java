package foo.bar.reducers;

/**
 * A Reducible is a collection that knows how to reduce itself,
 * given a reduction operation and an initial value.
 * Created by jasgrant on 07/02/2017.
 */
public interface Reducible<X> {
    <A> A runReduction(ReducingOperation<A, X> op, A init);
}
