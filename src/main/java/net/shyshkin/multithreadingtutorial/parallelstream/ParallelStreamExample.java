package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.DataSet;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreamExample {

    public static void main(String[] args) {

        List<String> resultList;
        List<String> names = DataSet.indexedNamesList(32);
        LoggerUtil.log("names : " + names);

        CommonUtil.stopWatch.start();

        resultList = new ParallelStreamExample().stringTransform(names);

        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Final Result : " + resultList);
        LoggerUtil.log("Total Time Taken : " + CommonUtil.stopWatch.getTime());
    }

    public List<String> stringTransform(List<String> names) {
        List<String> resultList;
        resultList = names
                .parallelStream()
                .map(ParallelStreamExample::addNameLengthTransform)
//                .sequential()
                .collect(Collectors.toList());
        return resultList;
    }

    public List<String> stringTransform(List<String> names, boolean parallel) {
        Stream<String> namesStream = names.stream();

        if (parallel) namesStream = namesStream.parallel();

        return namesStream.map(ParallelStreamExample::addNameLengthTransform)
                .collect(Collectors.toList());
    }

    public List<String> string_toLowerCase(List<String> names) {
        return names
                .parallelStream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }

    private static String addNameLengthTransform(String name) {
        CommonUtil.delay(100);
        return name.length() + " - " + name;
    }
}
