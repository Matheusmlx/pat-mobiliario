package br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Concedente;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.converter.EditarConvenioOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.ConvenioJaCadastradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.ConvenioNaoEncontradoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.configuracao.convenios.edicao.exception.DataInicioMaiorDataFimException;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarConvenioUseCaseImpl implements EditarConvenioUseCase {

    private ConvenioDataProvider convenioDataProvider;

    private EditarConvenioOutputDataConverter outputDataConverter;

    @Override
    public EditarConvenioOutputData executar(EditarConvenioInputData inputData) {
        validarEntrada(inputData);
        validarConvenioExiste(inputData);
        verificarDataInicioMaiorDataFim(inputData);

        Convenio convenio = buscar(inputData);
        verificarNumero(inputData, convenio);

        setarDados(convenio, inputData);
        Convenio convenioSalvo = atualizar(convenio);

        return outputDataConverter.to(convenioSalvo);
    }

    private void validarEntrada(EditarConvenioInputData inputData) {
        Validator.of(inputData)
            .validate(EditarConvenioInputData::getNome, Objects::nonNull, "O nome é nulo")
            .validate(EditarConvenioInputData::getNumero, Objects::nonNull, "O número é nulo")
            .validate(EditarConvenioInputData::getConcedente, Objects::nonNull, "O concedente é nulo")
            .validate(EditarConvenioInputData::getSituacao, Objects::nonNull, "A situação é nula")
            .validate(EditarConvenioInputData::getDataVigenciaInicio, Objects::nonNull, "A data incial é nula")
            .validate(EditarConvenioInputData::getDataVigenciaFim, Objects::nonNull, "A data final é nula")
            .get();
    }

    private void validarConvenioExiste(EditarConvenioInputData input){
        if(!convenioDataProvider.existe(input.getId())) {
            throw new ConvenioNaoEncontradoException();
        }
    }

    private void verificarNumero(EditarConvenioInputData inputData, Convenio convenio) {
        if (!inputData.getNumero().equals(convenio.getNumero()) && convenioDataProvider.existePorNumero(inputData.getNumero())) {
            throw new ConvenioJaCadastradoException();
        }
    }

    private void verificarDataInicioMaiorDataFim(EditarConvenioInputData inputData) {
        if(inputData.getDataVigenciaInicio().after(inputData.getDataVigenciaFim())) {
            throw new DataInicioMaiorDataFimException();
        }
    }

    private Convenio buscar(EditarConvenioInputData inputData) {
        Optional<Convenio> convenio = convenioDataProvider.buscarPorId(inputData.getId());
        return convenio.orElseThrow(ConvenioNaoEncontradoException::new);
    }

    private void setarDados(Convenio convenio, EditarConvenioInputData input) {
        convenio.setNumero(input.getNumero());
        convenio.setNome(input.getNome());
        convenio.setConcedente(Concedente.builder()
            .id(input.getConcedente())
            .build());
        convenio.setFonteRecurso(input.getFonteRecurso());
        convenio.setDataVigenciaInicio(LocalDateTime.ofInstant(input.getDataVigenciaInicio().toInstant(), ZoneId.systemDefault()));
        convenio.setDataVigenciaFim(LocalDateTime.ofInstant(input.getDataVigenciaFim().toInstant(), ZoneId.systemDefault()));
        convenio.setSituacao(Convenio.Situacao.valueOf(input.getSituacao()));
    }

    private Convenio atualizar(Convenio convenio) {
        return convenioDataProvider.atualizar(convenio);
    }
}
