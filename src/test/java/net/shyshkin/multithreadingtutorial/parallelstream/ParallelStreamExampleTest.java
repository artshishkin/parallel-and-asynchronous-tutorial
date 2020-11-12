package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.DataSet;
import org.junit.jupiter.api.Test;

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
            resultList.forEach(result -> assertTrue(result.contains(" - "),"Result row must contain ` - `"));
        });
    }
}