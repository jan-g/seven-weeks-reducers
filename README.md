# 7 models, 7 weeks: the "collection drives the reduction", in java

Having gone away and implemented this from scratch in Java (also in SML, not shown) I think the
book doesn't start from the right point in its explanation. The critical thing is that (although
Clojure's protocols supply a default implementation) the collection is responsible for reducing
itself.

 
```java
package foo.bar.reducers;

/**
 * A Reducible is a collection that knows how to reduce itself,
 * given a reduction operation and an initial value.
 * Created by jang on 2017-02-06.
 */
public interface Reducible<X> {
    <A> A runReduction(ReducingOperation<A, X> op, A init);
}
```

And the ReducingOperator takes an accumulator and an item, returning a new accumulator:
 
```java
public interface ReducingOperation<A, X> extends BiFunction<A, X, A> {}
 
We might as well start with a simple implementation of a collection.
 
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
```
 
Here's an example use:
 
```java
public class Main {
    public static void main(String[] args) {
        System.out.println("Making a collection");
        ArrayListCollection<Integer> items = new ArrayListCollection<Integer>(Arrays.asList(1, 2, 3, 4, 5));

        System.out.println("reduce + 0 items = " + items.runReduction((a, x) -> a + x, 0));

        Function<Integer, Integer> inc = x -> x + 1;
        MapCollection<Integer, Integer> myMap = new MapCollection<>(inc, items);

        System.out.println("reduce + (map inc items) = " + myMap.runReduction((a, x) -> a + x, 0));
```
 
The map implementation is a Reducible too:
 
```java
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
```
 
The other idea here (that is lost in the OO syntax a bit) is that when a MapCollection is asked
to reduce itself with a given ReducingOperation, it first constructs a new ReducingOperation based
on the original, and it's that which it passes along to its underlying collection. The definition
of the new ReducingOperation encapsulates the essential nature of a map (or arguable, of the
relationship between map and reduction).

More of the main function - this time with Into - spelt out in terms of Conj:

```java
 
        // Try Into.
        System.out.println("The Reducible as a list is: " +
        items.runReduction(new Conj<Integer>(), new ArrayList<Integer>()));
 
and its (appalling) implementation:
 
public class Conj<X> implements ReducingOperation<List<X>, X> {
    public List<X> apply(List<X> acc, X item) {
        // XXX This makes the underlying ArrayList look immutable,
        // but it's unbelievably inefficient.
        ArrayList<X> result = new ArrayList<>(acc); // ahem
        result.add(item);
        return result;
    }
}
```
 
And to round it off, a filter implementation:
 
```java
        // Try a filter.
        System.out.println("reduce conj [] (filter isEven items) = " +
                new FilterCollection<Integer>((x) -> x % 2 == 0, items)
                .runReduction(new Into<Integer>(), new ArrayList<Integer>()));
```
 
Here's the filter collection:
 
```java
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
```
 
Finally, the example class just combines a few of those:
 
```java
        System.out.println("reduce conj [] (map inc (filter isEven items)) = " +
                new MapCollection<Integer, Integer>(inc,
                        new FilterCollection<Integer>((x) -> x % 2 == 0, items))
                        .runReduction(new Conj<Integer>(), new ArrayList<Integer>()));

        System.out.println("reduce conj [] (filter isEven (map inc items)) = " +
                new FilterCollection<Integer>((x) -> x % 2 == 0,
                        new MapCollection<Integer, Integer>(inc, items))
                        .runReduction(new Conj<Integer>(), new ArrayList<Integer>()));

    }
}
```
