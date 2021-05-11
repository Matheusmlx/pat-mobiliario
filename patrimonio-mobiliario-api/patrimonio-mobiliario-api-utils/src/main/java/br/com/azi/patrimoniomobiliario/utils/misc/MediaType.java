package br.com.azi.patrimoniomobiliario.utils.misc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MediaType {
    private static Map<String, String> TYPES;

    static {
        TYPES = new HashMap<>();
        TYPES.put("txt", "text/plain");
        TYPES.put("html", "text/html");
        TYPES.put("jpeg", "image/jpeg");
        TYPES.put("png", "image/png");
        TYPES.put("mpeg", "audio/mpeg");
        TYPES.put("ogg", "audio/ogg");
        TYPES.put("mp4", "video/mp4");
        TYPES.put("pdf", "application/pdf");
        TYPES.put("json", "application/json");

        TYPES.put("unknown", "application/octet-stream");
    }

    public static String findMIMETypeBy(String fileExtention) {
        Optional<Map.Entry<String, String>> mimeEntry = TYPES.entrySet().stream()
                .filter(mimeTypeEntry -> mimeTypeEntry.getKey().equalsIgnoreCase(fileExtention))
                .findFirst();

        if (mimeEntry.isPresent())
            return mimeEntry.get().getValue();

        return TYPES.get("unknown");
    }
}
