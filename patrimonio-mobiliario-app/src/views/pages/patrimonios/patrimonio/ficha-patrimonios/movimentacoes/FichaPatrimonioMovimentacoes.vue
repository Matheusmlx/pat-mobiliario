<template>
    <div class="corpo-conteiner">
            <v-col md="7" sm="12" xs="12" class="alinhamentoTimeline">
                <v-timeline>
                    <v-timeline-item
                        v-for="item in movimentacoes"
                        :key="item.dataFinalizacao"
                        :color="retornaCor(item)"
                        :icon="retornaIcone(item)"
                        fill-dot small>
                        <template v-slot:opposite>
                            <span
                                class="text-left text--darken-1 grey--text text-body-2">{{ retornaData(item) | azDate() }}</span>
                        </template>
                        <v-card v-if="movimentacaoIncorporacao(item)"
                            width="350"
                                @click="redirecionaRegistroIncorporacao(item)">
                            <v-card-title
                                class="font-weight-bold text-body-1 pb-0 text--darken-1 primary--text">
                                {{ item.tipo | azEnum(tipoMovimentacoes) }}
                            </v-card-title>
                            <v-card-text class="text-left text--darken-1 grey--text">
                                <span class="font-weight-bold">N° Nota: </span>{{ item.numeroNotaLancamentoContabil | textoSemValorSimples }}<br/>
                                <span class="font-weight-bold">Valor Total: </span>{{ item.valorTotal | valorParaReal }}<br/>
                                <span class="font-weight-bold">Situação: </span>{{ item.situacao | azEnum(situacoesIncorporacao) }}<br/>
                            </v-card-text>
                        </v-card>

                        <v-card width="350"
                                v-else
                                @click="redirecionaRegistroMovimentacao(item)">
                            <v-card-title
                                class="font-weight-bold text-body-1 pb-0 text--darken-1" :class="[movimentacaoEmElaboracao(item)? 'orange--text': 'primary--text']">
                                {{ item.tipo | azEnum(tipoMovimentacoes) }}
                            </v-card-title>
                            <v-card-text class="text-left text--darken-1 grey--text">
                                <span class="font-weight-bold">De: </span>{{ item.setorOrigem | textoSemValorSimples }}<br/>
                                <span class="font-weight-bold">Para: </span>{{ item.setorDestino | textoSemValorSimples }}<br/>
                                <span class="font-weight-bold">Situação: </span>{{ item.situacao | azEnum(situacoesMovimentacaoInterna) }}<br/>
                            </v-card-text>
                        </v-card>
                    </v-timeline-item>
                </v-timeline>
            </v-col>
        <v-col v-if="mostraBotoes" md="12" sm="12" xs="12" class="d-flex justify-center align-center">
            <botao-ver-mais v-if="mostraVerMais" @verMais="tratarEventoVerMais" />
            <botao-ver-menos v-else @verMenos="tratarEventoVerMenos" />
        </v-col>
    </div>
</template>

<script>
    import {createNamespacedHelpers, mapActions} from 'vuex'
    import _ from 'lodash'
    import {actionTypes} from '@/core/constants'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import BotaoVerMais from '@/views/components/botoes/BotaoVerMais'
    import BotaoVerMenos from '@/views/components/botoes/BotaoVerMenos'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'FichaPatrimonioMovimentacoes',
        components: {BotaoVerMenos, BotaoVerMais},
        props: {
            dadosIncorporacao: {
                type: Object,
                required: true
            }
        },
        data() {
            return {
                movimentacoes: [],
                todasMovimentacoes: [],
                cincoMovimentacoes: [],
                incorporacao:{},
                situacoesSemAlteracao: ['FINALIZADO','DEVOLVIDO','ESTORNADO','PARCIALMENTE_ESTORNADO'],
                nomeTelaDadosGerais: 'MOVIMENTACAO_INTERNA_DADOS_GERAIS',
                nomeTelaIncorporacao: 'INCORPORACAO_DADOS_GERAIS'
            }
        },
        watch: {
            'dadosIncorporacao'() {
                this.preencherIncorporacao()
                this.buscarMovimentacoes()
            }
        },
        mounted() {
            this.buscarIncorporacao()
        },
        computed: {
            ...mapGetters(['getDistribuicaoValidado']),
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Normal'])
            },
            verificaPermissaoEdicaoMovimentacao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
            mostraVerMais(){
                return this.movimentacoes.length <= 5
            },
            mostraBotoes(){
                return this.todasMovimentacoes.length > 5
            }
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.BUSCAR_TODAS_MOVIMENTACOES,
                actionTypes.MOVIMENTACAO_INTERNA.ITEM.BUSCAR_QUATIDADE_ITENS_ADICIONADOS_MOVIMENTACAO_INTERNA
            ]),
            buscarIncorporacao() {
                this.$emit('buscarIncorporacao')
            },
            preencherIncorporacao() {
                this.incorporacao = _.cloneDeep(this.dadosIncorporacao)
            },
            redirecionaRegistroIncorporacao(item) {
                if(this.verificaPermissaoEdicao){
                    this.$router.push({name: 'VisualizarRegistroIncorporacao', params:{incorporacaoId:item.incorporacaoId}})
                }else{
                    this.$router.push({name: 'VisualizarRegistroIncorporacaoVisualizacao', params: {incorporacaoId: item.incorporacaoId}})
                }
            },
            async redirecionaRegistroMovimentacao(item) {
                if (this.verificaPermissaoEdicaoMovimentacao) {
                    const id = item.id
                    if (item.situacao === 'FINALIZADO' || item.situacao === 'AGUARDANDO_DEVOLUCAO' || item.situacao === 'DEVOLVIDO') {
                        await this.$router.push({name: 'VisualizarRegistroMovimentacaoInterna', params: {movimentacaoInternaId: id}})
                    }
                    else if (item.situacao === 'EM_ELABORACAO') {
                        if (await this.verificarSeCamposObrigatoriosPreenchidos(item)) {
                            if (await this.verificarSePossuiItensNaMovimentacao(id)) {
                                this.redirecionaParaDocumentos(id)
                            } else {
                                this.redirecionaParaItens(id)
                            }
                        } else {
                            this.redirecionaParaDadosGerais(id)
                        }
                    }
                }
            },
            async verificarSeCamposObrigatoriosPreenchidos(item) {
                if(item.tipo === 'DISTRIBUICAO'){
                    return await this.getDistribuicaoValidado(item, this.nomeTelaDadosGerais, this.nomeTelaIncorporacao)
                }
                return false
            },
            async verificarSePossuiItensNaMovimentacao(id) {
                const resultado = await this.buscarQuantidadeItensAdicionadosMovimentacaoInterna(id)
                return resultado ? (resultado.quantidadeItens > 0) : false
            },
            redirecionaParaDocumentos(id) {
                this.$router.push({name: 'MovimentacaoInternaEdicaoDocumentos', params: {movimentacaoInternaId: id}})
            },
            redirecionaParaItens(id) {
                this.$router.push({name: 'MovimentacaoInternaEdicaoItens', params: {movimentacaoInternaId: id}})
            },
            redirecionaParaDadosGerais(id) {
                this.$router.push({name: 'MovimentacaoInternaEdicaoDadosGerais', params: {movimentacaoInternaId: id}})
            },
            async buscarMovimentacoes(){
                const retorno = await this.buscarTodasMovimentacoes(this.$route.params.patrimonioId)
                if (!this.incorporacao.isEmpty) {
                    retorno.items.push(this.incorporacao)
                }
                if(retorno && retorno.items.length > 0){
                    this.verificaQuantidadeMovimentacoes(retorno.items)
                }
            },
            verificaQuantidadeMovimentacoes(movimentacoes){
                this.todasMovimentacoes = movimentacoes
                this.cincoMovimentacoes = movimentacoes.slice(0, 5)
                if(movimentacoes.length > 5){
                    this.movimentacoes = this.cincoMovimentacoes
                }else{
                    this.movimentacoes = this.todasMovimentacoes
                }
            },
            tratarEventoVerMais(){
                this.movimentacoes = this.todasMovimentacoes
            },
            tratarEventoVerMenos(){
                this.movimentacoes = this.cincoMovimentacoes
            },
            retornaIcone(item){
                return this.situacoesSemAlteracao.includes(item.situacao) ? 'done' : 'priority_high'
            },
            retornaCor(item){
                return this.situacoesSemAlteracao.includes(item.situacao) ? 'primary' : 'orange'
            },
            movimentacaoIncorporacao(item){
                return item.tipo === 'INCORPORACAO'
            },
            movimentacaoEmElaboracao(item) {
                return item.situacao === 'EM_ELABORACAO'
            },
            retornaData(item){
                return item.situacao === 'FINALIZADO' ||  item.situacao === 'DEVOLVIDO' ? item.dataFinalizacao : item.dataCriacao
            }
        }
    }
</script>

<style scoped lang="stylus">
.corpo-conteiner
    min-height 75vh

.alinhamentoTimeline
    margin-left auto
    margin-right auto
    padding-top 100px !important
</style>
