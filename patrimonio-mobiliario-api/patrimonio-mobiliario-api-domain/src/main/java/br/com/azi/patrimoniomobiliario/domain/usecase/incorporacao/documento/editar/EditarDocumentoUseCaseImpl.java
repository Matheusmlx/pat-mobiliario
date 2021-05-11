package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.NumeroTipoUnicoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.converter.EditarDocumentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception.EditarDocumentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception.IncorporacaoEmProcessamentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarDocumentoUseCaseImpl implements EditarDocumentoUseCase {

    private final DocumentoDataProvider documentoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final EditarDocumentoOutputDataConverter outputDataConverter;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public EditarDocumentoOutputData executar(EditarDocumentoInputData inputData) {
        validarDadosEntrada(inputData);
        validarNumeroTipoDocumentoUnico(inputData);

        Incorporacao incorporacao = buscarIncorporacao(inputData.getIncorporacao());
        validarIncorporacaoNaoEstaEmProcessamento(incorporacao);

        Documento documento = buscarDocumento(inputData);
        removerArquivoTemporario(documento, inputData);
        promoverArquivoTemporario(documento.getUriAnexo(), inputData.getUriAnexo());
        setarDados(documento, inputData);
        Documento documentoSalvo = atualizar(documento);

        return outputDataConverter.to(documentoSalvo);
    }

    private void validarDadosEntrada(EditarDocumentoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarDocumentoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private void validarNumeroTipoDocumentoUnico(EditarDocumentoInputData inputData) {
        if (documentoDataProvider.existeDocumentoComNumeroETipoEdicao(inputData.getId(), inputData.getIncorporacao(), inputData.getNumero(), inputData.getTipo())) {
            throw new NumeroTipoUnicoException();
        }
    }

    private Incorporacao buscarIncorporacao(Long incorporacaoId) {
        return incorporacaoDataProvider.buscarPorId(incorporacaoId)
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoNaoEstaEmProcessamento(Incorporacao incorporacao) {
        if (Incorporacao.Situacao.EM_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoEmProcessamentoException();
        }
    }

    private Documento buscarDocumento(EditarDocumentoInputData inputData) {
        return (documentoDataProvider.buscarPorId(inputData.getId()).orElseThrow(EditarDocumentoException::new));
    }

    private void setarDados(Documento documento, EditarDocumentoInputData inputData) {
        documento.setUriAnexo(inputData.getUriAnexo());

        documento.setValor(inputData.getValor());

        documento.setData(
            inputData.getData() != null
                ? LocalDateTime.ofInstant(inputData.getData().toInstant(), ZoneOffset.UTC)
                : null
        );

        Optional.ofNullable(inputData.getNumero()).ifPresent(documento::setNumero);

        Optional.ofNullable(inputData.getTipo()).ifPresent(tipoDocumento -> {
            documento.setTipo(TipoDocumento.builder()
                .id(tipoDocumento)
                .build());
        });

        Optional.ofNullable(inputData.getIncorporacao()).ifPresent(idIncorporacao -> {
            documento.setIncorporacao(Incorporacao.builder()
                .id(idIncorporacao)
                .build());
        });
    }

    private void removerArquivoTemporario(Documento documento, EditarDocumentoInputData inputData) {
        if (Objects.nonNull(documento.getUriAnexo()) && !documento.getUriAnexo().equals(inputData.getUriAnexo())) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(documento.getUriAnexo()).build());
        }
    }

    private void promoverArquivoTemporario(String uriAnexo, String uriInput) {
        if (Objects.nonNull(uriInput) && !uriInput.equals(uriAnexo)) {
            sistemaDeArquivosIntegration.promote(Arquivo.builder().uri(uriInput).build());
        }
    }

    private Documento atualizar(Documento documento) {
        return documentoDataProvider.atualizar(documento);
    }
}
