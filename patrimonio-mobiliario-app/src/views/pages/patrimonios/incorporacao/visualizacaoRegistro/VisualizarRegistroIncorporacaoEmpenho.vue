<template>

    <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
        <v-col cols="12">
            <v-card flat class="dados-gerais">
                <v-expansion-panels flat v-model="exibirPanel" active-class="">
                    <v-expansion-panel>
                        <v-expansion-panel-header class="dados-gerais-panel-header">
                            <h4 class="titulo">Empenho</h4>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content class="panel-content">
                            <v-data-table
                                :height="tamanhoTabela"
                                fixed-header
                                :headers="colunas"
                                :items="empenhos"
                                ref="table"
                                :loading="false"
                                :server-items-length="empenhos.length"
                                class="az-table-list tabela-empenhos"
                                hide-default-footer
                                no-data-text="Não há empenhos cadastrados">
                                <template v-slot:header.numeroEmpenho>
                                    <label-personalizado class="ml-4" campo="numeroEmpenho" :tela="nomeTela" :apenasNome="true"/>
                                </template>
                                <template v-slot:header.dataEmpenho>
                                    <label-personalizado class="ml-4" campo="dataEmpenho" :tela="nomeTela" :apenasNome="true"/>
                                </template>
                                <template v-slot:header.valorEmpenho>
                                    <label-personalizado class="ml-4" campo="valorEmpenho" :tela="nomeTela" :apenasNome="true"/>
                                </template>

                                <template v-slot:item.numeroEmpenho="{item}">
                                    <span class="d-inline-block ml-4">{{item.numeroEmpenho}}</span>
                                </template>
                                <template v-slot:item.dataEmpenho="{item}">
                                    <span class="d-inline-block ml-4">{{item.dataEmpenho | azDate()}}</span>
                                </template>
                                <template v-slot:item.valorEmpenho="{item}">
                                    <span class="d-inline-block ml-4">R$ {{item.valorEmpenho | valorParaDinheiro}}</span>
                                </template>
                            </v-data-table>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-card>
        </v-col>
    </v-row>

</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'VisualizarRegistroIncorporacaoEmpenho',
        components:{ LabelPersonalizado },
        props: {
            incorporacaoId: {
                type: Number,
                required: true
            }
        },
        data() {
            return{
                nomeTela: 'INCORPORACAO_DADOS_GERAIS',
                exibirPanel: 1,
                empenhos: [],
                tamanhoTabela: '200'
            }
        },
        async mounted() {
            await this.buscarEmpenhos()
        },
        computed:{
            colunas(){
                return this.criarColunas(3,
                                         [],
                                         ['numeroEmpenho','dataEmpenho','valorEmpenho'],
                                         [false,false,false],
                                         ['left','left','left'],
                                         ['33%','33%','33%'],
                                         'gray--text')
            }
        },
        watch: {
            async incorporacaoId() {
                await this.buscarEmpenhos()
            }
        },
        methods:{
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.EMPENHO.BUSCAR_TODOS_EMPENHOS,
            ]),
            async buscarEmpenhos() {
                const resultado = await this.buscarTodosEmpenhos(this.incorporacaoId)
                if(resultado){
                    this.empenhos = this.ordenaEmpenhos(resultado)
                    this.atualizaTamanhoTabela(this.empenhos)
                }
            },
            ordenaEmpenhos(resultado){
                return resultado.items.sort(function(a,b) {
                    if(a.numeroEmpenho < b.numeroEmpenho){
                        return -1
                    }else if(a.numeroEmpenho > b.numeroEmpenho){
                        return 1
                    }
                    return 0
                })
            },
            atualizaTamanhoTabela(empenhos){
                if(empenhos.length >2){
                    this.tamanhoTabela = '300'
                }
            }
        }
    }
</script>

<style scoped lang="stylus">
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

.tabela-empenhos
    cursor auto !important

>>>
    .panel-content
        .v-expansion-panel-content__wrap
            padding-right 0 !important
            padding-left 0 !important

</style>
