package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.converter.BuscarDocumentosOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarDocumentosUseCaseImpl implements BuscarDocumentosUseCase {

    private DocumentoDataProvider documentoDataProvider;

    private BuscarDocumentosOutputDataConverter outputDataConverter;

    @Override
    public BuscarDocumentosOutputData executar(BuscarDocumentosInputData inputData) {
        validarDadosEntrada(inputData);

        List<Documento> documentosEncontrados = buscar(inputData);

        return outputDataConverter.to(documentosEncontrados);
    }

    private void validarDadosEntrada(BuscarDocumentosInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarDocumentosInputData::getIncorporacao, Objects::nonNull, "O id da incorporação é nulo")
            .get();
    }

    private List<Documento> buscar(BuscarDocumentosInputData inputData) {
        return documentoDataProvider.buscarDocumentoPorIncorporacaoId(inputData.getIncorporacao());
    }
}
