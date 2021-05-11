package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.converter.CadastrarReconhecimentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception.ExecucaoOrcamentariaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception.ReconhecimentoJaCadastradoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarReconhecimentoTest {

    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    @Before
    public void inicializa() {
        reconhecimentoDataProvider = Mockito.mock(ReconhecimentoDataProvider.class);
    }

    @Test
    public void deveSalvarReconhecimento() {

        CadastrarReconhecimentoUsecaseImpl useCase = new CadastrarReconhecimentoUsecaseImpl(
            reconhecimentoDataProvider,
            new CadastrarReconhecimentoOutputDataConverter());

        CadastrarReconhecimentoInputData inputData = new CadastrarReconhecimentoInputData();
        inputData.setNome("Reconhecimento");
        inputData.setSituacao("ATIVO");
        inputData.setExecucaoOrcamentaria(true);
        inputData.setEmpenho(true);
        inputData.setNotaFiscal(false);

        Mockito.when(reconhecimentoDataProvider.salvar(any(Reconhecimento.class)))
            .thenReturn(Reconhecimento
                .builder()
                .id(1L)
                .nome("Reconhecimento")
                .situacao(Reconhecimento.Situacao.ATIVO)
                .execucaoOrcamentaria(true)
                .empenho(true)
                .notaFiscal(false)
                .build());

        CadastrarReconhecimentoOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(1L), outputData.getId());
        Assert.assertEquals("Reconhecimento", outputData.getNome());
        Assert.assertEquals("ATIVO", outputData.getSituacao());
        Assert.assertEquals(true, outputData.getExecucaoOrcamentaria());
        Assert.assertEquals(true, outputData.getEmpenho());
        Assert.assertEquals(false, outputData.getNotaFiscal());
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoEntradaForVazia() {
        CadastrarReconhecimentoUsecaseImpl useCase = new CadastrarReconhecimentoUsecaseImpl(
            reconhecimentoDataProvider,
            new CadastrarReconhecimentoOutputDataConverter());

        useCase.executar(new CadastrarReconhecimentoInputData());
    }

    @Test(expected = ReconhecimentoJaCadastradoException.class)
    public void deveFalharQuandoNomeJaForCadastrado() {
        CadastrarReconhecimentoUsecaseImpl useCase = new CadastrarReconhecimentoUsecaseImpl(
            reconhecimentoDataProvider,
            new CadastrarReconhecimentoOutputDataConverter());

        CadastrarReconhecimentoInputData inputData = CadastrarReconhecimentoInputData
            .builder()
            .nome("Reconhecimento")
            .situacao("ATIVO")
            .execucaoOrcamentaria(false)
            .build();

        Mockito.when(reconhecimentoDataProvider.existePorNome(any(String.class)))
            .thenReturn(Boolean.TRUE);

        useCase.executar(inputData);
    }

    @Test(expected = ExecucaoOrcamentariaException.class)
    public void deveFalharQuandoExecucaoOrcamentariaECamposFalsos() {
        CadastrarReconhecimentoUsecaseImpl useCase = new CadastrarReconhecimentoUsecaseImpl(
            reconhecimentoDataProvider,
            new CadastrarReconhecimentoOutputDataConverter());

        CadastrarReconhecimentoInputData inputData = CadastrarReconhecimentoInputData
            .builder()
            .nome("Reconhecimento")
            .situacao("ATIVO")
            .execucaoOrcamentaria(true)
            .empenho(false)
            .notaFiscal(false)
            .build();

        useCase.executar(inputData);
    }
}
