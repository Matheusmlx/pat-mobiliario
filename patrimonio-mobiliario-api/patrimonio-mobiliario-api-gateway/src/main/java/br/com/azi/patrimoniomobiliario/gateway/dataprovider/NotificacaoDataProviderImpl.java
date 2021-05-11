package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotificacaoDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.NotificacaoConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NotificacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QNotificacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.NotificacaoRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificacaoDataProviderImpl implements NotificacaoDataProvider {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private NotificacaoConverter converter;

    @Override
    public ListaPaginada<Notificacao> buscarPorFiltro(Notificacao.Filtro filtro) {
        QNotificacaoEntity qNotificacaoEntity = QNotificacaoEntity.notificacaoEntity;

        BooleanExpression expression = qNotificacaoEntity.usuario.id.eq(filtro.getUsuario());

        filtro.setSort("dataCriacao");
        filtro.setDirection("DESC");
        filtro.setSize(3L);

        Page<NotificacaoEntity> notificacoesEncontradadas = repository.findAll(expression, FiltroConverter.extrairPaginacao(filtro));

        List<Notificacao> notificacoes = notificacoesEncontradadas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada
            .<Notificacao>builder()
            .items(notificacoes)
            .totalElements(notificacoesEncontradadas.getTotalElements())
            .totalPages((long) notificacoesEncontradadas.getTotalPages())
            .build();
    }

    @Override
    public void atualizarNotificacoes(List<Notificacao> notificacoes) {
        for (Notificacao notificacao : notificacoes) {
            repository.save(converter.from(notificacao));
        }
    }

    @Override
    public Long buscarQuantidadeNotificacoesNaoVisualizadas(Long usuarioId) {
        return repository.countAllByVisualizadaIsFalseAndUsuarioIdEquals(usuarioId);
    }

    @Override
    public Notificacao salvar(Notificacao notificacao) {
        final NotificacaoEntity entidadeSalva = repository.save(converter.from(notificacao));
        return converter.to(entidadeSalva);
    }

    @Override
    @Transactional
    public int removerNotificacoesAntigas(Date dataReferencia) {
        return repository.removerNotificacoesPorDataCriacaoMaisAntigaQue(dataReferencia);
    }

    @Override
    public Notificacao buscarNotificacaoPorOrigemEOrigemId(String origem, Long origemId) {
        final NotificacaoEntity entidadeSalva = repository.findFirstByOrigemAndOrigemIdOrderByDataCriacaoDesc(origem, origemId);
        return converter.to(entidadeSalva);
    }
}
