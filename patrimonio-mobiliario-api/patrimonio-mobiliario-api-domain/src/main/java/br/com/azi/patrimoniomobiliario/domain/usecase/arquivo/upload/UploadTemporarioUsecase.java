package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload;

import br.com.azi.patrimoniomobiliario.domain.usecase.UseCase;

public interface UploadTemporarioUsecase extends UseCase<UploadTemporarioInputData, UploadTemporarioOutputData> {

    UploadTemporarioOutputData executar(UploadTemporarioInputData uploadTemporarioInputData);
}
