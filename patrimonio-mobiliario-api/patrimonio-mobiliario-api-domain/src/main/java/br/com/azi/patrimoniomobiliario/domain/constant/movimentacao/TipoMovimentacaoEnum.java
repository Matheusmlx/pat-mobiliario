package br.com.azi.patrimoniomobiliario.domain.constant.movimentacao;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoMovimentacaoEnum {
    DISTRIBUICAO("Distribuição"),
    ENTRE_SETORES("Entre Setores"),
    ENTRE_ESTOQUES("Entre Estoques"),
    DEVOLUCAO_ALMOXARIFADO("Devolução Almoxarifado"),
    TEMPORARIA("Temporária");

    private final String valor;

    public String getValor() {
        return valor;
    }
}
