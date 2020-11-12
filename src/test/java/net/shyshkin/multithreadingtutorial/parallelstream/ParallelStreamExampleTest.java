package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamExampleTest {

    @Test
    void stringTransform() {
        //given
        final int SIZE = 16;
        ParallelStreamExample parallelStreamExample = new ParallelStreamExample();
        List<String> names = DataSet.indexedNamesList(SIZE);

        int coresCount = Runtime.getRuntime().availableProcessors();
        double maxDurationMs = 1.7 * 100 * SIZE / coresCount;

        //when - then
        assertTimeout(Duration.ofMillis((long) maxDurationMs), () -> {
            List<String> resultList = parallelStreamExample.stringTransform(names);
            assertNotNull(resultList);
            assertEquals(SIZE, resultList.size());
            resultList.forEach(result -> assertTrue(result.contains(" - "), "Result row must contain ` - `"));
        });
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @DisplayName("Parallel execution must run quickly")
    void stringTransform_parallel(boolean parallel) {
        //given
        final int SIZE = 16;
        ParallelStreamExample parallelStreamExample = new ParallelStreamExample();
        List<String> names = DataSet.indexedNamesList(SIZE);

        int coresCount = Runtime.getRuntime().availableProcessors();
        double maxDurationMs = ((parallel) ? 1.9 / coresCount : 1.2) * 100 * SIZE;

        //when - then
        assertTimeout(Duration.ofMillis((long) maxDurationMs), () -> {
            List<String> resultList = parallelStreamExample.stringTransform(names, parallel);
            assertNotNull(resultList);
            assertEquals(SIZE, resultList.size());
            resultList.forEach(result -> assertTrue(result.contains(" - "), "Result row must contain ` - `"));
        });
    }

    @Test
    @DisplayName("Parallel conversion to Lower Case must be made in certain timeout")
    void string_toLowerCase_test() {
        //given
        ParallelStreamExample parallelStreamExample = new ParallelStreamExample();
        List<String> names = DataSet.namesList();

        int coresCount = Runtime.getRuntime().availableProcessors();
        int size = names.size();

        double durationCoefficient = 1.2;
        final int ONE_NAME_DELAY_MS = 500;
        double maxDurationMs = durationCoefficient * ONE_NAME_DELAY_MS * size / coresCount;

        //when - then
        assertTimeout(Duration.ofMillis((long) maxDurationMs), () -> {
            List<String> resultList = parallelStreamExample.string_toLowerCase(names);
            assertNotNull(resultList);
            assertEquals(size, resultList.size());
            resultList.forEach(result -> assertEquals(result, result.toLowerCase())); //conversion does not take effect
        });
    }


}