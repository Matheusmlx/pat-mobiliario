<template>
  <v-card-actions>
    <v-col md="12" sm="12" xl="12" xs="12" class="pa-0">
      <div class="az-actions-form mt-3 pb-3">
        <botao-cancelar @cancelar="fecharModal"/>
        <botao-finalizar-desabilitado v-if="!podeFinalizar" texto="Preencha os campos obrigatórios para coninuar"/>
        <botao-finalizar v-else @finalizar="finalizar"/>
      </div>
    </v-col>
  </v-card-actions>
</template>

<script>
    import BotaoCancelar from '@/views/components/botoes/BotaoCancelar'
    import BotaoFinalizarDesabilitado from '@/views/components/botoes/BotaoFinalizarDesabilitado'
    import BotaoFinalizar from '@/views/components/botoes/BotaoFinalizar'

    export default {
        name: 'ModalAdicionarIntervalosAcoes',
        components: { BotaoFinalizar, BotaoFinalizarDesabilitado, BotaoCancelar },
        props: ['podeFinalizar'],
        methods: {
            fecharModal () {
                this.$router.push({ name: 'ReservaListagem' })
            },
            finalizar () {
                this.$emit('finalizar')
            },
        },
    }
</script>

<style scoped>

</style>