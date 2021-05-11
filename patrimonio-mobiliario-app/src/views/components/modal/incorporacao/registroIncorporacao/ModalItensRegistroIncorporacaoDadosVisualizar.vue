<template>
    <div>
        <div class="headerRegistro">
            <v-row>
                <v-col cols="12" class="headerItensDescricaoRegistro">
                    <v-tooltip top nudge-top max-width="800">
                        <template v-slot:activator="{ on }">
                            <div v-on="on">
                                <label-personalizado class="font-weight-bold" campo="descricao" :tela="nomeTela" :apenasNome="true"/>
                                <p class="mb-0 mt-1 text-truncate">{{dadosGerais.descricao | textoSemValorSimples}}</p>
                            </div>
                        </template>
                        {{dadosGerais.descricao | textoSemValorSimples}}
                    </v-tooltip>
                </v-col>
            </v-row>
            <v-row class="mb-2">
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="marca" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1 text-truncate">{{dadosGerais.marca | textoSemValorSimples}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="modelo" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1 text-truncate">{{dadosGerais.modelo | textoSemValorSimples}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="fabricante" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1 text-truncate">{{dadosGerais.fabricante | textoSemValorSimples}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="quantidade" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1">{{dadosGerais.quantidade | textoSemValorSimples}}</p>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="valorTotal" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1"><span v-if="dadosGerais.valorTotal">R$</span>{{dadosGerais.valorTotal | valorParaDinheiro}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="contaContabil" :tela="nomeTela" :apenasNome="true"/>
                    <campo-apresentacao-tool-tip :texto="dadosGerais.contaContabilDescricao"/>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="estadoConservacao" :tela="nomeTela" :apenasNome="true"/>
                    <campo-apresentacao-tool-tip :texto="dadosGerais.estadoConservacaoNome"/>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="tipoBem" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1">{{dadosGerais.tipoBem | tipoBem}}</p>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="anoFabricacaoModelo" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1" v-if="dadosGerais.anoFabricacaoModelo">{{dadosGerais.anoFabricacaoModelo | textoSemValorSimples}}</p>
                    <p class="mb-0 mt-1" v-else >{{dadosGerais.anoFabricacaoModelo | textoSemValorSimples}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="combustivel" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1" v-if="dadosGerais.combustivel">{{dadosGerais.combustivel | azEnum(combustivelItemIncorporacao)}}</p>
                    <p class="mb-0 mt-1" v-else >{{dadosGerais.combustivel | textoSemValorSimples}}</p>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="categoria" :tela="nomeTela" :apenasNome="true"/>
                    <p class="mb-0 mt-1" v-if="dadosGerais.categoria">{{dadosGerais.categoria | azEnum(categoriaItemIncorporacao)}}</p>
                    <p class="mb-0 mt-1" v-else >{{dadosGerais.categoria | textoSemValorSimples}}</p>
                </v-col>
            </v-row>
        </div>
        <v-divider></v-divider>
    </div>
</template>

<script>
    import _ from 'lodash'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'

    export default {
        name: 'ModalItensRegistroIncorporacaoDadosVisualizar',
        components: {LabelPersonalizado, CampoApresentacaoToolTip},
        props: ['value'],
        data() {
            return {
                nomeTela: 'ITEM_INCORPORACAO',
                dadosGerais: {},
                contaContabilNome: null,
                anoFabricacaoModelo: []
            }
        },
        computed: {
            tipoVeiculo() {
                return this.dadosGerais.tipoBem === 'VEICULO'
            },
        },
        async mounted() {
            this.construirItem()
            this.geraAnoModeloFabricacao()
        },
        methods: {
            construirItem() {
                this.dadosGerais = _.cloneDeep(this.value)
            },
            geraAnoModeloFabricacao() {
                const anoMinimo = 1980
                const anoAtual = new Date().getFullYear()
                for (let ano = anoAtual; ano >= anoMinimo; ano--) {
                    let anoFabricacao = parseInt(ano)
                    let anoModeloMaior = parseInt(ano + 1)
                    this.anoFabricacaoModelo.push(anoFabricacao + '/' + anoModeloMaior)
                    this.anoFabricacaoModelo.push(anoFabricacao + '/' + anoFabricacao)
                }
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .headerItemsRegistro
        font-size 14.4px !important
        color #838383
        max-height 65px
        min-width 170px
        max-width 270px

    .headerRegistro
        background #FFF !important
        padding 0px 24px 20px

    .headerItensDescricaoRegistro
        font-size 14.4px !important
        color #838383
        display inline-block

    .headerItensDescricaoRegistro p
        max-width 1050px

    >>>
        .texto-campo-apresentacao-tooltip
            width 13vw

</style>
