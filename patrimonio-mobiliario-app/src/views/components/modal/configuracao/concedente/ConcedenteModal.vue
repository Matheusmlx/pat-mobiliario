<template>
    <v-dialog v-model="concedenteModal" max-width="1000" @keydown.esc="fecharModal">
        <v-card>
            <v-toolbar dark color="primary">
                <v-toolbar-title>Gerenciar Concedentes</v-toolbar-title>
                <v-spacer/>
                <v-btn class="close__button" text @click="fecharModal">
                    <v-icon>close</v-icon>
                </v-btn>
            </v-toolbar>
            <az-toolbar>
                <botao-novo-barra-superior slot="actions"
                                           @click="adicionarCampo"
                                           :desabilitar="botaoNovoDesabilitado"
                                           mensagem-tooltip="Finalize o concedente em edição antes de criar um novo"/>
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    :maxlengthInput="40"
                    @simple-search="tratarEventoBuscaSimples"
                    simple-search-placeholder="Busque por: concedente, situação ou cpf/cnpj"
                    slot="simpleSearch">
                </az-search>
            </az-toolbar>
            <concedente-modal-listagem
                class="pa-5"
                v-model="concedentes"
                :paginas="paginas"
                :paginacao="todosConcedentes.paginacao"
                :total-itens="totalItens"
                @paginar="tratarEventoPaginar"
                @cancelar="cancelar"
                @salvarConcedente="salvaConcedente"
                @selecionarConcedente="selecionarConcedente"
                @editarConcedente="editaConcedente"
                @desabilitaBotaoNovo="desabilitaBotaoNovo"
                @habilitaBotaoNovo="habilitaBotaoNovo"/>
        </v-card>
    </v-dialog>
</template>

<script>
    import BotaoNovoBarraSuperior from '@/views/components/botoes/BotaoNovoBarraSuperior'
    import ConcedenteModalListagem from './ConcedenteModalListagem'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import _ from 'lodash'

    export default {
        name: 'ConcedenteModal',
        props: ['modalConcedente'],
        components: {ConcedenteModalListagem, BotaoNovoBarraSuperior},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                concedentes: [],
                paginas: 1,
                totalItens: 10,
                botaoNovoDesabilitado: false
            }
        },
        computed: {
            ...mapState({todosConcedentes: state => state.concedente.todosConcedentes}),
            concedenteModal: {
                get: function () {
                    return this.modalConcedente
                },
                set: function (condicao) {
                    if (!condicao) {
                        this.fecharModal()
                    }
                }
            }
        },
        mounted() {
            this.buscar()
        },
        methods: {
            ...mapMutations([
                mutationTypes.CONFIGURACAO.CONCEDENTE.SET_FILTROS_BUSCA_TODOS_CONCEDENTES,
                mutationTypes.CONFIGURACAO.CONCEDENTE.SET_PAGINACAO_BUSCA_TODOS_CONCEDENTES,
                mutationTypes.CONFIGURACAO.CONCEDENTE.RESETA_PAGE_CONCEDENTE
            ]),
            ...mapActions([
                actionTypes.CONFIGURACAO.CONCEDENTE.BUSCAR_TODOS_CONCEDENTES,
                actionTypes.CONFIGURACAO.CONCEDENTE.SALVAR_CONCEDENTE,
                actionTypes.CONFIGURACAO.CONCEDENTE.EDITAR_CONCEDENTE
            ]),
            adicionarCampo() {
                if (!this.validarSeExisteNovoConcedente()) {
                    this.concedentes.unshift({
                        cpfCnpj: '',
                        nome: '',
                        situacao: 'ATIVO'
                    })
                    this.totalItens += 1
                    this.desabilitaBotaoNovo()
                }
            },
            desabilitaBotaoNovo() {
                this.botaoNovoDesabilitado = true
            },
            habilitaBotaoNovo() {
                this.botaoNovoDesabilitado = false
            },
            async buscar() {
                this.setFiltrosBuscaTodosConcedentes(this.getFiltrosInterno())
                await this.buscarConcedentes()
            },
            async buscarConcedentes() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarTodosConcedentes()
                this.habilitarLoadingGlobal()
                if (resultado) {
                    this.concedentes = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalItens
                }
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPageConcedente()
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosConcedentes(paginacao)
                this.resetarPaginacaoSortBy(paginacao)
                this.buscar()
            },
            tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                this.buscar()
            },
            resetarPaginacaoSortBy(paginacao) {
                if (!paginacao.sortBy[0]) {
                    this.todosConcedentes.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.concedente.todosConcedentes.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            cancelar() {
                this.itens = []
                this.buscar()
                this.habilitaBotaoNovo()
            },
            fecharModal() {
                this.$emit('fecharModal')
            },
            removerCampo() {
                this.itens.splice(0, 1)
            },
            async salvaConcedente(concedente) {
                this.setMensagemLoading('Salvando o concedente...')
                await this.salvarConcedente(concedente)
                this.mostrarNotificacaoSucessoDefault()
                await this.buscarConcedentes()
                this.habilitaBotaoNovo()
            },
            selecionarConcedente(concedente) {
                this.$emit('selecionarConcedente', concedente)
            },
            async editaConcedente(concedente) {
                this.setMensagemLoading('Editando o concedente...')
                await this.editarConcedente(concedente)
                this.mostrarNotificacaoSucessoDefault()
                await this.buscarConcedentes()
                this.habilitaBotaoNovo()
            },
            validarSeExisteNovoConcedente() {
                if (this.concedentes.length === 0) {
                    return false
                } else {
                    let novoConcedentes = this.concedentes.filter((concedente) => {
                        return concedente && !concedente.id
                    })
                    return novoConcedentes.length !== 0
                }
            }
        }
    }
</script>

<style lang="stylus">
    .v-dialog
        overflow hidden

    .close__button::before
        background transparent

    .az-table-list
        .v-text-field__details
            display flex !important
            margin-top 2px

        .v-input__slot
            border none
            font-size 0.8em
            padding-left 0
            padding-right 0
            max-height 65px

            input
                color #777
                border-color #777

    .input >>> .v-input__control
        background-color white

    .input >>> .v-input
        max-width 300px

    .v-data-table
        cursor pointer !important

        tbody tr
            button
                visibility visible
                float right !important
                margin-top 3px
                margin-right 2%

        button
            visibility hidden
            text-align center !important

        .concedente-situacao
            margin-top 10px
            margin-bottom 10px
            display inline-flex

    .v-data-table tbody tr:after
        cursor pointer !important
</style>
