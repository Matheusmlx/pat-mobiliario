package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.converter.BuscarDocumentosMovimentacaoOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarDocumentosMovimentacaoUseCaseImpl implements BuscarDocumentosMovimentacaoUseCase {

    private final DocumentoDataProvider documentoDataProvider;

    private final BuscarDocumentosMovimentacaoOutputDataConverter converter;

    @Override
    public BuscarDocumentosMovimentacaoOutputData executar(BuscarDocumentosMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final List<Documento> documentos = buscarDocumentosMovimentacao(inputData.getMovimentacaoId());

        return converter.to(documentos);
    }

    private void validarDadosEntrada(BuscarDocumentosMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarDocumentosMovimentacaoInputData::getMovimentacaoId, Objects::nonNull, "O id da movimentação é nulo.")
            .get();
    }

    private List<Documento> buscarDocumentosMovimentacao(Long movimentacaoId) {
        return documentoDataProvider.buscarDocumentoPorMovimentacaoId(movimentacaoId);
    }
}
