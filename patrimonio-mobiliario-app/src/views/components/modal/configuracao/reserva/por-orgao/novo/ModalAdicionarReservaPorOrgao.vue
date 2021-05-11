<template>
  <v-dialog persistent v-model="dialog" width="900">
    <v-card>
      <v-card-title class="text-subtitle-2 primary white--text font-weight-bold pt-2 pb-1">
        Adicionar Reserva
        <v-spacer/>
        <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
          <v-icon small>fas fa-times</v-icon>
        </v-btn>
      </v-card-title>

      <modal-adicionar-reserva-por-orgao-form
          :reserva="reserva"
          :orgaos="orgaos"
          :proximo-numero-disponivel="proximoNumeroDisponivel"
          @buscarProximoNumeroDisponivel="buscarProximoNumeroDisponivel"
          @buscarIntervalo="buscarIntervalo"
          @liberarBotaoFinalizar="liberarBotaoFinalizar"
          @bloquearBotaoFinalizar="bloquearBotaoFinalizar"
          @finalizar="finalizarReserva"
          @resetarReserva="resetarReserva"/>

      <v-divider/>
      <modal-adicionar-reserva-por-orgao-acoes :pode-finalizar="podeFinalizar" @finalizar="finalizarReserva"/>
    </v-card>
  </v-dialog>
</template>

<script>
    import ModalAdicionarReservaPorOrgaoForm from './ModalAdicionarReservaPorOrgaoForm'
    import ModalAdicionarReservaPorOrgaoAcoes from './ModalAdicionarReservaPorOrgaoAcoes'
    import { mapActions } from 'vuex'
    import { actionTypes } from '@/core/constants'

    export default {
        name: 'ModalAdicionarReservaPorOrgao',
        components: { ModalAdicionarReservaPorOrgaoForm, ModalAdicionarReservaPorOrgaoAcoes },
        data () {
            return {
                dialog: true,
                reserva: { preenchimento: 'AUTOMATICO' },
                orgaos: [],
                proximoNumeroDisponivel: '',
                podeFinalizar: false,
            }
        },
        async mounted () {
            await this.buscarOrgaos()
        },
        methods: {
            ...mapActions([
                actionTypes.COMUM.BUSCAR_TODOS_ORGAOS,
                actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_INTERVALO_POR_ORGAO,
                actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.BUSCAR_PROXIMO_NUMERO_DISPONIVEL_POR_ORGAO,
                actionTypes.CONFIGURACAO.RESERVA.POR_ORGAO.FINALIZAR_RESERVA_POR_ORGAO,
            ]),
            async buscarOrgaos () {
                const resultado = await this.buscarTodosOrgaos()

                if (resultado) {
                    this.orgaos = resultado.items
                    this.selecionaOrgaoSeArrayConterApenasUmOrgao(this.orgaos)
                }
            },
            selecionaOrgaoSeArrayConterApenasUmOrgao (orgaos) {
                if (this.verificaSeArrayContemApenasUmOrgao(orgaos)) {
                    this.reserva.orgaoId = orgaos[0].id
                }
            },
            verificaSeArrayContemApenasUmOrgao (orgaos) {
                return orgaos.length === 1
            },
            async buscarProximoNumeroDisponivel (orgaoId) {
                this.resetarReserva()
                const resultado = await this.buscarProximoNumeroDisponivelPorOrgao(orgaoId)
                this.proximoNumeroDisponivel = resultado.proximoNumero
                this.reserva = {
                    ...this.reserva,
                    numeroInicio: resultado.proximoNumero,
                }
            },
            resetarReserva () {
                this.reserva = {
                    ...this.reserva,
                    numeroInicio: null,
                    numeroFim: null,
                    quantidadeReservada: null
                }
            },
            async buscarIntervalo (buscarIntervaloEntidade) {
                const resultado = await this.buscarIntervaloPorOrgao(buscarIntervaloEntidade)
                if (resultado) {
                    this.reserva = {
                        ...this.reserva,
                        numeroInicio: resultado.numeroInicio,
                        numeroFim: resultado.numeroFim
                    }
                }
            },
            async finalizarReserva () {
                await this.finalizarReservaPorOrgao(this.reserva)
                this.mostrarNotificacaoSucessoDefault()
                this.atualizarListagem()
                this.fecharModal()
            },
            fecharModal () {
                this.$router.push({ name: 'ReservaListagem' })
            },
            atualizarListagem () {
                this.$emit('atualizarListagem')
            },
            liberarBotaoFinalizar () {
                this.podeFinalizar = true
            },
            bloquearBotaoFinalizar () {
                this.podeFinalizar = false
            },
        },

    }
</script>

<style scoped>

</style>