package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ContaContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.LancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Movimentacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.LancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.LancamentoContabilConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.LancamentoContabilEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.LancamentoContabilRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LancamentoContabilDataProviderImpl implements LancamentoContabilDataProvider {

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    @Autowired
    private LancamentoContabilConverter converter;

    @Transactional
    @Modifying(flushAutomatically = true)
    @Override
    public LancamentoContabil salvar(LancamentoContabil lancamentoContabil) {
        LancamentoContabilEntity entidadeSalva = lancamentoContabilRepository.save(converter.from(lancamentoContabil));
        return converter.to(entidadeSalva);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Override
    public LancamentoContabil registrarLancamentoContabil(@NonNull ContaContabil contaContabil,
                                                          @NonNull Patrimonio patrimonio,
                                                          @NonNull Movimentacao movimentacao,
                                                          @NonNull UnidadeOrganizacional orgao,
                                                          @NonNull UnidadeOrganizacional setor,
                                                          @NonNull LancamentoContabil.TipoMovimentacao tipoMovimentacao,
                                                          @NonNull LancamentoContabil.TipoLancamento tipoLancamento) {

        LancamentoContabil lancamentoContabil = LancamentoContabil.builder()
            .contaContabil(contaContabil)
            .orgao(orgao)
            .setor(setor)
            .dataLancamento(LocalDateTime.now())
            .valor(patrimonio.getValorLiquido())
            .tipoMovimentacao(tipoMovimentacao)
            .patrimonio(patrimonio)
            .movimentacao(movimentacao)
            .tipoLancamento(tipoLancamento)
            .build();

        LancamentoContabilEntity entidadeSalva = lancamentoContabilRepository.save(converter.from(lancamentoContabil));
        return converter.to(entidadeSalva);
    }

    @Transactional
    @Modifying(flushAutomatically = true)
    @Override
    public void removerPorPatrimonio(Patrimonio patrimonio) {
        lancamentoContabilRepository.deleteAllByPatrimonioId(patrimonio.getId());
    }

    @Transactional
    @Modifying(flushAutomatically = true)
    @Override
    public void removerPorMovimentacao(Long id) {
        lancamentoContabilRepository.deleteAllByMovimentacao_Id(id);
    }

    @Override
    public List<LancamentoContabil> buscarCreditoPorPatrimoniosEMovimentacao(List<Long> patrimoniosId, Long movimentacaoId) {
        List<LancamentoContabilEntity> lancamentoContabilEntities = lancamentoContabilRepository.findAllByPatrimonio_IdInAndMovimentacao_IdAndTipoLancamento(patrimoniosId, movimentacaoId, "CREDITO");
        return lancamentoContabilEntities.stream().map(converter::to).collect(Collectors.toList());
    }


    @Override
    public List<LancamentoContabil> buscarCreditoNaVoltaPorPatrimoniosEMovimentacao(List<Long> patrimoniosId, Long movimentacaoId) {
        List<LancamentoContabilEntity> lancamentoContabilEntities = lancamentoContabilRepository.
            findAllByPatrimonio_IdInAndMovimentacao_IdAndTipoLancamentoAndTipoMovimentacao(patrimoniosId, movimentacaoId, "CREDITO",
                LancamentoContabil.TipoMovimentacao.TEMPORARIA_VOLTA.name());
        return lancamentoContabilEntities.stream().map(converter::to).collect(Collectors.toList());
    }
}
