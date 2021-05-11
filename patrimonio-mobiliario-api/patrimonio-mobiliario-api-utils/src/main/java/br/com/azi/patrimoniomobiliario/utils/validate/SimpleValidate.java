package br.com.azi.patrimoniomobiliario.utils.validate;

import java.util.Collection;

public class SimpleValidate {

    public static boolean isNullOrBlank(String str) {

        if (str != null) {
            str = str.trim();
        }

        return (str == null || "".equals(str));
    }

    public static boolean isNullOrEmpty(Collection<?> collection) {

        return (collection == null || collection.isEmpty());

    }

    public static boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }
}
