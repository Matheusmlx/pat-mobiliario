<template>
    <div>
        <div v-if="existeItens" class="itens-listagem-tabela">
            <az-toolbar class="incorporacao-item-toolbar">
                <az-call-to-action
                    slot="actions"
                    :disabled="!verificaPermissaoEdicao"
                    active
                    @click="adicionarItem">
                    <v-icon>add_circle</v-icon>
                    Adicionar Item
                </az-call-to-action>
                <v-flex slot="simpleSearch">
                    <az-search
                        :filter="filtrosInterno"
                        @remove-filter="tratarEventoRemoverFiltro"
                        @simple-search="tratarEventoBuscaSimples"
                        :maxlengthInput="500"
                        id="azSearch"
                        simple-search-placeholder="Busque por código ou descrição"
                        class="searchItemIncorporacaoListagem">
                    </az-search>
                </v-flex>
            </az-toolbar>
            <itens-tabela
                :itens="itens"
                :paginas="paginas"
                :total-itens="totalItens"
                :paginacao="$store.state.itemIncorporacao.resultadoBuscaTodosItensIncorporacao.paginacao"
                @deletar="tratarEvendtoDeletarItem"
                @acessar="tratarEventoAcessar"
                @paginar="tratarEventoPaginar"/>
        </div>
        <itens-listagem-vazio v-else @adicionarItem="adicionarItem"/>

        <router-view @fechar="buscaTodasIncorporacoes"/>

        <acoes-botoes-continuar-voltar :controleContinuar="existeItens" @voltar="voltarPasso1"
                                @continuar="tratarEventoContinuar"/>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {createNamespacedHelpers, mapActions, mapMutations, mapState} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import ItensTabela from './ItensTabela'
    import ItensListagemVazio from './ItensListagemVazio'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'ItensIncorporacao',
        components: {ItensListagemVazio, ItensTabela, AcoesBotoesContinuarVoltar},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
                incorporacaoId: null,
            }
        },
        computed: {
            existeItens() {
                return !!(this.totalItens > 0 || (this.totalItens === 0 && this.filtrosInterno.conteudo.value))
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            ...mapGetters([
                'getItemIncorporacaoValidado'
            ]),
            ...mapState({todosItensIncorporacao: state => state.itemIncorporacao.resultadoBuscaTodosItensIncorporacao})
        },
        async mounted() {
            this.setIncorporacaoId()
            await this.buscaTodasIncorporacoes()
            if (!this.verificaPermissaoEdicao) {
                this.itensListagemVisualizacao()
            }
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_TODOS_ITENS_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.DELETAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.CADASTRAR_PATRIMONIO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_FILTROS_BUSCA_TODOS_ITENS_INCORPORACAO,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_TODOS_ITENS_INCORPORACAO,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_INCORPORACAO_ITEM
            ]),
            adicionarItem() {
                if (this.verificaPermissaoEdicao) {
                    this.$router.push({name: 'ItensCatalogoModalNovo', params: {incorporacaoId: this.incorporacaoId}})
                } else {
                    this.$router.push({
                        name: 'ItensCatalogoModalVisualizacao',
                        params: {incorporacaoId: this.incorporacaoId}
                    })
                }
            },
            async buscar() {
                this.setFiltrosBuscaTodosItensIncorporacao(this.getFiltrosInterno())
                await this.buscaTodasIncorporacoes()
            },
            async buscaTodasIncorporacoes() {
                const resultado = await this.buscarTodosItensIncorporacao(this.incorporacaoId)
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
                this.controlaPasso3()
            },
            controlaPasso3() {
                if (this.existeItens) {
                    this.$emit('habilitaPasso3')
                } else {
                    this.$emit('desabilitaPasso3')
                }
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.itemIncorporacao.resultadoBuscaTodosItensIncorporacao.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
            setIncorporacaoId() {
                this.incorporacaoId = this.$route.params.incorporacaoId
            },
            itensListagemVisualizacao() {
                this.$router.push({
                    name: 'VisualizarItensIncorporacao',
                    params: {
                        incorporacaoId: this.incorporacaoId
                    }
                })
            },
            async tratarEventoAcessar(item) {
                let nomeRota = 'DadosGeraisDoItemModal'

                if (this.verificaPermissaoEdicao) {
                    if (this.getItemIncorporacaoValidado(item, this.tipoVeiculo(item), 'ITEM_INCORPORACAO')) {
                        await this.cadastraPatrimonio(item)
                        nomeRota = 'IncorporacaoItemListagemPatrimonios'
                    }
                    this.$router.push({
                        name: nomeRota,
                        params: {incorporacaoId: this.incorporacaoId, itemIncorporacaoId: item.id}
                    })
                } else {
                    nomeRota = 'VisualizacaoDadosGerais'
                    if (this.getItemIncorporacaoValidado(item, this.tipoVeiculo(item), 'ITEM_INCORPORACAO')) {
                        nomeRota = 'IncorporacaoItemListagemPatrimoniosVisualizacao'
                    }
                    this.$router.push({
                        name: nomeRota,
                        params: {incorporacaoId: this.incorporacaoId, itemIncorporacaoId: item.id}
                    })
                }
            },
            tipoVeiculo(item) {
                return item.tipoBem === 'VEICULO'
            },
            async cadastraPatrimonio(item) {
                if (item.quantidade && item.valorTotal && item.contaContabil && item.id) {
                    const patrimonio = {
                        quantidade: item.quantidade,
                        valorTotal: item.valorTotal,
                        contaContabilId: item.contaContabil,
                        itemIncorporacaoId: item.id
                    }
                    await this.cadastrarPatrimonio(patrimonio)
                }
            },
            tratarEventoBuscaSimples(valor) {
                this.resetaPageIncorporacaoItem()
                this.filtrosInterno.conteudo.value = valor
                this.buscar()
            },
            tratarEventoContinuar() {
                if(this.verificaPermissaoEdicao) {
                    this.$router.push({
                        name: 'IncorporacaoDocumentosEdicao',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                } else{
                    this.$router.push({
                        name: 'VisualizarIncorporacaoDocumentos',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                }
            },
            async tratarEvendtoDeletarItem(id) {
                if(!this.verificaPermissaoEdicao) return
                const dados = {idIncorporacao: this.incorporacaoId, idItem: id}
                await this.deletarItemIncorporacao(dados)
                this.buscar()
            },
            tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosItensIncorporacao(paginacao)
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
                    this.todosItensIncorporacao.paginacao.sortBy[0] = paginacao.defaultSortBy
                }
            },
            voltarPasso1() {
                if (this.verificaPermissaoEdicao) {
                    this.$router.push({
                        name: 'EditarIncorporacao',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                } else{
                    this.$router.push({
                        name: 'VisualizarIncorporacao',
                        params: {incorporacaoId: this.incorporacaoId},
                    })
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
    .itens-listagem-tabela
        min-height 62vh

    >>>
        .searchItemIncorporacaoListagem .input-search
            background-color #fff !important

            span
                max-width 200px

            .v-chip__content
                width 100%

                span
                    white-space nowrap
                    overflow hidden
                    text-overflow ellipsis

    .text-info
        color #777

    .incorporacao-item-toolbar
        background-color white
</style>
