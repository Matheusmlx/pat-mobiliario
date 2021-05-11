package br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;

public interface NotaLancamentoContabilDataProvider {

    boolean existePorNumero(String numeroNotaLancamento);

    NotaLancamentoContabil salvar(NotaLancamentoContabil notaLancamentoContabil);

    void remover(Long notaLancamentoContabilId);
}
