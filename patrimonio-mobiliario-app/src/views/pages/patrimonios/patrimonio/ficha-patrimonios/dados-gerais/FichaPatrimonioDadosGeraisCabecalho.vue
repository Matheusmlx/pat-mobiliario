<template>
    <v-row class="primary ml-0 mr-0">
        <v-row class="pl-5"  md="3">
            <v-col class="headerCabecalho pr-0" md="1" sm="2" xs="6">
                <span class="font-weight-bold">
                    <label-personalizado campo="numero" :tela="nomeTela" apenasNome/>
                </span>
                <p class="mb-0">{{ patrimonio.numero }}</p>
            </v-col>
            <v-col class="headerCabecalho pl-0" md="2" sm="2" xs="6">
                <span class="font-weight-bold">Incorporado em</span>
                <p class="mb-0">{{ patrimonio.dataIncorporado | azDate }}</p>
            </v-col>
            <v-col class="headerCabecalho" md="2" sm="2" xs="6">
                <span class="font-weight-bold">Situação</span>
                <p class="mb-0">{{ patrimonio.situacao | azEnum(situacoesPatrimonio)}}</p>
            </v-col>
        </v-row>
    </v-row>
</template>

<script>
    import {situacoesPatrimonio} from '@/core/constants'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    export default {
        name: 'FichaPatrimonioDadosGeraisCabecalho',
        components: {LabelPersonalizado},
        props: {
            patrimonio: {
                required: true
            }
        },
        data: () => ({
            nomeTela: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO',
            situacoesPatrimonio
        })
    }
</script>

<style scoped lang="stylus">
.headerCabecalho
    font-size 0.9em
    color: white
    max-height 65px
    min-width 150px
    max-width 150px


.reabrirIncorporacao
    opacity 0.3
    font-size 15px
</style>
