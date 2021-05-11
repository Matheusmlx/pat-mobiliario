<template>
    <v-form ref="form" class="az-form-content az-formItem mt-0 pl-0 pr-0">
        <v-container fluid>
            <v-row class="layoutModal">
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.marca"
                        v-validate="getObrigatorioRotulosPersonalizados('marca', nomeTela) ? 'required|max:100' : ''"
                        name="marca"
                        :error-messages="errors.collect('marca')"
                        placeholder="Informe a marca"
                        counter="100"
                        maxlength="100"
                        @focusout="salvarFormulario"
                        @change="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="marca" :tela="nomeTela"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.modelo"
                        v-validate="getObrigatorioRotulosPersonalizados('modelo', nomeTela) ? 'required|max:100' : ''"
                        name="modelo"
                        :error-messages="errors.collect('modelo')"
                        placeholder="Informe o modelo"
                        counter="100"
                        maxlength="100"
                        @focusout="salvarFormulario"
                        @change="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="modelo" :tela="nomeTela"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model="dadosDeEntrada.fabricante"
                        v-validate="getObrigatorioRotulosPersonalizados('fabricante', nomeTela) ? 'required|max:100' : ''"
                        name="fabricante"
                        :error-messages="errors.collect('fabricante')"
                        placeholder="Informe o fabricante"
                        counter="100"
                        maxlength="100"
                        @change="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="fabricante" :tela="nomeTela"/>
                        </template>
                    </v-text-field>
                </v-col>
                <v-col cols="4">
                    <v-text-field
                        v-model.number="dadosDeEntrada.quantidade"
                        v-validate="'required|max:6'"
                        name="quantidade"
                        v-mask="maskQuantidade"
                        :error-messages="errors.collect('quantidade')"
                        min="0"
                        maxlength="4"
                        placeholder="Informe a quantidade de itens"
                        @focusout="tratarCampoQuantidade">
                        <template v-slot:label>
                            <label-personalizado campo="quantidade" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </v-text-field>
                </v-col>

                <v-col :class="['valorTotal']" cols="4">
                    <az-money
                        v-if="dadosDeEntrada.id"
                        v-model="dadosDeEntrada.valorTotal"
                        :maxLength="10"
                        :validate-length="true"
                        :event-submit="'blur'"
                        :required="true"
                        class="botaoExcluirMoney"
                        :placeholder="'Informe o valor'"
                        @input="atualizarValorInserido($event)">
                        <template v-slot:label>
                            <label-personalizado campo="valorTotal" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </az-money>
                </v-col>

                <v-col cols="4">
                    <v-radio-group
                        v-model="dadosDeEntrada.numeracaoPatrimonial"
                        :mandatory="false"
                        row
                        v-validate="'required'"
                        name="numeracaoPatrimonio"
                        :error-messages="mostraErroSituacao ? errors.collect('numeracaoPatrimonio') : ''"
                        class="az-text radio-numeracao-patrimonial"
                        @change="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="numeracaoPatrimonial" :tela="nomeTela" obrigatorioPorPadrao apresentaTooltip/>
                        </template>
                        <v-radio label="Reserva" value="RESERVA" name="RESERVA" disabled/>
                        <v-radio
                            disabled
                            label="Automática"
                            value="AUTOMATICA"
                            name="AUTOMATICA"
                            @blur="mostrarErroSituacao"
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
                        ref="autoCompleteNaturezaDespesa"
                        v-validate="'required'"
                        :items="naturezasDespesa"
                        :placeholder="'Selecione a natureza de despesa do item'"
                        :filter="filtroComboAutoComplete"
                        :error-messages="errors.collect('naturezaDespesa')"
                        @change="salvarFormulario($event)">
                        <template v-slot:label>
                            <label-personalizado campo="naturezaDespesa" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                        <template slot="selection" slot-scope="data">
                            <span style="max-width: 100%; font-size: 12px; color: gray" class="text-truncate"> {{ data.item.despesa }} - {{ data.item.descricao }} </span>
                        </template>
                        <template slot="item" slot-scope="data">
                            <span
                                :style="obterLarguraAutoComplete('autoCompleteNaturezaDespesa')"
                                style="font-size: 12px" class="text-truncate">
                                {{ data.item.despesa }} - {{ data.item.descricao }}
                            </span>
                        </template>
                        <template v-slot:append-outer v-if="naturezaDespesaInativo">
                            <tooltip-campos-inativos mensagem="Esta natureza de despesa foi inativada, por favor selecione outra!"/>
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
                        placeholder="Selecione um estado de conservação"
                        :items="estadosConservacao"
                        item-text="nome"
                        item-value="id"
                        :insertNullItem="false"
                        is-required
                        @input="salvarFormulario"
                        name="EstadoDeConservacao">
                        <template v-slot:label>
                            <label-personalizado campo="estadoConservacao" :tela="nomeTela" obrigatorioPorPadrao/>
                        </template>
                    </v-select>
                </v-col>
                <v-col cols="4">
                    <v-tooltip top nudge-top max-width="800" v-model="apresentaTooltipImagem">
                        <template v-slot:activator="{ on, attrs }">
                            <div v-on="on"  v-bind="attrs">
                                <v-file-input
                                    class="mt-3"
                                    v-model="dadosDeEntrada.uriImagem"
                                    v-if="!dadosDeEntrada.uriImagem"
                                    file
                                    dense
                                    name="uriImagem"
                                    outlined
                                    append-icon="fa-file-upload"
                                    prepend-icon
                                    accept="image/png, image/jpeg, image/jpg"
                                    placeholder="Selecione uma imagem"
                                    @change="tratarEventoSalvarAnexo">
                                    <template v-slot:label>
                                        <label-personalizado campo="uriImagem" :tela="nomeTela"/>
                                    </template>
                                </v-file-input>
                                <v-text-field
                                    outlined
                                    readonly
                                    v-if="dadosDeEntrada.uriImagem"
                                    :value="nomeAnexo">
                                    <template v-slot:label>
                                        <label-personalizado campo="uriImagem" :tela="nomeTela"/>
                                    </template>
                                    <template v-slot:append>
                                        <v-tooltip top>
                                            <template v-slot:activator="{ on }">
                                                <a
                                                    v-on="on"
                                                    slot="activator"
                                                    v-on:click="anularCampoAnexo"
                                                    target="_blank"
                                                    class="az-action-link-icon mt-1">
                                                    <i class="fas fa-times-circle"></i>
                                                </a>
                                            </template>
                                            Remover
                                        </v-tooltip>
                                    </template>
                                    <template v-slot:append-outer>
                                        <v-tooltip v-if="dadosDeEntrada.id" top>
                                            <template v-slot:activator="{ on }">
                                                <a v-on="on"
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
                    <v-autocomplete
                        v-model="dadosDeEntrada.anoFabricacaoModelo"
                        :items="anoFabricacaoModelo"
                        placeholder="Informe o ano de fabricacao/modelo"
                        name="anoFabricacaoModelo"
                        v-validate="getObrigatorioRotulosPersonalizados('anoFabricacaoModelo', nomeTela) ? 'required' : ''"
                        :error-messages="errors.collect('anoFabricacaoModelo')"
                        @focusout="salvarFormulario"
                        @change="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="anoFabricacaoModelo" :tela="nomeTela"/>
                        </template>
                    </v-autocomplete>
                </v-col>
                <v-col cols="4" v-if="dadosDeEntrada.tipoBem === 'Veículo'">
                    <az-combo-enum
                        v-model="dadosDeEntrada.combustivel"
                        placeholder="Selecione"
                        maxlength="100"
                        name="CombustivelVeiculo"
                        :order-by="'value'"
                        :is-required="getObrigatorioRotulosPersonalizados('combustivel', nomeTela)"
                        :enum-object="combustivelItemIncorporacao"
                        :insertNullItem="false"
                        @input="salvarFormulario">
                        <template v-slot:label>
                            <label-personalizado campo="combustivel" :tela="nomeTela"/>
                        </template>
                    </az-combo-enum>
                </v-col>
                <v-col cols="4" v-if="dadosDeEntrada.tipoBem === 'Veículo'">
                    <az-combo-enum
                        v-model="dadosDeEntrada.categoria"
                        placeholder="Selecione"
                        maxlength="100"
                        name="CategoriaVeiculo"
                        :is-required="getObrigatorioRotulosPersonalizados('categoria', nomeTela)"
                        :enum-object="categoriaItemIncorporacao"
                        :insertNullItem="false"
                        @input="salvarFormulario"
                        order-by="''">
                        <template v-slot:label>
                            <label-personalizado campo="categoria" :tela="nomeTela"/>
                        </template>
                    </az-combo-enum>
                </v-col>
            </v-row>
        </v-container>
    </v-form>
</template>

<script>
    import {mutationTypes} from '@/core/constants'
    import {createNamespacedHelpers, mapMutations} from 'vuex'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        components: {LabelPersonalizado},
        $_veeValidate: {
            validator: 'new',
        },
        props: {
            dadosDeEntrada: {
                type: Object,
                required: true
            },
            naturezasDespesa: {
                type: Array,
                required: true
            },
            estadosConservacao: {
                type: Array,
                required: true
            },
            anoFabricacaoModelo: {
                type: Array,
                required: true
            },
            naturezaDespesaInativo:{
                type: Boolean,
                required: true
            },
            apresentaTooltipImagem: {
                type: Boolean,
                required: true
            },
            errorObrigatoriedadeAnexo: {
                type: Number,
                required: true
            },
            errorTamanhoExcedido: {
                type: Number,
                required: true
            },
        },
        data() {
            return {
                mostraErroSituacao: false,
                nomeAnexo: '',
                maskQuantidade: '######',
                nomeTela: 'ITEM_INCORPORACAO'
            }
        },
        async mounted() {
            this.tratarCampoAnexo()
            this.verificaSePodeProsseguir()
        },
        watch: {
            async 'dadosDeEntrada.uriImagem'() {
                this.tratarCampoAnexo()
            },
            'dadosDeEntrada.tipoBem': function (val) {
                this.dadosDeEntrada.tipoBem = this.$options.filters.tipoBem(val)
            },
            dadosDeEntrada: {
                async handler() {
                    this.verificaSePodeProsseguir()
                },
                deep: true
            },
        },
        computed: {
            ...mapGetters([
                'getObrigatorioRotulosPersonalizados',
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
            ...mapMutations([
                mutationTypes.PATRIMONIO.RESETA_PAGE_PATRIMONIO
            ]),
            async salvarFormulario() {
                await this.$emit('editarItem', this.dadosDeEntrada)
            },
            verificaSePodeProsseguir() {
                if (this.getItemIncorporacaoValidado(this.dadosDeEntrada, this.tipoVeiculo(), this.nomeTela)) {
                    this.$emit('bloquearPasso3', false)
                } else {
                    this.$emit('bloquearPasso3', true)
                }
            },
            tipoVeiculo() {
                return this.dadosDeEntrada.tipoBem === 'Veículo'
            },
            tratarCampoAnexo() {
                if (typeof this.dadosDeEntrada.uriImagem == 'string') {
                    this.nomeAnexo = this.dadosDeEntrada.uriImagem.split('/')[1]
                }
            },
            tratarCampoQuantidade() {
                if (this.dadosDeEntrada.quantidade === '' ||
                    this.dadosDeEntrada.quantidade === null) {
                    this.dadosDeEntrada.quantidade = 0
                }
                this.dadosDeEntrada.quantidade = parseInt(
                    this.dadosDeEntrada.quantidade
                )
                if (this.dadosDeEntrada.quantidade === 0) {
                    this.dadosDeEntrada.quantidade = null
                }
                this.resetaPagePatrimonio()
                this.salvarFormulario()
            },
            mostrarErroSituacao() {
                this.mostraErroSituacao = true
            },
            anularCampoAnexo() {
                this.dadosDeEntrada.uriImagem = null
                this.errorObrigatoriedadeAnexo = 0
                this.errorTamanhoExcedido = 0
                this.$emit('editarItem', this.dadosDeEntrada)
            },
            async tratarEventoSalvarAnexo() {
                this.$emit('salvarAnexo', this.dadosDeEntrada)
            },
            atualizarValorInserido(dadosDeEntrada) {
                this.dadosDeEntrada.valorTotal = dadosDeEntrada
                this.salvarFormulario()
            },
            filtroComboAutoComplete(item, queryText) {
                const text = item.nome.toLowerCase()
                const searchText = queryText.toLowerCase()
                return text.indexOf(searchText) > -1
            },
            obterLarguraAutoComplete(ref) {
                if (this.$refs[ref] && this.$refs[ref].$el) {
                    return {
                        width: (this.$refs[ref].$el.clientWidth-30)+'px'
                    }
                }
                return {}
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
