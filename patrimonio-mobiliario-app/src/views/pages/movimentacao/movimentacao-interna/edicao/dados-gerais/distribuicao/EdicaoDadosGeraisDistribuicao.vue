<template>
    <div>
        <v-form class="az-form-content pb-0" ref="form">
            <v-container class="white movimentacao-edicao-dados-gerais" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="distribuicao.orgao"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há órgãos com este nome"
                            name="orgao"
                            :required="true"
                            :requiredIcon="verificaPermissaoEdicao"
                            :items="orgaosOrigem"
                            :disabled="!verificaPermissaoEdicao"
                            :placeholder="verificaPermissaoEdicao ? 'Selecione o órgão de origem' : ' '"
                            :filter="filtroComboAutoComplete"
                            campo="orgao"
                            :nomeTela="nomeTelaIncorporacao"
                            :obrigatorioPorPadrao="true"
                            label="Órgão Responsável"
                            @changeValue="alterarValor"
                            @change="tratarEventoMudancaOrgaoOrigem">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="distribuicao.setorOrigem"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há setores com este nome"
                            name="setorOrigem"
                            :required="distribuicao.orgao ? 'required' : ''"
                            :requiredIcon="distribuicao.orgao && verificaPermissaoEdicao"
                            :items="setoresOrigem"
                            :disabled="!distribuicao.orgao || !verificaPermissaoEdicao"
                            :placeholder="verificaPermissaoEdicao && distribuicao.orgao ? 'Selecione o setor de origem' : ' '"
                            label="Almoxarifado Origem"
                            @changeValue="alterarValor"
                            @change="editarDistribuicao">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="distribuicao.setorDestino"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há setores com este nome"
                            name="setorDestino"
                            :required="distribuicao.orgao ? 'required' : ''"
                            :items="setoresDestino"
                            :disabled="!distribuicao.orgao || !verificaPermissaoEdicao"
                            :placeholder="verificaPermissaoEdicao && distribuicao.orgao ? 'Selecione o setor de destino' : ' '"
                            :permissaoEdicao="!!(distribuicao.orgao) && verificaPermissaoEdicao"
                            campo="setorDestino"
                            :nomeTela="nomeTela"
                            :obrigatorioPorPadrao="true"
                            @changeValue="alterarValor"
                            @change="editarDistribuicao">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="12" sm="12" xs="12">
                        <v-textarea
                            v-model="distribuicao.motivoObservacao"
                            :counter="500"
                            :error-messages="errors.collect('motivoObservacao')"
                            auto-grow
                            rows="1"
                            maxlength="500"
                            name="motivoObservacao"
                            v-validate="getObrigatorioRotulosPersonalizados('motivoObservacao', nomeTela)? 'required': ''"
                            placeholder="Digite o motivo"
                            class="mt-3"
                            @change="editarDistribuicao">
                            <template v-slot:label>
                                <label-personalizado class="label-motivo" campo="motivoObservacao" :tela="nomeTela"
                                                     :permissaoEdicao="verificaPermissaoEdicao"/>
                            </template>
                        </v-textarea>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="distribuicao.numeroNotaLancamentoContabil"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name="numeroNotaLancamentoContabil"
                            :placeholder="verificaPermissaoEdicao ? placeholders.numeroNotaLancamentoContabil : ' '"
                            maxlength="12"
                            v-mask="'####NL######'"
                            v-validate="getObrigatorioRotulosPersonalizados('numeroNotaLancamentoContabil', nomeTelaIncorporacao)?'required|max:12' : 'max:12'"
                            :error-messages="errors.collect('numeroNotaLancamentoContabil')"
                            @change="editarDistribuicao">
                            <template v-slot:label>
                                <label-personalizado campo="numeroNotaLancamentoContabil" :tela="nomeTelaIncorporacao"
                                                     :permissaoEdicao="verificaPermissaoEdicao"/>
                            </template>
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="distribuicao.dataNotaLancamentoContabil"
                            :is-disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name-date="dataNotaLancamentoContabil"
                            date
                            :is-required="getObrigatorioRotulosPersonalizados('dataNotaLancamentoContabil', nomeTelaIncorporacao)"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            @input="editarDistribuicao"
                            :placeholderDate="(distribuicao.dataNotaLancamentoContabil) ? ' ' : placeholders.date"
                            :error-messages="errors.collect('dataNotaLancamentoContabil')">
                            <template v-slot:label-date>
                                <label-personalizado campo="dataNotaLancamentoContabil" :tela="nomeTelaIncorporacao"
                                                     :permissaoEdicao="verificaPermissaoEdicao"/>
                            </template>
                        </az-date>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="distribuicao.numeroProcesso"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name="numeroProcesso"
                            :placeholder="verificaPermissaoEdicao ? 'Informe o número' : ' '"
                            maxlength="100"
                            :counter="100"
                            v-validate="getObrigatorioRotulosPersonalizados('numeroProcesso', nomeTela)?'required|max:100' : 'max:100'"
                            :error-messages="errors.collect('numeroProcesso')"
                            @change="editarDistribuicao">
                            <template v-slot:label>
                                <label-personalizado campo="numeroProcesso" :tela="nomeTela"
                                                     :permissaoEdicao="verificaPermissaoEdicao"/>
                            </template>
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="distribuicao.responsavel"
                            :disabled="!verificaPermissaoEdicao"
                            :items="responsaveis"
                            item-text="nome"
                            item-value="id"
                            no-data-text="Não há responśaveis com esse nome"
                            name="responsavel"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            v-validate="getObrigatorioRotulosPersonalizados('responsavel', nomeTelaDistribuicao)? 'required': ''"
                            :error-messages="errors.collect('responsavel')"
                            placeholder="Selecione o responsável"
                            @change="editarDistribuicao">
                            <template v-slot:label>
                                <label-personalizado campo="responsavel" :tela="nomeTelaDistribuicao"
                                                     :permissaoEdicao="verificaPermissaoEdicao"/>
                            </template>
                        </v-autocomplete>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>
        <acoes-botoes-continuar-voltar :controleContinuar="podeContinuar" @voltar="voltar" @continuar="continuar"/>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {createNamespacedHelpers, mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import LabelPersonalizado from '@/views/components/label/LabelPersonalizado'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import AutoComplete from '@/views/components/autocomplete/AutoComplete'

    const {mapGetters} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        name: 'EdicaoDadosGeraisDistribuicao',
        components: {AutoComplete, AcoesBotoesContinuarVoltar, LabelPersonalizado},
        data() {
            return {
                distribuicao: {
                    responsavel: null
                },
                dadosDeEntrada: {},
                responsaveis: [],
                orgaosOrigem: [],
                setoresOrigem: [],
                setoresDestino: [],
                placeholders: {
                    date: '__/__/____',
                    numeroNotaLancamentoContabil: '____NL_____'
                },
                nomeTela: 'MOVIMENTACAO_INTERNA_DADOS_GERAIS',
                nomeTelaIncorporacao: 'INCORPORACAO_DADOS_GERAIS',
                nomeTelaDistribuicao: 'DISTRIBUICAO_DADOS_GERAIS'
            }
        },
        async mounted() {
            await this.buscarDistribuicao()
            await this.buscarOrgaosOrigem()
            await this.buscarResponsaveis()
        },
        computed: {
            ...mapGetters([
                'getObrigatorioRotulosPersonalizados',
                'getDistribuicaoValidado'
            ]),
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
            podeContinuar(){
                if(this.getDistribuicaoValidado(this.distribuicao,this.nomeTela,this.nomeTelaIncorporacao, this.nomeTelaDistribuicao)){
                    this.habilitaPasso3()
                    return true
                }else{
                    this.desabilitaPasso3()
                    return false
                }
            }
        },
        methods: {
            ...mapActions({
                buscarTodosOrgaosOrigem: actionTypes.COMUM.BUSCAR_TODOS_ORGAOS,
                buscarTodosResponsaveis: actionTypes.RESPONSAVEL.BUSCAR_TODOS_RESPONSAVEIS
            }),
            ...mapActions([
                actionTypes.COMUM.BUSCAR_SETORES_ALMOXARIFADO,
                actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO,
                actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.ATUALIZAR_DISTRIBUICAO,
                actionTypes.MOVIMENTACAO_INTERNA.DISTRIBUICAO.BUSCAR_DISTRIBUICAO_POR_ID
            ]),
            habilitaPasso3(){
                this.$emit('habilitaPasso3')
            },
            desabilitaPasso3(){
                this.$emit('desabilitaPasso3')
            },
            voltar() {
                this.$router.replace({name: 'MovimentacaoInternaEdicaoTipo'})
            },
            continuar() {
                this.$router.replace({
                    name: 'MovimentacaoInternaEdicaoItens',
                    params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                })
            },
            filtroComboAutoComplete(item, queryText) {
                if(item && item.nome && item.sigla){
                    const text = item.nome.toLowerCase()
                    const textSigla = item.sigla.toLowerCase()
                    const searchText = queryText.toLowerCase()
                    return (text.indexOf(searchText) > -1 || textSigla.indexOf(searchText) > -1)
                }
                return false
            },
            async buscarOrgaosOrigem() {
                this.desabilitarLoadingGlobal()
                const resultado = await this.buscarTodosOrgaosOrigem()
                this.habilitarLoadingGlobal()
                if (resultado) {
                    this.orgaosOrigem = resultado.items
                    await this.selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(this.orgaosOrigem)
                }
            },
            async buscarSetoresOrigem() {
                this.setoresOrigem = []
                if (this.distribuicao.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresAlmoxarifado(this.distribuicao.orgao)
                    this.habilitarLoadingGlobal()
                    this.setoresOrigem = resultado.items
                    this.selecionaSetorSeArrayConterApenasUmSetorOrigem(this.setoresOrigem)
                }
            },
            async buscarSetoresDestino() {
                this.setoresDestino = []
                if (this.distribuicao.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresNaoAlmoxarifado(this.distribuicao.orgao)
                    this.habilitarLoadingGlobal()
                    this.setoresDestino = resultado.items
                    this.selecionaSetorSeArrayConterApenasUmSetorDestino(this.setoresDestino)
                }
            },
            async buscarResponsaveis() {
                const resposta = await this.buscarTodosResponsaveis()
                this.responsaveis = resposta.items
            },
            selecionaSetorSeArrayConterApenasUmSetorOrigem(setores) {
                if (setores.length === 1 && !this.distribuicao.setorOrigem) {
                    this.distribuicao.setorOrigem = setores[0].id
                    this.editarDistribuicao()
                }
            },
            selecionaSetorSeArrayConterApenasUmSetorDestino(setores) {
                if (setores.length === 1 && !this.distribuicao.setorDestino) {
                    this.distribuicao.setorDestino = setores[0].id
                    this.editarDistribuicao()
                }
            },
            async selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaos) {
                if (orgaos.length === 1 && !this.distribuicao.orgao) {
                    this.distribuicao.orgao = orgaos[0].id
                }
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            async editarDistribuicao() {
                this.desabilitarLoadingGlobal()
                try {
                    const dadosAtualizados = await this.atualizarDistribuicao(this.distribuicao)
                    this.setarDadosAtualizados(dadosAtualizados)
                } catch(err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                    await this.buscarDistribuicao()
                } finally {
                    this.habilitarLoadingGlobal()
                }
            },
            async buscarDistribuicao(){
                this.desabilitarLoadingGlobal()
                this.distribuicao = await this.buscarDistribuicaoPorId(this.$route.params.movimentacaoInternaId)
                this.habilitarLoadingGlobal()
            },
            async trataEventoSelecaoDeOrgaoOrigem() {
                await this.editarDistribuicao()
                await this.buscarSetoresOrigem()
                await this.buscarSetoresDestino()
            },
            async tratarEventoMudancaOrgaoOrigem(){
                this.anulaSetoresAoMudarOrgao()
                this.limparListaSetores()
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            anulaSetoresAoMudarOrgao(){
                this.distribuicao.setorOrigem = null
                this.distribuicao.setorDestino = null
            },
            limparListaSetores() {
                this.setoresOrigem = []
                this.setoresDestino = []
            },
            setarDadosAtualizados(dados) {
                this.verificarMudancasPropriedades(dados, this.dadosDeEntrada, this.distribuicao)
            },
            alterarValor(chave, valor) {
                this.distribuicao[chave] = valor
            }
        }
    }
</script>

<style scoped lang="stylus">
.movimentacao-edicao-dados-gerais
    min-height 62vh


.label-motivo
    font-size 14px
    font-weight bold
    color #555555
    opacity 0.7

</style>
