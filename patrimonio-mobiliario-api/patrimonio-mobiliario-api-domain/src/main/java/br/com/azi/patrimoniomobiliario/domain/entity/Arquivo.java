package br.com.azi.patrimoniomobiliario.domain.entity;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class Arquivo {
    private String nome;
    private String contentType;
    private byte[] content;
    private String uri;
    private String url;
}
