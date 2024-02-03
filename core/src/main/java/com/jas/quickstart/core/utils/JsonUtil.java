package com.jas.quickstart.core.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @author ReaJason
 * @since 2024/1/27
 */
public class JsonUtil {
    private static final Gson GSON = new GsonBuilder().disableJdkUnsafe().serializeNulls().setPrettyPrinting().create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}
