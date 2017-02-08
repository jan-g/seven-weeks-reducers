package foo.bar.reducers.example;

import foo.bar.reducers.impl.ArrayListCollection;
import foo.bar.reducers.impl.Conj;
import foo.bar.reducers.impl.FilterCollection;
import foo.bar.reducers.impl.MapCollection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

/**
 * Created by jasgrant on 07/02/2017.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("Making a collection");
        ArrayListCollection<Integer> items = new ArrayListCollection<>(Arrays.asList(1, 2, 3, 4, 5));

        System.out.println("reduce + 0 items = " + items.runReduction((a, x) -> a + x, 0));

        Function<Integer, Integer> inc = x -> x + 1;

        MapCollection<Integer, Integer> myMap = new MapCollection<>(inc, items);

        System.out.println("reduce + (map inc items) = " + myMap.runReduction((a, x) -> a + x, 0));

        // Try Conj.
        System.out.println("The Reducible as a list is: " +
                items.runReduction(new Conj<Integer>(), new ArrayList<Integer>()));

        // Try a filter.
        System.out.println("reduce conj [] (filter isEven items) = " +
                new FilterCollection<Integer>((x) -> x % 2 == 0, items)
                        .runReduction(new Conj<Integer>(), new ArrayList<Integer>()));

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
