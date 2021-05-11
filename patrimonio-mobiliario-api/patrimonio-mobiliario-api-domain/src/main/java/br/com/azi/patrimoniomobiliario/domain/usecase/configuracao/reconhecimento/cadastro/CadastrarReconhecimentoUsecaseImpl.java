package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.converter.CadastrarReconhecimentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception.ExecucaoOrcamentariaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.cadastro.exception.ReconhecimentoJaCadastradoException;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor
public class CadastrarReconhecimentoUsecaseImpl implements CadastrarReconhecimentoUsecase {

    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    private CadastrarReconhecimentoOutputDataConverter outputDataConverter;

    @Override
    public CadastrarReconhecimentoOutputData executar(CadastrarReconhecimentoInputData inputData) {
        validarDadosEntrada(inputData);
        validarExecucaoOrcamentaria(inputData);
        verificaSeNomeJaExiste(inputData);

        Reconhecimento reconhecimento = criarEntidade(inputData);
        Reconhecimento reconhecimentoSalvo = salvar(reconhecimento);

        return outputDataConverter.to(reconhecimentoSalvo);
    }

    private void validarDadosEntrada(CadastrarReconhecimentoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarReconhecimentoInputData::getNome, Objects::nonNull, "Nome é nulo")
            .validate(CadastrarReconhecimentoInputData::getSituacao, Objects::nonNull, "Situação é nulo")
            .validate(CadastrarReconhecimentoInputData::getExecucaoOrcamentaria, Objects::nonNull, "Execução orçamentária é nula")
            .get();
    }

    private void verificaSeNomeJaExiste(CadastrarReconhecimentoInputData inputData) {
        if (reconhecimentoDataProvider.existePorNome(inputData.getNome().trim())) {
            throw new ReconhecimentoJaCadastradoException();
        }
    }

    private void validarExecucaoOrcamentaria(CadastrarReconhecimentoInputData inputData) {
        if(inputData.getExecucaoOrcamentaria().equals(true) && !(inputData.getEmpenho() || inputData.getNotaFiscal())) {
            throw new ExecucaoOrcamentariaException();
        }
    }

    private Reconhecimento criarEntidade(CadastrarReconhecimentoInputData inputData) {
        return Reconhecimento
            .builder()
            .nome(inputData.getNome().trim())
            .situacao(Reconhecimento.Situacao.valueOf(inputData.getSituacao()))
            .execucaoOrcamentaria(inputData.getExecucaoOrcamentaria())
            .notaFiscal(inputData.getNotaFiscal())
            .empenho(inputData.getEmpenho())
            .build();
    }

    private Reconhecimento salvar(Reconhecimento reconhecimento) {
        return reconhecimentoDataProvider.salvar(reconhecimento);
    }
}
