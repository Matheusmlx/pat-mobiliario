package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.edicao.exception;

public class PatrimonioEstornadoException extends RuntimeException {
    public PatrimonioEstornadoException() {
        super("Não é possível editar patriônios estornados.");
    }
}
