package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.converter.EditarEmpenhoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.EmpenhoNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.EmpenhoNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.edicao.exception.IncorporacaoNaoExisteException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarEmpenhoUseCaseImpl implements EditarEmpenhoUseCase {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final EditarEmpenhoOutputDataConverter outputDataConverter;

    @Override
    public EditarEmpenhoOutputData executar(EditarEmpenhoInputData inputData) {
        validarDadosEntrada(inputData);
        verificarEmpenhoExiste(inputData);

        final Incorporacao incorporacao = buscarIncorporacao(inputData.getIncorporacaoId());
        validarIncorporacaoEmModoElaboracao(incorporacao);

        Empenho empenho = buscar(inputData);
        setarDados(inputData, empenho);
        Empenho empenhoAtualizado = atualizar(empenho);

        return outputDataConverter.to(empenhoAtualizado);
    }

    private void validarDadosEntrada(EditarEmpenhoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarEmpenhoInputData::getId, Objects::nonNull, "Id é nulo.")
            .validate(EditarEmpenhoInputData::getIncorporacaoId, Objects::nonNull, "Id da incorporação é nulo.")
            .get();
    }

    private void verificarEmpenhoExiste(EditarEmpenhoInputData inputData) {
        if (!empenhoDataProvider.existePorId(inputData.getId())) {
            throw new EmpenhoNaoExisteException();
        }
    }

    private Incorporacao buscarIncorporacao(Long incorporacaoId) {
        return incorporacaoDataProvider.buscarPorId(incorporacaoId)
            .orElseThrow(IncorporacaoNaoExisteException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private Empenho buscar(EditarEmpenhoInputData inputData) {
        Optional<Empenho> empenho = empenhoDataProvider.buscarPorId(inputData.getId());
        return empenho.orElseThrow(EmpenhoNaoEncontradoException::new);
    }

    private void setarDados(EditarEmpenhoInputData inputData, Empenho empenho) {
        empenho.setNumeroEmpenho(inputData.getNumeroEmpenho());

        if (Objects.nonNull(inputData.getDataEmpenho())) {
            empenho.setDataEmpenho(LocalDateTime.ofInstant(inputData.getDataEmpenho().toInstant(), ZoneId.systemDefault()));
        } else {
            empenho.setDataEmpenho(null);
        }

        empenho.setValorEmpenho(inputData.getValorEmpenho());
    }

    private Empenho atualizar(Empenho empenho) {
        return empenhoDataProvider.salvar(empenho);
    }

}
