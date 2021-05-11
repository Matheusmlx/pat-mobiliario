package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.converter.CadastrarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.DocumentoJaCadastradoComMesmoNumeroETipoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastrarDocumentoMovimentacaoUseCaseImpl implements CadastrarDocumentoMovimentacaoUseCase {

    private static final int QUANTIDADE_MAXIMA_DOCUMENTOS_POR_MOVIMENTACAO = 30;

    private final MovimentacaoDataProvider movimentacaoDataProvider;

    private final TipoDocumentoDataprovider tipoDocumentoDataprovider;

    private final DocumentoDataProvider documentoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final CadastrarDocumentoMovimentacaoOutputDataConverter converter;

    @Override
    public CadastrarDocumentoMovimentacaoOutputData executar(CadastrarDocumentoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);
        validarQuantidadeMaximaDocumentosPorMovimentacaoAtingida(inputData.getMovimentacao());
        validarDocumentoExistentePorMovimentacaoNumeroETipo(inputData);

        final Documento novoDocumento = criarNovoDocumento(inputData);
        final Documento documentoSalvo = salvarDocumento(novoDocumento);
        promoverArquivoTemporario(documentoSalvo);

        return converter.to(documentoSalvo);
    }

    private void validarDadosEntrada(CadastrarDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarDocumentoMovimentacaoInputData::getMovimentacao, Objects::nonNull, "O id da movimentação é nulo.")
            .validate(CadastrarDocumentoMovimentacaoInputData::getNumero, StringUtils::isNotEmpty, "O número do documento é nulo ou vazio.")
            .validate(CadastrarDocumentoMovimentacaoInputData::getTipo, Objects::nonNull, "O id do tipo de documento é nulo.")
            .get();
    }

    private void validarQuantidadeMaximaDocumentosPorMovimentacaoAtingida(Long movimentacaoId) {
        final Long quantidadeDocumentosMovimentacao = documentoDataProvider.qntDocumentosPorMovimentacaoId(movimentacaoId);
        if (quantidadeDocumentosMovimentacao >= QUANTIDADE_MAXIMA_DOCUMENTOS_POR_MOVIMENTACAO) {
            throw new QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException();
        }
    }

    private void validarDocumentoExistentePorMovimentacaoNumeroETipo(CadastrarDocumentoMovimentacaoInputData inputData) {
        if (documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacao(inputData.getMovimentacao(),
            inputData.getNumero(), inputData.getTipo())) {
            throw new DocumentoJaCadastradoComMesmoNumeroETipoException();
        }
    }

    private Documento criarNovoDocumento(CadastrarDocumentoMovimentacaoInputData inputData) {
        final Movimentacao movimentacao = buscarMovimentacao(inputData.getMovimentacao());
        final TipoDocumento tipoDocumento = buscarTipoDocumento(inputData.getTipo());

        final Documento.DocumentoBuilder documentoBuilder = Documento.builder()
            .numero(inputData.getNumero())
            .valor(inputData.getValor())
            .uriAnexo(inputData.getUriAnexo())
            .movimentacao(movimentacao)
            .tipo(tipoDocumento);

        if (Objects.nonNull(inputData.getData())) {
            documentoBuilder.data(DateUtils.asLocalDateTime(inputData.getData()));
        }

        return documentoBuilder.build();
    }

    private Movimentacao buscarMovimentacao(Long movimentacaoId) {
        final Optional<Movimentacao> movimentacao = movimentacaoDataProvider.buscarPorId(movimentacaoId);
        return movimentacao.orElseThrow(MovimentacaoNaoEncontradaException::new);
    }

    private TipoDocumento buscarTipoDocumento(Long tipoDocumentoId) {
        return tipoDocumentoDataprovider.buscarPorId(tipoDocumentoId)
            .orElseThrow(TipoDocumentoNaoEncontradoException::new);
    }

    private Documento salvarDocumento(Documento novoDocumento) {
        return documentoDataProvider.salvar(novoDocumento);
    }

    private void promoverArquivoTemporario(Documento documentoSalvo) {
        sistemaDeArquivosIntegration.promote(
            Arquivo.builder().uri(documentoSalvo.getUriAnexo()).build()
        );
    }
}
