package br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter;

public interface Converter<BUSINESS_ENTITY, JPA_ENTITY> {

    JPA_ENTITY toJpaEntity(BUSINESS_ENTITY businessEntity);

    BUSINESS_ENTITY toBusinessEntity(JPA_ENTITY jpaEntity);
}
