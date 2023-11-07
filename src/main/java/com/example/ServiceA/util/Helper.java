package com.example.ServiceA.util;

import com.google.gson.Gson;

public class Helper {

    public static String jsonSerialize(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T jsonDeserialize(String json, Class<T> type) {
        Gson gson = new Gson();
        T object = gson.fromJson(json, type);
        return object;
    }
}
