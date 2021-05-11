<template>
    <div>
        <az-toolbar>
            <botao-novo-barra-superior slot="actions"
                                       @click="prepararParaInserirNovoReconhecimento"
                                       :desabilitar="botaoNovoDesabilitado"
                                       mensagem-tooltip="Finalize o reconhecimento em edição antes de criar um novo"/>
            <az-search :filter="filtrosInterno"
                       @remove-filter="tratarEventoRemoverFiltro"
                       @simple-search="tratarEventoBuscaSimples"
                       simple-search-placeholder="Busque por: nome, situação ou exec. orçamentária"
                       :maxlengthInput="500"
                       class="seachReconhecimento"
                       slot="simpleSearch"/>
        </az-toolbar>
        <reconhecimento-listagem
            v-model="itens"
            :paginacao="todosReconhecimentos.paginacao"
            :paginas="paginas"
            :total-itens="totalItens"
            :permitir-edicao="permitirAcoes"
            @atualizarReconhecimento="atualizarReconhecimento"
            @cancelarEdicao="cancelarEdicao"
            @desabilitaBotaoNovo="desabilitaBotaoNovo"
            @habilitaBotaoNovo="habilitaBotaoNovo"
            @inserirNovoReconhecimento="inserirNovoReconhecimento"
            @paginar="tratarEventoPaginar"
        />
    </div>
</template>

<script>
    import BotaoNovoBarraSuperior from '@/views/components/botoes/BotaoNovoBarraSuperior'
    import ReconhecimentoListagem from './listagem/ReconhecimentoListagem'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import _ from 'lodash'

    export default {
        components: {
            BotaoNovoBarraSuperior,
            ReconhecimentoListagem
        },
        data() {
            return {
                filtrosInterno: this.buscarFiltros(),
                paginas: 0,
                itens: [],
                totalItens: 0,
                permitirAcoes: true,
                botaoNovoDesabilitado: false
            }
        },
        computed: {
            ...mapState({todosReconhecimentos: state => state.reconhecimento.todosReconhecimentos})
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RECONHECIMENTO.EDITAR_RECONHECIMENTO,
                actionTypes.CONFIGURACAO.RECONHECIMENTO.BUSCAR_TODOS_RECONHECIMENTOS,
                actionTypes.CONFIGURACAO.RECONHECIMENTO.INSERIR_RECONHECIMENTO
            ]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_FILTROS_BUSCA_TODOS_RECONHECIMENTOS,
                mutationTypes.CONFIGURACAO.RECONHECIMENTO.SET_PAGINACAO_BUSCA_TODOS_RECONHECIMENTOS,
                mutationTypes.CONFIGURACAO.RECONHECIMENTO.RESETA_PAGE_RECONHECIMENTO
            ]),
            async atualizarReconhecimento(reconhecimento) {
                if(await this.editarReconhecimento(reconhecimento)) {
                    this.habilitaBotaoNovo()
                    this.permitirAcoes = true
                }
                await this.buscarReconhecimentos()
                this.mostrarNotificacaoAviso('Atenção: alterar os dados do reconhecimento não afeta incorporações finalizadas anteriormente!')
            },
            async buscarReconhecimentos() {
                this.setFiltrosBuscaTodosReconhecimentos(this.buscarFiltrosInterno())
                this.setMensagemLoading('Carregando ...')
                const resultado = await this.buscarTodosReconhecimentos()
                this.itens = resultado.items
                this.paginas = resultado.totalPages
                this.totalItens = resultado.totalElements
            },
            buscarFiltros() {
                return _.cloneDeep(this.$store.state.reconhecimento.todosReconhecimentos.filtros)
            },
            buscarFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            async cancelarEdicao() {
                this.habilitaBotaoNovo()
                await this.buscarReconhecimentos()
            },
            desabilitaBotaoNovo() {
                this.botaoNovoDesabilitado = true
            },
            habilitaBotaoNovo() {
                this.botaoNovoDesabilitado = false
            },
            async inserirNovoReconhecimento(novoReconhecimento) {
                if(await this.inserirReconhecimento(novoReconhecimento)) {
                    this.permitirAcoes = true
                    this.habilitaBotaoNovo()
                }
                await this.buscarReconhecimentos()
                this.mostrarNotificacaoSucessoDefault()
            },
            async prepararParaInserirNovoReconhecimento() {
                if (!this.validarSeExisteNovoReconhecimento()) {
                    await this.buscarReconhecimentos()
                    this.itens.unshift({
                        nome: '',
                        situacao: 'ATIVO',
                        execucaoOrcamentaria: null,
                        notaFiscal: false,
                        empenho: false
                    })
                    if (this.paginas === 0 && this.totalItens === 0) {
                        this.paginas = 1
                        this.totalItens = 1
                    }
                }
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPageReconhecimento()
                this.filtrosInterno.conteudo.value = valor
                await this.buscarReconhecimentos()
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosReconhecimentos(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                await this.buscarReconhecimentos()
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscarReconhecimentos()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.todosReconhecimentos.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            validarSeExisteNovoReconhecimento() {
                if (this.itens.length === 0) {
                    return false
                } else {
                    let novoReconhecimento = this.itens.filter((item) => {
                        return item && !item.id
                    })
                    return novoReconhecimento.length !== 0
                }
            }
        }
    }
</script>
<style lang="stylus">
    .seachReconhecimento .input-search
        background-color #fff !important
        width 28vw !important

        span
            max-width 350px

        .v-chip__content
            width 100%

            span
                white-space nowrap
                overflow hidden
                text-overflow ellipsis

    @media (max-width 1250px)
        .seachReconhecimento .input-search
            background-color #fff !important
            width 40vw !important

            span
                max-width 230px

    @media (max-width 890px)
        .seachReconhecimento .input-search
            background-color #fff !important
            width 50vw !important

    @media (max-width 720px)
        .seachReconhecimento .input-search
            background-color #fff !important
            width 60vw !important
</style>
