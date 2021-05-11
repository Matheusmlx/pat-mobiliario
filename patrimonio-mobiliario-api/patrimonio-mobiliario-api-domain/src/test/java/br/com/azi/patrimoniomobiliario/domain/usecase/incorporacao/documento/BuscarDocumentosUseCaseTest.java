package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.BuscarDocumentosUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.documento.buscar.converter.BuscarDocumentosOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
public class BuscarDocumentosUseCaseTest {

    DocumentoDataProvider documentoDataProvider;

    @Before
    public void inicializa() {
        documentoDataProvider = Mockito.mock(DocumentoDataProvider.class);
    }


    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoPassarIdPatrimonio(){
        BuscarDocumentosInputData inputData = new BuscarDocumentosInputData();
        inputData.setIncorporacao(null);
        BuscarDocumentosUseCaseImpl usecase = new BuscarDocumentosUseCaseImpl(Mockito.mock(DocumentoDataProvider.class),
            Mockito.mock(BuscarDocumentosOutputDataConverter.class));

        usecase.executar(inputData);
    }

    @Test
    public void deveBuscarDocumento(){
        BuscarDocumentosInputData inputData = BuscarDocumentosInputData
            .builder().incorporacao(1L).build();

        final LocalDateTime dataDocumento = LocalDateTime
            .ofEpochSecond(50000, 50000, ZoneOffset.UTC)
            .withYear(2020)
            .withDayOfMonth(7)
            .withMonth(Month.SEPTEMBER.getValue());

        Mockito.when(documentoDataProvider.buscarDocumentoPorIncorporacaoId(inputData.getIncorporacao())).thenReturn(
            Collections.singletonList(
                Documento
                    .builder()
                    .id(1L)
                    .numero("1234556")
                    .data(dataDocumento)
                    .valor(BigDecimal.TEN)
                    .uriAnexo("setup.anexo")
                    .tipo(TipoDocumento.builder()
                        .id(1L)
                        .build())
                    .incorporacao(Incorporacao.builder() .id(1L) .build())
                    .build()));

        BuscarDocumentosUseCaseImpl usecase = new BuscarDocumentosUseCaseImpl(documentoDataProvider,
            new BuscarDocumentosOutputDataConverter());

        BuscarDocumentosOutputData outputData = usecase.executar(inputData);

        Assert.assertEquals(1, outputData.getItems().size());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getId());
        Assert.assertEquals("1234556", outputData.getItems().get(0).getNumero());
        Assert.assertEquals(DateValidate.formatarData(dataDocumento), outputData.getItems().get(0).getData());
        Assert.assertEquals(BigDecimal.TEN, outputData.getItems().get(0).getValor());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getTipo());
        Assert.assertEquals(Long.valueOf(1), outputData.getItems().get(0).getIncorporacao());
        Assert.assertEquals("setup.anexo", outputData.getItems().get(0).getUriAnexo());
    }
}
