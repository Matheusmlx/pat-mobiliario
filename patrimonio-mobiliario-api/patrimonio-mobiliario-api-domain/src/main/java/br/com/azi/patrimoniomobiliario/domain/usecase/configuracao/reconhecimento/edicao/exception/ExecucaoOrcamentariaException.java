package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception;

public class ExecucaoOrcamentariaException extends RuntimeException {
    public ExecucaoOrcamentariaException() {
        super("Campo empenho ou nota fiscal devem ser marcados");
    }
}
