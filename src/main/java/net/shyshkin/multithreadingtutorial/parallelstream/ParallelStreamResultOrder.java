package net.shyshkin.multithreadingtutorial.parallelstream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static net.shyshkin.multithreadingtutorial.util.LoggerUtil.log;

public class ParallelStreamResultOrder {

    private static Collection<Integer> collectionOrder(Collection<Integer> collection,
                                                       Supplier<Collection<Integer>> collectionSupplier) {
        return collection.parallelStream()
                .map(val -> val * 2)
                .collect(Collectors.toCollection(collectionSupplier));
    }

    public static void main(String[] args) {
        Supplier<Stream<Integer>> integerStreamSupplier = () -> IntStream.iterate(12, i -> i - 1).limit(12).boxed();
        Collection<Integer> inputCollection;

        inputCollection = integerStreamSupplier.get().collect(Collectors.toList());
        convertAndLog(inputCollection,  ArrayList::new);
        inputCollection = integerStreamSupplier.get().collect(Collectors.toSet());
        convertAndLog(inputCollection, ArrayList::new);
        inputCollection = integerStreamSupplier.get().collect(Collectors.toList());
        convertAndLog(inputCollection,  HashSet::new);
        inputCollection = integerStreamSupplier.get().collect(Collectors.toSet());
        convertAndLog(inputCollection, HashSet::new);
    }

    private static void convertAndLog(Collection<Integer> inputCollection, Supplier<Collection<Integer>> collectionSupplier) {
        log("Input of type: " + inputCollection.getClass().getSimpleName() + " with elements " + inputCollection);
        Collection<Integer> collection = collectionOrder(inputCollection,  collectionSupplier);
        log("Output of type: " + collection.getClass().getSimpleName() + " with elements " + collection);
        log("---------------------------------------");
    }

}
