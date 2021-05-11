package br.com.azi.patrimoniomobiliario.gateway.integration.utils;

import com.querydsl.core.util.ArrayUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UrlBuilder {

    private String domain;
    private String pathTemplate;
    private Object[] variables;
    private List<String> queryParams;

    public UrlBuilder domain(String domain) {
        this.domain = domain;
        this.pathTemplate = null;
        this.variables = null;
        this.queryParams = null;
        return this;
    }

    public UrlBuilder path(String path) {
        this.pathTemplate = path;
        return this;
    }

    public UrlBuilder params(String... parametros) {
        this.variables = parametros;
        return this;
    }

    public UrlBuilder addQueryParameter(String param) {
        if (Objects.isNull(this.queryParams)) {
            this.queryParams = new ArrayList<>();
        }
        this.queryParams.add(param);
        return this;
    }

    public String build() {
        String urlTemplate = this.domain + this.pathTemplate;
        urlTemplate = (ArrayUtils.isEmpty(this.variables)) ? urlTemplate : String.format(urlTemplate, this.variables);
        urlTemplate = urlTemplate + formatQueryParams();

        return urlTemplate;
    }

    private String formatQueryParams() {
        if (Objects.isNull(this.queryParams)) {
            return "";
        }

        String queryParamsFormated = "";
        boolean firstParam = true;

        for (String param : this.queryParams) {
            if (firstParam) {
                queryParamsFormated = String.format("?%s", param);
                firstParam = false;
            } else {
                queryParamsFormated = String.format("%s&%s", queryParamsFormated, param);
            }
        }

        return queryParamsFormated;
    }

}
