package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadTemporarioOutputData {
    private String nome;
    private String contentType;
    private String uri;
    private String url;
}
