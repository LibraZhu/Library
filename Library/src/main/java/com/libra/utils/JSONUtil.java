package com.libra.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 * Created by libra on 15/7/27 下午3:49.
 */
public class JSONUtil {

    private static Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT).create();

    public static String toJson(Object o){
        return gson.toJson(o);
    }

    public static <T> T fromJson(String json,Class<T> classOfT){
        try {
            return gson.fromJson(json, classOfT);
        }catch (Exception e){
            return null;
        }
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        try{
            return gson.fromJson(json, typeOfT);
        }catch (Exception e){
            return  null;
        }
    }
}
