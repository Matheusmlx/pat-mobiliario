package br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporid.converter;


import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.gateway.integration.efornecedor.usecase.buscarporid.entity.BuscarFornecedorPorIdIntegrationResponse;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import org.springframework.stereotype.Component;

@Component
public class BuscarFornecedorPorIdIntegrationConverter extends GenericConverter<BuscarFornecedorPorIdIntegrationResponse, Fornecedor> {

    @Override
    public Fornecedor to(BuscarFornecedorPorIdIntegrationResponse source) {
        Fornecedor target = super.to(source);

        target.setNome(source.getRazaoSocial());

        return target;
    }
}
