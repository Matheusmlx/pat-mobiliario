package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.download;

import br.com.azi.patrimoniomobiliario.domain.usecase.UseCase;

public interface DownloadUsecase extends UseCase<DownloadInputData, DownloadOutputData> {

    DownloadOutputData executar(DownloadInputData inputData);
}
