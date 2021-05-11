<template>
  <v-dialog v-model="dialog" width="800" persistent scrollable @keydown.esc="fecharModal">
    <v-card class="dialog">
      <v-card-title class="subtitle-1 font-weight-bold primary white--text" primary-title>
        Depreciações do Bem
        <v-spacer/>
        <v-btn @click="fecharModal" icon x-small dark>
          <v-icon class="icon-close">fas fa-times</v-icon>
        </v-btn>
      </v-card-title>

      <v-card-text class=" d-flex justify-center align-center">
        <v-row>
          <v-col cols="12">
            <v-data-table
                :headers="colunas"
                :items="depreciacoesDoBem"
                :server-items-length="depreciacoesDoBem.length"
                height="400"
                fixed-header
                hide-default-footer
                class="simple-table"
                no-data-text="Não há depreciações para este patrimônio">
              <template v-slot:item.orgaoSigla="{item}">
                <span class="grey--text caption">{{ item.orgaoSigla }}</span>
              </template>
              <template v-slot:item.setorSigla="{item}">
                <span class="grey--text caption">{{ item.setorSigla }}</span>
              </template>
              <template v-slot:item.mesReferencia="{item}">
                <span class="grey--text caption">{{ item.mesReferencia }}</span>
              </template>
              <template v-slot:item.taxaAplicada="{item}">
                <span class="grey--text caption">{{ item.taxaAplicada | valorParaPorcentagem }}</span>
              </template>
              <template v-slot:item.valorAnterior="{item}">
                <span class="grey--text caption">{{ item.valorAnterior | valorParaReal }}</span>
              </template>
              <template v-slot:item.valorPosterior="{item}">
                <span class="grey--text caption">{{ item.valorPosterior | valorParaReal }}</span>
              </template>
              <template v-slot:item.valorSubtraido="{item}">
                <span class="grey--text caption">{{ item.valorSubtraido | valorParaReal }}</span>
              </template>
            </v-data-table>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
    export default {
        name: 'ModalTabelaDepreciacoes',
        props: {
            depreciacoesDoBem: {
                type: Array,
                default: () => {
                    return []
                },
            },
        },
        data () {
            return {
                dialog: true,
            }
        },
        computed: {
            colunas(){
                return this.criarColunas(7,
                                         ['Órgão', 'Setor', 'Mês/ano', 'Taxa', 'Valor anterior a depreciação', 'Valor após a depreciação', 'Valor depreciado'],
                                         ['orgaoSigla','setorSigla','mesReferencia','taxaAplicada','valorAnterior','valorPosterior','valorSubtraido'],
                                         [false,false,false,false,false,false,false],
                                         ['center','center','center','center','center','center','center'],
                                         ['12%','12%','12%','12%','20%','20%','12%'],
                                         'gray--text')
            }
        },
        methods: {
            fecharModal () {
                this.$router.push({
                    name: 'FichaPatrimonioDadosGerais',
                    params: { patrimonioId: this.$route.params.patrimonioId },
                })
            }
        }
    }
</script>

<style lang="stylus" scoped>
.header-tabela
  color #838383
  min-width 90px

.icon-close
  opacity: 0.6

.simple-table td
  height: 48px

.simple-table th
  font-size: 0.75rem
  padding: 10px 10px

.simple-table tr
  cursor default
</style>
