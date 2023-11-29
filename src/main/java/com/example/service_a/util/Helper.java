package com.example.service_a.util;

import com.google.gson.Gson;

public class Helper {

  public static String jsonSerialize(Object object) {
    Gson gson = new Gson();
    return gson.toJson(object);
  }

  public static <T> T jsonDeserialize(String json, Class<T> type) {
    Gson gson = new Gson();
    return gson.fromJson(json, type);
  }
}
