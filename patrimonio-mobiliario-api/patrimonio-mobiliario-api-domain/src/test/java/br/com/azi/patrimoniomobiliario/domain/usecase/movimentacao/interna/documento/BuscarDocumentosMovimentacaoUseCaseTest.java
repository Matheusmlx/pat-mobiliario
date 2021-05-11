package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento;

import br.com.azi.patrimoniomobiliario.domain.entity.Documento;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.TipoDocumento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.DocumentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.BuscarDocumentosMovimentacaoUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.documento.buscar.converter.BuscarDocumentosMovimentacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuscarDocumentosMovimentacaoUseCaseTest {

    @Mock
    private DocumentoDataProvider documentoDataProvider;

    private BuscarDocumentosMovimentacaoUseCase useCase;

    @Before
    public void inicializar() {
        useCase = new BuscarDocumentosMovimentacaoUseCaseImpl(
            documentoDataProvider,
            new BuscarDocumentosMovimentacaoOutputDataConverter()
        );
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdDaMovimentacaoForNulo() {
        useCase.executar(new BuscarDocumentosMovimentacaoInputData());
    }

    @Test
    public void deveBuscarOsDocumentosPeloIdDaMovimentacaoInformado() {
        final Long movimentacaoId = 1L;
        final BuscarDocumentosMovimentacaoInputData inputData = new BuscarDocumentosMovimentacaoInputData(movimentacaoId);

        useCase.executar(inputData);

        verify(documentoDataProvider, times(1)).buscarDocumentoPorMovimentacaoId(movimentacaoId);
    }

    @Test
    public void deveRetornarUmaListaVaziaQuandoNaoEncontrarDocumentos() {
        final Long movimentacaoId = 1L;
        final BuscarDocumentosMovimentacaoInputData inputData = new BuscarDocumentosMovimentacaoInputData(movimentacaoId);

        when(documentoDataProvider.buscarDocumentoPorMovimentacaoId(anyLong())).thenReturn(new ArrayList<>());

        final BuscarDocumentosMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertNotNull(outputData);
        assertEquals(0, outputData.getItems().size());
    }

    @Test
    public void deveRetornarOsDocumentosQuandoEncontrarDocumentos() {
        final Long movimentacaoId = 1L;
        final LocalDateTime dataDocumento = LocalDateTime.of(2020, Month.JULY, 18, 10, 10, 10);
        final BuscarDocumentosMovimentacaoInputData inputData = new BuscarDocumentosMovimentacaoInputData(movimentacaoId);

        final Documento[] documentos = new Documento[]{
            Documento.builder()
                .id(1L)
                .numero("1234")
                .data(dataDocumento)
                .valor(BigDecimal.valueOf(100))
                .uriAnexo("http://uriAnexo")
                .tipo(TipoDocumento.builder().id(1L).build())
                .movimentacao(Movimentacao.builder().id(movimentacaoId).build())
                .build()
        };

        final BuscarDocumentosMovimentacaoOutputData outputDataEsperado = BuscarDocumentosMovimentacaoOutputData.builder()
            .items(Arrays.asList(
                BuscarDocumentosMovimentacaoOutputData.Documento.builder()
                    .id(documentos[0].getId())
                    .numero(documentos[0].getNumero())
                    .data(DateValidate.formatarData(dataDocumento))
                    .valor(documentos[0].getValor())
                    .uriAnexo(documentos[0].getUriAnexo())
                    .tipo(documentos[0].getTipo().getId())
                    .movimentacao(documentos[0].getMovimentacao().getId())
                    .build()
            ))
            .build();

        when(documentoDataProvider.buscarDocumentoPorMovimentacaoId(anyLong())).thenReturn(Arrays.asList(documentos));

        final BuscarDocumentosMovimentacaoOutputData outputData = useCase.executar(inputData);

        assertNotNull(outputData);
        assertEquals(1, outputData.getItems().size());
        assertEquals(outputDataEsperado, outputData);
    }
}
