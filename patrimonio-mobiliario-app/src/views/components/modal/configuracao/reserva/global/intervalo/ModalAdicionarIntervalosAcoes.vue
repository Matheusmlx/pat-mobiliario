<template>
  <v-card-actions>
    <v-col md="12" sm="12" xl="12" xs="12" class="pa-0">
      <div class="az-actions-form mt-3 pb-3">
        <botao-cancelar @cancelar="fecharModal"/>
        <v-tooltip top max-width="200"
                   v-if="quantidadeSelecionadaIgualZero || existeIntervalosSemPreenchimento">
          <template v-slot:activator="{on}">
            <div v-on="on" class="btn-disabled">
              <v-btn disabled depressed color="primary">
                <span>Adicionar ({{ quantidadeSelecionados }})</span>
              </v-btn>
            </div>
          </template>
          <span v-if="quantidadeSelecionadaIgualZero">Nenhum órgão selecionado</span>
          <span v-else>Verifique inconsistências no preenchimento dos intervalos</span>
        </v-tooltip>
        <v-btn
            v-else
            @click="adicionar"
            color="primary"
            depressed>
          <span>Adicionar ({{ quantidadeSelecionados }})</span>
        </v-btn>
      </div>
    </v-col>
  </v-card-actions>
</template>

<script>
    import BotaoCancelar from '@/views/components/botoes/BotaoCancelar'

    export default {
        name: 'ModalAdicionarIntervalosAcoes',
        components: { BotaoCancelar },
        props: ['quantidadeSelecionadaIgualZero', 'existeIntervalosSemPreenchimento', 'quantidadeSelecionados'],
        methods: {
            fecharModal () {
                this.$emit('fecharModal')
            },
            adicionar () {
                this.$emit('adicionar')
            },
        },
    }
</script>

<style scoped>

</style>