package saman.com.lib;

import java.util.HashMap;
import java.util.Map;

/**
 * <b>Project:</b> ${file_name}<br>
 * <b>Create Date:</b> 2018/1/30 09:28<br>
 * <b>Author:</b> zixin<br>
 * <b>Description:</b> <br>
 */

public class TimeCache {

    public static Map<String, Long> sStartTime = new HashMap<>();
    public static Map<String, Long> sEndTime = new HashMap<>();

    public static void setStartTime(String methodName, long time) {
        sStartTime.put(methodName, time);
    }

    public static void setEndTime(String methodName, long time) {
        sEndTime.put(methodName, time);
    }

    public static String getCostTime(String methodName) {
        long start = sStartTime.get(methodName);
        long end = sEndTime.get(methodName);
        return "method: " + methodName + " main " + Long.valueOf(end - start) + " ns";
    }

}
