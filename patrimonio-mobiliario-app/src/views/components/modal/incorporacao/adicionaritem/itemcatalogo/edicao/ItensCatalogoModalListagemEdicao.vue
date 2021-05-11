<template>
    <div>
        <v-layout column>
                <v-data-table @click:row="selecionaItem" item-key="codigo" single-select
                              :headers="colunas"
                              :items="value"
                              :loading="false"
                              :server-items-length="totalItens"
                              :options.sync="paginacaoInterna"
                              class="tabela-modal-incorporacao-itens item-catalogo-tabela"
                              hide-default-footer
                              disable-pagination
                              must-sort
                              no-data-text="Não há itens cadastrados">
                    <template v-slot:header.codigo>
                        Código
                    </template>
                    <template v-slot:header.descricao>
                        Descrição
                    </template>
                    <template v-slot:header.adiconado>
                    </template>

                    <template v-slot:item.codigo="{ item }">
                        <span class="d-inline-block text-truncate max-16">{{item.codigo}}</span>
                    </template>
                    <template v-slot:item.descricao="{item}">
                        <span class="d-inline-block">{{item.descricao}}</span>
                    </template>
                    <template v-slot:item.adicionado="{item}">
                        <span v-if='itemSelecionado.codigo === item.codigo'><v-icon
                            size="15">fas fa-check</v-icon></span>
                    </template>
                </v-data-table>
            <paginacao :paginacao-interna="paginacaoInterna" :paginas="paginas" @resetaPage="resetaPage" class="mr-5 ml-5"/>
        </v-layout>
    </div>
</template>

<script>
    import {createNamespacedHelpers, mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'
    import Paginacao from '@/views/components/tabela/Paginacao'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'ItensCatalogoModalListagem',
        components: {Paginacao},
        props: ['value', 'paginas', 'paginacao', 'totalItens'],
        data() {
            return {
                itemSelecionado: {
                    codigo: null
                },
                paginacaoInterna: this.paginacao
            }
        },
        computed: {
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            colunas(){
                return this.criarColunas(3,
                                         ['Código','Descrição'],
                                         ['codigo','descricao','adicionado'],
                                         [true,true,false],
                                         ['left','left','left'],
                                         ['20%','75%','5%'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            }
        },
        methods: {
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_INCORPORACAO_ITEM
            ]),
            selecionaItem(item, row) {
                row.select(true)
                this.itemSelecionado = item
                this.setIncorporacaoItem(item)
                this.$emit('camposAceitos')
            },
            tratarPaginacao(pagina) {
                this.paginacaoInterna.page = pagina
            },
            resetaPage() {
                this.$store.commit(mutationTypes.ITEM_CATALOGO.RESETA_PAGE_CATALOGO)
            }
        }
    }
</script>

<style lang="stylus">
    .acoes
        height 100%

    tr.v-data-table__selected
        background #487B9B !important
        color white !important

    tr.v-data-table__selected td .v-icon
        color white !important

    tr.v-data-table__selected td
        color white !important
</style>
