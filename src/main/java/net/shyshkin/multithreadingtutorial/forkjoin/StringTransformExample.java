package net.shyshkin.multithreadingtutorial.forkjoin;

import net.shyshkin.multithreadingtutorial.util.DataSet;
import net.shyshkin.multithreadingtutorial.util.CommonUtil;
import net.shyshkin.multithreadingtutorial.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;

public class StringTransformExample {

    public static void main(String[] args) {

        CommonUtil.stopWatch.start();
        List<String> resultList = new ArrayList<>();
        List<String> names = DataSet.namesList();
        LoggerUtil.log("names : "+ names);

        names.forEach((name)->{
            String newValue = addNameLengthTransform(name);
            resultList.add(newValue);
        });
        CommonUtil.stopWatch.stop();
        LoggerUtil.log("Final Result : "+ resultList);
        LoggerUtil.log("Total Time Taken : "+ CommonUtil.stopWatch.getTime());
    }


    private static String addNameLengthTransform(String name) {
        CommonUtil.delay(500);
        return name.length()+" - "+name ;
    }
}
