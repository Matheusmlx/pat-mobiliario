package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.cadastro.exception.NumeroTipoUnicoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.EditarDocumentoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.converter.EditarDocumentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.editar.exception.IncorporacaoEmProcessamentoException;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EditarDocumentoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    @Mock
    private IncorporacaoDataProvider incorporacaoDataProvider;

    @Mock
    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @InjectMocks
    private EditarDocumentoOutputDataConverter outputDataConverter;

    private EditarDocumentoUseCaseImpl useCase;

    @Before
    public void inicializa() {
        useCase = new EditarDocumentoUseCaseImpl(documentoDataProvider,incorporacaoDataProvider,outputDataConverter,sistemaDeArquivosIntegration);
    }

    @Test
    public void deveEditarDocumento() {
        EditarDocumentoInputData inputData = new EditarDocumentoInputData();
        inputData.setId(1L);
        inputData.setNumero("6");
        inputData.setIncorporacao(10L);
        inputData.setTipo(1L);
        inputData.setValor(BigDecimal.valueOf(450));
        inputData.setData(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setUriAnexo("documento.pdf");

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(10L)
                .situacao(Incorporacao.Situacao.EM_ELABORACAO)
                .build()));

        when(documentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Documento
                .builder()
                .id(1L)
                .numero("6")
                .incorporacao(Incorporacao.builder().id(1L).build())
                .tipo(TipoDocumento.builder().id(2L).build())
                .valor(BigDecimal.valueOf(450))
                .data(LocalDateTime.parse("2019-09-09T10:15:30"))
                .uriAnexo("documento.pdf")
                .build()
            ));
        when(documentoDataProvider.atualizar(any(Documento.class)))
            .thenReturn(Documento.builder()
                .id(1L)
                .numero("6")
                .incorporacao(Incorporacao.builder().id(10L).build())
                .tipo(TipoDocumento.builder().id(1L).build())
                .valor(BigDecimal.valueOf(450))
                .data(LocalDateTime.parse("2019-09-09T10:15:30"))
                .uriAnexo("documento.pdf")
                .build());

        EditarDocumentoOutputData outputData = useCase.executar(inputData);

        assertEquals(Long.valueOf(1), outputData.getId());
        assertEquals("6", outputData.getNumero());
        assertEquals("documento.pdf", outputData.getUriAnexo());
        assertEquals(LocalDateTime.of(2019, Month.SEPTEMBER, 9, 10, 15, 30), outputData.getData());
        assertEquals(BigDecimal.valueOf(450), outputData.getValor());
        assertEquals(Long.valueOf(1), outputData.getTipo());
        assertEquals(Long.valueOf(10), outputData.getIncorporacao());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalaharQuandoNaopassarId() {
        useCase.executar(new EditarDocumentoInputData());
    }

    @Test(expected = NumeroTipoUnicoException.class)
    public void deveFalharQuandoHouverDocumentoCadastradoComMesmoNumeroETipo() {

        EditarDocumentoInputData inputData = EditarDocumentoInputData
            .builder()
            .id(1L)
            .numero("6")
            .incorporacao(10L)
            .tipo(1L)
            .valor(BigDecimal.valueOf(450))
            .data(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()))
            .build();

        when(documentoDataProvider.existeDocumentoComNumeroETipoEdicao(any(Long.class), any(Long.class), any(String.class), any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = IncorporacaoEmProcessamentoException.class)
    public void deveFalharQuandoIncorporacaoEstiverEmProcessamento() {
        EditarDocumentoInputData inputData = new EditarDocumentoInputData();
        inputData.setId(1L);
        inputData.setNumero("6");
        inputData.setIncorporacao(10L);
        inputData.setTipo(1L);
        inputData.setValor(BigDecimal.valueOf(450));
        inputData.setData(Date.from(LocalDateTime.now().withMonth(Month.SEPTEMBER.getValue()).withDayOfMonth(9).atZone(ZoneId.systemDefault()).toInstant()));
        inputData.setUriAnexo("documento.pdf");

        when(incorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Incorporacao
                .builder()
                .id(10L)
                .situacao(Incorporacao.Situacao.EM_PROCESSAMENTO)
                .build()));

        useCase.executar(inputData);
    }

}
