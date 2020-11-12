package net.shyshkin.multithreadingtutorial.util;


import net.shyshkin.multithreadingtutorial.domain.checkout.Cart;
import net.shyshkin.multithreadingtutorial.domain.checkout.CartItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataSet {

    public static List<String> lowerCaseAlphabetList = List.of("a", "b", "c", "a", "d", "e", "f", "e", "g", "h", "i");

    public static Cart createCart(int noOfItemsInCart) {

        Cart.CartBuilder cartBuilder = Cart.builder();
        IntStream.rangeClosed(1, noOfItemsInCart)
                .mapToObj(DataSet::generateStubCartItem)
                .forEach(cartBuilder::cartItem);
        return cartBuilder.build();
    }

    private static CartItem generateStubCartItem(int index) {
        return CartItem.builder()
                .itemId(index)
                .itemName("CartItem -" + index)
                .rate(generateRandomPrice())
                .quantity(index)
                .build();
    }

    public static List<String> namesList() {
        return List.of("Bob", "Jamie", "Jill", "Rick");

    }

    public static List<String> indexedNamesList(int size) {
        return IntStream
                .rangeClosed(1, size)
                .mapToObj(i -> "Name" + i)
                .collect(Collectors.toList());
    }

    public static List<Integer> generateIntegerList(int maxNumber) {
        return IntStream.rangeClosed(1, maxNumber)
                .boxed().collect(Collectors.toList());
    }

    public static ArrayList<Integer> generateArrayList(int maxNumber) {

        return IntStream.rangeClosed(1, maxNumber)
                .boxed()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static LinkedList<Integer> generateIntegerLinkedList(int maxNumber) {
        LinkedList<Integer> linkedList = new LinkedList<>();
        IntStream.rangeClosed(1, maxNumber)
                .boxed()
                .forEach((linkedList::add));
        return linkedList;
    }

    public static Set<Integer> generateIntegerSet(int maxNumber) {
        return IntStream.rangeClosed(1, maxNumber)
                .boxed().collect(Collectors.toSet());
    }


    public static double generateRandomPrice() {
        int min = 50;
        int max = 100;
        return Math.random() * (max - min + 1) + min;
    }
}
