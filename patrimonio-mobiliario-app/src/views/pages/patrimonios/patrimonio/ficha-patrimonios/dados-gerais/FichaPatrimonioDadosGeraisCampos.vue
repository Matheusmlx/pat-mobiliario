<template>
    <az-container>
        <v-row class="ml-4">
            <v-col cols="12" md="3" sm="6" xs="12">
                <campo-de-imagem-editavel
                    v-if="!patrimonioEstornado"
                    v-model="patrimonio.imagem"
                    :uri="patrimonio.uriImagem"
                    :name="'patrimonio-imagem'"
                    :placeholder="'Selecione uma imagem'"
                    :validate="getObrigatorioRotulosPersonalizados('uriImagem', telaItem) ? 'required' : ''"
                    @input="atualizarPatrimonio"/>
                <v-img
                    v-else
                    :src="'data:image/png;base64,' + patrimonio.imagem"
                    max-width="50"
                    max-height="50"
                    aspect-ratio="1"
                    class="img-campo"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12">
                <campo-apresentacao label-padrao="Descrição" :text="patrimonio.descricao | textoSemValorSimples"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12">
                <campo-apresentacao campo="orgao" :tela="telaIncorporacao" :text="patrimonio.orgao | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12">
                <campo-apresentacao campo="setor" :tela="telaIncorporacao" :text="patrimonio.setor | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="reconhecimento" :tela="telaIncorporacao" :text="patrimonio.reconhecimento | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="convenio" :tela="telaIncorporacao" :text="patrimonio.convenio | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="fundo" :tela="telaIncorporacao" :text="patrimonio.fundo | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="projeto" :tela="telaIncorporacao" :text="patrimonio.projeto | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="comodante" :tela="telaIncorporacao" :text="patrimonio.comodante | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="naturezaDespesa" :tela="telaItem" :text="patrimonio.naturezaDespesa | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="contaContabil" :tela="telaItem" :text="patrimonio.contaContabilClassificacao | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <label-personalizado class="label" campo="contaContabilAtual" :tela="telaFichaIndividual" :apresentaTooltip="true" :apenas-nome="true"/>
                <campo-apresentacao-tool-tip :texto="patrimonio.contaContabilAtual" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao label-padrao="Valor Aquisição" :text="patrimonio.valorAquisicao | valorParaReal" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="tipoBem" :tela="telaItem" :text="patrimonio.tipo | azEnum(tipoBem)" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="estadoConservacao" :tela="telaItem" :text="patrimonio.estadoConservacao | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="marca" :tela="telaItem" :text="patrimonio.marca | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="modelo" :tela="telaItem" :text="patrimonio.modelo | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="fabricante" :tela="telaItem" :text="patrimonio.fabricante | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoEquipamentoArmamento && !patrimonioEstornado">
                <campo-de-texto-editavel-ficha
                    v-model="patrimonio.numeroSerie"
                    name="numeroSerie"
                    campo="numeroSerie"
                    maxlength="50"
                    :tela="telaListagemPatrimonio"
                    :validate="getObrigatorioRotulosPersonalizados('numeroSerie', telaListagemPatrimonio) ? 'required|max:50' : 'max:50'"
                    placeholder="Informe o número de série"
                    @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoEquipamentoArmamento && patrimonioEstornado">
                <campo-apresentacao  campo="numeroSerie" :tela="telaListagemPatrimonio" :text="patrimonio.numeroSerie | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao  campo="anoFabricacaoModelo" :tela="telaItem" :text="patrimonio.anoFabricacaoModelo | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao v-if="patrimonio.combustivel" campo="combustivel" :tela="telaItem" :text="patrimonio.combustivel | azEnum(combustivelItemIncorporacao)" />
                <campo-apresentacao v-else campo="combustivel" :tela="telaItem" :text="patrimonio.combustivel | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao v-if="patrimonio.categoria" campo="categoria" :tela="telaItem" :text="patrimonio.categoria | azEnum(categoriaItemIncorporacao)" />
                <campo-apresentacao v-else campo="categoria" :tela="telaItem" :text="patrimonio.categoria | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && !patrimonioEstornado">
            <campo-de-texto-editavel-ficha
                v-model="patrimonio.placa"
                name="placa"
                maxlength="8"
                :mask="mascaraPlaca"
                placa
                :tela="telaListagemPatrimonio"
                campo="placa"
                :validate="getObrigatorioRotulosPersonalizados('placa', telaListagemPatrimonio) ? 'required|max:8' : 'max:8'"
                placeholder="Informe a placa"
                @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && patrimonioEstornado">
                <campo-apresentacao  campo="placa" :tela="telaListagemPatrimonio" :text="placaFormatada | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && !patrimonioEstornado">
                <campo-de-texto-editavel-ficha
                    v-model="patrimonio.renavam"
                    name="renavam"
                    maxlength="17"
                    :mask="mascaraRenavam"
                    :tela="telaListagemPatrimonio"
                    campo="renavam"
                    :validate="getObrigatorioRotulosPersonalizados('renavam', telaListagemPatrimonio) ? 'required|max:17' : 'max:17'"
                    placeholder="Informe o renavam"
                    @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && patrimonioEstornado">
                <campo-apresentacao  campo="renavam" :tela="telaListagemPatrimonio" :text="patrimonio.renavam | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && !patrimonioEstornado">
                <label-personalizado class="label" campo="licenciamento" :tela="telaListagemPatrimonio" :apenasNome="true"/>
                <campo-de-select-enum-editavel
                    class="licenciamento"
                    v-model="patrimonio.licenciamento"
                    :items="mesesLicenciamento"
                    placeholder="Informe o licenciamento"
                    name="licenciamento"
                    :orderBy="''"
                    :insert-null-item="!getObrigatorioRotulosPersonalizados('licenciamento', telaListagemPatrimonio)"
                    :validate="getObrigatorioRotulosPersonalizados('licenciamento', telaListagemPatrimonio)"
                    @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && patrimonioEstornado">
                <campo-apresentacao  campo="licenciamento" :tela="telaListagemPatrimonio" :text="patrimonio.licenciamento | azEnum(mesesLicenciamento)" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && !patrimonioEstornado">
                <campo-de-texto-editavel-ficha
                    v-model="patrimonio.motor"
                    name="motor"
                    maxlength="17"
                    :mask="mascaraMotor"
                    :tela="telaListagemPatrimonio"
                    campo="motor"
                    :validate="getObrigatorioRotulosPersonalizados('motor', telaListagemPatrimonio) ? 'required|max:17' : 'max:17'"
                    placeholder="Informe o motor"
                    @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && patrimonioEstornado">
                <campo-apresentacao  campo="motor" :tela="telaListagemPatrimonio" :text="patrimonio.motor | textoSemValorSimples" />
            </v-col>

            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && !patrimonioEstornado">
                <campo-de-texto-editavel-ficha
                    v-model="patrimonio.chassi"
                    name="chassi"
                    maxlength="20"
                    :mask="mascaraChassi"
                    chassi
                    :tela="telaListagemPatrimonio"
                    campo="chassi"
                    :validate="getObrigatorioRotulosPersonalizados('chassi', telaListagemPatrimonio) ? 'required|max:20' : 'max:20'"
                    placeholder="Informe o chassi"
                    @input="atualizarPatrimonio"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo && patrimonioEstornado">
                <campo-apresentacao  campo="chassi" :tela="telaListagemPatrimonio" :text="chassiFormatado | textoSemValorSimples" />
            </v-col>
        </v-row>

    </az-container>

</template>
<script>
    import {createNamespacedHelpers} from 'vuex'
    import CampoApresentacao from '@/views/components/campos/CampoApresentacao'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoDeTextoEditavelFicha from '@/views/components/camposEditaveis/campo-de-texto-editavel-ficha'
    import CampoDeSelectEnumEditavel from '@/views/components/camposEditaveis/campo-de-select-enum-editavel'
    import CampoDeImagemEditavel from '@/views/components/camposEditaveis/campo-de-imagem-editavel'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'FichaPatrimonioDadosGeraisCampos',
        components: {
            CampoApresentacaoToolTip,
            CampoApresentacao,
            CampoDeTextoEditavelFicha,
            LabelPersonalizado ,
            CampoDeImagemEditavel,
            CampoDeSelectEnumEditavel},
        props: {
            patrimonio: {
                required: true
            }
        },
        data: () => ({
            telaListagemPatrimonio: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO',
            telaIncorporacao: 'INCORPORACAO_DADOS_GERAIS',
            telaItem: 'ITEM_INCORPORACAO',
            telaFichaIndividual: 'FICHA_INDIVIDUAL_PATRIMONIO',
            mascaraPlaca: 'AAA-#X##',
            mascaraChassi: 'XXX XXXXX X XXXXXXXX',
            mascaraRenavam: '#################',
            mascaraMotor: 'XXXXXXXXXXXXXXXXX',
        }),
        computed: {
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            tipoVeiculo(){
                return this.patrimonio.tipo === 'VEICULO'
            },
            tipoEquipamentoArmamento(){
                return !!(this.patrimonio.tipo === 'ARMAMENTO' || this.patrimonio.tipo === 'EQUIPAMENTO')
            },
            patrimonioEstornado(){
                return this.patrimonio.situacao === 'ESTORNADO'
            },
            placaFormatada(){
                if(this.patrimonio.placa) {
                    if (this.patrimonio.placa.charAt(3) !== '-') {
                        const placa1 = this.patrimonio.placa.substr(0, 3)
                        const placa2 = this.patrimonio.placa.substr(3, 7)
                        return placa1 + '-' + placa2
                    }
                    return this.patrimonio.placa
                }
                return null
            },
            chassiFormatado(){
                if(this.patrimonio.chassi) {
                    if (this.patrimonio.chassi.charAt(3) !== ' ') {
                        const chassi1 = this.patrimonio.chassi.substr(0, 3)
                        const chassi2 = this.patrimonio.chassi.substr(3, 5)
                        const chassi3 = this.patrimonio.chassi.charAt(8)
                        const chassi4 = this.patrimonio.chassi.substr(9, 8)
                        return chassi1 + ' ' + chassi2 + ' ' + chassi3 + ' ' + chassi4
                    }
                    return this.patrimonio.chassi
                }
                return null
            }
        },
        methods: {
            atualizarPatrimonio() {
                this.$emit('atualizarPatrimonio', this.patrimonio)
            },
        }
    }
</script>

<style scoped lang="stylus">
.img-campo
    border 1px solid #777

.label
    font-size 13px
    font-weight bold
    color #777 !important

.licenciamento
    font-size: 13px
    margin-top: -5px
    margin-bottom 0 !important

>>>
    .texto-campo-apresentacao-tooltip
        width 13vw
        font-size 13px
        color #777 !important

</style>
