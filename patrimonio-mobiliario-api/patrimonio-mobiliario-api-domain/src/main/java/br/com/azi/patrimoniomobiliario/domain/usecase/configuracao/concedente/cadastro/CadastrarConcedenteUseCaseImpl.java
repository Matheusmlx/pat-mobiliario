package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConcedenteDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.converter.CadastrarConcedenteOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.concedente.cadastro.exception.CpfCnpjIncorretoException;
import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CadastrarConcedenteUseCaseImpl implements CadastrarConcedenteUseCase {

    private ConcedenteDataProvider concedenteDataProvider;

    private CadastrarConcedenteOutputDataConverter outputDataConverter;

    @Override
    public CadastrarConcedenteOutputData executar(CadastrarConcedenteInputData inputdata) {
        validaEntrada(inputdata);
        verificaSeCpfCnpjCadastrado(inputdata);
        Concedente concedente = criaEntidade(inputdata);
        Concedente concedenteSalvo = salvar(concedente);

        return outputDataConverter.to(concedenteSalvo);
    }


    private void validaEntrada(CadastrarConcedenteInputData inputData){
        Validator.of(inputData)
            .validate(CadastrarConcedenteInputData::getCpfCnpj, Objects::nonNull, "O CPF/CNPJ é nulo")
            .validate(CadastrarConcedenteInputData::getNome, Objects::nonNull, "O Nome do concedente é nulo")
            .validate(CadastrarConcedenteInputData::getSituacao, Objects::nonNull, "A Situação é nula")
            .get();
    }
    private void verificaSeCpfCnpjCadastrado(CadastrarConcedenteInputData inputData){
        if(concedenteDataProvider.existePorCpf(inputData.getCpfCnpj())){
            throw new CpfCnpjCadastradoException();
        }
    }

    private Concedente criaEntidade(CadastrarConcedenteInputData inputData){
        Concedente.Pessoa pessoa = verificaPessoa(inputData);
        return Concedente
            .builder()
            .cpfCnpj(inputData.getCpfCnpj())
            .tipoPessoa(pessoa)
            .nome(inputData.getNome())
            .situacao(Concedente.Situacao.valueOf(inputData.getSituacao()))
            .inclusaoSistema(true)
            .build();
    }

    private Concedente salvar(Concedente concedente){
        return concedenteDataProvider.salvar(concedente);
    }

    private Concedente.Pessoa verificaPessoa(CadastrarConcedenteInputData inputData){
        if(verificaCPF(inputData.getCpfCnpj())){
            return Concedente.Pessoa.FISICA;
        }else if(verificaCNPJ(inputData.getCpfCnpj())){
            return Concedente.Pessoa.JURIDICA;
        }
        throw new CpfCnpjIncorretoException();
    }

    public static boolean verificaCPF(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
        try{
            cpfValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean verificaCNPJ(String cpf) {
        CNPJValidator cnpjValidator = new CNPJValidator();
        try{
            cnpjValidator.assertValid(cpf);
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
