package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.MovimentacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.CadastrarDocumentoMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.converter.CadastrarDocumentoMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.DocumentoJaCadastradoComMesmoNumeroETipoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.MovimentacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.cadastro.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarDocumentoMovimentacaoUseCaseTest {

    @Mock
    private MovimentacaoDataProvider movimentacaoDataProvider;

    @Mock
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private CadastrarDocumentoMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new CadastrarDocumentoMovimentacaoUseCaseImpl(
            movimentacaoDataProvider,
            tipoDocumentoDataprovider,
            documentoDataProvider,
            sistemaDeArquivosIntegration,
            new CadastrarDocumentoMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdDaMovimentacaoForNulo() {
        useCase.executar(new CadastrarDocumentoMovimentacaoInputData());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandNumeroForNulo() {
        useCase.executar(CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(1L)
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandNumeroForVazio() {
        useCase.executar(CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(1L)
            .numero("")
            .build());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandTipoForNulo() {
        useCase.executar(CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(1L)
            .numero("1234")
            .build());
    }

    @Test(expected = MovimentacaoNaoEncontradaException.class)
    public void deveFalharQuandoMovimentacaoNaoExistir() {
        final Long movimentacaoId = 1L;
        final Long tipoId = 1L;
        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(movimentacaoId)
            .tipo(tipoId)
            .numero("1234")
            .uriAnexo("http://uriAnexo")
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        when(documentoDataProvider.qntDocumentosPorMovimentacaoId(anyLong())).thenReturn(1L);

        useCase.executar(inputData);

        verify(movimentacaoDataProvider, times(1)).buscarPorId(movimentacaoId);
    }

    @Test(expected = TipoDocumentoNaoEncontradoException.class)
    public void deveFalharQuandoTipoDocumentoNaoExistir() {
        final Long movimentacaoId = 1L;
        final Long tipoDocumento = 1L;
        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(movimentacaoId)
            .tipo(tipoDocumento)
            .numero("1234")
            .uriAnexo("http://uriAnexo")
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao
                .builder()
                .id(movimentacaoId)
                .build()));

        when(tipoDocumentoDataprovider.buscarPorId(anyLong())).thenReturn(Optional.empty());

        when(documentoDataProvider.qntDocumentosPorMovimentacaoId(anyLong()))
            .thenReturn(1L);

        useCase.executar(inputData);

        verify(tipoDocumentoDataprovider, times(1)).buscarPorId(tipoDocumento);
    }

    @Test(expected = QuantidadeMaximaDeDocumentosAtigindoPorMovimentacaoException.class)
    public void deveFalharQuandoJaExistirAQuantidadeMaximaDeDocumentosCadastradosParaAMovimentacao() {
        final Long movimentacaoId = 1L;
        final Long tipoDocumento = 1L;
        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(movimentacaoId)
            .tipo(tipoDocumento)
            .numero("1234")
            .uriAnexo("http://uriAnexo")
            .build();

        when(documentoDataProvider.qntDocumentosPorMovimentacaoId(anyLong()))
            .thenReturn(30L);

        useCase.executar(inputData);
    }

    @Test(expected = DocumentoJaCadastradoComMesmoNumeroETipoException.class)
    public void deveFalharQuandoTentarCadastrarUmDocumentoComMesmoNumeroETipo() {
        final Long movimentacaoId = 1L;
        final Long tipoDocumento = 1L;
        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .movimentacao(movimentacaoId)
            .tipo(tipoDocumento)
            .numero("1234")
            .uriAnexo("http://uriAnexo")
            .build();

        when(documentoDataProvider.qntDocumentosPorMovimentacaoId(anyLong()))
            .thenReturn(1L);

        when(documentoDataProvider.existeDocumentoComNumeroETipoEMovimentacao(anyLong(), anyString(), anyLong()))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveCadastrarDocumentoEPromoverOArquivoTemporario() {
        final Long movimentacaoId = 1L;
        final Long tipoDocumento = 1L;
        final Date dataDocumento = new Date();

        final CadastrarDocumentoMovimentacaoInputData inputData = CadastrarDocumentoMovimentacaoInputData.builder()
            .numero("1234")
            .valor(BigDecimal.valueOf(120))
            .data(dataDocumento)
            .uriAnexo("http://uriAnexo")
            .movimentacao(movimentacaoId)
            .tipo(tipoDocumento)
            .build();

        final Documento novoDocumento = Documento.builder()
            .numero(inputData.getNumero())
            .valor(inputData.getValor())
            .data(DateUtils.asLocalDateTime(dataDocumento))
            .uriAnexo("http://uriAnexo")
            .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
            .tipo(TipoDocumento.builder().id(tipoDocumento).build())
            .build();

        final Documento documentoSalvo = Documento.builder()
            .id(1L)
            .numero(inputData.getNumero())
            .valor(inputData.getValor())
            .data(DateUtils.asLocalDateTime(dataDocumento))
            .uriAnexo("http://uriAnexo")
            .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
            .tipo(TipoDocumento.builder().id(tipoDocumento).build())
            .build();

        final CadastrarDocumentoMovimentacaoOutputData outputDataEsperado = CadastrarDocumentoMovimentacaoOutputData.builder()
            .id(documentoSalvo.getId())
            .numero(documentoSalvo.getNumero())
            .valor(documentoSalvo.getValor())
            .data(DateValidate.formatarData(documentoSalvo.getData()))
            .uriAnexo(documentoSalvo.getUriAnexo())
            .movimentacao(documentoSalvo.getMovimentacao().getId())
            .tipo(documentoSalvo.getTipo().getId())
            .build();

        when(movimentacaoDataProvider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(Movimentacao.builder().id(movimentacaoId).build()));

        when(tipoDocumentoDataprovider.buscarPorId(anyLong()))
            .thenReturn(Optional.of(TipoDocumento.builder().id(tipoDocumento).build()));

        when(documentoDataProvider.salvar(any(Documento.class)))
            .thenReturn(documentoSalvo);

        final CadastrarDocumentoMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertEquals(outputDataEsperado, outputData);

        verify(documentoDataProvider, times(1)).salvar(novoDocumento);
        verify(sistemaDeArquivosIntegration, times(1))
            .promote(Arquivo.builder().uri(documentoSalvo.getUriAnexo()).build());
    }
}
