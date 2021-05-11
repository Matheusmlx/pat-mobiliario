package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.EmpenhoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.converter.CadastrarEmpenhoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception.IncorporacaoNaoExisteException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.empenho.cadastro.exception.QuantidadeExcedidaException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@AllArgsConstructor
public class CadastrarEmpenhoUseCaseImpl implements CadastrarEmpenhoUseCase {

    private final EmpenhoDataProvider empenhoDataProvider;

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final CadastrarEmpenhoOutputDataConverter outputDataConverter;

    @Override
    public CadastrarEmpenhoOutputData executar(CadastrarEmpenhoInputData inputData) {
        validarDadosEntrada(inputData);
        validarQuantidade(inputData);

        final Incorporacao incorporacao = buscarIncorporacao(inputData.getIncorporacaoId());
        validarIncorporacaoEmModoElaboracao(incorporacao);

        Empenho empenho = criaEntidade(inputData);
        Empenho empenhoSalvo = salvar(empenho);

        return outputDataConverter.to(empenhoSalvo);
    }

    private void validarDadosEntrada(CadastrarEmpenhoInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarEmpenhoInputData::getIncorporacaoId, Objects::nonNull, "Incorporação é nula")
            .get();
    }

    private void validarQuantidade(CadastrarEmpenhoInputData inputData) {
        Long quantidadeEmpenhos = empenhoDataProvider.retornaQuantidadePorIncorporacaoId(inputData.getIncorporacaoId());
        if (quantidadeEmpenhos >= 10) {
            throw new QuantidadeExcedidaException();
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

    private Empenho criaEntidade(CadastrarEmpenhoInputData source) {
        Empenho target = Empenho
            .builder()
            .numeroEmpenho(source.getNumeroEmpenho())
            .valorEmpenho(source.getValorEmpenho())
            .incorporacaoId(source.getIncorporacaoId())
            .build();

        if (Objects.nonNull(source.getDataEmpenho())) {
            target.setDataEmpenho(LocalDateTime.ofInstant(source.getDataEmpenho().toInstant(), ZoneId.systemDefault()));
        }

        return target;
    }

    private Empenho salvar(Empenho empenho) {
        return empenhoDataProvider.salvar(empenho);
    }
}
