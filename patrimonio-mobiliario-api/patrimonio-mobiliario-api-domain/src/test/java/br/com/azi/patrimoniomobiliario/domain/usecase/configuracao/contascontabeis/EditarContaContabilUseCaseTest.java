package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contascontabeis;

import br.com.azi.patrimoniomobiliario.domain.entity.ConfigContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConfigContaContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilOutputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.EditarContaContabilUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.converter.EditarContaContabilOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.ConfigContaContabilNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualExcedidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNaoNuloException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualNuloException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.PercentualResidualZeroException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.TipoAssociadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilNaoNulaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilNulaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.dadosdepreciacao.exception.VidaUtilZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

public class EditarContaContabilUseCaseTest {

    ConfigContaContabilDataProvider dataProvider;

    EditarContaContabilUseCaseImpl useCase;

    @Before
    public void inicializar() {
        dataProvider = Mockito.mock(ConfigContaContabilDataProvider.class);
        useCase = new EditarContaContabilUseCaseImpl(dataProvider, new EditarContaContabilOutputDataConverter());
    }

    @Test
    public void deveCriarContaContabilDepreciavel(){
        EditarContaContabilInputData inputData = new EditarContaContabilInputData();
        inputData.setContaContabil(2L);
        inputData.setId(1L);
        inputData.setTipo("DEPRECIAVEL");
        inputData.setTipoBem("VEICULO");
        inputData.setPercentualResidual(BigDecimal.valueOf(45));
        inputData.setVidaUtil(4);
        inputData.setTaxa(BigDecimal.ZERO);

        Mockito.when(dataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.atualizar(any(ConfigContaContabil.class)))
            .thenReturn(ConfigContaContabil.builder()
                .id(1L)
                .contaContabil(ContaContabil.builder().id(2L).build())
                .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .percentualResidual(BigDecimal.valueOf(45))
                .vidaUtil(4)
                .build());

        EditarContaContabilOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("DEPRECIAVEL",outputData.getTipo());
        Assert.assertEquals(Integer.valueOf(4), outputData.getVidaUtil());
        Assert.assertEquals(BigDecimal.valueOf(45), outputData.getPercentualResidual());
        Assert.assertEquals(Long.valueOf(2), outputData.getContaContabil());

    }

    @Test
    public void deveEditarContaContabilDepreciavel(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("VEICULO")
            .vidaUtil(4)
            .percentualResidual(BigDecimal.valueOf(45))
            .build();

        Mockito.when(dataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.existePorContaContabil(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil.builder()
                    .id(2L)
                    .contaContabil(ContaContabil.builder().id(2L).build())
                    .tipo(ConfigContaContabil.Tipo.NAO_DEPRECIAVEL)
                    .tipoBem(ConfigContaContabil.TipoBem.MOVEL)
                    .vidaUtil(5)
                    .percentualResidual(BigDecimal.valueOf(50))
                .build()
            ));

        Mockito.when(dataProvider.atualizar(any(ConfigContaContabil.class)))
            .thenReturn(ConfigContaContabil.builder()
                .id(1L)
                .contaContabil(ContaContabil.builder().id(2L).build())
                .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .tipo(ConfigContaContabil.Tipo.DEPRECIAVEL)
                .build());

        EditarContaContabilOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("DEPRECIAVEL",outputData.getTipo());
    }

    @Test(expected = ConfigContaContabilNaoEncontradaException.class)
    public void deveFalharSeNaoEncontrarConfigContaContabil(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("VEICULO")
            .vidaUtil(4)
            .percentualResidual(BigDecimal.valueOf(45))
            .build();

        Mockito.when(dataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.existePorContaContabil(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.empty());

        useCase.executar(inputData);
    }

    @Test
    public void deveCriarContaContabilNaoDepreciavel(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("NAO_DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .tipo("NAO_DEPRECIAVEL")
            .build();

        Mockito.when(dataProvider.existe(any(Long.class))).thenReturn(true);

        Mockito.when(dataProvider.atualizar(any(ConfigContaContabil.class)))
            .thenReturn(ConfigContaContabil.builder()
                .id(1L)
                .contaContabil(ContaContabil.builder().id(2L).build())
                .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                .tipo(ConfigContaContabil.Tipo.NAO_DEPRECIAVEL)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.ONE)
                .tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO)
                .vidaUtil(1)
                .build());

        EditarContaContabilOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("QUOTAS_CONSTANTES",outputData.getMetodo());
        Assert.assertEquals("ARMAMENTO",outputData.getTipoBem());
        Assert.assertEquals("NAO_DEPRECIAVEL",outputData.getTipo());
    }

    @Test
    public void deveEditarContaContabilNaoDepreciavel(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("NAO_DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .tipo("NAO_DEPRECIAVEL")
            .build();

        Mockito.when(dataProvider.existe(any(Long.class))).thenReturn(true);

        Mockito.when(dataProvider.existePorContaContabil(any(Long.class)))
            .thenReturn(true);

        Mockito.when(dataProvider.buscarAtualPorContaContabil(any(Long.class)))
            .thenReturn(Optional.of(
                ConfigContaContabil.builder()
                    .id(2L)
                    .contaContabil(ContaContabil.builder().id(2L).build())
                    .tipo(ConfigContaContabil.Tipo.NAO_DEPRECIAVEL)
                    .tipoBem(ConfigContaContabil.TipoBem.MOVEL)
                    .vidaUtil(5)
                    .percentualResidual(BigDecimal.valueOf(50))
                    .build()
            ));

        Mockito.when(dataProvider.atualizar(any(ConfigContaContabil.class)))
            .thenReturn(ConfigContaContabil.builder()
                .id(1L)
                .contaContabil(ContaContabil.builder().id(2L).build())
                .metodo(ConfigContaContabil.Metodo.QUOTAS_CONSTANTES)
                .tipo(ConfigContaContabil.Tipo.NAO_DEPRECIAVEL)
                .situacao(ConfigContaContabil.Situacao.ATIVO)
                .percentualResidual(BigDecimal.ONE)
                .tipoBem(ConfigContaContabil.TipoBem.ARMAMENTO)
                .vidaUtil(1)
                .build());

        EditarContaContabilOutputData outputData = useCase.executar(inputData);

        Assert.assertEquals(Long.valueOf(1),outputData.getId());
        Assert.assertEquals("QUOTAS_CONSTANTES",outputData.getMetodo());
        Assert.assertEquals("ARMAMENTO",outputData.getTipoBem());
        Assert.assertEquals("NAO_DEPRECIAVEL",outputData.getTipo());
    }

    @Test(expected = TipoAssociadoException.class)
    public void deveFalharQuandoTipoAssociadoDoBemForInvalido(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("teste")
            .tipoBem("ARMAMENTO")
            .vidaUtil(10)
            .percentualResidual(BigDecimal.valueOf(10))
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = VidaUtilNulaException.class)
    public void deveFalharQuandoVidaUtilNula(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .percentualResidual(BigDecimal.valueOf(45))
            .vidaUtil(null)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = VidaUtilZeroException.class)
    public void deveFalharQuandoVidaUtilZero(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .percentualResidual(BigDecimal.valueOf(45))
            .vidaUtil(0)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PercentualResidualNuloException.class)
    public void deveFalharQuandoPercentualResidualNulo(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .percentualResidual(null)
            .vidaUtil(10)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PercentualResidualExcedidoException.class)
    public void deveFalharQuandoValorPercentualResidualForMaiorQueCem(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .vidaUtil(10)
            .percentualResidual(BigDecimal.valueOf(100))
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PercentualResidualZeroException.class)
    public void deveFalharQuandoPercentualResidualZero(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .percentualResidual(BigDecimal.ZERO)
            .vidaUtil(10)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PercentualResidualZeroException.class)
    public void deveFalharQuandoPercentualResidualMenorQueZero(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .percentualResidual(BigDecimal.valueOf(-10))
            .vidaUtil(10)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = VidaUtilNaoNulaException.class)
    public void deveFalharQuandoTiverVidaUtilParaContaNaoDepreciavel(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("NAO_DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .vidaUtil(10)
            .percentualResidual(null)
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PercentualResidualNaoNuloException.class)
    public void deveFalharQuandoTiverPercentualResidualParaContaNaoDepreciavel(){
        EditarContaContabilInputData inputData = EditarContaContabilInputData
            .builder()
            .id(1L)
            .contaContabil(2L)
            .tipo("NAO_DEPRECIAVEL")
            .tipoBem("ARMAMENTO")
            .vidaUtil(null)
            .percentualResidual(BigDecimal.valueOf(45))
            .build();

        useCase.executar(inputData);
    }
}
