package br.com.azi.patrimoniomobiliario.integrationtest.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHelper {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'").create();

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }
}
