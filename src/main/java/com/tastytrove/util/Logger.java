package com.tastytrove.util;

import java.util.Map;

public class Logger {
    public static void logg(String msg){
        System.out.println(msg);
    }

    public static void logg(String msg, Map<String, String> map){
        System.out.print(msg + "{");
        for (String key : map.keySet()) {
            System.out.print(" [" + key + " : " + map.get(key) + "]");
        }
        System.out.println(" }");

    }
}
