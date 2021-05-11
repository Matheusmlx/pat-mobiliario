package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar.exception.NumeracaoPatrimonialException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class CadastrarPatrimonioUseCaseImpl implements CadastrarPatrimonioUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private String quantidadeDigitos;

    @Override
    public void executar(CadastrarPatrimonioInputData inputData) {
        validarEntrada(inputData);

        Long quantidadePatrimoniosExistentes = buscarQuantidadePatrimoniosExistentes(inputData.getItemIncorporacaoId());

        if (validarSeQuantidadePatrimoniosAlterada(quantidadePatrimoniosExistentes, inputData)) {
            Long quantidadePatrimoniosNovos = calcularQuantidadePatrimonios(inputData, quantidadePatrimoniosExistentes);
            List<Patrimonio> patrimonios = criarPatrimonios(inputData, quantidadePatrimoniosExistentes, quantidadePatrimoniosNovos);
            salvar(patrimonios);
        } else if (validarSeValorTotalAlterado(inputData)) {
            CadastrarPatrimonioHelper cadastrarPatrimonioHelper = calcularValoresAquisicao(inputData);
            atualizarValorAquisicao(inputData, cadastrarPatrimonioHelper);
        }
    }

    private void validarEntrada(CadastrarPatrimonioInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarPatrimonioInputData::getQuantidade, Objects::nonNull, "A quantidade é nula")
            .validate(CadastrarPatrimonioInputData::getItemIncorporacaoId, Objects::nonNull, "O id do item da incorporação é nulo")
            .get();
    }

    private boolean validarSeQuantidadePatrimoniosAlterada(Long quantidadePatrimoniosExistentes, CadastrarPatrimonioInputData inputData) {
        return !quantidadePatrimoniosExistentes.equals(inputData.getQuantidade());
    }

    private boolean validarSeValorTotalAlterado(CadastrarPatrimonioInputData inputData) {
        return Objects.nonNull(inputData.getValorTotalAnterior()) && !inputData.getValorTotal().equals(inputData.getValorTotalAnterior());
    }

    private List<Patrimonio> criarPatrimonios(CadastrarPatrimonioInputData inputData, Long quantidadePatrimoniosExistentes, Long quantidadePatrimoniosNovos) {
        List<Patrimonio> patrimonios = new ArrayList<>();

        CadastrarPatrimonioHelper cadastrarPatrimonioHelper = calcularValoresAquisicao(inputData);
        validarQuantidadeDigitos();
        String numeroPatrimonioInicial = buscarUltimoNumero();

        for (int i = 0; i < quantidadePatrimoniosNovos; i++) {
            Patrimonio patrimonio = criarPatrimonio(inputData, cadastrarPatrimonioHelper, quantidadePatrimoniosExistentes.intValue() + i);
            gerarNumeroPatrimonio(patrimonio, numeroPatrimonioInicial, i);
            patrimonios.add(patrimonio);
        }

        atualizarPatrimonios(inputData, quantidadePatrimoniosNovos, cadastrarPatrimonioHelper);

        return patrimonios;
    }

    private void gerarNumeroPatrimonio(Patrimonio patrimonio, String numeroPatrimonioInicial, int indiceNumeroAtual) {
        patrimonio.setNumero(gerarNumero(numeroPatrimonioInicial, indiceNumeroAtual + 1));
    }

    private String gerarNumero(String numeroInicial, int indiceNumeroAtual) {
        if (Objects.isNull(numeroInicial)) {
            if (indiceNumeroAtual == 1) {
                return formatarProximoNumero("1", quantidadeDigitos);
            } else {
                return formatarProximoNumero(Integer.toString(indiceNumeroAtual), quantidadeDigitos);
            }
        }
        return formatarProximoNumero(gerarProximoNumero(numeroInicial, indiceNumeroAtual), quantidadeDigitos);
    }


    private String gerarProximoNumero(String numeroInicial, int indiceNumeroAtual) {
        return Integer.toString(Integer.parseInt(numeroInicial) + indiceNumeroAtual);
    }

    private void salvar(List<Patrimonio> patrimonios) {
        patrimonioDataProvider.salvar(patrimonios);
    }

    private Patrimonio criarPatrimonio(CadastrarPatrimonioInputData inputData,
                                       CadastrarPatrimonioHelper cadastrarPatrimonioHelper, int indicePatrimonio) {
        return Patrimonio
            .builder()
            .itemIncorporacao(ItemIncorporacao.builder().id(inputData.getItemIncorporacaoId()).build())
            .valorAquisicao(cadastrarPatrimonioHelper.getValorUnitario(indicePatrimonio))
            .diferencaDizima(cadastrarPatrimonioHelper.getTooltip(indicePatrimonio))
            .build();
    }

    private Long buscarQuantidadePatrimoniosExistentes(Long itemIncorporacaoId) {
        return patrimonioDataProvider.quantidadePorItemIncorporacao(itemIncorporacaoId);
    }

    private Long calcularQuantidadePatrimonios(CadastrarPatrimonioInputData inputData, Long quantidadePatrimoniosExistentes) {
        Long quantidade = inputData.getQuantidade();

        if (Objects.nonNull(quantidadePatrimoniosExistentes)) {
            if (acrescentarPatrimoniosAoItem(quantidade, quantidadePatrimoniosExistentes)) {
                return quantidade - quantidadePatrimoniosExistentes;
            }

            if (removerPatrimoniosDoItem(quantidade, quantidadePatrimoniosExistentes)) {
                Long qtdPatrimoniosASeremExcluidos = quantidadePatrimoniosExistentes - quantidade;
                excluirUltimosPatrimonios(qtdPatrimoniosASeremExcluidos, inputData.getItemIncorporacaoId());
                return -1L;
            }

            return quantidadePatrimoniosExistentes;
        }
        return quantidade;
    }

    private Boolean acrescentarPatrimoniosAoItem(Long quantidadePatrimoniosParaCadastrar, Long quantidadePatrimoniosExistentes) {
        return quantidadePatrimoniosParaCadastrar.compareTo(quantidadePatrimoniosExistentes) > 0;
    }

    private Boolean removerPatrimoniosDoItem(Long quantidadePatrimoniosParaCadastrar, Long quantidadePatrimoniosExistentes) {
        return quantidadePatrimoniosParaCadastrar.compareTo(quantidadePatrimoniosExistentes) < 0;
    }

    private void excluirUltimosPatrimonios(Long qtdPatrimoniosASeremExcluidos, Long itemIncorporacaoId) {
        patrimonioDataProvider.excluiUltimosPatrimonios(qtdPatrimoniosASeremExcluidos, itemIncorporacaoId);
    }

    private CadastrarPatrimonioHelper calcularValoresAquisicao(CadastrarPatrimonioInputData inputData) {
        BigDecimal valorTotal = inputData.getValorTotal().setScale(2, RoundingMode.DOWN);
        BigDecimal quantidade = BigDecimal.valueOf(inputData.getQuantidade());

        BigDecimal valorUnitario = valorTotal.divide(quantidade, RoundingMode.DOWN);
        BigDecimal diferenca = valorTotal.subtract(valorUnitario.multiply(quantidade));

        CadastrarPatrimonioHelper cadastrarPatrimonioHelper = new CadastrarPatrimonioHelper(inputData.getQuantidade().intValue(), valorUnitario);

        while (diferenca.compareTo(BigDecimal.ZERO) != 0) {
            for (int i = 0; i < inputData.getQuantidade(); i++) {
                cadastrarPatrimonioHelper.setValorUnitario(i, cadastrarPatrimonioHelper.getValorUnitario(i).add(BigDecimal.valueOf(0.01)));
                cadastrarPatrimonioHelper.setValorTooltip(i, true);
                diferenca = diferenca.subtract(BigDecimal.valueOf(0.01));

                if (diferenca.compareTo(BigDecimal.ZERO) == 0) break;
            }
        }

        return cadastrarPatrimonioHelper;
    }

    private void atualizarPatrimonios(CadastrarPatrimonioInputData inputData, Long quantidadePatrimonios, CadastrarPatrimonioHelper cadastrarPatrimonioHelper) {
        if (quantidadePatrimonios.compareTo(0L) != 0) {
            atualizarValorAquisicao(inputData, cadastrarPatrimonioHelper);
        } else {
            if (Objects.nonNull(inputData.getValorTotalAnterior()) && (inputData.getValorTotalAnterior().compareTo(inputData.getValorTotal()) != 0)) {
                atualizarValorAquisicao(inputData, cadastrarPatrimonioHelper);
            }
        }
    }

    private void atualizarValorAquisicao(CadastrarPatrimonioInputData inputData, CadastrarPatrimonioHelper cadastrarPatrimonioHelper) {
        List<Patrimonio> patrimonios = patrimonioDataProvider.buscarTodos(inputData.getItemIncorporacaoId());

        for (int i = 0; i < patrimonios.size(); i++) {
            patrimonios.get(i).setValorAquisicao(cadastrarPatrimonioHelper.getValorUnitario(i));
            patrimonios.get(i).setDiferencaDizima(cadastrarPatrimonioHelper.getTooltip(i));
        }

        patrimonioDataProvider.atualizarTodos(patrimonios);
    }

    private void validarQuantidadeDigitos() {
        if (Integer.parseInt(quantidadeDigitos) < 8 || Integer.parseInt(quantidadeDigitos) > 20) {
            throw new NumeracaoPatrimonialException("O número de patrimônio deve ter entre 8 e 20 dígitos!");
        }
    }

    private String buscarUltimoNumero() {
        Optional<Patrimonio> patrimonio = patrimonioDataProvider.buscarPatrimonioComMaiorNumero();
        return patrimonio.map(Patrimonio::getNumero).orElse(null);
    }


    private String formatarProximoNumero(String string, String tamanho) {
        return String.format("%" + tamanho + "s", string).replace(' ', '0');
    }

}
