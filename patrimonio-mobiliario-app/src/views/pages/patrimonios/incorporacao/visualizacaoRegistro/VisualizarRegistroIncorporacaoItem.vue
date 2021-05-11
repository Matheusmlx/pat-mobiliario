<template>
    <v-container fluid>
        <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
            <v-col cols="12">
                <v-card elevation="0" flat class="itens-incorporacao">
                    <v-expansion-panels flat v-model="exibirPainel">
                        <v-expansion-panel>
                            <v-expansion-panel-header  @click="abrirPanelDocumentos" class="itens-panel-header">
                                <h4 class="session-title">Itens Incorporados</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-data-table
                                    id="listagemTabelaItemIncorporacao"
                                    :headers="colunas"
                                    :items="dadosGerais.itens"
                                    :server-items-length="dadosGerais.totalItens"
                                    :options.sync="paginacaoInterna"
                                    :loading="false"
                                    no-data-text="Não há itens cadastrados"
                                    class="az-table-lista pr-tabela-todos-itens-incorporacao"
                                    must-sort
                                    hide-default-footer
                                    @click:row="tratarEventoAcessar">
                                    <template v-slot:header.imagem>
                                        <label-personalizado campo="uriImagem" :tela="nomeTela" apenas-nome/>
                                    </template>
                                    <template v-slot:header.codigo>
                                        <label-personalizado campo="codigo" :tela="nomeTela" apenas-nome/>
                                    </template>
                                    <template v-slot:header.descricao>
                                        <label-personalizado campo="descricao" :tela="nomeTela" apenas-nome/>
                                    </template>
                                    <template v-slot:header.quantidade>
                                        <label-personalizado campo="quantidade" :tela="nomeTela" apenas-nome/>
                                    </template>
                                    <template v-slot:header.valorTotal>
                                        <label-personalizado campo="valorTotal" :tela="nomeTela" apenas-nome/>
                                    </template>
                                    <template v-slot:item.imagem="{item}">
                                        <v-img :src="'data:image/png;base64,' + item.imagem" max-width="40"
                                               max-height="40" aspect-ratio="1" class="img-item-incorporacao"/>
                                    </template>
                                    <template v-slot:item.codigo="{item}">
                                    <span
                                        class="d-inline-block text-truncate max-10">{{item.codigo | textoSemValor}}</span>
                                    </template>
                                    <template v-slot:item.descricao="{item}">
                                    <span
                                        class="d-inline-block text-truncate max-15">{{item.descricao | textoSemValor}}</span>
                                    </template>
                                    <template v-slot:item.quantidade="{item}">
                                    <span
                                        class="d-inline-block text-truncate max-5">{{item.quantidade | textoSemValor}}</span>
                                    </template>
                                    <template v-slot:item.valorUnitario="{item}">
                                        <span class="d-inline-block text-truncate max-7">{{ item.valorUnitario | valorParaDinheiro}}</span>
                                    </template>
                                    <template v-slot:item.valorTotal="{item}">
                                        <span class="d-inline-block text-truncate max-7">{{ item.valorTotal | valorParaDinheiro}}</span>
                                    </template>
                                    <template v-slot:item.acoes="{item}">
                                        <v-icon>keyboard_arrow_right</v-icon>
                                        <span class="mobile">Acessar</span>
                                    </template>
                                </v-data-table>
                                <paginacao
                                    :paginacao-interna="paginacaoInterna"
                                    :paginas="dadosGerais.paginas"/>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import actionTypes from '@/core/constants/actionTypes'
    import mutationTypes from '@/core/constants/mutationTypes'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import {mapActions, mapMutations} from 'vuex'

    export default {
        name: 'VisualizarRegistroIncorporacaoItem',
        components: {Paginacao, LabelPersonalizado},
        props: {
            incorporacaoId: {
                type: Number,
                required: true
            }
        },
        data: () => ({
            exibirPainel: 0,
            dadosGerais: {
                itens: [],
                totalItens: 0,
                paginas: 0,
            },
            paginacaoInterna: {},
            nomeTela: 'ITEM_INCORPORACAO'
        }),
        computed:{
            colunas(){
                return this.criarColunas(6,
                                         [],
                                         ['imagem','codigo','descricao','quantidade','valorTotal','acoes'],
                                         [false,true,true,true,true,false],
                                         ['left','left','left','left','left','right'],
                                         ['1px','100px','100px','130px','120px','1px'],
                                         'gray--text')
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true,
            },
            async incorporacaoId() {
                this.resetaPageBuscaItensIncorporados()
                await this.buscarItensIncorporacao()
            },
            async '$store.state.incorporacao.buscarRegistro'(){
                await this.buscarItensIncorporacao()
            }
        },
        created() {
            this.paginacaoInterna = this.getPaginacao()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_REGISTRO_ITENS_INCORPORACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.SET_PAGINACAO_BUSCA_ITENS_INCORPORADOS,
                mutationTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.RESETA_PAGE_BUSCA_ITENS_INCORPORADOS
            ]),
            getPaginacao() {
                return this.$store.state.itemIncorporacao.buscaRegistroItensIncorporacao.paginacao
            },
            tratarEventoAcessar(item) {
                this.$router.push({
                    name: 'ModalItemVisualizarRegistro',
                    params: {incorporacaoId: this.incorporacaoId, itemIncorporacaoId: item.id}
                })
            },
            async buscarItensIncorporacao() {
                const resultado = await this.buscarRegistroItensIncorporacao(this.incorporacaoId)
                if (resultado) {
                    this.dadosGerais.itens = resultado.items
                    this.dadosGerais.paginas = resultado.totalPages
                    this.dadosGerais.totalItens = resultado.totalElements
                }
            },
            async tratarEventoPaginar(valor) {
                await this.setPaginacaoBuscaItensIncorporados(valor)
                await this.buscarItensIncorporacao()
            },
            abrirPanelDocumentos() {
                setTimeout(() => {
                    let options = {duration: 945, offset: -1, easing: 'linear',}
                    this.$vuetify.goTo('#listagemTabelaItemIncorporacao', options)
                }, 250)
            }
        }
    }
</script>

<style scoped lang="stylus">
    .itens-panel-header
        border-bottom solid 1px #e7e7e7 !important
        background-color #F6F6F6
        border-radius 5px !important
        min-height 20px !important
        padding-bottom 10px
        padding-top 10px

    .itens-incorporacao
        border solid 1px #e7e7e7 !important
        border-radius 5px !important
        box-shadow none

    .session-title
        font-size 15px
        font-weight bold
        color #777 !important
</style>
