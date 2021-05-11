<template>
  <v-row class="ma-0 ml-2">
    <v-col md="9" sm="10" xs="10" class="pt-2 pb-1">
      <v-row>
        <v-col class="pb-0 reserva">
          <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Reserva </span>
          <p class="text--darken-1 grey--text text-body-2">{{ reserva.codigo }}</p>
        </v-col>
        <v-col class="pb-0 situacao">
          <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Situação </span>
          <p class="text--darken-1 grey--text text-body-2">{{ reserva.situacao | azEnum(situacoesReservas) }}</p>
        </v-col>
        <v-col  class="pb-0 preenchimento">
          <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Preenchimento </span>
          <p class="text--darken-1 grey--text text-body-2">{{ reserva.preenchimento | azEnum(preenchimentoReserva) }}</p>
        </v-col>
        <v-col  class="pb-0 quantidade">
          <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Quantidade </span>
          <p class="text--darken-1 grey--text text-body-2">{{ reserva.quantidadeReservada | formataValor }}</p>
        </v-col>
          <v-col class="pb-0 restante">
              <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Restante </span>
              <p class="text--darken-1 grey--text text-body-2">{{ reserva.quantidadeRestante | formataValor }}</p>
          </v-col>
        <v-col  class="pb-0 intervalos">
          <span class="text--darken-1 grey--text text-body-2 font-weight-bold">Intervalo </span>
          <p class="text--darken-1 grey--text text-body-2">{{ reserva.numeroInicio }} - {{ reserva.numeroFim }}</p>
        </v-col>
      </v-row>
    </v-col>
    <v-col md="3" sm="2" class="d-flex justify-end pt-5 pr-6">
      <botao-remover @remover="removerReserva" v-if="!possuiNumerosUtilizados" />
    </v-col>
  </v-row>
</template>

<script>
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import {actionTypes} from '@/core/constants'
    import {mapActions} from 'vuex'
    export default {
        name: 'ReservaIntervaloListagemCabecalho',
        components: { BotaoRemover },
        props: {
            reserva: {
                type: Object,
                requided: true
            }
        },
        data(){
            return{
                possuiNumerosUtilizados:false
            }
        },
        updated(){
            this.verificarNumeroReservaUtilizando()
        },
        methods:{
            async removerReserva(){
                await this.deletarReserva(this.reserva.id)
                await this.$router.replace({name:'ReservaListagem'})
                this.mostrarNotificacaoSucessoDefault()
            },
            ...mapActions([actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO,
                           actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]),
            async verificarNumeroReservaUtilizando() {
                let {possuiNumerosUtilizados} = await this.verificarReservaPossuiNumeroUtilizado(this.reserva.id)
                this.possuiNumerosUtilizados = possuiNumerosUtilizados
            },
        }
    }
</script>

<style lang="stylus" scoped>
.reserva
  max-width 95px
.situacao
  max-width: 140px
  min-width: 140px
.preenchimento
  max-width 160px
.quantidade
  max-width 120px
.restante
  max-width 120px
.intervalos
  max-width 300px

</style>
