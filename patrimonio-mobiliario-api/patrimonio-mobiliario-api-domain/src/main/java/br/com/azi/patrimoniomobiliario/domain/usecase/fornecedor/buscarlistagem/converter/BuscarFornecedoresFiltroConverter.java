package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.converter;


import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarlistagem.BuscarFornecedoresInputData;
import br.com.azi.patrimoniomobiliario.utils.converter.GenericConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BuscarFornecedoresFiltroConverter extends GenericConverter<BuscarFornecedoresInputData, Fornecedor.Filtro> {
}
