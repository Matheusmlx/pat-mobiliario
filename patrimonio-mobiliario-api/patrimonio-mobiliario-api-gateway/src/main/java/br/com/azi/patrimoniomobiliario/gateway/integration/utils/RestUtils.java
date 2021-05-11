package br.com.azi.patrimoniomobiliario.gateway.integration.utils;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RestUtils {

    private OkHttpClient restClient;

    @PostConstruct
    public void postConstruct() {
        this.restClient = new OkHttpClient
                .Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
    }

    public Response execute(Request request) throws IOException {
        return executeRequest(request);
    }

    public Response get(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return executeRequest(request);
    }

    public Response get(String url, String[] headersArr) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headersArr))
                .build();
        return executeRequest(request);
    }

    public Response post(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return executeRequest(request);
    }

    public Response post(String url, String[] headersArr, RequestBody requestBody) throws IOException {
        Headers headers = buildHeaders(headersArr);
        Request request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();
        return executeRequest(request);
    }

    public Response delete(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();
        return executeRequest(request);
    }

    public Response delete(String url, String[] headersArr) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .headers(buildHeaders(headersArr))
                .delete()
                .build();
        return executeRequest(request);
    }

    private Response executeRequest(Request request) throws IOException {
        logRequest(request);
        logHeaders(request.headers());
        Response response = restClient.newCall(request).execute();
        logResponse(response);
        return response;
    }

    private static Headers buildHeaders(String[] headersArr) {
        Headers.Builder headers = new Headers.Builder();
        for (String header : headersArr) {
            headers.add(header);
        }
        return headers.build();
    }

    private static void logHeaders(Headers headers) {
        log.debug("[HEADERS]: " + headers.toString().replace("\n", "; "));
    }

    private static void logRequest(Request request) {
        log.debug("[REQUEST]: " + request.toString());
    }

    private static void logResponse(Response response) {
        log.debug("[RESPONSE]: " + response.toString());
    }
}
