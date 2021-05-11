<template>
    <v-container fluid>
        <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5 " v-if="dadosGerais.totalItens > 0">
            <v-col cols="12">
                <v-card flat class="patrimonios-estornados">
                    <v-expansion-panels flat v-model="exibirPainel">
                        <v-expansion-panel>
                            <v-expansion-panel-header @click="abrirPanelDocumentos" class="itens-panel-header">
                                <h4 class="session-title">Patrimônios Estornados ({{dadosGerais.totalItens}})</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <v-data-table
                                    id="listagemTabelaPatrimonios"
                                    :headers="colunas"
                                    :items="dadosGerais.itens"
                                    :server-items-length="dadosGerais.totalItens"
                                    :options.sync="paginacaoInterna"
                                    :loading="false"
                                    no-data-text="Não há patrimônios estornados"
                                    class="az-table-lista pr-tabela-todos-itens-incorporacao tabela-patrimonios-estornados"
                                    must-sort
                                    hide-default-footer
                                    @click:row="tratarEventoAcessar">
                                    <template v-slot:item.patrimonio="{item}">
                                    <span
                                        class="d-inline-block text-truncate">{{item.numero | textoSemValor}}</span>
                                    </template>
                                    <template v-slot:item.descricao="{item}">
                                        <campo-apresentacao-tool-tip
                                            class="descricao__tooltip"
                                            open-delay="500"
                                            :texto="item.descricao"
                                        />
                                    </template>
                                    <template v-slot:item.valor="{item}">
                                    <span
                                        class="d-inline-block text-truncate">{{item.valor | valorParaReal}}</span>
                                    </template>
                                    <template v-slot:item.justificativa="{item}">
                                        <v-icon class="justifica__button" @click.stop="tratarEventoMostrarMotivo(item)">mode_comment</v-icon>
                                    </template>
                                </v-data-table>
                                <paginacao
                                    :paginacao-interna="paginacaoInterna"
                                    :paginas="dadosGerais.paginas"
                                    :linhasPorPagina="linhasPorPagina"/>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
    import {mapActions, mapMutations} from 'vuex'
    import actionTypes from '@/core/constants/actionTypes'
    import mutationTypes from '@/core/constants/mutationTypes'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    export default {
        name: 'VisualizarRegistroIncorporacaoItensEstornados',
        components: {CampoApresentacaoToolTip, Paginacao},
        props: {
            incorporacaoId: {
                type: Number,
                required: true
            }
        },
        data: () => ({
            exibirPainel: 1,
            paginacaoInterna: {},
            dadosGerais: {
                itens: [],
                totalItens: 0,
                paginas: 0,
            },
            linhasPorPagina: [5, 10, 15, 20]
        }),
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true
            },
            async incorporacaoId() {
                this.resetaPageBuscaTodosPatrimoniosEstornados()
                await this.buscarPatrimoniosEstornados()
            },
            async '$store.state.incorporacao.buscarRegistro'(){
                await this.buscarPatrimoniosEstornados()
            }
        },
        computed:{
            colunas(){
                return this.criarColunas(4,
                                         ['Patrimônio','Descrição','Valor','Justificativa'],
                                         ['patrimonio','descricao','valor','justificativa'],
                                         [false,false,false,false],
                                         ['left','left','left','center'],
                                         ['10%','30%','30%','5%'],
                                         'gray--text')
            }
        },
        created() {
            this.paginacaoInterna = this.getPaginacao()
        },
        methods: {
            ...mapActions([actionTypes.PATRIMONIO.BUSCAR_TODOS_PATRIMONIOS_ESTORNADOS]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.SET_PAGINACAO_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS,
                mutationTypes.PATRIMONIO.RESETA_PAGE_BUSCA_TODOS_PATRIMONIOS_ESTORNADOS
            ]),
            getPaginacao() {
                return this.$store.state.patrimonio.resultadoBuscaTodosPatrimoniosEstornados.paginacao
            },
            async buscarPatrimoniosEstornados() {
                const resultado = await this.buscarTodosPatrimoniosEstornados(this.incorporacaoId)
                if (resultado) {
                    this.dadosGerais.itens = resultado.items
                    this.dadosGerais.totalItens = resultado.totalElements
                    this.dadosGerais.paginas = resultado.totalPages
                }
            },
            tratarEventoAcessar(item){
                const id = item.id
                if(this.verificaPermissaoEdicao()){
                    this.$router.push({name: 'FichaPatrimonioDadosGerais', params: {patrimonioId: id}})
                }else{
                    this.$router.push({name: 'FichaPatrimonioDadosGeraisVisualizacao', params: {patrimonioId: id}})
                }
            },
            async tratarEventoPaginar(valor) {
                await this.setPaginacaoBuscaTodosPatrimoniosEstornados(valor)
                await this.buscarPatrimoniosEstornados()
            },
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, [
                    'Mobiliario.Normal',
                ])
            },
            tratarEventoMostrarMotivo(item) {
                this.$emit('mostrarMotivoEstorno', item)
            },
            abrirPanelDocumentos() {
                setTimeout(() => {
                    let options = {duration: 945, offset: -1, easing: 'linear',}
                    this.$vuetify.goTo('#listagemTabelaPatrimonios', options)
                }, 250)
            }
        }
    }
</script>

<style scoped lang="stylus">
    >>>
        .texto-campo-apresentacao-tooltip
            width 30vw

    .itens-panel-header
        border-bottom solid 1px #e7e7e7 !important
        background-color #F6F6F6
        border-radius 5px !important
        min-height 20px !important
        padding-bottom 10px
        padding-top 10px

    .patrimonios-estornados
        border solid 1px #e7e7e7 !important
        border-radius 5px !important
        box-shadow none

    .tabela-patrimonios-estornados
        tr:hover
            .justifica__button
                float none !important

    .justifica__button
        float none !important

</style>
