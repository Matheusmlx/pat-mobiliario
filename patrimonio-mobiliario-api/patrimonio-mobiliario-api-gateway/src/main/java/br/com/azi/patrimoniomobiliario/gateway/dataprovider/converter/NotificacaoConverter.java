package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Notificacao;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.NotificacaoEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UsuarioEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoConverter extends GenericConverter<NotificacaoEntity, Notificacao> {

    @Override
    public Notificacao to(NotificacaoEntity source) {
        Notificacao target = super.to(source);

        target.setDataCriacao(DateUtils.asLocalDateTime(source.getDataCriacao()));
        target.setUsuario(Usuario
            .builder()
            .id(source.getUsuario().getId())
            .nome(source.getUsuario().getNome())
            .build());
        target.setOrigem(Notificacao.Origem.valueOf(source.getOrigem()));

        return target;
    }

    @Override
    public NotificacaoEntity from(Notificacao source) {
        NotificacaoEntity target = super.from(source);

        target.setDataCriacao(DateUtils.asDate(source.getDataCriacao()));
        target.setUsuario(UsuarioEntity
            .builder()
            .id(source.getUsuario().getId())
            .build());
        target.setOrigem(source.getOrigem().name());

        return target;
    }

}
