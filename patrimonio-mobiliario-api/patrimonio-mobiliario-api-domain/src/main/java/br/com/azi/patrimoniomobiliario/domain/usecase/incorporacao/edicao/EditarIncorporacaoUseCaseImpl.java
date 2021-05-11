package br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao;

import br.com.azi.patrimoniomobiliario.domain.entity.Comodante;
import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.Empenho;
import br.com.azi.patrimoniomobiliario.domain.entity.Fornecedor;
import br.com.azi.patrimoniomobiliario.domain.entity.Incorporacao;
import br.com.azi.patrimoniomobiliario.domain.entity.NaturezaDespesa;
import br.com.azi.patrimoniomobiliario.domain.entity.NotaLancamentoContabil;
import br.com.azi.patrimoniomobiliario.domain.entity.Reconhecimento;
import br.com.azi.patrimoniomobiliario.domain.entity.UnidadeOrganizacional;
import br.com.azi.patrimoniomobiliario.domain.entity.Usuario;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.IncorporacaoDataProvider;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.NotaLancamentoContabilDataProvider;
import br.com.azi.patrimoniomobiliario.domain.usecase.Validator;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.converter.EditarIncorporacaoOutputDataConverter;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoNaoEncontradaException;
import br.com.azi.patrimoniomobiliario.domain.usecase.incorporacao.edicao.exception.IncorporacaoNaoEstaEmElaboracaoException;
import br.com.azi.patrimoniomobiliario.utils.date.DateUtils;
import br.com.azi.patrimoniomobiliario.utils.string.StringUtils;
import br.com.azi.patrimoniomobiliario.utils.validate.NotaLancamentoContabilValidate;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
public class EditarIncorporacaoUseCaseImpl implements EditarIncorporacaoUseCase {

    private final IncorporacaoDataProvider incorporacaoDataProvider;

    private final NotaLancamentoContabilDataProvider notaLancamentoContabilDataProvider;

    private final EditarIncorporacaoOutputDataConverter outputDataConverter;

    @Override
    public EditarIncorporacaoOutputData executar(EditarIncorporacaoInputData inputData) {
        validarDadosEntrada(inputData);

        Incorporacao incorporacao = buscar(inputData);
        validarIncorporacaoEmModoElaboracao(incorporacao);

        setarDados(incorporacao, inputData);
        Incorporacao incorporacaoSalva = atualizar(incorporacao);
        return outputDataConverter.to(incorporacaoSalva);
    }

    private void validarDadosEntrada(EditarIncorporacaoInputData inputData) {
        Validator.of(inputData)
            .validate(EditarIncorporacaoInputData::getId, Objects::nonNull, "Id é nulo")
            .get();
    }

    private Incorporacao buscar(EditarIncorporacaoInputData inputData) {
        Optional<Incorporacao> incorporacao = incorporacaoDataProvider.buscarPorId(inputData.getId());
        return incorporacao.orElseThrow(IncorporacaoNaoEncontradaException::new);
    }

    private void validarIncorporacaoEmModoElaboracao(Incorporacao incorporacao) {
        if (!Incorporacao.Situacao.EM_ELABORACAO.equals(incorporacao.getSituacao()) &&
            !Incorporacao.Situacao.ERRO_PROCESSAMENTO.equals(incorporacao.getSituacao())) {
            throw new IncorporacaoNaoEstaEmElaboracaoException();
        }
    }

    private void setarDados(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        if (Objects.nonNull(inputData.getReconhecimento())) {
            incorporacao.setReconhecimento(
                Reconhecimento.builder()
                    .id(inputData.getReconhecimento())
                    .build()
            );
        }
        if (Objects.nonNull(inputData.getFornecedor())) {
            incorporacao.setFornecedor(
                Fornecedor.builder()
                    .id(inputData.getFornecedor())
                    .build()
            );
        } else {
            incorporacao.setFornecedor(null);
        }
        if (Objects.nonNull(inputData.getOrgao())) {
            incorporacao.setOrgao(
                UnidadeOrganizacional.builder()
                    .id(inputData.getOrgao())
                    .build()
            );
        }
        if (Objects.nonNull(inputData.getSetor())) {
            incorporacao.setSetor(
                UnidadeOrganizacional.builder()
                    .id(inputData.getSetor())
                    .build()
            );
        } else {
            incorporacao.setSetor(null);
        }
        if (Objects.nonNull(inputData.getDataRecebimento())) {
            incorporacao.setDataRecebimento(LocalDateTime.ofInstant(
                inputData.getDataRecebimento().toInstant(),
                ZoneId.systemDefault()
            ));
        }
        if (Objects.nonNull(inputData.getDataNota())) {
            incorporacao.setDataNota(LocalDateTime.ofInstant(
                inputData.getDataNota().toInstant(),
                ZoneId.systemDefault()
            ));
        }
        if (Objects.nonNull(inputData.getConvenio())) {
            incorporacao.setConvenio(
                Convenio.builder()
                    .id(inputData.getConvenio())
                    .build()
            );
        }
        if (Objects.nonNull(inputData.getEmpenho())) {
            incorporacao.setEmpenho(
                Empenho.builder()
                    .id(inputData.getEmpenho())
                    .build()
            );
        }
        if (Objects.nonNull(inputData.getNaturezaDespesa())) {
            incorporacao.setNaturezaDespesa(
                NaturezaDespesa.builder()
                    .id(inputData.getNaturezaDespesa())
                    .build()
            );
        }
        if (Objects.nonNull(inputData.getUsuario())) {
            incorporacao.setUsuario(
                Usuario.builder()
                    .id(inputData.getUsuario())
                    .build()
            );
        }

        if (Objects.nonNull(inputData.getFundo())) {
            incorporacao.setFundo(
                UnidadeOrganizacional.builder()
                    .id(inputData.getFundo())
                    .build());
        }

        if (StringUtils.isNotEmpty(inputData.getProjeto())) {
            incorporacao.setProjeto(inputData.getProjeto());
        }

        if (Objects.nonNull(inputData.getComodante())) {
            incorporacao.setComodante(Comodante
                .builder()
                .id(inputData.getComodante())
                .build());
        }

        incorporacao.setCodigo(inputData.getCodigo());
        incorporacao.setNumProcesso(inputData.getNumProcesso());
        incorporacao.setNota(inputData.getNota());
        incorporacao.setValorNota(inputData.getValorNota());
        atualizarOrigemConvenio(incorporacao, inputData);
        atualizarOrigemFundo(incorporacao, inputData);
        atualizarOrigemProjeto(incorporacao, inputData);
        atualizarOrigemComodato(incorporacao, inputData);
        atualizarNotaLancamentoContabil(incorporacao, inputData);
    }

    private void atualizarOrigemConvenio(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        incorporacao.setOrigemConvenio(inputData.getOrigemConvenio());
        if (Objects.nonNull(inputData.getOrigemConvenio()) && inputData.getOrigemConvenio().equals(Boolean.FALSE)) {
            incorporacao.setConvenio(null);
        }
    }

    private void atualizarOrigemFundo(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        incorporacao.setOrigemFundo(inputData.getOrigemFundo());
        if (Objects.nonNull(inputData.getOrigemFundo()) && inputData.getOrigemFundo().equals(Boolean.FALSE)) {
            incorporacao.setFundo(null);
        }
    }

    private void atualizarOrigemProjeto(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        incorporacao.setOrigemProjeto(inputData.getOrigemProjeto());
        if (Objects.nonNull(inputData.getOrigemProjeto()) && inputData.getOrigemProjeto().equals(Boolean.FALSE)) {
            incorporacao.setProjeto(null);
        }
    }

    private void atualizarOrigemComodato(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        incorporacao.setOrigemComodato(inputData.getOrigemComodato());
        if (Objects.nonNull(inputData.getOrigemComodato()) && inputData.getOrigemComodato().equals(Boolean.FALSE)) {
            incorporacao.setComodante(null);
        }
    }

    private void atualizarNotaLancamentoContabil(Incorporacao incorporacao, EditarIncorporacaoInputData inputData) {
        NotaLancamentoContabil notaLancamentoContabil = incorporacao.getNotaLancamentoContabil();

        if (!deveAtualizarNotaLancamentoContabil(notaLancamentoContabil, inputData)) {
            return;
        }

        if (Objects.isNull(notaLancamentoContabil)) {
            notaLancamentoContabil = new NotaLancamentoContabil();
        }

        if (StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil())) {
            validarNumeroNotaLancamentoContabil(notaLancamentoContabil, inputData);
        }

        if (Objects.nonNull(inputData.getDataNotaLancamentoContabil())) {
            NotaLancamentoContabilValidate.validarDataNotaLancamentoContabil(inputData.getDataNotaLancamentoContabil());
        }

        notaLancamentoContabil.setNumero(inputData.getNumeroNotaLancamentoContabil());
        notaLancamentoContabil.setDataLancamento(DateUtils.asLocalDateTime(inputData.getDataNotaLancamentoContabil()));
        incorporacao.setNotaLancamentoContabil(notaLancamentoContabilDataProvider.salvar(notaLancamentoContabil));
    }

    private boolean deveAtualizarNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarIncorporacaoInputData inputData) {
        return Objects.nonNull(notaLancamentoContabil) ||
            StringUtils.isNotEmpty(inputData.getNumeroNotaLancamentoContabil()) ||
            Objects.nonNull(inputData.getDataNotaLancamentoContabil());
    }

    private void validarNumeroNotaLancamentoContabil(NotaLancamentoContabil notaLancamentoContabil, EditarIncorporacaoInputData inputData) {
        final String numero = inputData.getNumeroNotaLancamentoContabil();

        if (numero.equals(notaLancamentoContabil.getNumero())) {
            return;
        }

        NotaLancamentoContabilValidate.validarFormatoNumeroNotaLancamentoContabil(numero);

        if (notaLancamentoContabilDataProvider.existePorNumero(numero)) {
            throw new IncorporacaoComNumeroNotaLancamentoContabilDuplicadoException();
        }
    }

    private Incorporacao atualizar(Incorporacao incorporacao) {
        return incorporacaoDataProvider.salvar(incorporacao);
    }
}
