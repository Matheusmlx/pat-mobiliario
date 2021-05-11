package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.buscarporid.exception.ConcedenteNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.converter.EditarConcedenteOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.edicao.exception.ConcedenteNaoExisteException;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class EditarConcedenteUseCaseImpl implements EditarConcedenteUseCase{

    private ConcedenteDataProvider concedenteDataProvider;

    private EditarConcedenteOutputDataConverter outputDataConverter;

    @Override
    public EditarConcedenteOutputData executar(EditarConcedenteInputData inputData) {
        validarDadosEntrada(inputData);
        verificarConcedenteExiste(inputData);

        Concedente concedente = buscar(inputData);
        verificaSeCpfCnpjJaCadastrado(inputData, concedente);

        setarDados(concedente, inputData);
        Concedente concedenteSalvo = atualizar(concedente);

        return outputDataConverter.to(concedenteSalvo);
    }

    private void validarDadosEntrada(EditarConcedenteInputData inputData) {
        Validator.of(inputData)
            .validate(EditarConcedenteInputData::getId, Objects::nonNull, "Id é nulo")
            .validate(EditarConcedenteInputData::getCpfCnpj, Objects::nonNull, "Cpf/cnpj é nulo")
            .validate(EditarConcedenteInputData::getNome, Objects::nonNull, "Nome é nulo")
            .validate(EditarConcedenteInputData::getSituacao, Objects::nonNull, "Situação é nula")
            .get();
    }

    private void verificarConcedenteExiste(EditarConcedenteInputData inputData) {
        if(!concedenteDataProvider.existe(inputData.getId())) {
            throw new ConcedenteNaoExisteException();
        }
    }

    private Concedente buscar(EditarConcedenteInputData inputData) {
        return concedenteDataProvider.buscarPorId(inputData.getId()).orElseThrow(ConcedenteNaoEncontradoException::new);
    }

    private void verificaSeCpfCnpjJaCadastrado(EditarConcedenteInputData inputData, Concedente concedente) {
        if(!inputData.getCpfCnpj().equals(concedente.getCpfCnpj())) {
            verificaSeCpfCnpjCadastrado(inputData);
        }
    }

    private void setarDados(Concedente concedente, EditarConcedenteInputData inputData) {
        concedente.setTipoPessoa(verificaPessoa(inputData));
        concedente.setCpfCnpj(inputData.getCpfCnpj());
        concedente.setNome(inputData.getNome());
        concedente.setSituacao(Concedente.Situacao.valueOf(inputData.getSituacao()));
    }

    private Concedente atualizar(Concedente concedente) {
        return concedenteDataProvider.salvar(concedente);
    }

    private void verificaSeCpfCnpjCadastrado(EditarConcedenteInputData inputData){
        if(concedenteDataProvider.existePorCpf(inputData.getCpfCnpj())){
            throw new CpfCnpjCadastradoException();
        }
    }

    private Concedente.Pessoa verificaPessoa(EditarConcedenteInputData inputData){
        return Concedente.Pessoa.valueOf(StringUtils.verificaPessoa(inputData.getCpfCnpj()).name());
    }

}
