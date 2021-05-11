<template>
    <v-form ref="form" class="az-form-content az-formItem mt-0 pl-0 pr-0">
        <v-container fluid>
            <v-row class="layoutModal">
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.marca"
                        disabled
                        placeholder=" "
                        counter="100"
                        maxlength="100">
                        <template v-slot:label>
                            <label-personalizado campo="marca" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.modelo"
                        disabled
                        placeholder=" "
                        counter="100"
                        maxlength="100">
                        <template v-slot:label>
                            <label-personalizado campo="modelo" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.fabricante"
                        disabled
                        placeholder=" "
                        counter="100"
                        maxlength="100">
                        <template v-slot:label>
                            <label-personalizado campo="fabricante" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model.number="dadosDeEntrada.quantidade"
                        disabled
                        name="quantidade"
                        v-mask="maskQuantidade"
                        min="0"
                        placeholder=" ">
                        <template v-slot:label>
                            <label-personalizado campo="quantidade" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>

                <v-col :class="['valorTotal']" cols="4">
                    <az-money
                        v-if="dadosDeEntrada.id"
                        v-model="dadosDeEntrada.valorTotal"
                        :disabled="true"
                        :maxLength="18"
                        :event-submit="'blur'"
                        class="botaoExcluirMoney"
                        placeholder=" ">
                        <template v-slot:label>
                            <label-personalizado campo="valorTotal" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </az-money>
                </v-col>

                <v-col cols="4">
                    <v-radio-group
                        v-model="dadosDeEntrada.numeracaoPatrimonial"
                        :mandatory="false"
                        row
                        name="numeracaoPatrimonio"
                        class="az-text radio-numeracao-patrimonial">
                        <template v-slot:label>
                            <label-personalizado campo="numeracaoPatrimonial" :tela="nomeTela" :apresentaTooltip="true"/>
                        </template>
                        <v-radio label="Reserva" value="RESERVA" name="RESERVA" disabled />
                        <v-radio
                            disabled
                            label="Automática"
                            value="AUTOMATICA"
                            name="AUTOMATICA"
                        />
                    </v-radio-group>
                </v-col>
                <v-col cols="4">
                    <v-autocomplete
                        v-model="dadosDeEntrada.naturezaDespesa"
                        item-text="despesa"
                        item-value="id"
                        no-data-text="Não há naturezas de despesa com este nome"
                        :return-object="false"
                        name="naturezaDespesa"
                        :items="naturezasDespesa"
                        disabled
                        placeholder=" ">
                        <template v-slot:label>
                            <label-personalizado campo="naturezaDespesa" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                        <template slot="selection" slot-scope="data">
                            <span style="max-width: 330px; font-size: 12px; color: gray" class="text-truncate"> {{ data.item.despesa }} - {{ data.item.descricao }} </span>
                        </template>
                        <template slot="item" slot-scope="data">
                            <span style="max-width: 330px; font-size: 12px" class="text-truncate"> {{ data.item.despesa }} - {{ data.item.descricao }} </span>
                        </template>
                    </v-autocomplete>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="contaContabil"
                        :return-object="false"
                        placeholder=" "
                        disabled>
                        <template v-slot:label>
                            <label-personalizado campo="contaContabil" :tela="nomeTela" :apresentaTooltip="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.tipoBem"
                        placeholder=" "
                        disabled>
                        <template v-slot:label>
                            <label-personalizado campo="tipoBem" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-select
                        v-model="dadosDeEntrada.estadoConservacao"
                        placeholder=" "
                        :items="estadosConservacao"
                        item-text="nome"
                        item-value="id"
                        :insertNullItem="false"
                        disabled
                        name="EstadoDeConservacao">
                        <template v-slot:label>
                            <label-personalizado campo="estadoConservacao" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-select>
                </v-col>
                <v-col cols="4">
                    <v-tooltip top nudge-top max-width="800">
                        <template v-slot:activator="{ on }">
                            <div v-on="on">
                    <v-file-input
                        class="mt-3"
                        v-model="dadosDeEntrada.uriImagem"
                        v-if="!dadosDeEntrada.uriImagem"
                        file
                        dense
                        name="uriImagem"
                        outlined
                        disabled
                        append-icon="fa-file-upload"
                        prepend-icon
                        accept="image/png, image/jpeg, image/jpg, application/pdf"
                        placeholder=" ">
                        <template v-slot:label>
                            <label-personalizado campo="uriImagem" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-file-input>
                    <v-text-field
                        outlined
                        readonly
                        disabled
                        v-if="dadosDeEntrada.uriImagem"
                        :value="nomeAnexo">
                        <template v-slot:label>
                            <label-personalizado campo="uriImagem" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                        <template v-slot:append-outer>
                            <v-tooltip v-if="dadosDeEntrada.id" top>
                                <template v-slot:activator="{ on }">
                                    <a  v-on="on"
                                        slot="activator"
                                        :href="criarLinkDownload(dadosDeEntrada.uriImagem)"
                                        target="_blank"
                                        class="az-action-link-icon mt-1">
                                        <i class="fas fa-download"></i>
                                    </a>
                                </template>
                                Download
                            </v-tooltip>
                        </template>
                    </v-text-field>
                            </div>
                        </template>
                        Até 15mb
                    </v-tooltip>
                </v-col>
                <v-col cols="4" v-if="dadosDeEntrada.tipoBem === 'Veículo'">
                    <v-text-field
                        v-model="dadosDeEntrada.anoFabricacaoModelo"
                        placeholder=" "
                        v-mask="maskAnoFabricacao"
                        name="anoFabricacaoModelo"
                        disabled>
                        <template v-slot:label>
                            <label-personalizado campo="anoFabricacaoModelo" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4" v-if="dadosDeEntrada.tipoBem === 'Veículo'">
                    <az-combo-enum
                        v-model="dadosDeEntrada.combustivel"
                        placeholder=" "
                        maxlength="100"
                        name="CombustivelVeiculo"
                        :order-by="'value'"
                        :disabled="true"
                        :enum-object="combustivelItemIncorporacao"
                        :insertNullItem="false">
                        <template v-slot:label>
                            <label-personalizado campo="combustivel" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </az-combo-enum>
                </v-col>
                <v-col cols="4" v-if="dadosDeEntrada.tipoBem === 'Veículo'">
                    <az-combo-enum
                        v-model="dadosDeEntrada.categoria"
                        placeholder=" "
                        maxlength="100"
                        name="CategoriaVeiculo"
                        :disabled="true"
                        :enum-object="categoriaItemIncorporacao"
                        :insertNullItem="false">
                        <template v-slot:label>
                            <label-personalizado campo="categoria" :tela="nomeTela" :apenasNome="true"/>
                        </template>
                    </az-combo-enum>
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {createNamespacedHelpers, mapActions, mapMutations} from 'vuex'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        $_veeValidate: {
            validator: 'new',
        },
        props: {
            dadosDeEntrada: {
                default: {},
            },
        },
        components: {LabelPersonalizado},
        data() {
            return {
                naturezasDespesa: [],
                estadosConservacao: [],
                mostraErroSituacao: false,
                errorObrigatoriedadeAnexo: 0,
                errorTamanhoExcedido: 0,
                nomeAnexo: '',
                maskQuantidade: '######',
                maskAnoFabricacao: '####/####',
                nomeTela: 'ITEM_INCORPORACAO'
            }
        },
        async mounted() {
            this.tratarCampoAnexo()
            await this.buscarDadosGeraisItemIncorporacao()
            await this.buscarNaturezasDespesa()
            await this.buscarTodosEstadosConservacao()
        },
        watch: {
            async 'dadosDeEntrada.uriImagem'() {
                this.tratarCampoAnexo()
            },
            'dadosDeEntrada.tipoBem': function (val) {
                this.dadosDeEntrada.tipoBem = this.$options.filters.tipoBem(val)
            },
            dadosDeEntrada() {
                this.verificaSePodeProsseguir()
            },
        },
        computed: {
            ...mapGetters([
                'getItemIncorporacaoValidado',
            ]),

            contaContabil: {
                get() {
                    const value = this.dadosDeEntrada.contaContabil
                    if (value && value.codigo) {
                        let codigo1 = value.codigo.substr(0,2)
                        let codigo2 = value.codigo.substr(2,3)
                        let codigo3 = value.codigo.substr(5,2)
                        let codigo4 = value.codigo.substr(7,2)
                        return `${codigo1}.${codigo2}.${codigo3}.${codigo4} - ${value.descricao}`
                    }
                    return null
                }
            },
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.BUSCAR_ITEM_INCORPORACAO,
                actionTypes.PATRIMONIO.INCORPORACAO.INCORPORACAO_ITEM.ESTADO_CONSERVACAO.BUSCAR_ESTADOS_CONSERVACAO
            ]),
            ...mapMutations([
                mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO
            ]),
            verificaSePodeProsseguir() {
                if (this.getItemIncorporacaoValidado(this.dadosDeEntrada,this.tipoVeiculo() , this.nomeTela)) {
                    this.$emit('bloquearPasso3', false)
                } else {
                    this.$emit('bloquearPasso3', true)
                }
            },
            tipoVeiculo(){
                return this.dadosDeEntrada.tipoBem === 'Veículo'
            },
            tratarCampoAnexo() {
                if (typeof this.dadosDeEntrada.uriImagem == 'string') {
                    this.nomeAnexo = this.dadosDeEntrada.uriImagem.split('/')[1]
                }
            },
            async buscarDadosGeraisItemIncorporacao() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarItemIncorporacao({idItem:  this.$route.params.itemIncorporacaoId, idIncorporacao: this.$route.params.incorporacaoId})
                this.habilitarLoadingGlobal()
                this.dadosDeEntrada = resultado
                this.dadosDeEntrada.numeracaoPatrimonial = 'AUTOMATICA'
            },
            async buscarNaturezasDespesa() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.$store.dispatch(
                    actionTypes.COMUM.BUSCAR_NATUREZAS_DESPESA,
                    this.dadosDeEntrada.codigo
                )
                this.habilitarLoadingGlobal()
                if(resultado){
                    if(resultado.items.length === 0){
                        this.mostrarNotificacaoAviso('Item sem conta contábil, natureza de despesa ou tipo ativos. Escolha outro item')
                    }
                    this.naturezasDespesa = resultado.items
                }
            },
            async buscarTodosEstadosConservacao() {
                const {estadosConservacao} = await this.buscarEstadosConservacao()
                this.estadosConservacao = estadosConservacao
            }
        }
    }
</script>

<style lang="stylus">
    .layoutModal
        background #FFF !important
        padding 15px
        margin 0px

    .botaoExcluirMoney .v-icon.v-icon
        font-size 16px !important

    .az-formItem
        padding-bottom 0px
        margin-bottom 0px
        height auto

    .tamanhoPermitidoClass
        color #a5a5a5
        margin-top -10px
        font-size 12px

    .radio-numeracao-patrimonial
        cursor not-allowed

</style>
