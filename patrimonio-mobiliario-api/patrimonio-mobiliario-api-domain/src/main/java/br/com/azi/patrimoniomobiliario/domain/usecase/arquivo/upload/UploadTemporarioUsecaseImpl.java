package br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.arquivo.upload.converter.UploadTemporarioOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class UploadTemporarioUsecaseImpl implements UploadTemporarioUsecase {

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private UploadTemporarioOutputDataConverter outputDataConverter;

    @Override
    public UploadTemporarioOutputData executar(UploadTemporarioInputData inputData) {
        validarDadosEntrada(inputData);

        Arquivo arquivo = criarArquivo(inputData);
        Arquivo arquivoSalvo = enviarArquivo(arquivo);

        return outputDataConverter.to(arquivoSalvo);
    }

    private void validarDadosEntrada(UploadTemporarioInputData inputData) {
        Validator.of(inputData)
                .validate(UploadTemporarioInputData::getNome, Objects::nonNull, "O nome é nulo")
                .validate(UploadTemporarioInputData::getContentType, Objects::nonNull, "O content type é nulo")
                .validate(UploadTemporarioInputData::getContent, Objects::nonNull, "O content é nulo")
                .get();
    }

    private Arquivo criarArquivo(UploadTemporarioInputData inputData) {
        return Arquivo
                .builder()
                .nome(inputData.getNome())
                .contentType(inputData.getContentType())
                .content(inputData.getContent())
                .build();
    }

    private Arquivo enviarArquivo(Arquivo arquivo) {
        return sistemaDeArquivosIntegration.upload(arquivo);
    }
}
