package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.NotaLancamentoContabilConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NotaLancamentoContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.NotaLancamentoContabilRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class NotaLancamentoContabilDataProviderImpl implements NotaLancamentoContabilDataProvider {

    private final NotaLancamentoContabilRepository repository;

    private final NotaLancamentoContabilConverter converter;

    @Override
    public boolean existePorNumero(String numeroNotaLancamento) {
        return repository.existsByNumero(numeroNotaLancamento);
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public NotaLancamentoContabil salvar(NotaLancamentoContabil notaLancamentoContabil) {
        NotaLancamentoContabilEntity notaLancamentoContabilSalva = repository.save(converter.from(notaLancamentoContabil));
        return converter.to(notaLancamentoContabilSalva);
    }

    @Override
    @Transactional
    @Modifying(flushAutomatically = true)
    public void remover(Long notaLancamentoContabilId) {
        repository.deleteById(notaLancamentoContabilId);
    }
}
