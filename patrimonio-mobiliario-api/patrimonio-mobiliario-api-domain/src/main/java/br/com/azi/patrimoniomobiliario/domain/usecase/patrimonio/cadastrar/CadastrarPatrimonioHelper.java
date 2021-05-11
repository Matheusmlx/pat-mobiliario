package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.cadastrar;

import java.math.BigDecimal;
import java.util.Arrays;

public class CadastrarPatrimonioHelper {

    private final BigDecimal[] valoresUnitarios;
    private final Boolean[] tooltips;

    public CadastrarPatrimonioHelper(int tamanho, BigDecimal valorUnitario) {
        valoresUnitarios = new BigDecimal[tamanho];
        tooltips = new Boolean[tamanho];

        Arrays.fill(valoresUnitarios, valorUnitario);
        Arrays.fill(tooltips, false);
    }

    public BigDecimal getValorUnitario(int index) {
        return valoresUnitarios[index];
    }

    public void setValorUnitario(int index, BigDecimal valor) {
        if (valoresUnitarios.length > index) {
            valoresUnitarios[index] = valor;
        }
    }

    public Boolean getTooltip(int index) {
        return tooltips[index];
    }

    public void setValorTooltip(int index, Boolean valor) {
        if (tooltips.length > index) {
            tooltips[index] = valor;
        }
    }

}
