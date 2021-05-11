package br.com.azi.patrimoniomobiliario.gateway.dataprovider;

import br.com.azi.patrimoniomobiliario.domain.entity.Convenio;
import br.com.azi.patrimoniomobiliario.domain.entity.ListaPaginada;
import br.com.azi.patrimoniomobiliario.domain.gateway.dataprovider.ConvenioDataProvider;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.ConvenioConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.converter.FiltroConverter;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.ConvenioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.entity.QConvenioEntity;
import br.com.azi.patrimoniomobiliario.gateway.dataprovider.repository.ConvenioRepository;
import br.com.azi.patrimoniomobiliario.utils.AcetuacaoUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ConvenioDataProviderImpl implements ConvenioDataProvider {

    @Autowired
    private ConvenioRepository repository;

    @Autowired
    private ConvenioConverter converter;

    @Override
    public Convenio salvar(Convenio convenio) {
        ConvenioEntity salva = repository.save(converter.from(convenio));
        return converter.to(salva);
    }

    @Override
    @Transactional
    public Optional<Convenio> buscarPorId(Long convenioId) {
        Optional<ConvenioEntity> encontrado = repository.findById(convenioId);
        return encontrado.map(convenioEntity -> converter.to(convenioEntity));
    }

    @Override
    public ListaPaginada<Convenio> buscarPorFiltro(Convenio.Filtro filtro) {
        QConvenioEntity convenioQuery = QConvenioEntity.convenioEntity;
        BooleanExpression expression = convenioQuery.id.isNotNull();

        if(!StringUtils.isEmpty(filtro.getConteudo())){
            BooleanExpression conteudoExp;
            if(Objects.isNull(filtro.getSituacao())) {
                conteudoExp = compararSemAcentuacao(convenioQuery.nome, filtro.getConteudo())
                    .or(convenioQuery.numero.containsIgnoreCase(filtro.getConteudo().trim()))
                    .or(compararSemAcentuacao(convenioQuery.concedente.nome, filtro.getConteudo()))
                    .or(convenioQuery.situacao.containsIgnoreCase(filtro.getConteudo().trim()))
                    .or(convenioQuery.fonteRecurso.containsIgnoreCase(filtro.getConteudo().trim()));
            } else {
                conteudoExp = compararSemAcentuacao(convenioQuery.nome, filtro.getConteudo())
                    .and(convenioQuery.situacao.equalsIgnoreCase(filtro.getSituacao().trim()));
            }
            expression = expression.and(conteudoExp);
        }
        Page<ConvenioEntity> entidadesEncontradas = buscarComOrdenacao(expression, filtro);

        List<Convenio> entidades = entidadesEncontradas
            .getContent()
            .stream()
            .map(converter::to)
            .collect(Collectors.toList());

        return ListaPaginada.<Convenio>builder()
            .items(entidades)
            .totalElements(entidadesEncontradas.getTotalElements())
            .totalPages((long) entidadesEncontradas.getTotalPages())
            .build();

    }

    private Page<ConvenioEntity> buscarComOrdenacao(BooleanExpression expression, Convenio.Filtro filtro) {
        if (filtro.getSort().equalsIgnoreCase("situacao")) {
            return repository.findAll(expression, FiltroConverter.extrairPaginacaoCadastroEdicao(filtro, "dataAlteracao"));
        }
        return repository.findAll(expression, FiltroConverter.extrairPaginacaoComSegundaOrdenacao(filtro,filtro.getSort()));
    }

    @Override
    public Convenio atualizar(Convenio convenio) {
        ConvenioEntity atualizado = repository.save(converter.from(convenio));
        return converter.to(atualizado);
    }

    @Override
    public boolean existePorNumero(String numero) {
        return  repository.existsByNumero(numero);
    }

    @Override
    public boolean existe(Long id) {
        return repository.existsById(id);
    }

    private BooleanExpression compararSemAcentuacao(StringExpression path, String value) {
        StringExpression expr = Expressions.stringTemplate("upper(translate({0}, 'âàãáÁÂÀÃéêÉÊíÍóôõÓÔÕüúÜÚÇç', 'AAAAAAAAEEEEIIOOOOOOUUUUCC'))", path.trim());
        return expr.containsIgnoreCase(AcetuacaoUtils.retiraAcentuacao(value.trim()));
    }
}
