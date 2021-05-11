<template>
    <div>
        <router-view @recarregarDadosReserva="recarregarDadosReserva"/>

        <reserva-intervalo-listagem
            :reserva-id="reservaId" :reserva="reserva"
            @recarregarDadosReserva="recarregarDadosReserva"
            v-if="existeIntervalos"
        />
        <reserva-dados-gerais-edicao
            :reserva-id="reservaId"
            :reserva="reserva"
            @recarregarDadosReserva="recarregarDadosReserva"
            v-else
        />

        <v-divider/>

        <acoes-botoes-finalizar-voltar
            :desabilitado="!adicionandoIntervalos"
            texto-em-processamento="Aguarde"
            texto-bloqueio="Adicione órgãos na reserva para finalizar."
            @finalizar="tratarEventoFinalizar"
            @voltar="voltarParaListagem"
        />
    </div>
</template>

<script>
    import AcoesBotoesFinalizarVoltar from '@/views/components/acoes/AcoesBotoesFinalizarVoltar'
    import ReservaDadosGeraisEdicao from '@/views/pages/configuracao/reserva/global/edicao/ReservaDadosGeraisEdicao'
    import ReservaIntervaloListagem
        from '@/views/pages/configuracao/reserva/global/edicao/intervalo/listagem/ReservaIntervaloListagem'
    import {mapActions, mapMutations, mapState} from 'vuex'
    import {actionTypes, mutationTypes} from '@/core/constants'

    export default {
        name: 'ReservaDadosGeraisEdicaoIntervalo',
        components: {AcoesBotoesFinalizarVoltar, ReservaIntervaloListagem, ReservaDadosGeraisEdicao},
        data() {
            return {
                reservaId: '',
                reserva: {},
                existeIntervalos: false,
                tituloAlerta: 'Atenção!',
                subTituloAlerta: 'A situação será alterada para Parcial, pois ainda possui intervalos disponíveis nesta reserva. Deseja confirmar?',
                textoBotaoAlertaConfirm: 'Confirmar',
                iconeAlerta: 'warning'
            }
        },
        async mounted() {
            this.setReservaId()
            await this.buscarReserva()
            await this.verificarExistenciaIntervalosEmElaboracao()
        },
        computed: {
            ...mapState({
                adicionandoIntervalos: state => state.intervalo.existeEmElaboracao
            })
        },
        methods: {
            ...mapMutations([mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]),
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.FINALIZAR_RESERVA,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.EXISTE_EM_ELABORACAO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_TODOS_INTERVALOS_LISTAGEM
            ]),
            async recarregarDadosReserva() {
                await this.buscarReserva()
                await this.verificarExistenciaIntervalosEmElaboracao()
                this.resetaPageReservaIntervalo()
                await this.buscarTodosIntervalosListagem(this.reservaId)
            },
            setReservaId() {
                this.reservaId = this.$route.params.id.toString()
            },
            async buscarReserva() {
                try {
                    this.reserva = await this.buscarReservaPorId(this.reservaId)
                    if (this.reserva) {
                        this.validarSeExisteIntervalos(this.reserva.quantidadeReservada, this.reserva.quantidadeRestante)
                    }
                } catch (err) {
                    await this.$router.replace({name: 'ReservaListagem'})
                }
            },
            validarSeExisteIntervalos(quantidadeReservada, quantidadeRestante) {
                this.existeIntervalos = quantidadeReservada !== quantidadeRestante
            },
            async verificarExistenciaIntervalosEmElaboracao() {
                await this.existeIntervalosEmElaboracao(this.reservaId)
            },
            tratarEventoFinalizar() {
                if(this.reserva.quantidadeRestante !== 0) {
                    this.mostrarConfirmacao({
                        titulo: this.tituloAlerta,
                        subTitulo: this.subTituloAlerta,
                        textoBotaoConfirm: this.textoBotaoAlertaConfirm,
                        icon: this.iconeAlerta
                    }, (result) => {
                        if (result.isConfirmed) {
                            this.finalizar()
                        }
                    })
                } else {
                    this.finalizar()
                }
            },
            async finalizar() {
                const sucesso = await this.finalizarReserva(this.reservaId)
                if (sucesso) {
                    await this.$router.replace({name: 'ReservaVisualizacao'})
                }
            },
            async voltarParaListagem() {
                await this.$router.replace({name: 'ReservaListagem'})
            }
        }
    }
</script>
