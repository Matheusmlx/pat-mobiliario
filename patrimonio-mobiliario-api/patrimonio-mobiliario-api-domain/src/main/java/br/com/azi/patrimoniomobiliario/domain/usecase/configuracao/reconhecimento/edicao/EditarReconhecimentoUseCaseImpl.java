package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ReconhecimentoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.converter.EditarReconhecimentoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.EditarReconhecimentoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.ExecucaoOrcamentariaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.ReconhecimentoJaCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.reconhecimento.edicao.exception.ReconhecimentoNaoEncontradoException;
import lombok.AllArgsConstructor;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarReconhecimentoUseCaseImpl implements EditarReconhecimentoUseCase {

    private ReconhecimentoDataProvider reconhecimentoDataProvider;

    private EditarReconhecimentoOutputDataConverter outputDataConverter;

    @Override
    public EditarReconhecimentoOutputData executar(EditarReconhecimentoInputData inputData) {
        validarEntrada(inputData);
        validarExecucaoOrcamentaria(inputData);
        validarReconhecimentoExiste(inputData);
        verificaSeNomeJaExiste(inputData);

        Reconhecimento reconhecimento = buscar(inputData);
        setarDados(reconhecimento, inputData);
        Reconhecimento reconhecimentoSalvo = atualizar(reconhecimento);

        return outputDataConverter.to(reconhecimentoSalvo);
    }

    private void validarEntrada(EditarReconhecimentoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarReconhecimentoInputData::getNome, Objects::nonNull, "O nome é nulo")
            .validate(EditarReconhecimentoInputData::getSituacao, Objects::nonNull, "a situação é nula")
            .validate(EditarReconhecimentoInputData::getExecucaoOrcamentaria, Objects::nonNull, "A execução orçamentária é nula")
            .get();
    }

    private void validarExecucaoOrcamentaria(EditarReconhecimentoInputData inputData) {
        if(inputData.getExecucaoOrcamentaria().equals(true) && !(inputData.getEmpenho() || inputData.getNotaFiscal())) {
            throw new ExecucaoOrcamentariaException();
        }
    }

    private void validarReconhecimentoExiste(EditarReconhecimentoInputData input){
        if(!reconhecimentoDataProvider.existe(input.getId())) {
            throw new ReconhecimentoNaoEncontradoException();
        }
    }


    private void verificaSeNomeJaExiste(EditarReconhecimentoInputData inputData){
        if(reconhecimentoDataProvider.existePorNome(inputData.getNome()) &&  verificaMesmoId(inputData)){
            throw new ReconhecimentoJaCadastradoException();
        }
    }

    private boolean verificaMesmoId(EditarReconhecimentoInputData inputData) {
        return !buscarNomeReconhecimentoUnico(inputData).getId().equals(inputData.getId());
    }

    private Reconhecimento buscarNomeReconhecimentoUnico(EditarReconhecimentoInputData inputData){
        Optional<Reconhecimento> reconhecimento = reconhecimentoDataProvider.buscarReconhecimentoComNome(inputData.getId(),inputData.getNome());
        return (reconhecimento.orElseThrow(EditarReconhecimentoException::new));
    }
    private Reconhecimento buscar(EditarReconhecimentoInputData inputData) {
        Optional<Reconhecimento> reconhecimento = reconhecimentoDataProvider.buscarPorId(inputData.getId());
        return reconhecimento.orElseThrow(ReconhecimentoNaoEncontradoException::new);
    }

    private void setarDados(Reconhecimento reconhecimento, EditarReconhecimentoInputData input) {
        reconhecimento.setNome(input.getNome());
        reconhecimento.setSituacao(Reconhecimento.Situacao.valueOf(input.getSituacao()));
        reconhecimento.setExecucaoOrcamentaria(input.getExecucaoOrcamentaria());
        reconhecimento.setNotaFiscal(input.getNotaFiscal());
        reconhecimento.setEmpenho(input.getEmpenho());
    }

    private Reconhecimento atualizar(Reconhecimento reconhecimento) { return reconhecimentoDataProvider.atualizar(reconhecimento); }
}
