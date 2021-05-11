package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.cadastro.converter.CadastrarIncorporacaoOutputDataConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CadastrarIncorporacaoUseCaseImpl implements CadastrarIncorporacaoUseCase {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private CadastrarIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public CadastrarIncorporacaoOutputData executar() {
        Incorporacao incorporacao = criarEntidade();
        Incorporacao incorporacaoSalvo = salvar(incorporacao);
        setCodigoIncorporacao(incorporacaoSalvo);
        salvarCodigoIncorporacao(incorporacaoSalvo);

        return outputDataConverter.to(incorporacaoSalvo);
    }

    private Incorporacao criarEntidade() {
        return Incorporacao
            .builder()
            .situacao(Incorporacao.Situacao.EM_ELABORACAO)
            .build();
    }

    private void setCodigoIncorporacao(Incorporacao incorporacao) {
        incorporacao.setCodigo(incorporacao.getId().toString());
    }

    private void salvarCodigoIncorporacao(Incorporacao incorporacao) {
        incorporacaoDataProvider.salvar(incorporacao);
    }

    private Incorporacao salvar(Incorporacao incorporacao) {
        return incorporacaoDataProvider.salvar(incorporacao);
    }
}
