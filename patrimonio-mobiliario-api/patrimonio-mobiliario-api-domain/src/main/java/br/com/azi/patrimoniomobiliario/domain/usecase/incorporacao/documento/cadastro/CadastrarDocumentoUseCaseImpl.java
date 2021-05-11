package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro;


import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.IncorporacaoEmProcessamentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.NumeroTipoUnicoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.QuantidadeDeDocumentosCadastradosExcedidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.TipoDocumentoNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastrarDocumentoUseCaseImpl implements CadastrarDocumentoUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final TipoDocumentoDataprovider tipoDocumentoDataprovider;

    private final DocumentoDataProvider documentoDataProvider;

    private final CadastrarDocumentoOutputDataConverter outputDataConverter;

    private final SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public CadastrarDocumentoOutputData executar(CadastrarDocumentoInputData input) {
        validarEntrada(input);
        validarNumeroTipoDocumentoUnico(input);
        validaQuantidadeDeDocumentos(input);

        Incorporacao incorporacao = buscarIncorporacao(input);
        validarIncorporacaoNaoEstaEmProcessamento(incorporacao);

        Documento documento = criarEntidade(input, incorporacao);
        Documento documentoSalvo = salvar(documento);
        promoverArquivoTemporario(documentoSalvo);

        return outputDataConverter.to(documentoSalvo);
    }

    private void validarEntrada(CadastrarDocumentoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarDocumentoInputData::getIncorporacao, Objects::nonNull, "Não há documentos em anexo")
            .get();
    }

    private void validarNumeroTipoDocumentoUnico(CadastrarDocumentoInputData inputData) {
        if (documentoDataProvider.existeDocumentoComNumeroETipo(inputData.getIncorporacao(), inputData.getNumero(), inputData.getTipo())) {
            throw new NumeroTipoUnicoException();
        }
    }

    public void validaQuantidadeDeDocumentos(CadastrarDocumentoInputData inputData) {
        Long quantidade = documentoDataProvider.qntDocumentosPorIncorporacaoId(inputData.getIncorporacao());
        if (quantidade >= 30) {
            throw new QuantidadeDeDocumentosCadastradosExcedidoException();
        }
    }

    private Incorporacao buscarIncorporacao(CadastrarDocumentoInputData input) {
        Optional<Incorporacao> entidade = incorporacaoDataProvider.buscarPorId(input.getIncorporacao());
        return entidade.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoNaoEstaEmProcessamento(Incorporacao incorporacao) {
        if (Incorporacao.Situacao.EM_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoEmProcessamentoException();
        }
    }

    private Documento criarEntidade(CadastrarDocumentoInputData input, Incorporacao incorporacao) {
        TipoDocumento tipoDocumentoEncontrado = buscarTipoDocumento(input);

        BigDecimal valor = null;
        LocalDateTime data = null;

        if (input.getValor() != null) {
            valor = input.getValor();
        }
        if (input.getData() != null) {
            data = LocalDateTime.ofInstant(input.getData().toInstant(), ZoneOffset.UTC);
        }
        return Documento
            .builder()
            .numero(input.getNumero())
            .incorporacao(incorporacao)
            .tipo(tipoDocumentoEncontrado)
            .data(data)
            .uriAnexo(input.getUriAnexo())
            .valor(valor)
            .build();
    }

    private TipoDocumento buscarTipoDocumento(CadastrarDocumentoInputData input) {
        Optional<TipoDocumento> entidade = tipoDocumentoDataprovider.buscarPorId(input.getTipo());
        return entidade.orElseThrow(TipoDocumentoNaoEncontradoException::new);
    }

    private Documento salvar(Documento documento) {
        return documentoDataProvider.salvar(documento);
    }

    private void promoverArquivoTemporario(Documento documento) {
        Arquivo arquivo = Arquivo.builder()
            .uri(documento.getUriAnexo())
            .build();
        sistemaDeArquivosIntegration.promote(arquivo);
    }
}
