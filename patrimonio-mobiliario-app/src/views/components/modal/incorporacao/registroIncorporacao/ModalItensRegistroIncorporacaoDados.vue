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
                    <campo-de-texto-editavel
                        v-model="dadosGerais.marca"
                        :tipo="'marca'"
                        :idObjeto="dadosGerais.id"
                        name="Marca"
                        :validate="getObrigatorioRotulosPersonalizados('marca', nomeTela) ? 'required|max:100' : ''"
                        placeholder="Informe a marca"
                        :counter="100"
                        :maxlength="100"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="modelo" :tela="nomeTela" :apenasNome="true"/>
                    <campo-de-texto-editavel
                        v-model="dadosGerais.modelo"
                        :tipo="'modelo'"
                        :idObjeto="dadosGerais.id"
                        name="Modelo"
                        :validate="getObrigatorioRotulosPersonalizados('modelo', nomeTela) ? 'required|max:100' : ''"
                        placeholder="Informe o modelo"
                        :counter="100"
                        :maxlength="100"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <label-personalizado class="font-weight-bold" campo="fabricante" :tela="nomeTela" :apenasNome="true"/>
                    <campo-de-texto-editavel
                        v-model="dadosGerais.fabricante"
                        :tipo="'fabricante'"
                        :idObjeto="dadosGerais.id"
                        name="Fabricante"
                        :validate="getObrigatorioRotulosPersonalizados('fabricante', nomeTela) ? 'required|max:100' : ''"
                        placeholder="Informe o fabricante"
                        :counter="100"
                        :maxlength="100"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <campo-apresentacao  campo="quantidade" :tela="nomeTela" :text="dadosGerais.quantidade | textoSemValorSimples"/>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <campo-apresentacao  campo="valorTotal" :tela="nomeTela" :text="dadosGerais.valorTotal | valorParaReal"/>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <campo-apresentacao  campo="contaContabil" :tela="nomeTela" :text="dadosGerais.contaContabilDescricao | textoSemValorSimples"/>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <campo-apresentacao  campo="estadoConservacao" :tela="nomeTela" :text="dadosGerais.estadoConservacaoNome | textoSemValorSimples"/>
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3">
                    <campo-apresentacao  campo="tipoBem" :tela="nomeTela" :text="dadosGerais.tipoBem | tipoBem"/>
                </v-col>
            </v-row>
            <v-row>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="anoFabricacaoModelo" :tela="nomeTela" :apenasNome="true"/>
                    <campo-de-select-editavel
                        v-model="dadosGerais.anoFabricacaoModelo"
                        :tipo="'anoFabricacaoModelo'"
                        :idObjeto="dadosGerais.id"
                        :items="anoFabricacaoModelo"
                        :placeholder="'Informe o ano de fabricacao/modelo'"
                        :noDataText="'Ano não identificado'"
                        :name="'anoFabricacaoModelo'"
                        :validate="getObrigatorioRotulosPersonalizados('anoFabricacaoModelo', nomeTela) ? 'required' : ''"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="combustivel" :tela="nomeTela" :apenasNome="true"/>
                    <campo-de-select-enum-editavel
                        v-model="dadosGerais.combustivel"
                        :placeholder="'Selecione'"
                        :name="'CombustivelVeiculo'"
                        :order-by="'value'"
                        :validate="getObrigatorioRotulosPersonalizados('combustivel', nomeTela)"
                        :items="combustivelItemIncorporacao"
                        :insertNullItem="false"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
                <v-col class="headerItemsRegistro pt-0" cols="3" v-if="tipoVeiculo">
                    <label-personalizado class="font-weight-bold" campo="categoria" :tela="nomeTela" :apenasNome="true"/>
                    <campo-de-select-enum-editavel
                        v-model="dadosGerais.categoria"
                        :placeholder="'Selecione'"
                        :name="'CategoriaVeiculo'"
                        :order-by="'value'"
                        :validate="getObrigatorioRotulosPersonalizados('categoria', nomeTela)"
                        :items="categoriaItemIncorporacao"
                        :insertNullItem="false"
                        @input="salvarFormulario(dadosGerais)"
                    />
                </v-col>
            </v-row>
        </div>
        <v-divider></v-divider>
    </div>
</template>

<script>
    import _ from 'lodash'
    import {createNamespacedHelpers, mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'
    import CampoDeTextoEditavel from '@/views/components/camposEditaveis/campo-de-texto-editavel'
    import CampoDeSelectEditavel from '@/views/components/camposEditaveis/campo-de-select-editavel'
    import CampoDeSelectEnumEditavel from '@/views/components/camposEditaveis/campo-de-select-enum-editavel'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoApresentacao from '@/views/components/campos/CampoApresentacao'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'ModalItensRegistroIncorporacaoDados',
        components: {LabelPersonalizado, CampoDeSelectEnumEditavel, CampoDeSelectEditavel, CampoDeTextoEditavel, CampoApresentacao},
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
            ...mapGetters([
                'getObrigatorioRotulosPersonalizados',
            ]),
            tipoVeiculo() {
                return this.dadosGerais.tipoBem === 'VEICULO'
            },
        },
        async mounted() {
            this.construirItem()
            this.geraAnoModeloFabricacao()
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.EDITAR_ITEM_INCORPORACAO,
            ]),
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
            async salvarFormulario() {
                this.desabilitarLoadingGlobal()
                await this.editarItemIncorporacao(this.dadosGerais)
                this.habilitarLoadingGlobal()
            },
        }
    }
</script>

<style lang="stylus" scoped>
    .campo-tooltip
        margin-top 5px
        display flex

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

</style>
