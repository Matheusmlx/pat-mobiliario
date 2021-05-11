package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception;

public class ReconhecimentoJaCadastradoException extends RuntimeException {

    public ReconhecimentoJaCadastradoException() {
        super("Nome de reconhecimento já cadastrado!");
    }

}
