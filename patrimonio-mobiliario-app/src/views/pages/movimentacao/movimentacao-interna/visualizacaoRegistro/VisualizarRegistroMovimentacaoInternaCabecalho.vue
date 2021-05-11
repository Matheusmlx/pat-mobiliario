<template>
    <div>
        <v-row class=" primary ml-0 mr-0">
                <v-col class="headerCabecalho" cols="1">
                <span class="font-weight-bold">
                    <label-personalizado campo="codigo" :tela="nomeTela" apenasNome/>
                </span>
                    <p class="mb-0">{{ movimentacao.codigo | textoSemValorSimples }}</p>
                </v-col>
                <v-col class="headerCabecalho" cols="1">
                <span class="font-weight-bold">
                    <label-personalizado campo="tipo" :tela="nomeTela" apenasNome/>
                </span>
                    <campo-apresentacao-tool-tip :texto="movimentacao.tipo | azEnum(tipoMovimentacoes)"/>
                </v-col>
                <v-col class="headerCabecalho text-no-wrap" sm="2" md="2">
                    <span class="font-weight-bold">Criado por</span>
                    <campo-apresentacao-tool-tip :texto="movimentacao.usuarioCriacao"/>
                </v-col>
                <v-col class="headerCabecalho" sm="2" v-if="movimentacao.tipo !== 'TEMPORARIA'">
                    <span class="font-weight-bold">Finalizado em</span>
                    <p class="mb-0">{{ movimentacao.dataFinalizacao | azDate }}</p>
                </v-col>
                <v-col class="headerCabecalho text-no-wrap" md="2" sm="2" v-if="movimentacao.tipo === 'TEMPORARIA'">
                    <span class="font-weight-bold">Devolver em</span>
                    <p class="mb-0">{{ movimentacao.dataDevolucao | azDate }}</p>
                </v-col>
                <v-col class="headerCabecalho ml-2 text-no-wrap" cols="2">
                <span class="font-weight-bold">
                    <label-personalizado campo="situacao" :tela="nomeTela" apenasNome/>
                </span>
                    <p class="mb-0">{{ movimentacao.situacao | azEnum(situacoesMovimentacaoInterna) }}</p>
                </v-col>
                <v-spacer></v-spacer>
            <v-row v-if="possuiItensParaDevolver && ehDestinatario"
                   class="d-flex justify-md-end acoes__temporaria flex-grow-0">
                    <v-col md="3" class="button__devolver_parcial">
                        <v-btn class="styleButton" @click="abrirModalDevolverPatrimonios" dark text>
                            <span class="text-capitalize">Devolver Parcial</span>
                        </v-btn>
                    </v-col>
                    <v-col class="headerCabecalho button__devolver" md="3">
                        <v-menu
                            offset-y
                            top
                            left
                            nudge-top="8"
                            content-class="elevation-0 az-validacao-remove">
                            <template v-slot:activator="{on}">
                                <v-btn width="85" outlined class="styleButton yes" color="white" v-on="on">
                                    DEVOLVER
                                </v-btn>
                            </template>
                            <span class="az-validacao-remove-content">
                                Você tem certeza ?
                                <a @click="devolverTodosPatrimonios" class="remove ml-1">Devolver</a>
                                <a class="cancel ml-1">Cancelar</a>
                            </span>
                        </v-menu>
                    </v-col>
                    <v-col md="3" class="button__documento">
                        <documento-button :movimentacaoInternaId="movimentacao.id" :movimentacaoTipo="movimentacao.tipo" :visualizacao="true"/>
                    </v-col>
                </v-row>
                <v-row v-else>
                    <v-col md="11" sm="3" xs="7" class="d-flex justify-end">
                        <documento-button :movimentacaoInternaId="movimentacao.id" :movimentacaoTipo="movimentacao.tipo" :visualizacao="true"/>
                    </v-col>
                </v-row>
            </v-row>
    </div>

</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'
    import DocumentoButton from '@/views/components/botoes/DocumentoButton.vue'

    export default {
        name: 'VisualizarRegistroMovimentacaoInternaCabecalho',
        components: {
            LabelPersonalizado,
            CampoApresentacaoToolTip,
            DocumentoButton
        },
        props: {
            movimentacao: {
                type: Object
            },
            ehDestinatario: {
                type: Boolean,
                default: false
            }
        },
        computed: {
            possuiItensParaDevolver() {
                return ['AGUARDANDO_DEVOLUCAO','DEVOLVIDO_PARCIAL'].includes(this.movimentacao.situacao)
            }
        },
        data() {
            return {
                situacaoTemporaria:['AGUARDANDO_DEVOLUCAO','DEVOLVIDO_PARCIAL'],
                nomeTela: 'MOVIMENTACAO_INTERNA_LISTAGEM'
            }
        },
        methods: {
            devolverTodosPatrimonios() {
                this.$emit('devolverTodosPatrimonios')
            },
            abrirModalDevolverPatrimonios() {
                this.$router.push({name: 'ModalDevolverPatrimonios'})
            }
        }
    }
</script>

<style scoped lang="stylus">
.headerCabecalho
    font-size 0.9em
    color: white
    max-height 65px
    max-width 137px

    >>>
    .texto-campo-apresentacao-tooltip
        width 25vw

.az-validacao-remove
    min-width 240px!important
    max-width max-content
    overflow-y hidden

    &-content
        position: relative
        border-radius: .4em
        pointer-events: initial
        padding 5px
        background-color black
        font-size small
        color white

    .remove
        font-size small
        text-transform capitalize
        color red

    .cancel
        font-size small
        text-transform capitalize
        color grey

.acoes__temporaria
    width 385px

.button__devolver_parcial
    max-width none

.styleButton
    font-size 12px

@media (max-width: 1250px)
    .styleButton
        font-size 10px

    .acoes__temporaria
        width 190px
        margin 0
        flex-wrap nowrap

    .button__devolver_parcial
        padding-left 0
        padding-right 0

        button
            padding inherit !important

    .button__devolver
        padding-left 4px
        padding-right 0

        button
            font-size 9px
            width 0 !important
            padding 0 !important

    .button__documento
        padding-left 0
        padding-right 0
        flex-basis 10%
        width 30px

</style>
