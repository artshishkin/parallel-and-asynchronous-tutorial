package net.shyshkin.multithreadingtutorial.forkjoin;

import lombok.AllArgsConstructor;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
public class ForkJoinUsingRecursion extends RecursiveTask<List<String>> {

    private List<String> inputStringList;

    public static void main(String[] args) {


        CommonUtil.stopWatch.start();
        List<String> resultList = new ArrayList<>();
//        List<String> names = DataSet.namesList();
        List<String> names = IntStream.rangeClosed(1,32).mapToObj(i->"Name"+i).collect(Collectors.toList());
        LoggerUtil.log("names : " + names);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinUsingRecursion forkJoinUsingRecursion = new ForkJoinUsingRecursion(names);
        resultList = forkJoinPool.invoke(forkJoinUsingRecursion);

        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Final Result : " + resultList);
        LoggerUtil.log("Total Time Taken : " + CommonUtil.stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        CommonUtil.delay(100);
        return name.length() + " - " + name;
    }

    @Override
    protected List<String> compute() {
        int size = inputStringList.size();
        if (size == 0) return Collections.emptyList();
        if (size == 1) {
            return inputStringList.stream()
                    .map(ForkJoinUsingRecursion::addNameLengthTransform)
                    .collect(Collectors.toCollection(LinkedList::new));
        }

        int midPoint = size / 2;
        ForkJoinTask<List<String>> leftInputList = new ForkJoinUsingRecursion(inputStringList.subList(0, midPoint)).fork();
        inputStringList = inputStringList.subList(midPoint, size);
        List<String> rightResult = compute();   // recursion happens
        List<String> leftResult = leftInputList.join();
        leftResult.addAll(rightResult);
        return leftResult;
    }
}
