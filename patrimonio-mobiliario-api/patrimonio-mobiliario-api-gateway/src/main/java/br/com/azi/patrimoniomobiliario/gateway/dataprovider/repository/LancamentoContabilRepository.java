package br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository;

import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.LancamentoContabilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LancamentoContabilRepository extends JpaRepository<LancamentoContabilEntity, Long>, QuerydslPredicateExecutor<LancamentoContabilEntity> {
    void deleteAllByPatrimonioId(Long patrimonioId);

    void deleteAllByMovimentacao_Id(Long movimentacaoId);

    List<LancamentoContabilEntity> findAllByPatrimonio_IdInAndMovimentacao_IdAndTipoLancamento(List<Long> patrimonios, Long movimentacao, String tipoLancamento);

    List<LancamentoContabilEntity> findAllByPatrimonio_IdInAndMovimentacao_IdAndTipoLancamentoAndTipoMovimentacao(List<Long> patrimonios, Long movimentacao, String tipoLancamento, String tipoMovimentacao);
}
