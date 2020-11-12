package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;

import java.util.LinkedList;
import java.util.List;

import static net.shyshkin.multithreadingtutorial.util.CommonUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedListSpliteratorExampleTest {

    LinkedListSpliteratorExample linkedListSpliteratorExample = new LinkedListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValue_sequential() {
        //given
        int size = 1000000;
        LinkedList<Integer> linkedList = DataSet.generateIntegerLinkedList(size);
        startTimer();

        //when
        List<Integer> outputList = linkedListSpliteratorExample.multiplyEachValue(linkedList, 2, false);

        //then
        timeTaken();
        assertEquals(size, outputList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValue_parallel() {
        //given
        int size = 1000000;
        LinkedList<Integer> linkedList = DataSet.generateIntegerLinkedList(size);
        startTimer();

        //when
        List<Integer> outputList = linkedListSpliteratorExample.multiplyEachValue(linkedList, 2, true);

        //then
        timeTaken();
        assertEquals(size, outputList.size());
    }

    @AfterEach
    void tearDown() {
        stopWatchReset();
    }
}