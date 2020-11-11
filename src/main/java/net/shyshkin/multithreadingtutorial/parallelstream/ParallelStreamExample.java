package net.shyshkin.multithreadingtutorial.parallelstream;

import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.DataSet;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.List;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
        return resultList;
    }


    private static String addNameLengthTransform(String name) {
        CommonUtil.delay(100);
        return name.length() + " - " + name;
    }
}
