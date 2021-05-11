<template>
    <div class="intervalos-listagem">
        <v-row class="white pl-3 pt-3 pr-3 ml-0 mr-0 pb-5">
            <v-col cols="12">
                <v-card flat class="dados-gerais">
                    <v-expansion-panels v-model="exibirPanel" active-class="" flat>
                        <v-expansion-panel>
                            <v-expansion-panel-header class="dados-gerais-panel-header">
                                <h4 class="titulo">Órgãos ({{quantidadeIntervalos}})</h4>
                            </v-expansion-panel-header>
                            <v-expansion-panel-content>
                                <az-form>
                                    <v-data-table
                                        ref="table"
                                        :headers="colunas"
                                        :items="intervalos"
                                        :server-items-length="totalItens"
                                        :options.sync="paginacaoInterna"
                                        :loading="false"
                                        no-data-text="Não há reservas cadastradas"
                                        class="az-table-list pr-tabela-todos-intervalos-reserva"
                                        hide-default-footer>
                                        <template v-slot:item.codigo="{item}">
                                            <span class="d-inline-block text-truncate max-10">{{ item.codigo }}</span>
                                        </template>
                                        <template v-slot:item.orgao="{item}">
                                            <span class="d-inline-block text-truncate max-25">{{ item.orgao }}</span>
                                        </template>
                                        <template v-slot:item.quantidadeReservada="{item}">
                                            <span class="d-inline-block text-truncate max-10">{{ item.quantidadeReservada | formataValor }}</span>
                                        </template>
                                        <template v-slot:item.preenchimento="{item}">
                                            <span class="d-inline-block text-truncate max-10">
                                                {{ item.preenchimento | azEnum(preenchimentoReserva) }}
                                            </span>
                                        </template>
                                        <template v-slot:item.intervalo="{item}">
                                            <span class="d-inline-block text-truncate max-10">
                                                {{ item.numeroInicio }} - {{ item.numeroFim }}
                                            </span>
                                        </template>
                                        <template v-slot:item.termoGuarda="{item}">
                                            <span class="d-inline-block text-truncate max-10 termoDeGuarda" @click="gerarTermoDeGuarda(item)">
                                                Baixar
                                            </span>
                                        </template>
                                    </v-data-table>
                                    <paginacao
                                        :paginacao-interna="paginacaoInterna"
                                        :paginas="paginas"
                                        @resetaPage="resetaPage"/>
                                </az-form>
                            </v-expansion-panel-content>
                        </v-expansion-panel>
                    </v-expansion-panels>
                </v-card>
            </v-col>
        </v-row>
    </div>
</template>

<script>
    import Paginacao from '@/views/components/tabela/Paginacao'
    import {mapMutations} from 'vuex'
    import {mutationTypes} from '@/core/constants'

    export default {
        name: 'ReservaVisualizacaoDadosGerais',
        props: ['intervalos', 'paginas', 'totalItens', 'paginacao'],
        components: {Paginacao},
        data() {
            return {
                exibirPanel: 0,
                paginacaoInterna: this.paginacao
            }
        },
        watch: {
            paginacaoInterna: {
                handler(novoValor) {
                    this.$emit('paginar', novoValor)
                },
                deep: true
            },
        },
        computed: {
            colunas() {
                return this.criarColunas(
                    6,
                    ['Código', 'Órgão', 'Quantidade', 'Preenchimento', 'Intervalo', 'Termo de Guarda'],
                    ['codigo', 'orgao', 'quantidadeReservada', 'preenchimento', 'intervalo', 'termoGuarda'],
                    [false, false, false, false, false, false],
                    ['left', 'left', 'left', 'left', 'left', 'left'],
                    ['10%', '35%', '14%', '16%', '10%', '20%'],
                    'gray--text')
            },
            quantidadeIntervalos() {
                return String(this.totalItens).padStart(2, '0')
            }
        },
        methods: {
            ...mapMutations([mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.RESETA_PAGE_RESERVA_INTERVALO]),
            resetaPage() {
                this.resetaPageReservaIntervalo()
            },
            gerarTermoDeGuarda(item) {
                this.$emit('gerarTermoDeGuarda', item.id)
            }
        }
    }
</script>

<style scoped lang="stylus">
.intervalos-listagem

    min-height 75vh

.dados-gerais
    border solid 1px #e7e7e7 !important
    border-radius 5px !important

.dados-gerais-panel-header
    border-bottom solid 1px #e7e7e7 !important
    background-color #F6F6F6
    border-radius 5px !important
    min-height 20px !important
    padding-bottom 10px
    padding-top 10px

.titulo
    font-size 15px
    font-weight bold
    color #777 !important

.termoDeGuarda:hover
    cursor pointer
    text-decoration underline
</style>
