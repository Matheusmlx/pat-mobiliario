<template>
    <v-row class="white pl-3 pr-3 ml-0 mr-0 pb-5">
        <v-col cols="12">
            <v-card flat class="dados-gerais">
                <v-expansion-panels v-model="exibirPanel" active-class="" flat>
                    <v-expansion-panel>
                        <v-expansion-panel-header class="dados-gerais-panel-header">
                            <h4 class="titulo">Dados Gerais</h4>
                        </v-expansion-panel-header>
                        <v-expansion-panel-content>
                            <v-row>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="reconhecimento"
                                        :tela="nomeTela"
                                        :text="incorporacao.reconhecimento"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="numProcesso"
                                        :tela="nomeTela"
                                        :text="incorporacao.numProcesso | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao campo="orgao" :tela="nomeTela" :text="incorporacao.orgao"/>
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao campo="setor" :tela="nomeTela" :text="incorporacao.setor"/>
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="dataFinalizacao"
                                        :tela="nomeTela"
                                        :text="incorporacao.dataFinalizacao | azDate"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="nota"
                                        :tela="nomeTela"
                                        :text="incorporacao.nota | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="dataNota"
                                        :tela="nomeTela"
                                        :text="incorporacao.dataNota | azDate"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="valorNota"
                                        :tela="nomeTela"
                                        :text="incorporacao.valorNota | valorParaDinheiro"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="numeroNotaLancamentoContabil"
                                        :tela="nomeTela"
                                        :text="incorporacao.numeroNotaLancamentoContabil | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="dataNotaLancamentoContabil"
                                        :tela="nomeTela"
                                        :text="incorporacao.dataNotaLancamentoContabil | azDate"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="convenio"
                                        :tela="nomeTela"
                                        :text="incorporacao.convenio | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="fundo"
                                        :tela="nomeTela"
                                        :text="incorporacao.fundo | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="projeto"
                                        :tela="nomeTela"
                                        :text="incorporacao.projeto | textoSemValorSimples"
                                    />
                                </v-col>
                                <v-col cols="12" md="3" sm="6" xs="12">
                                    <campo-apresentacao
                                        campo="comodante"
                                        :tela="nomeTela"
                                        :text="incorporacao.comodante | textoSemValorSimples"
                                    />
                                </v-col>
                            </v-row>
                        </v-expansion-panel-content>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
    import CampoApresentacao from '@/views/components/campos/CampoApresentacao'

    export default {
        name: 'VisualizarRegistroIncorporacaoDadosGerais',
        components: {CampoApresentacao},
        props: {
            incorporacao: {
                required: true
            }
        },
        data() {
            return {
                nomeTela: 'INCORPORACAO_DADOS_GERAIS',
                exibirPanel: 0
            }
        },
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

</style>
