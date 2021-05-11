package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NotaLancamentoContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaLancamentoContabilRepository extends JpaRepository<NotaLancamentoContabilEntity, Long> {

    boolean existsByNumero(String numero);
}
