<template>
    <div>
        <reserva-visualizacao-cabecalho :reserva="reserva"/>
        <reserva-visualizacao-dados-gerais
            :intervalos="itens"
            :paginas="paginas"
            :totalItens="totalItens"
            :paginacao="paginacao"
            @paginar="tratarEventoPaginar"
            @gerarTermoDeGuarda="tratarGerarTermoDeGuarda"/>
    </div>
</template>

<script>
    import ReservaVisualizacaoCabecalho from '@/views/pages/configuracao/reserva/global/visualizacao/ReservaVisualizacaoCabecalho'
    import ReservaVisualizacaoDadosGerais from '@/views/pages/configuracao/reserva/global/visualizacao/ReservaVisualizacaoDadosGerais'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapState, mapMutations} from 'vuex'

    export default {
        name: 'ReservaVisualizacao',
        components: {
            ReservaVisualizacaoCabecalho,
            ReservaVisualizacaoDadosGerais
        },
        data() {
            return {
                reservaId: this.$route.params.id,
                reservaIntervalos: [],
                reserva: {}
            }
        },
        computed: {
            ...mapState({
                itens: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.itens,
                paginas: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.totalPaginas,
                totalItens: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.totalItens,
                paginacao: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.paginacao
            }),
        },
        async mounted() {
            this.reserva = await this.buscarReservaPorId(this.reservaId)
            await this.buscarTodosIntervalosListagem(this.reservaId)
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM,
                actionTypes.CONFIGURACAO.RESERVA.INTERVALO.BAIXAR_TERMO_DE_GUARDA
            ]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS
            ]),
            async buscarTodosIntervalos() {
                await this.buscarTodosIntervalosListagem(this.reservaId)
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosIntervalos(paginacao)
                await this.buscarTodosIntervalos()
            },
            async tratarGerarTermoDeGuarda(reservaIntervaloId) {
                await this.baixarTermoDeGuarda({reservaId: this.reservaId, reservaIntervaloId: reservaIntervaloId})
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
