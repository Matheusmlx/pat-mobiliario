<template>
    <div>
        <v-form ref="form" class="az-form-content pb-0">
            <v-container class="white movimentacao-edicao-dados-gerais" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="devolucaoAlmoxarifado.orgao"
                            :disabled="!verificaPermissaoEdicao"
                            :filter="filtroComboAutoComplete"
                            :items="orgaosOrigem"
                            :placeholder="verificaPermissaoEdicao ? 'Selecione o órgão de origem' : ' '"
                            :required="true"
                            :requiredIcon="verificaPermissaoEdicao"
                            item-text="descricao"
                            item-value="id"
                            label="Órgão Responsável"
                            name="orgao"
                            no-data-text="Não há órgãos com este nome"
                            @change="tratarEventoMudancaOrgaoOrigem"
                            @changeValue="alterarValor">
                        </auto-complete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="devolucaoAlmoxarifado.setorOrigem"
                            :disabled="!devolucaoAlmoxarifado.orgao || !verificaPermissaoEdicao"
                            :items="setoresOrigem"
                            :placeholder="verificaPermissaoEdicao && devolucaoAlmoxarifado.orgao ? 'Selecione o setor de origem' : ' '"
                            :required="devolucaoAlmoxarifado.orgao ? 'required' : ''"
                            :requiredIcon="devolucaoAlmoxarifado.orgao && verificaPermissaoEdicao"
                            item-text="descricao"
                            item-value="id"
                            label="Setor Origem"
                            name="setorOrigem"
                            no-data-text="Não há setores com este nome"
                            @change="editarSetorOrigem"
                            @changeValue="alterarValor">
                        </auto-complete>
                    </v-col>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="devolucaoAlmoxarifado.setorDestino"
                            :disabled="!devolucaoAlmoxarifado.orgao || !verificaPermissaoEdicao"
                            :items="setoresDestino"
                            :placeholder="verificaPermissaoEdicao && devolucaoAlmoxarifado.orgao ? 'Selecione o setor de destino' : ' '"
                            :required="devolucaoAlmoxarifado.orgao ? 'required' : ''"
                            :requiredIcon="devolucaoAlmoxarifado.orgao && verificaPermissaoEdicao"
                            item-text="descricao"
                            item-value="id"
                            label="Almoxarifado Destino"
                            name="setorDestino"
                            no-data-text="Não há setores com este nome"
                            @change="editarSetorDestino"
                            @changeValue="alterarValor">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="12" sm="12" xs="12">
                        <v-textarea
                            v-model="devolucaoAlmoxarifado.motivoObservacao"
                            :counter="500"
                            :error-messages="errors.collect('motivoObservacao')"
                            auto-grow
                            class="mt-3"
                            label="Motivo/Obs."
                            maxlength="500"
                            name="motivoObservacao"
                            placeholder="Digite o motivo"
                            rows="1"
                            @change="editarDevolucaoAlmoxarifado">
                        </v-textarea>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="devolucaoAlmoxarifado.numeroNotaLancamentoContabil"
                            v-mask="'####NL######'"
                            v-validate="'max:12'"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            :disabled="!verificaPermissaoEdicao"
                            :error-messages="errors.collect('numeroNotaLancamentoContabil')"
                            :placeholder="verificaPermissaoEdicao ? placeholders.numeroNotaLancamentoContabil : ' '"
                            label="Número da NL"
                            maxlength="12"
                            name="numeroNotaLancamentoContabil"
                            @change="editarDevolucaoAlmoxarifado">
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="devolucaoAlmoxarifado.dataNotaLancamentoContabil"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            :error-messages="errors.collect('dataNotaLancamentoContabil')"
                            :is-disabled="!verificaPermissaoEdicao"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            :placeholderDate="(devolucaoAlmoxarifado.dataNotaLancamentoContabil) ? ' ' : placeholders.date"
                            date
                            label="Data da NL"
                            name-date="dataNotaLancamentoContabil"
                            @input="editarDevolucaoAlmoxarifado">
                        </az-date>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="devolucaoAlmoxarifado.numeroProcesso"
                            v-validate="'max:100'"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            :counter="100"
                            :disabled="!verificaPermissaoEdicao"
                            :error-messages="errors.collect('numeroProcesso')"
                            :placeholder="verificaPermissaoEdicao ? 'Informe o número' : ' '"
                            label="N° Processo"
                            maxlength="100"
                            name="numeroProcesso"
                            @change="editarDevolucaoAlmoxarifado">
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="devolucaoAlmoxarifado.responsavel"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            :disabled="!verificaPermissaoEdicao"
                            :error-messages="errors.collect('responsavel')"
                            :items="responsaveis"
                            item-text="nome"
                            item-value="id"
                            label="Responsável"
                            name="responsavel"
                            no-data-text="Não há responśaveis com esse nome"
                            placeholder="Selecione o responsável"
                            @change="editarDevolucaoAlmoxarifado">
                        </v-autocomplete>
                    </v-col>
                </v-row>
            </v-container>
        </v-form>
        <acoes-botoes-continuar-voltar :controleContinuar="podeContinuar" @continuar="continuar" @voltar="voltar"/>
    </div>
</template>

<script>
    import {actionTypes, mutationTypes} from '@/core/constants'
    import {mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import AutoComplete from '@/views/components/autocomplete/AutoComplete'

    export default {
        name: 'EdicaoDadosGeraisDevolucaoAlmoxarifado',
        components: {AutoComplete, AcoesBotoesContinuarVoltar},
        data() {
            return {
                devolucaoAlmoxarifado: {
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
                }
            }
        },
        async mounted() {
            await this.buscarDevolucaoAlmoxarifado()
            await this.buscarOrgaosOrigem()
            await this.buscarResponsaveis()
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
            podeContinuar() {
                if (this.devolucaoAlmoxarifado.orgao && this.devolucaoAlmoxarifado.setorOrigem && this.devolucaoAlmoxarifado.setorDestino) {
                    this.habilitaPasso3()
                    return true
                } else {
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
                actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.ATUALIZAR_DEVOLUCAO_ALMOXARIFADO,
                actionTypes.MOVIMENTACAO_INTERNA.DEVOLUCAO_ALMOXARIFADO.BUSCAR_DEVOLUCAO_ALMOXARIFADO_POR_ID
            ]),
            habilitaPasso3() {
                this.$emit('habilitaPasso3')
            },
            desabilitaPasso3() {
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
                if (item && item.nome && item.sigla) {
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
                if (this.devolucaoAlmoxarifado.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresNaoAlmoxarifado(this.devolucaoAlmoxarifado.orgao)
                    this.habilitarLoadingGlobal()
                    this.setoresOrigem = resultado.items
                    this.selecionaSetorSeArrayConterApenasUmSetorOrigem(this.setoresOrigem)
                }
            },
            async buscarSetoresDestino() {
                this.setoresDestino = []
                if (this.devolucaoAlmoxarifado.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresAlmoxarifado(this.devolucaoAlmoxarifado.orgao)
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
                if ((setores.length === 1) && !this.devolucaoAlmoxarifado.setorOrigem) {
                    this.devolucaoAlmoxarifado.setorOrigem = setores[0].id
                    this.editarDevolucaoAlmoxarifado()
                }
            },
            selecionaSetorSeArrayConterApenasUmSetorDestino(setores) {
                if (setores.length === 1 && !this.devolucaoAlmoxarifado.setorDestino) {
                    this.devolucaoAlmoxarifado.setorDestino = setores[0].id
                    this.editarDevolucaoAlmoxarifado()
                }
            },
            async selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaos) {
                if (orgaos.length === 1 && !this.devolucaoAlmoxarifado.orgao) {
                    this.devolucaoAlmoxarifado.orgao = orgaos[0].id
                }
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            async editarDevolucaoAlmoxarifado() {
                this.desabilitarLoadingGlobal()
                try {
                    const dadosAtualizados = await this.atualizarDevolucaoAlmoxarifado(this.devolucaoAlmoxarifado)
                    this.setarDadosAtualizados(dadosAtualizados)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                    await this.buscarDevolucaoAlmoxarifado()
                }
                this.habilitarLoadingGlobal()
            },
            async buscarDevolucaoAlmoxarifado() {
                this.desabilitarLoadingGlobal()
                this.devolucaoAlmoxarifado = await this.buscarDevolucaoAlmoxarifadoPorId(this.$route.params.movimentacaoInternaId)
                this.habilitarLoadingGlobal()
            },
            editarSetorOrigem(setorOrigem) {
                if (setorOrigem) {
                    if (setorOrigem !== this.devolucaoAlmoxarifado.setorDestino) {
                        this.devolucaoAlmoxarifado.setorOrigem = setorOrigem
                        this.editarDevolucaoAlmoxarifado()
                    }
                }
            },
            editarSetorDestino(setorDestino) {
                if (setorDestino) {
                    if (setorDestino !== this.devolucaoAlmoxarifado.setorOrigem) {
                        this.devolucaoAlmoxarifado.setorDestino = setorDestino
                        this.editarDevolucaoAlmoxarifado()
                    }
                }
            },
            async trataEventoSelecaoDeOrgaoOrigem() {
                await this.editarDevolucaoAlmoxarifado()
                await this.buscarSetoresOrigem()
                await this.buscarSetoresDestino()
            },
            async tratarEventoMudancaOrgaoOrigem() {
                this.anulaSetoresAoMudarOrgao()
                this.limparListaSetores()
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            anulaSetoresAoMudarOrgao() {
                this.devolucaoAlmoxarifado.setorOrigem = null
                this.devolucaoAlmoxarifado.setorDestino = null
            },
            limparListaSetores() {
                this.setoresOrigem = []
                this.setoresDestino = []
            },
            setarDadosAtualizados(dados) {
                this.verificarMudancasPropriedades(dados, this.dadosDeEntrada, this.devolucaoAlmoxarifado)
            },
            alterarValor(chave, valor) {
                this.devolucaoAlmoxarifado[chave] = valor
            }
        }
    }
</script>

<style lang="stylus" scoped>
.movimentacao-edicao-dados-gerais
    min-height 62vh

.label-motivo
    font-size 14px
    font-weight bold
    color #555555
    opacity 0.7

</style>
