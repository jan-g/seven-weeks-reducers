package foo.bar.reducers.example;

import foo.bar.reducers.Reducible;
import foo.bar.reducers.ReducingOperation;
import foo.bar.reducers.impl.ArrayListCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static foo.bar.reducers.Reduce.reduce;
import static foo.bar.reducers.impl.Conj.conj;
import static foo.bar.reducers.impl.FilterCollection.filter;
import static foo.bar.reducers.impl.MapCollection.map;
import static foo.bar.reducers.impl.MapCollection.mapping;

/**
 * Created by jang on 07/02/2017.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Making a collection");
        ArrayListCollection<Integer> items = new ArrayListCollection<>(Arrays.asList(1, 2, 3, 4, 5));

        System.out.println("reduce + 0 items = " + reduce((a, x) -> a + x, 0, items));

        Function<Integer, Integer> inc = x -> x + 1;

        Reducible<Integer> myMap = map(inc, items);

        System.out.println("reduce + (map inc items) = " + reduce((a, x) -> a + x, 0, myMap));

        // With all the clojure HOF g(l)ory
        ReducingOperation<Integer, Integer> sum = (a, x) -> a + x;

        Function<ReducingOperation<Integer, Integer>, ReducingOperation<Integer, Integer>> myMapping = mapping(inc);

        System.out.println("reduce ((mapping inc) +) 0 items = " +
                reduce(myMapping.apply((a, x) -> a + x), 0, items)
            );

        // Try Conj.
        System.out.println("The Reducible as a list is: " +
                reduce(conj(), new ArrayList<>(), items));

        // Try a filter.
        Predicate<Integer> isEven = x -> x % 2 == 0;

        System.out.println("reduce conj [] (filter isEven items) = " +
                reduce(conj(), new ArrayList<>(), filter(isEven, items)));

        System.out.println("reduce conj [] (map inc (filter isEven items)) = " +
                reduce(conj(), new ArrayList<>(),
                        map(inc,
                            filter(isEven, items))));

        System.out.println("reduce conj [] (filter isEven (map inc items)) = " +
                reduce(conj(), new ArrayList<>(),
                        filter(isEven,
                            map(inc, items))));

    }
}
