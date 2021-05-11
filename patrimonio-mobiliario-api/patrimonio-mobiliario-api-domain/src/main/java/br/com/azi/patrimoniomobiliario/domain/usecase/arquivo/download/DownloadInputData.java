package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DownloadInputData {
    private String uri;
}
