package br.com.azi.patrimoniomobiliario.utils.string;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoPessoaEnum {

    FISICA("Pessoa Física"),
    JURIDICA("Pessoa Jurídica");

    private final String valor;

    public String getValor(){ return valor; }
}
