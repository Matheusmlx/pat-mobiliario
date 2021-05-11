<template>
    <div>
        <az-toolbar>
            <botao-novo
                slot="actions"
                @click="redirecionarNovaReserva"/>

            <v-flex slot="simpleSearch">
                <az-search
                    :filter="filtrosInterno"
                    @remove-filter="tratarEventoRemoverFiltro"
                    @simple-search="tratarEventoBuscaSimples"
                    :maxlengthInput="500"
                    id="azSearch"
                    simple-search-placeholder="Busque por: código, quantidade, órgão ou situação"
                    class="searchReservaListagem"/>
            </v-flex>
        </az-toolbar>
        <reserva-listagem-tabela
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            :paginacao="todasReservas.paginacao"
            @acessar="tratarEventoAcessar"
            @paginar="tratarEventoPaginar"/>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import ReservaListagemTabela from './ReservaListagemTabela'
    import BotaoNovo from '@/views/components/botoes/BotaoNovo'

    export default {
        name: 'ReservaListagem',
        components: {ReservaListagemTabela, BotaoNovo},
        data() {
            return {
                filtrosInterno: this.getFiltros(),
                itens: [],
                paginas: 0,
                totalItens: 0,
            }
        },
        computed: {
            ...mapState({todasReservas: state => state.reservaGeral.resultadoBuscaTodasReservasListagem}),
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.BUSCAR_TODAS_RESERVAS_LISTAGEM,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO
            ]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.RESERVA.SET_FILTROS_BUSCA_TODAS_RESERVAS,
                mutationTypes.CONFIGURACAO.RESERVA.SET_PAGINACAO_BUSCA_TODAS_RESERVAS,
                mutationTypes.CONFIGURACAO.RESERVA.RESETA_PAGE_RESERVA
            ]),
            async redirecionarNovaReserva() {
                await this.$router.push({name: 'ReservaCadastro'})
            },
            async tratarEventoBuscaSimples(valor) {
                this.resetaPageReserva()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            async buscar() {
                this.setFiltrosBuscaTodasReservas(this.getFiltrosInterno())
                await this.buscarTodasReservas()
            },
            async buscarTodasReservas() {
                const resultado = await this.buscarTodasReservasListagem()
                if (resultado) {
                    this.itens = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                }
            },
            async tratarEventoRemoverFiltro(propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
            },
            async tratarEventoAcessar(item) {
                if (item.situacao === 'EM_ELABORACAO') {
                    this.acessarEdicao(item.id)
                } else if (item.situacao === 'PARCIAL') {
                    const intervalosEmElaboracao = await this.existeIntervalosEmElaboracao(item.id)
                    intervalosEmElaboracao ? this.acessarEdicao(item.id) : this.acessarVisualizacao(item.id)
                } else if (item.situacao === 'FINALIZADO') {
                    this.acessarVisualizacao(item.id)
                }
            },
            acessarEdicao(id) {
                this.$router.push({name: 'ReservaEdicao', params: {id}})
            },
            acessarVisualizacao(id) {
                this.$router.push({name: 'ReservaVisualizacao', params: {id}})
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodasReservas(paginacao)
                await this.buscar()
            },
            getFiltros() {
                return _.cloneDeep(this.$store.state.reservaGeral.resultadoBuscaTodasReservasListagem.filtros)
            },
            getFiltrosInterno() {
                return _.cloneDeep(this.filtrosInterno)
            },
        },
    }
</script>

<style lang="stylus">
    .searchReservaListagem .input-search
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
        .searchReservaListagem .input-search
            background-color #fff !important
            width 40vw !important

            span
                max-width 230px

    @media (max-width 890px)
        .searchReservaListagem .input-search
            background-color #fff !important
            width 50vw !important

    @media (max-width 720px)
        .searchReservaListagem .input-search
            background-color #fff !important
            width 55vw !important
</style>
