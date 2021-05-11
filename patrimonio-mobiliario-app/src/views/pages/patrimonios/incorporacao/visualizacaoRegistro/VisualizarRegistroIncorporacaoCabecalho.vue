<template>
    <v-row class="primary ml-0 mr-0">
        <v-row class="pl-5" cols="12" md="12">
            <v-col class="headerCabecalho" md="1" sm="2" xs="6">
                <span class="font-weight-bold">Código</span>
                <p class="mb-0">{{ incorporacao.codigo }}</p>
            </v-col>
            <v-col v-if="incorporacao.fornecedor" class="headerCabecalho ml-2" md="2" sm="4" xs="6">
                <span class="font-weight-bold">
                    <label-personalizado campo="fornecedor" :tela="nomeTela" apenasNome/>
                </span>
                <campo-apresentacao-tool-tip :texto="incorporacao.fornecedor"/>
            </v-col>
            <v-col v-else class="headerCabecalho" md="2" sm="2" xs="6">
                <span class="font-weight-bold">
                    <label-personalizado campo="fornecedor" :tela="nomeTela" apenasNome/>
                </span>
                <p>-</p>
            </v-col>
            <v-col class="headerCabecalho mr-4" lg="1" md="2" sm="2" xs="6">
                <span class="font-weight-bold">
                    <label-personalizado campo="dataRecebimento" :tela="nomeTela" apenasNome/>
                </span>
                <p class="mb-0">{{ incorporacao.dataRecebimento | azDate }}</p>
            </v-col>
            <v-col class="headerCabecalho" md="2" sm="3" xs="6">
                <span class="font-weight-bold">Situação</span>
                <p class="mb-0 incorporacao__situacao">{{ incorporacao.situacao | azEnum(situacoesIncorporacao) }}</p>
            </v-col>
            <v-spacer/>

            <div v-if="!possuiPatrimoniosEmOutrasMovimentacoes">
                <v-col class="d-flex justify-end" v-if="mesCorrente">
                    <modal-reabertura-incorporacao @reabrirIncorporacao="reabrirIncorporacao"/>
                </v-col>
                <v-col v-else-if="possuiPatrimoniosAtivos" class="d-flex justify-end">
                    <v-btn @click="abrirModalEstornarPatrimonios" dark text class="mr-8">
                        <span class="text-capitalize">Estornar Patrimônios</span>
                    </v-btn>
                </v-col>
            </div>
        </v-row>
    </v-row>
</template>

<script>
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import CampoApresentacaoToolTip from '@/views/components/campos/CampoApresentacaoToolTip'
    import ModalReaberturaIncorporacao
        from '@/views/components/modal/incorporacao/registroIncorporacao/reabertura/ModalReaberturaIncorporacao'

    export default {
        name: 'VisualizarRegistroIncorporacaoCabecalho',
        components: {
            LabelPersonalizado,
            ModalReaberturaIncorporacao,
            CampoApresentacaoToolTip
        },
        props: {
            incorporacao: {
                required: true
            }
        },
        computed: {
            mesCorrente() {
                const dataAtual = new Date()
                const dataFinalizacao = new Date(this.incorporacao.dataFinalizacao)

                return dataFinalizacao.getMonth() === dataAtual.getMonth() &&
                    dataFinalizacao.getFullYear() === dataAtual.getFullYear()
            },
            possuiPatrimoniosAtivos() {
                return this.incorporacao.situacao !== 'ESTORNADO'
            },
            possuiPatrimoniosEmOutrasMovimentacoes() {
                return this.incorporacao.possuiPatrimoniosEmOutrasMovimentacoes
            }
        },
        data: () => ({
            nomeTela: 'INCORPORACAO_DADOS_GERAIS'
        }),
        methods: {

            async reabrirIncorporacao(incorporacaoId) {
                await this.$emit('reabrirIncorporacao', incorporacaoId)
            },
            abrirModalEstornarPatrimonios() {
                this.$router.push({
                    name: 'ModalEstornarPatrimoniosMotivo',
                    params: {incorporacaoId: this.incorporacao.id}
                })
            }
        }
    }
</script>

<style scoped lang="stylus">
.headerCabecalho
    font-size 0.9em
    color: white
    max-height 65px

    .incorporacao__situacao
        width 200px

    >>>
    .texto-campo-apresentacao-tooltip
        width 13vw
</style>
