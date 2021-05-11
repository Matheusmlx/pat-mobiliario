package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.DocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.DocumentoNaoPertenceAIncorporacaoInformadaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.IncorporacaoEmProcessamentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.exclusao.exception.IncorporacaoNaoEncontradaException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class DeletarDocumentoUseCaseImpl implements DeletarDocumentoUseCase {

    private final DocumentoDataProvider documentoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public void executar(DeletarDocumentoInputData inputData) {
        validarDadosEntrada(inputData);

        Documento documento = buscar(inputData);
        validarSeDocumentoPertenceAIncorporacao(inputData, documento);

        Incorporacao incorporacao = buscarIncorporacao(inputData);
        validarIncorporacaoNaoEstaEmProcessamento(incorporacao);

        removerArquivoTemporario(documento);
        remover(documento);
    }

    private void validarDadosEntrada(DeletarDocumentoInputData entrada) {
        Validator.of(entrada)
            .validate(DeletarDocumentoInputData::getId, Objects::nonNull, "O id é nulo!")
            .get();
    }

    private Documento buscar(DeletarDocumentoInputData inputData) {
        return documentoDataProvider.buscarPorId(inputData.getId()).orElseThrow(DocumentoNaoEncontradoException::new);
    }


    private void validarSeDocumentoPertenceAIncorporacao(DeletarDocumentoInputData inputData, Documento documento) {
        if (!documento.getIncorporacao().getId().equals(inputData.getIncorporacao())) {
            throw new DocumentoNaoPertenceAIncorporacaoInformadaException();
        }
    }

    private Incorporacao buscarIncorporacao(DeletarDocumentoInputData inputData) {
        return incorporacaoDataProvider.buscarPorId(inputData.getIncorporacao())
            .orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoNaoEstaEmProcessamento(Incorporacao incorporacao) {
        if (Incorporacao.Situacao.EM_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoEmProcessamentoException();
        }
    }

    private void removerArquivoTemporario(Documento documento) {
        if (Objects.nonNull(documento.getUriAnexo())) {
            sistemaDeArquivosIntegration.removeDefinitiveFile(Arquivo.builder().uri(documento.getUriAnexo()).build());
        }
    }

    private void remover(Documento documento) {
        documentoDataProvider.remover(documento.getId());
    }
}
