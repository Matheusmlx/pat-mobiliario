package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.TipoDocumentoDataprovider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.CadastrarDocumentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.converter.CadastrarDocumentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.IncorporacaoEmProcessamentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.NumeroTipoUnicoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.QuantidadeDeDocumentosCadastradosExcedidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.TipoDocumentoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CadastraDocumentoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private TipoDocumentoDataprovider tipoDocumentoDataprovider;

    @Mock
    SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    CadastrarDocumentoOutputDataConverter outputDataConverter;

    private CadastrarDocumentoUseCaseImpl useCase;

    @Before
    public void inicializa() {
        useCase = new CadastrarDocumentoUseCaseImpl(incorporacaoDataProvider,tipoDocumentoDataprovider,documentoDataProvider,outputDataConverter,sistemaDeArquivosIntegration);

    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoCadastrarSemNada() {
        useCase.executar(new CadastrarDocumentoInputData());
    }

    @Test
    public void deveSalvarDocumento() {
        CadastrarDocumentoInputData inputData = new CadastrarDocumentoInputData();
        inputData.setNumero("6");
        inputData.setIncorporacao(10L);
        inputData.setTipo(1L);
        inputData.setValor(BigDecimal.valueOf(450));
        inputData.setData(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()));

        final LocalDateTime dataDocumento = LocalDateTime.parse("2019-09-09T10:15:30");

        when(documentoDataProvider.salvar(any(Documento.class)))
            .thenReturn(Documento
                .builder()
                .id(1L)
                .numero("6")
                .incorporacao(Incorporacao.builder().id(10L).build())
                .tipo(TipoDocumento.builder().id(1L).build())
                .data(dataDocumento)
                .valor(BigDecimal.valueOf(450))
                .uriAnexo("documento.pdf")
                .build());

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao
                    .builder()
                    .id(7L)
                    .codigo("2442-2")
                    .numProcesso("2223")
                    .build()
            ));

        when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                TipoDocumento
                    .builder()
                    .id(1L)
                    .build()
            ));

        CadastrarDocumentoOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("6", outputData.getNumero());
        Assert.assertEquals(DateValidate.formatarData(dataDocumento), outputData.getData());
        Assert.assertEquals(BigDecimal.valueOf(450), outputData.getValor());
        Assert.assertEquals("documento.pdf", outputData.getUriAnexo());
        Assert.assertEquals(Long.valueOf(1), outputData.getTipo());
        Assert.assertEquals(Long.valueOf(10), outputData.getIncorporacao());
    }


    @Test(expected = QuantidadeDeDocumentosCadastradosExcedidoException.class)
    public void deveFalharQuandoHouver30DocumentosJaCadastrados() {
        CadastrarDocumentoInputData inputData = CadastrarDocumentoInputData
            .builder()
            .numero("6")
            .incorporacao(10L)
            .tipo(1L)
            .valor(BigDecimal.valueOf(450))
            .data(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        when(documentoDataProvider.qntDocumentosPorIncorporacaoId(any(Long.class)))
            .thenReturn(Long.valueOf(30));

        useCase.executar(inputData);
    }

    @Test(expected = NumeroTipoUnicoException.class)
    public void deveFalharQuandoHouverDocumentoCadastradoComMesmoNumeroETipo() {
        CadastrarDocumentoInputData inputData = CadastrarDocumentoInputData
            .builder()
            .numero("6")
            .incorporacao(10L)
            .tipo(1L)
            .valor(BigDecimal.valueOf(450))
            .data(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        when(documentoDataProvider.existeDocumentoComNumeroETipo(any(Long.class), any(String.class), any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = TipoDocumentoNaoEncontradoException.class)
    public void deveFalharQuandoNaoEncontrarTipoDocumento() {
        CadastrarDocumentoInputData inputData = new CadastrarDocumentoInputData();
        inputData.setNumero("6");
        inputData.setIncorporacao(10L);
        inputData.setTipo(1L);
        inputData.setValor(BigDecimal.valueOf(450));
        inputData.setData(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao
                    .builder()
                    .id(7L)
                    .codigo("2442-2")
                    .numProcesso("2223")
                    .build()
            ));

        when(tipoDocumentoDataprovider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.empty());

       useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoEmProcessamentoException.class)
    public void deveFalharQuandoIncorporacaoEstiverEmProcessamento() {
        CadastrarDocumentoInputData inputData = new CadastrarDocumentoInputData();
        inputData.setNumero("6");
        inputData.setIncorporacao(10L);
        inputData.setTipo(1L);
        inputData.setValor(BigDecimal.valueOf(450));
        inputData.setData(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()));

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Incorporacao
                    .builder()
                    .id(7L)
                    .codigo("2442-2")
                    .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
                    .numProcesso("2223")
                    .build()
            ));

        useCase.executar(inputData);
    }
}
