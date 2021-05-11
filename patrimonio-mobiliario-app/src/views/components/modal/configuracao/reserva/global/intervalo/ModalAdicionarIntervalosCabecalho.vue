<template>
  <div>
    <v-card-title class="text-subtitle-2 primary white--text font-weight-bold pt-2 pb-1">
      Adicionar Órgãos
      <v-spacer/>
      <v-btn min-width="10px" class="pa-1 white--text" text @click="fecharModal">
        <v-icon small>fas fa-times</v-icon>
      </v-btn>
    </v-card-title>
    <az-toolbar class="search-listagem-orgaos">
      <v-row slot="actions" style="width: 550px">
        <v-col class="sub-header">
          <v-subheader><span class="font-weight-bold">Quantidade</span></v-subheader>
          <v-subheader>{{ reserva.quantidadeReservada | formataValor }}</v-subheader>
        </v-col>
        <v-col class="sub-header">
          <v-subheader><span class="font-weight-bold">Intervalo</span></v-subheader>
          <v-subheader>{{ reserva.numeroInicio + ' - ' + reserva.numeroFim }}</v-subheader>
        </v-col>
        <v-col class="sub-header">
          <v-subheader><span class="font-weight-bold">Restante</span></v-subheader>
          <v-subheader>{{ restante | formataValor }}</v-subheader>
        </v-col>
      </v-row>
      <az-search
          :filter="filtrosInterno"
          @remove-filter="tratarEventoRemoverFiltro"
          @simple-search="tratarEventoBuscaSimples"
          :maxlengthInput="500"
          id="azSearch"
          slot="simpleSearch"
          simple-search-placeholder="Busque por: órgão"/>
    </az-toolbar>
  </div>
</template>

<script>
    export default {
        name: 'ModalAdicionarIntervalosCabecalho',
        props: {
            reserva: {
                type: Object,
                required: true,
            },
            filtrosInterno: {
                required: true,
            },
            restante: {
                required: true
            }
        },
        methods: {
            fecharModal () {
                this.$emit('fecharModal')
            },
            tratarEventoRemoverFiltro (propriedade) {
                this.$emit('removerFiltro', propriedade)
            },
            tratarEventoBuscaSimples (conteudo) {
                this.$emit('buscaSimples', conteudo)
            }
        },
    }
</script>

<style scoped lang="stylus">
.sub-header
  .v-subheader
    display table-header-group

.search-listagem-orgaos .input-search
  background-color #fff !important
  width 30vw !important

  span
    max-width 350px

  .v-chip__content
    width 100%

    span
      white-space nowrap
      overflow hidden
      text-overflow ellipsis

@media (max-width 1250px)
  .search-listagem-orgaos .input-search
    span
      max-width 230px

@media (max-width 720px)
  .search-listagem-orgaos .input-search
    background-color #fff !important
    width 50vw !important

    span
      max-width 210px

.search-listagem-orgaos
  background-color white !important

</style>
