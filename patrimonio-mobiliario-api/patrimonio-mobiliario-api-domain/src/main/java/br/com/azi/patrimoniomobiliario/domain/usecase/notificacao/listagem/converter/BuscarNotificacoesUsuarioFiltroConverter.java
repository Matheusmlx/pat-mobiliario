package br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.usecase.notificacao.listagem.BuscarNotificacoesUsuarioInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;

public class BuscarNotificacoesUsuarioFiltroConverter extends GenericConverter<BuscarNotificacoesUsuarioInputData, Notificacao.Filtro> {

    public Notificacao.Filtro to(BuscarNotificacoesUsuarioInputData source, Long usuario) {
        Notificacao.Filtro target = super.to(source);
        target.setPage(source.getPage()-1);
        target.setUsuario(usuario);
        return target;
    }

}
