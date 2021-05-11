package br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid;


import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeFornecedoresIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.converter.BuscarFornecedorPorIdOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.fornecedor.buscarporid.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class BuscarFornecedorPorIdUseCaseImpl implements BuscarFornecedorPorIdUseCase {

    private SistemaDeFornecedoresIntegration sistemaDeFornecedoresIntegration;

    private BuscarFornecedorPorIdOutputDataConverter outputDataConverter;

    @Override
    public BuscarFornecedorPorIdOutputData executar(BuscarFornecedorPorIdInputData inputData) {
        validarDadosEntrada(inputData);

        Fornecedor entidadeEncontrada = buscar(inputData);

        return outputDataConverter.to(entidadeEncontrada);
    }

    private void validarDadosEntrada(BuscarFornecedorPorIdInputData entrada) {
        Validator.of(entrada)
            .validate(BuscarFornecedorPorIdInputData::getId, Objects::nonNull, new FiltroIncompletoException("Ausência do id do fornecedor."))
            .get();
    }

    private Fornecedor buscar(BuscarFornecedorPorIdInputData inputData) {
        return sistemaDeFornecedoresIntegration.buscarPorId(inputData.getId());
    }
}
