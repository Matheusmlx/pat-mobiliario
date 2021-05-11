<template>
    <div class="pa-6">
        <v-row>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <campo-de-texto-editavel-ficha
                    v-model="movimentacao.numeroNotaLancamentoContabil"
                    name="numero"
                    campo="numeroNotaLancamentoContabil"
                    :disabled="!verificaPermissaoEdicao"
                    :tela="nomeTelaIncorporacao"
                    :mask="'####NL######'"
                    :validate="getObrigatorioRotulosPersonalizados('numeroNotaLancamentoContabil', nomeTelaIncorporacao) ? 'required' : ''"
                    :label-customizado-apenas-nome="false"
                    placeholder="Informe o número da nota"
                    @input="tratarEventoEdicao"/>
            </v-col>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <campo-de-data-editavel-ficha
                    v-model="movimentacao.dataNotaLancamentoContabil"
                    :disabled="!verificaPermissaoEdicao"
                    campo="dataNotaLancamentoContabil"
                    :tela="nomeTelaIncorporacao"
                    :required="getObrigatorioRotulosPersonalizados('dataNotaLancamentoContabil', nomeTelaIncorporacao)"
                    :name="'Data NL'"
                    :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                    @input="tratarEventoEdicao"/>
            </v-col>
            <v-col style="height: 67px" cols="12" sm="12" md="4" lg="3" v-if="movimentacaoTemporaria">
                <campo-apresentacao :labelPadrao="'Enviado em'" :text="movimentacao.dataEnvio | azDate"/>
            </v-col>
            <v-col style="height: 67px" cols="12" sm="12" md="4" lg="3" v-if="movimentacaoTemporaria">
                <div class="d-flex align-start">
                    <campo-apresentacao :labelPadrao="'Devolvido em'" :text="movimentacao.dataFinalizacao | azDate"/>
                    <v-tooltip top v-if="movimentacao.situacao === 'DEVOLVIDO_PARCIAL'">
                        <template v-slot:activator="{ on, attrs }">
                            <v-icon
                                class="ml-4"
                                color="#FBC02D"
                                dark
                                v-bind="attrs"
                                v-on="on">
                                error
                            </v-icon>
                        </template>
                        <span>{{ tooltip }}</span>
                    </v-tooltip>
                </div>
            </v-col>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <span class="font-weight-bold subtitle-2 grey--text text--darken-1">
                    <label-personalizado class="label" campo="orgao" :tela="nomeTelaIncorporacao" apenasNome/>
                </span>
                <campo-apresentacao-tool-tip :texto="movimentacao.orgao" />
            </v-col>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <span class="font-weight-bold subtitle-2 grey--text text--darken-1">
                    <span>{{setorOrigem}}</span>
                </span>
                <div class="campo-apresentacao-tooltip">
                    <campo-apresentacao-tool-tip :texto="movimentacao.setorOrigem" />
                </div>
            </v-col>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <span class="font-weight-bold subtitle-2 grey--text text--darken-1">
                    <span>{{setorDestino}}</span>
                </span>
                <div class="campo-apresentacao-tooltip">
                    <campo-apresentacao-tool-tip :texto="movimentacao.setorDestino" />
                </div>
            </v-col>
            <v-col style="height: 67px" cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <campo-apresentacao campo="numeroProcesso" :tela="nomeTelaMovimentacao" :text="movimentacao.numeroProcesso | textoSemValorSimples" />
            </v-col>
            <v-col cols="12" sm="12" md="4" :lg="movimentacaoTemporaria ? 3 : 4">
                <campo-apresentacao labelPadrao="Responsável" :text="movimentacao.responsavel | textoSemValorSimples" />
            </v-col>
            <v-col
                cols="12"
                sm="12"
                :md="movimentacaoTemporaria ? 12 : 8"
                :lg="movimentacaoTemporaria ? 9 : 8">
                <span class="font-weight-bold subtitle-2 grey--text text--darken-1">
                    <label-personalizado class="label" campo="motivoObservacao" :tela="nomeTelaMovimentacao" apenasNome/>
                </span>
                <p class="motivo body-2">{{ movimentacao.motivoObservacao | textoSemValorSimples}}</p>
            </v-col>
        </v-row>
    </div>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoDeTextoEditavelFicha from '@/views/components/camposEditaveis/campo-de-texto-editavel-ficha'
    import CampoDeDataEditavelFicha from '@/views/components/camposEditaveis/campo-de-data-editavel-ficha'
    import CampoApresentacao from '@/views/components/campos/CampoApresentacao'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'
    import {createNamespacedHelpers} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'VisualizarRegistroMovimentacaoInternaDadosGerais',
        components: {
            LabelPersonalizado,
            CampoDeTextoEditavelFicha,
            CampoDeDataEditavelFicha,
            CampoApresentacao,
            CampoApresentacaoToolTip
        },
        props: {
            movimentacao: {
                type: Object
            },
            tooltip: {
                type: String,
                default: ''
            }
        },
        computed: {
            ...mapGetters(['getObrigatorioRotulosPersonalizados']),
            verificaPermissaoEdicao() {
                return AzHasPermissions(
                    this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes']
                )
            },
            movimentacaoTemporaria() {
                return this.movimentacao.tipo === 'TEMPORARIA'
            }
        },
        mounted() {
            this.formatarLabels()
        },
        data() {
            return {
                nomeTelaMovimentacao: 'MOVIMENTACAO_INTERNA_DADOS_GERAIS',
                nomeTelaIncorporacao: 'INCORPORACAO_DADOS_GERAIS',
                setorOrigem:'Setor Origem',
                setorDestino:'Setor Destino',
                almoxarifado_origem:['ENTRE_ESTOQUES','DISTRIBUICAO'],
                almoxarifado_destino:['DEVOLUCAO_ALMOXARIFADO','ENTRE_ESTOQUES']
            }
        },
        methods: {
            tratarEventoEdicao() {
                this.$emit('editar', this.movimentacao)
            },
            async formatarLabels(){
                if (this.almoxarifado_origem.includes(await this.movimentacao.tipo)) {
                    this.setorOrigem = 'Almoxarifado Origem'
                }

                if (this.almoxarifado_destino.includes(await this.movimentacao.tipo)) {
                    this.setorDestino = 'Almoxarifado Destino'
                }
            }
        },
        updated() {
            this.formatarLabels()
        }
    }
</script>

<style scoped lang="stylus">
    .texto
    .motivo
        font-size 13px !important
        color #777 !important

    .label
        font-size 13px

    .campo-apresentacao-tooltip
        width 10vw

    .motivo
        overflow-wrap break-word

    >>>
        .texto-campo-apresentacao-tooltip
            width 18vw
            font-size 13px
            color #777 !important
            margin-left 0

</style>
