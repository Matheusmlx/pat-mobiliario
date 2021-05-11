<template>
    <v-container fluid white>
        <reserva-intervalo-listagem-cabecalho :reserva="reserva"/>
        <v-divider/>
        <v-row>
            <v-col md="12" sm="12" xs="12" class="d-flex align-end justify-end pr-10">
                <az-call-to-action
                    @click="adicionarIntervalos"
                    active
                    slot="actions">
                    <v-icon>add_circle</v-icon>
                    Adicionar órgãos
                </az-call-to-action>
            </v-col>
        </v-row>
        <v-divider/>
        <reserva-intervalo-listagem-tabela
            :itens="itens"
            :paginas="paginas"
            :total-itens="totalItens"
            :paginacao="paginacao"
            @removerIntervalo="tratarEventoRemoverIntervalo"
            @paginar="tratarEventoPaginar"/>
    </v-container>
</template>

<script>
    import ReservaIntervaloListagemCabecalho
        from '@/views/pages/configuracao/reserva/global/edicao/intervalo/listagem/ReservaIntervaloListagemCabecalho'
    import ReservaIntervaloListagemTabela
        from '@/views/pages/configuracao/reserva/global/edicao/intervalo/listagem/ReservaIntervaloListagemTabela'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'

    export default {
        name: 'ReservaIntervaloListagem',
        components: {ReservaIntervaloListagemTabela, ReservaIntervaloListagemCabecalho},
        props: {
            reservaId: {
                type: String,
                required: true,
            },
            reserva: {
                type: Object,
                default: () => {
                    return {
                        quantidadeReservada: '',
                        preenchimento: 'AUTOMATICO',
                    }
                },
            },
        },
        computed: {
            ...mapState({
                itens: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.itens,
                paginas: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.totalPaginas,
                totalItens: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.totalItens,
                paginacao: state => state.intervalo.resultadoBuscaTodosIntervalosListagem.paginacao
            }),
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.REMOVER_INTERVALO_POR_ID,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO
            ]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SET_PAGINACAO_BUSCA_TODOS_INTERVALOS,
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO,
            ]),
            async buscarTodosIntervalos() {
                await this.buscarTodosIntervalosListagem(this.reservaId)
            },
            async tratarEventoPaginar(paginacao) {
                this.setPaginacaoBuscaTodosIntervalos(paginacao)
                await this.buscarTodosIntervalos()
            },
            async adicionarIntervalos() {
                await this.$router.replace({name: 'ModalAdicionarIntervalos', params: {id: this.reservaId}})
            },
            async tratarEventoRemoverIntervalo(intervaloId) {
                await this.removerIntervaloPorId({reservaId: this.reservaId, intervaloId})
                this.$emit('recarregarDadosReserva')
            }
        }
    }
</script>
