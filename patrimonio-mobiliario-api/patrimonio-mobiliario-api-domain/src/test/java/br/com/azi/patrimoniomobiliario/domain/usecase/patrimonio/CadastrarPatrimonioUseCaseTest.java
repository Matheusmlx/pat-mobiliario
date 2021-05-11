package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio;

import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioInputData;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioUseCase;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.CadastrarPatrimonioUseCaseImpl;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.exception.NumeracaoPatrimonialException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class CadastrarPatrimonioUseCaseTest {

    private PatrimonioDataProvider patrimonioDataProvider;

    @Captor
    private ArgumentCaptor<List<Patrimonio>> patrimonioListArgumentCaptor;

    @Before
    public void inicializa() {
        patrimonioDataProvider = Mockito.mock(PatrimonioDataProvider.class);
    }

    @Test
    public void deveCadastrarPatrimonioComQuantidadePatrimoniosDiferenteCadastraNovo() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "8"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(3L)
            .valorTotal(BigDecimal.valueOf(10))
            .valorTotalAnterior(BigDecimal.ONE)
            .build();

        Mockito.when(patrimonioDataProvider.quantidadePorItemIncorporacao(any(Long.class))).thenReturn(1L);

       useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(patrimonioDataProvider);

        inOrder.verify(patrimonioDataProvider, times(1)).buscarTodos(1L);
        inOrder.verify(patrimonioDataProvider, times(1)).atualizarTodos(patrimonioListArgumentCaptor.capture());
    }

    @Test
    public void deveCadastrarPatrimonioComQuantidadePatrimoniosDiferenteDeletaUltimos() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "8"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(1L)
            .valorTotal(BigDecimal.valueOf(10))
            .valorTotalAnterior(BigDecimal.ONE)
            .build();

        Mockito.when(patrimonioDataProvider.quantidadePorItemIncorporacao(any(Long.class))).thenReturn(3L);

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(patrimonioDataProvider);

        inOrder.verify(patrimonioDataProvider, times(1)).excluiUltimosPatrimonios(2L,1L);
    }

    @Test
    public void deveCadastrarPatrimonioBuscandoUltimoNumero() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "8"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(3L)
            .valorTotal(BigDecimal.valueOf(10))
            .valorTotalAnterior(BigDecimal.ONE)
            .build();

        Mockito.when(patrimonioDataProvider.quantidadePorItemIncorporacao(any(Long.class))).thenReturn(1L);

        Mockito.when(patrimonioDataProvider.buscarPatrimonioComMaiorNumero()).thenReturn(Optional.of(Patrimonio.builder().numero("00000014").build()));

        useCase.executar(inputData);

        InOrder inOrder = Mockito.inOrder(patrimonioDataProvider);

        inOrder.verify(patrimonioDataProvider, times(1)).buscarTodos(1L);
        inOrder.verify(patrimonioDataProvider, times(1)).atualizarTodos(patrimonioListArgumentCaptor.capture());
    }

    @Test
    public void deveCadastrarPatrimonioComQuantidadePatrimoniosIguais() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "8"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(1L)
            .valorTotal(BigDecimal.valueOf(10))
            .valorTotalAnterior(BigDecimal.ONE)
            .build();

        Mockito.when(patrimonioDataProvider.quantidadePorItemIncorporacao(any(Long.class))).thenReturn(1L);

        useCase.executar(inputData);
    }

    @Test(expected = IllegalStateException.class)
    public void deveFalharQuandoNaoHouverQuantidade() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "6"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder().valorTotal(BigDecimal.valueOf(10)).build();

        useCase.executar(inputData);
    }

    @Test(expected = NumeracaoPatrimonialException.class)
    public void deveFalharQuandoQuantidadeDigitosMaiorQue20() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "21"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(1L)
            .valorTotal(BigDecimal.valueOf(10))
            .build();

        useCase.executar(inputData);
    }

    @Test(expected = NumeracaoPatrimonialException.class)
    public void deveFalharQuandoQuantidadeDigitosMenorQue8() {
        CadastrarPatrimonioUseCase useCase = new CadastrarPatrimonioUseCaseImpl(
            patrimonioDataProvider,
            "6"
        );

        CadastrarPatrimonioInputData inputData = CadastrarPatrimonioInputData
            .builder()
            .itemIncorporacaoId(1L)
            .contaContabilId(2L)
            .quantidade(1L)
            .valorTotal(BigDecimal.valueOf(10))
            .build();

        useCase.executar(inputData);
    }
}
