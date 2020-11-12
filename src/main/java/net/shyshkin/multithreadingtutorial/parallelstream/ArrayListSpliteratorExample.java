package net.shyshkin.multithreadingtutorial.parallelstream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListSpliteratorExample {

    public List<Integer> multiplyEachValue(ArrayList<Integer> inputList, int multiplier, boolean parallel) {
        Stream<Integer> stream = inputList.stream();
        if (parallel) stream = stream.parallel();
        return stream
                .map(val -> val * multiplier)
                .collect(Collectors.toList());
    }
}
