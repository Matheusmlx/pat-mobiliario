package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioOutputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.validate.DateValidate;

import java.util.stream.Collectors;

public class BuscarNotificacoesUsuarioOutputDataConverter {

    public BuscarNotificacoesUsuarioOutputData to(ListaPaginada<Notificacao> notificacoes, Long quantidadeNotificacoesNaoVisualizadas) {
        BuscarNotificacaoUsuarioOutputDataConverter outputDataConverter = new BuscarNotificacaoUsuarioOutputDataConverter();

        return BuscarNotificacoesUsuarioOutputData
            .builder()
            .items(notificacoes
                .getItems()
                .stream()
                .map(outputDataConverter::to)
                .collect(Collectors.toList()))
            .totalPages(notificacoes.getTotalPages())
            .totalElements(notificacoes.getTotalElements())
            .quantidadeNaoVisualizadas(quantidadeNotificacoesNaoVisualizadas)
            .build();
    }

    public static class BuscarNotificacaoUsuarioOutputDataConverter extends GenericConverter<Notificacao, BuscarNotificacoesUsuarioOutputData.Item> {
        @Override
        public BuscarNotificacoesUsuarioOutputData.Item to(Notificacao source) {
            BuscarNotificacoesUsuarioOutputData.Item target = super.to(source);
            target.setOrigem(source.getOrigem().name());
            target.setDataCriacao(DateValidate.formatarData(source.getDataCriacao()));
            return target;
        }
    }

}
