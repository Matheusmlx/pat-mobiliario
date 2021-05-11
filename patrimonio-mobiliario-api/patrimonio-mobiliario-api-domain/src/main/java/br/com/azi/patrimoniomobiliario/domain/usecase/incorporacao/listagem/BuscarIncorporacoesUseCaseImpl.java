package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem;

import br.com.azi.patrimoniomobiliario.domain.constant.permissoes.PermissaoMobiliarioConstants;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.SessaoUsuarioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.contacontabil.buscar.exception.FiltroIncompletoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.listagem.converter.BuscarIncorporacoesOutputDataConverter;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
public class BuscarIncorporacoesUseCaseImpl implements BuscarIncorporacoesUseCase {

    private IncorporacaoDataProvider incorporacaoDataProvider;

    private SessaoUsuarioDataProvider sessaoUsuarioDataProvider;

    private BuscarIncorporacoesFiltroConverter incorporacoesFiltroConverter;

    private BuscarIncorporacoesOutputDataConverter outputDataConverter;

    @Override
    public BuscarIncorporacoesOutputData executar(BuscarIncorporacoesInputData inputdata) {
        validarDadosEntrada(inputdata);
        Incorporacao.Filtro filtro = criarFiltro(inputdata);
        ListaPaginada<Incorporacao> entidadesEncontradas = buscar(filtro);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarIncorporacoesInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarIncorporacoesInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarIncorporacoesInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private Incorporacao.Filtro criarFiltro(BuscarIncorporacoesInputData inputData) {
        Incorporacao.Filtro filtro = incorporacoesFiltroConverter.to(inputData);
        filtro.setUsuarioId(sessaoUsuarioDataProvider.get().getId());
        filtro.setFuncoes(List.of(
            PermissaoMobiliarioConstants.NORMAL.getDescription(),
            PermissaoMobiliarioConstants.CONSULTA.getDescription()
        ));
        return filtro;
    }

    private ListaPaginada<Incorporacao> buscar(Incorporacao.Filtro filtro) {
        return incorporacaoDataProvider.buscarPorFiltro(filtro);
    }
}
