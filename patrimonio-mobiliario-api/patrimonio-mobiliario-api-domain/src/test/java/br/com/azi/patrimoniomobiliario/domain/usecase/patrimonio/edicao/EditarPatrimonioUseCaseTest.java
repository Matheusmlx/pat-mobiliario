package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.converter.EditarPatrimonioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.ChassiCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.ChassiInvalidoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.NumeroSerieCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioEstornadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PatrimonioNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PlacaCadastradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.PlacaInvalidaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.RenavamCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception.RenavamInvalidoException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class EditarPatrimonioUseCaseTest {

    private PatrimonioDataProvider patrimonioDataProvider;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    @Before
    public void setUp() {

        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
        sistemaDeArquivosIntegration = Mockito.mock(SistemaDeArquivosIntegration.class);
        itemIncorporacaoDataProvider = Mockito.mock(ItemIncorporacaoDataProvider.class);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoIdForNulo() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .numeroSerie("12341234")
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioNaoExisteException.class)
    public void deveFalharQuandoPatrimonioNaoExiste() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(5L)
            .numeroSerie("12341234")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(false);

        useCase.executar(inputData);
    }

    @Test(expected = RenavamInvalidoException.class)
    public void deveFalharQuandoRenavamInvalido() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(5L)
            .renavam("82975758271")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = ChassiInvalidoException.class)
    public void deveFalharQuandoChassiInvalido() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .chassi("11111111111111111")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = PlacaInvalidaException.class)
    public void deveFalharQuandoPlacaInvalida() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(5L)
            .placa("ND45683")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = PlacaCadastradaException.class)
    public void deveFalharQuandoPlacaJaCadastrada() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .placa("MWO6460")
            .renavam("82975758270")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.existePorPlaca(any(String.class),any(Long.class)))
            .thenReturn(true);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = RenavamCadastradoException.class)
    public void deveFalharQuandoRenavamJaCadastrada() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .placa("MWO6460")
            .renavam("82975758270")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.existePorRenavam(any(String.class),any(Long.class)))
            .thenReturn(true);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = ChassiCadastradoException.class)
    public void deveFalharQuandoChassiJaCadastrada() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .placa("MWO6460")
            .renavam("82975758270")
            .chassi("4T1BG22K6YU689119")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.existePorChassi(any(String.class),any(Long.class)))
            .thenReturn(true);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test
    public void deveAtualizarPatrimonio() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .placa("MWO6460")
            .renavam("82975758270")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(
                Patrimonio
                    .builder()
                    .id(1L)
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .build()
            );

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals("MWO6460", outputData.getPlaca());
        Assert.assertEquals("82975758270", outputData.getRenavam());
    }

    @Test
    public void deveAtualizarPatrimonioTipoMovel() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(12L)
            .placa("JOQ9089")
            .build();

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(12L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(
                Patrimonio
                    .builder()
                    .id(12L)
                    .build()
            );

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals(Long.valueOf(12), outputData.getId());
    }

    @Test
    public void deveAtualizarPatrimonioTipoEquipamento() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(15L)
            .numeroSerie("354831")
            .placa("JOQ9089")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(15L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.atualizar(any(Patrimonio.class)))
            .thenReturn(
                Patrimonio
                    .builder()
                    .id(15L)
                    .numeroSerie("354831")
                    .build()
            );

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        EditarPatrimonioOutputData outputData = useCase.executar(inputData);
        Assert.assertEquals("354831", outputData.getNumeroSerie());
    }

    @Test(expected = NumeroSerieCadastradoException.class)
    public void deveFalharQuandoNumeroSerieJaCadastrado() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(17L)
            .numeroSerie("7864885")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(17L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .situacao(Patrimonio.Situacao.ATIVO)
                    .build()
            ));

        Mockito.when(itemIncorporacaoDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                ItemIncorporacao
                    .builder()
                    .id(1L)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.existePorNumeroSerie(any(String.class),any(Long.class)))
            .thenReturn(true);

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }

    @Test(expected = PatrimonioEstornadoException.class)
    public void deveFalharAoAtualizarPatrimonioEstornado() {
        EditarPatrimonioUseCase useCase = new EditarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            new EditarPatrimonioOutputDataConverter(),
            sistemaDeArquivosIntegration,
            itemIncorporacaoDataProvider
        );

        EditarPatrimonioInputData inputData = EditarPatrimonioInputData
            .builder()
            .id(1L)
            .placa("MWO6460")
            .renavam("82975758270")
            .build();

        Mockito.when(patrimonioDataProvider.buscarPorId(any(Long.class)))
            .thenReturn(Optional.of(
                Patrimonio
                    .builder()
                    .id(1L)
                    .itemIncorporacao(ItemIncorporacao.builder().id(1L).build())
                    .renavam("82975758270")
                    .placa("MWO6460")
                    .valorLiquido(BigDecimal.valueOf(14900))
                    .valorResidual(BigDecimal.valueOf(12500))
                    .contaContabilClassificacao(ContaContabil.builder().id(5L).build())
                    .situacao(Patrimonio.Situacao.ESTORNADO)
                    .build()
            ));

        Mockito.when(patrimonioDataProvider.existe(any(Long.class)))
            .thenReturn(true);

        useCase.executar(inputData);
    }
}
