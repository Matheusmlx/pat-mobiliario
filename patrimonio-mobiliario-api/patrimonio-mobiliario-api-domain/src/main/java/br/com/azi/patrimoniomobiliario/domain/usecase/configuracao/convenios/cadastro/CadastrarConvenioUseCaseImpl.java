package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.converter.CadastrarConvenioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.ConvenioJaCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.cadastro.exception.DataInicioMaiorDataFimException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@AllArgsConstructor
public class CadastrarConvenioUseCaseImpl implements CadastrarConvenioUseCase{

    private ConvenioDataProvider convenioDataProvider;

    private CadastrarConvenioOutputDataConverter outputDataConverter;

    @Override
    public CadastrarConvenioOutputData executar(CadastrarConvenioInputData inputData) {
        validarEntrada(inputData);
        verificarNumero(inputData);
        verificarDataInicioMaiorDataFim(inputData);

        Convenio convenio = criarEntidade(inputData);
        Convenio convenioSalvo = salvar(convenio);

        return outputDataConverter.to(convenioSalvo);
    }

    private void validarEntrada(CadastrarConvenioInputData inputData) {
        Validator.of(inputData)
            .validate(CadastrarConvenioInputData::getNome, Objects::nonNull, "O nome é nulo")
            .validate(CadastrarConvenioInputData::getNumero, Objects::nonNull, "O número é nulo")
            .validate(CadastrarConvenioInputData::getConcedente, Objects::nonNull, "O concedente é nulo")
            .validate(CadastrarConvenioInputData::getSituacao, Objects::nonNull, "A situação é nula")
            .validate(CadastrarConvenioInputData::getDataVigenciaInicio, Objects::nonNull, "A data incial é nula")
            .validate(CadastrarConvenioInputData::getDataVigenciaFim, Objects::nonNull, "A data final é nula")
            .get();
    }

    private void verificarNumero(CadastrarConvenioInputData inputData) {
        if (convenioDataProvider.existePorNumero(inputData.getNumero())) {
            throw new ConvenioJaCadastradoException();
        }
    }

    private void verificarDataInicioMaiorDataFim(CadastrarConvenioInputData inputData) {
        if(inputData.getDataVigenciaInicio().after(inputData.getDataVigenciaFim())) {
            throw new DataInicioMaiorDataFimException();
        }
    }

    private Convenio criarEntidade(CadastrarConvenioInputData inputData) {
        return Convenio
            .builder()
            .nome(inputData.getNome())
            .numero(inputData.getNumero())
            .concedente(
                Concedente.builder()
                .id(inputData.getConcedente())
                .build())
            .fonteRecurso(inputData.getFonteRecurso())
            .dataVigenciaInicio(LocalDateTime.ofInstant(inputData.getDataVigenciaInicio().toInstant(),
                ZoneId.systemDefault()).toLocalDate().atStartOfDay())
            .dataVigenciaFim(LocalDateTime.ofInstant(inputData.getDataVigenciaFim().toInstant(),
                ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59))
            .situacao(Convenio.Situacao.valueOf(inputData.getSituacao()))
            .build();
    }

    private Convenio salvar(Convenio convenio) {
        return convenioDataProvider.salvar(convenio);
    }
}
