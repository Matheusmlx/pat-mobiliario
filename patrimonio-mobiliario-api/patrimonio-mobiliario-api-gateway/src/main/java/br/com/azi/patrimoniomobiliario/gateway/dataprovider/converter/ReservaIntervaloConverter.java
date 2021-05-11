package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.Reserva;
import br.com.azi.patrimoniomobiliario.domain.entity.ReservaIntervalo;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ReservaIntervaloEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.UnidadeOrganizacionalEntity;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReservaIntervaloConverter extends GenericConverter<ReservaIntervaloEntity, ReservaIntervalo> {
    @Override
    public ReservaIntervalo to(ReservaIntervaloEntity source) {
        ReservaIntervalo target = super.to(source);

        if (Objects.nonNull(source.getReserva())) {
            target.setReserva(Reserva.builder()
                .id(source.getReserva().getId())
                .build()
            );
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(UnidadeOrganizacional.builder()
                .id(source.getOrgao().getId())
                .sigla(source.getOrgao().getSigla())
                .nome(source.getOrgao().getNome())
                .build()
            );
        }

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asLocalDateTime(source.getDataFinalizacao()));
        }

        if (Objects.nonNull(source.getDataCadastro())) {
            target.setDataCadastro(DateUtils.asLocalDateTime(source.getDataCadastro()));
        }

        target.setPreenchimento(
            ReservaIntervalo
                .Preenchimento
                .valueOf(source.getPreenchimento()));

        return target;
    }

    @Override
    public ReservaIntervaloEntity from(ReservaIntervalo source) {
        ReservaIntervaloEntity target = super.from(source);

        if (Objects.nonNull(source.getReserva())) {
            target.setReserva(ReservaEntity.builder()
                .id(source.getReserva().getId())
                .build()
            );
        }

        if (Objects.nonNull(source.getOrgao())) {
            target.setOrgao(UnidadeOrganizacionalEntity.builder()
                .id(source.getOrgao().getId())
                .build()
            );
        }

        if (Objects.nonNull(source.getDataFinalizacao())) {
            target.setDataFinalizacao(DateUtils.asDate(source.getDataFinalizacao()));
        }

        return target;
    }
}
