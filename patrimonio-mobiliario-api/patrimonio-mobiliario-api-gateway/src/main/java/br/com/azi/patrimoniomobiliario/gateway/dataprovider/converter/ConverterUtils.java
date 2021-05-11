package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import java.util.Objects;

public class ConverterUtils {

    private ConverterUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Integer shotToInteger(Short value) {
        if (Objects.nonNull(value)) {
            return value.intValue();
        }
        return null;
    }

    public static <T extends Enum<T>> Enum<T> stringToEnum(Class<T> clazz, String value) {
        if (Objects.nonNull(value)) {
            return Enum.valueOf(clazz, value);
        }
        return null;
    }
}
