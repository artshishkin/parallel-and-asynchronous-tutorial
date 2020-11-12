package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(value = MethodOrderer.Alphanumeric.class)
class ParallelStreamPerformanceTest {
    public static final int EXPECTED_SUM = 1784293664;
    ParallelStreamPerformance intStreamExample = new ParallelStreamPerformance();

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sum_using_intstream(boolean parallel) {
        //given

        //when
        int sum = intStreamExample.sumUsingIntStream(1000000, parallel);
        System.out.println("sum : " + sum);

        //then
        assertEquals(EXPECTED_SUM, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sum_using_iterate(boolean parallel) {
        //given

        //when
        int sum = intStreamExample.sumUsingIterate(1000000, parallel);
        System.out.println("sum : " + sum);

        //then
        assertEquals(EXPECTED_SUM, sum);
    }

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void sum_using_list(boolean parallel) {
        //given
        int size = 1000000;
        ArrayList<Integer> inputList = DataSet.generateArrayList(size);
        //when
        int sum = intStreamExample.sumUsingList(inputList, parallel);
        System.out.println("sum : " + sum);

        //then
        assertEquals(EXPECTED_SUM, sum);
    }

    @AfterEach
    void tearDown() {
        CommonUtil.stopWatchReset();
    }
}