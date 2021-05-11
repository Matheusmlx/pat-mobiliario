package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ItemIncorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ItemIncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.converter.BuscarItensIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.item.listagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;
import org.springframework.util.Base64Utils;

import java.util.Objects;

@AllArgsConstructor
public class BuscarItensIncorporacaoUseCaseImpl implements BuscarItensIncorporacaoUseCase {

    private ItemIncorporacaoDataProvider itemIncorporacaoDataProvider;

    private BuscarItensIncorporacaoOutputDataConverter itensIncorporacaoOutputDataConverter;

    private BuscarItensIncorporacaoFiltroConverter itensIncorporacaoFiltroConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public BuscarItensIncorporacaoOutputData executar(BuscarItensIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        ItemIncorporacao.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<ItemIncorporacao> entidadesEncontradas = buscar(filtro);
        carregarImagens(entidadesEncontradas);

        return itensIncorporacaoOutputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarItensIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarItensIncorporacaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de registros por página."))
            .validate(BuscarItensIncorporacaoInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página."))
            .get();
    }

    private ItemIncorporacao.Filtro criarFiltro(BuscarItensIncorporacaoInputData inputData) {
        return this.itensIncorporacaoFiltroConverter.to(inputData);
    }

    private ListaPaginada<ItemIncorporacao> buscar(ItemIncorporacao.Filtro filtro) {
        return itemIncorporacaoDataProvider.buscarPorIncorporacaoId(filtro);
    }

    private void carregarImagens(ListaPaginada<ItemIncorporacao> entidades) {
        entidades
            .getItems()
            .forEach(this::carregarImagem);
    }

    private void carregarImagem(ItemIncorporacao entidade){
        if(Objects.nonNull(entidade.getUriImagem())){
            Arquivo arquivo = sistemaDeArquivosIntegration.download(entidade.getUriImagem());
            if(Objects.nonNull(arquivo.getContent())){
                entidade.setImagem(Base64Utils.encodeToString(arquivo.getContent()));
            }
        }
    }
}
