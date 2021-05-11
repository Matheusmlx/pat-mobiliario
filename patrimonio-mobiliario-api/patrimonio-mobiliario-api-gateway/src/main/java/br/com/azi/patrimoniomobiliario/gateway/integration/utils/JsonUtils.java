package br.com.azi.patrimoniomobiliario.gateway.integration.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class JsonUtils {

    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.mmm'Z'").create();

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }
}
