package br.com.azi.patrimoniomobiliario.domain.usecase.movimentacao.interna.temporaria.devolucao.exception;

public class PatrimonioJaDevolvidoException extends RuntimeException{

    public PatrimonioJaDevolvidoException() { super("Patrimônio já foi devolvido."); }
}
