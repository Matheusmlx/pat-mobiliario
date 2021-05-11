<template>
    <az-container>
        <v-row class="ml-4">
            <v-col cols="12" md="3" sm="6" xs="12">
                <v-img v-if="patrimonio.id" :src="'data:image/png;base64,' + patrimonio.imagem" max-width="50"
                       max-height="50" aspect-ratio="1" class="img-item-incorporacao"/>
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12">
                <campo-apresentacao label-padrao="Descrição" :text="patrimonio.descricao | textoSemValorSimples" />
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
                <campo-apresentacao label-padrao="Valor Aquisição" :text="patrimonio.valorAquisicao | valorParaReal" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="naturezaDespesa" :tela="telaItem" :text="patrimonio.naturezaDespesa | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0">
                <campo-apresentacao campo="contaContabil" :tela="telaItem" :text="patrimonio.contaContabil | textoSemValorSimples" />
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
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoEquipamentoArmamento">
                <campo-apresentacao campo="numeroSerie" :tela="telaListagemPatrimonio" :text="patrimonio.numeroSerie | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao campo="anoFabricacaoModelo" :tela="telaItem" :text="patrimonio.anoFabricacaoModelo | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao v-if="patrimonio.combustivel" campo="combustivel" :tela="telaItem" :text="patrimonio.combustivel | azEnum(combustivelItemIncorporacao)" />
                <campo-apresentacao v-else campo="combustivel" :tela="telaItem" :text="patrimonio.combustivel | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao v-if="patrimonio.categoria" campo="categoria" :tela="telaItem" :text="patrimonio.categoria | azEnum(categoriaItemIncorporacao)" />
                <campo-apresentacao v-else campo="categoria" :tela="telaItem" :text="patrimonio.categoria | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao campo="placa" :tela="telaListagemPatrimonio" :text="patrimonio.placa | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao campo="renavam" :tela="telaListagemPatrimonio" :text="patrimonio.renavam | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao v-if="patrimonio.licenciamento"
                                    campo="licenciamento"
                                    :tela="telaListagemPatrimonio"
                                    :text="patrimonio.licenciamento | azEnum(mesesLicenciamento)" />
                <campo-apresentacao v-else campo="licenciamento"
                                    :tela="telaListagemPatrimonio"
                                    :text="patrimonio.licenciamento | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao campo="motor" :tela="telaListagemPatrimonio" :text="patrimonio.motor | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" md="3" sm="6" xs="12" class="mt-0 pt-0" v-if="tipoVeiculo">
                <campo-apresentacao campo="chassi" :tela="telaListagemPatrimonio" :text="patrimonio.chassi | textoSemValorSimples" />
            </v-col>
        </v-row>

    </az-container>

</template>
<script>
    import {createNamespacedHelpers} from 'vuex'
    import CampoApresentacao from '@/views/components/campos/CampoApresentacao'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')
    export default {
        name: 'FichaPatrimonioDadosGeraisCamposVisualizar',
        components: {CampoApresentacao},
        props: {
            patrimonio: {
                required: true
            }
        },
        data: () => ({
            telaListagemPatrimonio: 'INCORPORACAO_ITEM_LISTAGEM_PATRMONIO',
            telaIncorporacao: 'INCORPORACAO_DADOS_GERAIS',
            telaItem: 'ITEM_INCORPORACAO',
            mascaraPlaca: 'AAA-#X##',
            mascaraChassi:'XXX XXXXX X XXXXXXXX',
            mascaraRenavam: '#################',
            mascaraMotor: 'XXXXXXXXXXXXXXXXX',
        }),
        computed:{
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            tipoVeiculo(){
                return this.patrimonio.tipo === 'VEICULO'
            },
            tipoEquipamentoArmamento(){
                return !!(this.patrimonio.tipo === 'ARMAMENTO' || this.patrimonio.tipo === 'EQUIPAMENTO')
            }
        }
    }
</script>

<style scoped lang="stylus">

.label
    font-size 13px
    font-weight bold
    color #777 !important

.licenciamento
    font-size: 13px
    margin-top: -5px
    margin-bottom 0 !important
</style>
