package br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem;

import br.com.azi.patrimoniomobiliario.domain.entity.Arquivo;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.entity.Patrimonio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.PatrimonioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.integration.SistemaDeArquivosIntegration;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoFiltroConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.converter.BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.patrimonio.listagem.exception.FiltroIncompletoException;
import lombok.AllArgsConstructor;
import org.springframework.util.Base64Utils;

import java.util.Objects;

@AllArgsConstructor
public class BuscarPatrimoniosPorItemIncorporacaoUseCaseImpl implements BuscarPatrimoniosPorItemIncorporacaoUseCase {

    private PatrimonioDataProvider patrimonioDataProvider;

    private BuscarPatrimoniosPorItemIncorporacaoFiltroConverter patrimoniosPorItemIncorporacaoFiltroConverter;

    private BuscarPatrimoniosPorItemIncorporacaoOutputDataConverter outputDataConverter;

    private SistemaDeArquivosIntegration sistemaDeArquivosIntegration;

    @Override
    public BuscarPatrimoniosPorItemIncorporacaoOutputData executar(BuscarPatrimoniosPorItemIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);
        Patrimonio.Filtro filtro = criarFiltro(inputData);
        ListaPaginada<Patrimonio> entidadesEncontradas = buscar(filtro);
        carregarImagens(entidadesEncontradas);

        return outputDataConverter.to(entidadesEncontradas);
    }

    private void validarDadosEntrada(BuscarPatrimoniosPorItemIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(BuscarPatrimoniosPorItemIncorporacaoInputData::getItemIncorporacaoId, Objects::nonNull, "Item Incorporação é nula")
            .validate(BuscarPatrimoniosPorItemIncorporacaoInputData::getSize, size -> Objects.nonNull(size) && size > 0, new FiltroIncompletoException("Ausência da quantidade de itens por página"))
            .validate(BuscarPatrimoniosPorItemIncorporacaoInputData::getPage, Objects::nonNull, new FiltroIncompletoException("Ausência do número da página"))
            .get();
    }

    private Patrimonio.Filtro criarFiltro(BuscarPatrimoniosPorItemIncorporacaoInputData inputData) {
        return patrimoniosPorItemIncorporacaoFiltroConverter.to(inputData);
    }

    private ListaPaginada<Patrimonio> buscar(Patrimonio.Filtro filtro) {
        return patrimonioDataProvider.buscarPorFiltro(filtro);
    }

    private void carregarImagens(ListaPaginada<Patrimonio> entidades) {
        entidades
            .getItems()
            .forEach(this::carregarImagem);
    }

    private void carregarImagem(Patrimonio entidade){
        if(Objects.nonNull(entidade.getUriImagem())){
            Arquivo arquivo = sistemaDeArquivosIntegration.download(entidade.getUriImagem());
            if(Objects.nonNull(arquivo.getContent())){
                entidade.setImagem(Base64Utils.encodeToString(arquivo.getContent()));
            }
        }
    }
}
