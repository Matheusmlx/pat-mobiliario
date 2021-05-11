package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.converter.EditarReconhecimentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.ExecucaoOrcamentariaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.ReconhecimentoJaCadastradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@RunWith(MockitoJUnitRunner.class)
public class EditarReconhecimentoTest {

    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    @Before
    public void inicializa() {
        reconhecimentoDataProvider = Mockito.mock(ReconhecimentoDataProvider.class);
    }

    @Test(expected = RuntimeException.class)
    public void deveFalharQuandoNaoEncontrarReconhecimento() {
        EditarReconhecimentoUseCaseImpl usecase = new EditarReconhecimentoUseCaseImpl(
            reconhecimentoDataProvider,
            new EditarReconhecimentoOutputDataConverter()
        );
        EditarReconhecimentoInputData inputData = new EditarReconhecimentoInputData();
        inputData.setNome("Reconhecimento");
        inputData.setSituacao("ATIVO");
        inputData.setExecucaoOrcamentaria(true);
        inputData.setEmpenho(true);
        inputData.setNotaFiscal(false);

        usecase.executar(inputData);
    }

    @Test
    public void deveEditarReconhecimento() {

        EditarReconhecimentoInputData inputData = EditarReconhecimentoInputData
            .builder()
            .id(1L)
            .nome("Reconhecimento")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .empenho(false)
            .notaFiscal(true)
            .build();

        EditarReconhecimentoOutputDataConverter atualizarReconhecimentoOutputDataConverter = new EditarReconhecimentoOutputDataConverter();

        EditarReconhecimentoUseCaseImpl useCase = new EditarReconhecimentoUseCaseImpl(
            reconhecimentoDataProvider,
            atualizarReconhecimentoOutputDataConverter
        );

        Mockito.when(reconhecimentoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(Reconhecimento.builder()
                .id(1L)
                .nome("Reconhecimento")
                .situacao(Reconhecimento.Situacao.ATIVO)
                .execucaoOrcamentaria(true)
                .notaFiscal(true)
                .empenho(true)
                .build()));

        Mockito.when(reconhecimentoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(reconhecimentoDataProvider.atualizar(any(Reconhecimento.class)))
            .thenReturn(Reconhecimento.builder()
                .id(1L)
                .nome("TESTE EDIÇÃO")
                .situacao(Reconhecimento.Situacao.INATIVO)
                .execucaoOrcamentaria(false)
                .empenho(true)
                .notaFiscal(true)
                .build());

        EditarReconhecimentoOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1), outputData.getId());
        Assert.assertEquals("TESTE EDIÇÃO", outputData.getNome());
        Assert.assertEquals("INATIVO", outputData.getSituacao());
        Assert.assertEquals(false, outputData.getExecucaoOrcamentaria());
        Assert.assertEquals(true, outputData.getEmpenho());
        Assert.assertEquals(true, outputData.getNotaFiscal());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoEntradaForVazia() {
        EditarReconhecimentoOutputDataConverter atualizarReconhecimentoOutputDataConverter = new EditarReconhecimentoOutputDataConverter();

        EditarReconhecimentoUseCaseImpl useCase = new EditarReconhecimentoUseCaseImpl(
            reconhecimentoDataProvider,
            atualizarReconhecimentoOutputDataConverter
        );

        useCase.executar(new EditarReconhecimentoInputData());
    }

    @Test(expected = ReconhecimentoJaCadastradoException.class)
    public void deveFalharQuandoNomeJaForCadastrado() {
        EditarReconhecimentoUseCaseImpl useCase = new EditarReconhecimentoUseCaseImpl(
            reconhecimentoDataProvider,
            new EditarReconhecimentoOutputDataConverter());

        EditarReconhecimentoInputData inputData = EditarReconhecimentoInputData
            .builder()
            .id(1L)
            .nome("Reconhecimento")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .empenho(true)
            .notaFiscal(true)
            .build();

        Mockito.when(reconhecimentoDataProvider.existe(any(Long.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(reconhecimentoDataProvider.existePorNome(any(String.class)))
            .thenReturn(Boolean.TRUE);

        Mockito.when(reconhecimentoDataProvider.buscarReconhecimentoComNome(any(Long.class), any(String.class)))
            .thenReturn(Optional.of(Reconhecimento.builder()
                .id(2L)
                .nome("Reconhemcineto")
                .build()));

        useCase.executar(inputData);
    }

    @Test(expected = ExecucaoOrcamentariaException.class)
    public void deveFalharQuandoCamposEmpenhoNotaFiscalFalsos() {
        EditarReconhecimentoUseCaseImpl useCase = new EditarReconhecimentoUseCaseImpl(
            reconhecimentoDataProvider,
            new EditarReconhecimentoOutputDataConverter());

        EditarReconhecimentoInputData inputData = EditarReconhecimentoInputData
            .builder()
            .id(1L)
            .nome("Reconhecimento")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .notaFiscal(false)
            .empenho(false)
            .build();

        useCase.executar(inputData);
    }
}
