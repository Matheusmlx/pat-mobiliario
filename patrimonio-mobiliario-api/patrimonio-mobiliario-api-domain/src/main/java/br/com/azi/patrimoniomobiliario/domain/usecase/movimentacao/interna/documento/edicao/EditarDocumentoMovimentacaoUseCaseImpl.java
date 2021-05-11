package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.converter.EditarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.DocumentoJaCadastradoComMesmoNumeroETipoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.edicao.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.Objects;

@AllArgsConstructor
public class EditarDocumentoMovimentacaoUseCaseImpl implements EditarDocumentoMovimentacaoUseCase {

    private final DocumentoDataProvider documentoDataProvider;

    private final TipoDocumentoDataprovider tipoDocumentoDataprovider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private final EditarDocumentoMovimentacaoOutputDataConverter converter;

    @Override
    public EditarDocumentoMovimentacaoOutputData executar(EditarDocumentoMovimentacaoInputData inputData) {
        validarDadosEntrada(inputData);

        final Documento documento = buscarDocumento(inputData.getId());
        atualizarDocumento(documento, inputData);

        final Documento documentoAtualizado = salvarAlteracoes(documento);
        return converter.to(documentoAtualizado);
    }

    private void validarDadosEntrada(EditarDocumentoMovimentacaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarDocumentoMovimentacaoInputData::getId, Objects::nonNull, "O id do documento é nulo.")
            .validate(EditarDocumentoMovimentacaoInputData::getMovimentacao, Objects::nonNull, "O id da movimentação é nulo.")
            .validate(EditarDocumentoMovimentacaoInputData::getNumero, StringUtils::isNotEmpty, "O número do documento é nulo ou vazio.")
            .validate(EditarDocumentoMovimentacaoInputData::getTipo, Objects::nonNull, "O id do tipo de documento é nulo.")
            .get();
    }

    private Documento buscarDocumento(Long id) {
        return documentoDataProvider.buscarPorId(id).orElseThrow(DocumentoNaoEncontradoException::new);
    }

    private void atualizarDocumento(Documento documento, EditarDocumentoMovimentacaoInputData inputData) {
        atualizarTipoDocumento(documento, inputData.getTipo());
        atualizarNumero(documento, inputData.getNumero());
        atualizarData(documento, inputData.getData());
        atualizarUriAnexo(documento, inputData.getUriAnexo());
        documento.setValor(inputData.getValor());
    }

    private void atualizarTipoDocumento(Documento documento, Long tipoDocumentoId) {
        if (!tipoDocumentoId.equals(documento.getTipo().getId())) {
            final TipoDocumento tipoDocumento = tipoDocumentoDataprovider.buscarPorId(tipoDocumentoId)
                .orElseThrow(TipoDocumentoNaoEncontradoException::new);

            validarDocumentoComTipoNumeroDuplicado(documento, tipoDocumentoId, documento.getNumero());
            documento.setTipo(tipoDocumento);
        }
    }

    private void validarDocumentoComTipoNumeroDuplicado(Documento documento, Long tipoId, String numero) {
        if (documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacaoEdicao(documento.getId(),
            documento.getMovimentacao().getId(), numero, tipoId)) {
            throw new DocumentoJaCadastradoComMesmoNumeroETipoException();
        }
    }

    private void atualizarNumero(Documento documento, String numero) {
        if (!numero.equals(documento.getNumero())) {
            validarDocumentoComTipoNumeroDuplicado(documento, documento.getTipo().getId(), numero);
            documento.setNumero(numero);
        }
    }

    private void atualizarData(Documento documento, Date data) {
        if (Objects.nonNull(data)) {
            documento.setData(DateUtils.asLocalDateTime(data));
        } else {
            documento.setData(null);
        }
    }

    private void atualizarUriAnexo(Documento documento, String uriAnexo) {
        if (!uriAnexo.equals(documento.getUriAnexo())) {
            removerArquivoTemporario(documento);
            promoverArquivoTemporario(uriAnexo);
            documento.setUriAnexo(uriAnexo);
        }
    }

    private void removerArquivoTemporario(Documento documento) {
        if (Objects.nonNull(documento.getUriAnexo())) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(documento.getUriAnexo()).build());
        }
    }

    private void promoverArquivoTemporario(String uriInput) {
        if (Objects.nonNull(uriInput)) {
            sistemaDeArquivosIntegration.promote(Arquivo.builder().uri(uriInput).build());
        }
    }

    private Documento salvarAlteracoes(Documento documento) {
        return documentoDataProvider.salvar(documento);
    }
}
