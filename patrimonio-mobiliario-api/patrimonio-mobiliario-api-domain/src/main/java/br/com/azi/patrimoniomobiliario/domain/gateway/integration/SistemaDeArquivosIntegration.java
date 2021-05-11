package br.com.azi.patrimoniomobiliario.domain.gateway.integration;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;

public interface SistemaDeArquivosIntegration {

    Arquivo upload(Arquivo arquivo);

    Arquivo download(String fileUri);

    void promote(Arquivo arquivo);

    void removeDefinitiveFile(Arquivo arquivo);

    Arquivo getMetadata(String fileUri);

}
