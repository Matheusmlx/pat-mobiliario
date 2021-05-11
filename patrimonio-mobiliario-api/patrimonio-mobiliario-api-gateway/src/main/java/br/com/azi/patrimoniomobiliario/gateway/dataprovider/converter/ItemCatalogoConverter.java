package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

import br.com.azi.patrimoniomobiliario.domain.entity.ItemCatalogo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemRegular;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemSimplificado;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemRegularEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ItemSimplificadoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ItemCatalogoConverter {

    @Autowired
    private ItemSimplificadoConverter itemSimplificadoConverter;

    @Autowired
    private ItemRegularConverter itemRegularConverter;

    public Object to(Object source) {
        if (source instanceof ItemSimplificadoEntity) {
            ItemSimplificadoEntity item = (ItemSimplificadoEntity) source;
            return itemSimplificadoConverter.to(item);
        }
        if (source instanceof ItemRegularEntity) {
            ItemRegularEntity item = (ItemRegularEntity) source;
            return itemRegularConverter.to(item);
        }
        return Object.class;
    }

    public ItemCatalogo to(ItemRegular source) {

        ItemCatalogo target = new ItemCatalogo();

        if (Objects.nonNull(source.getId())) {
            target.setId(source.getId());
        }

        if (Objects.nonNull(source.getCodigo())) {
            target.setCodigo(source.getCodigo());
        }

        if (Objects.nonNull(source.getDescricao())) {
            target.setDescricao(source.getDescricao());
        }
        return target;
    }

    public ItemCatalogo to(ItemSimplificado source) {

        ItemCatalogo target = new ItemCatalogo();

        if (Objects.nonNull(source.getId())) {
            target.setId(source.getId());
        }

        if (Objects.nonNull(source.getCodigo())) {
            target.setCodigo(source.getCodigo());
        }

        if (Objects.nonNull(source.getDescricao())) {
            target.setDescricao(source.getDescricao());
        }
        return target;
    }
}
