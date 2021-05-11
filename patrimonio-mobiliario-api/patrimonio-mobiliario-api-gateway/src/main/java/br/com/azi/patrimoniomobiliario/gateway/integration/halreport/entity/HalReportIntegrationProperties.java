package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HalReportIntegrationProperties {
    private Header header;
    private Footer footer;

    @Data
    @Builder
    public static class Header {
        String titulo1;
        String titulo2;
    }

    @Data
    @Builder
    public static class Footer {
        String rodape1;
        String rodape2;
    }
}
