package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class UploadTemporarioInputData {
    private String nome;
    private String contentType;
    private byte[] content;
}
