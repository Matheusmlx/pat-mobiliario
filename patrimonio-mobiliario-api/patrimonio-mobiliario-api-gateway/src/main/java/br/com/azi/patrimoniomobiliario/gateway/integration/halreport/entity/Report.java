package br.com.azi.patrimoniomobiliario.gateway.integration.halreport.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Report {
    String name;
    String mineType;
    byte[] content;
}
