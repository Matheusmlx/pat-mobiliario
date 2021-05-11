<template>
    <div>
        <v-form class="az-form-content pb-0" ref="form">
            <v-container class="white movimentacao-edicao-dados-gerais" fluid>
                <v-row>
                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="entreSetores.orgao"
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
                            label="Órgão Responsável"
                            @changeValue="alterarValor"
                            @change="tratarEventoMudancaOrgaoOrigem">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="entreSetores.setorOrigem"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há setores com este nome"
                            name="setorOrigem"
                            :required="entreSetores.orgao ? 'required' : ''"
                            :requiredIcon="entreSetores.orgao && verificaPermissaoEdicao"
                            :items="setoresOrigem"
                            :disabled="!entreSetores.orgao || !verificaPermissaoEdicao"
                            :placeholder="verificaPermissaoEdicao && entreSetores.orgao ? 'Selecione o setor de origem' : ' '"
                            label="Setor Origem"
                            @changeValue="alterarValor"
                            @change="editarEntreSetores">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <auto-complete
                            v-model="entreSetores.setorDestino"
                            item-text="descricao"
                            item-value="id"
                            no-data-text="Não há setores com este nome"
                            name="setorDestino"
                            :required="entreSetores.orgao ? 'required' : ''"
                            :requiredIcon="entreSetores.orgao && verificaPermissaoEdicao"
                            :items="setoresDestino"
                            :disabled="!entreSetores.orgao || !verificaPermissaoEdicao"
                            :placeholder="verificaPermissaoEdicao && entreSetores.orgao ? 'Selecione o setor de destino' : ' '"
                            label="Setor Destino"
                            @changeValue="alterarValor"
                            @change="editarEntreSetores">
                        </auto-complete>
                    </v-col>

                    <v-col cols="12" md="12" sm="12" xs="12">
                        <v-textarea
                            v-model="entreSetores.motivoObservacao"
                            :counter="500"
                            :error-messages="errors.collect('motivoObservacao')"
                            auto-grow
                            rows="1"
                            maxlength="500"
                            name="motivoObservacao"
                            label="Motivo/Obs."
                            placeholder="Digite o motivo"
                            class="mt-3"
                            @change="editarEntreSetores">
                        </v-textarea>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="entreSetores.numeroNotaLancamentoContabil"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name="numeroNotaLancamentoContabil"
                            :placeholder="verificaPermissaoEdicao ? placeholders.numeroNotaLancamentoContabil : ' '"
                            maxlength="12"
                            label="Número da NL"
                            v-mask="'####NL######'"
                            v-validate="'max:12'"
                            :error-messages="errors.collect('numeroNotaLancamentoContabil')"
                            @change="editarEntreSetores">
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <az-date
                            v-model="entreSetores.dataNotaLancamentoContabil"
                            :is-disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name-date="dataNotaLancamentoContabil"
                            date
                            label="Data da NL"
                            :max-date="moment(new Date()).format('YYYY-MM-DDTHH:mm:ssZZ')"
                            @input="editarEntreSetores"
                            :placeholderDate="(entreSetores.dataNotaLancamentoContabil) ? ' ' : placeholders.date"
                            :error-messages="errors.collect('dataNotaLancamentoContabil')">
                        </az-date>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-text-field
                            v-model="entreSetores.numeroProcesso"
                            :disabled="!verificaPermissaoEdicao"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            name="numeroProcesso"
                            :placeholder="verificaPermissaoEdicao ? 'Informe o número' : ' '"
                            maxlength="100"
                            :counter="100"
                            label="N° Processo"
                            v-validate="'max:100'"
                            :error-messages="errors.collect('numeroProcesso')"
                            @change="editarEntreSetores">
                        </v-text-field>
                    </v-col>

                    <v-col cols="12" md="4" sm="6" xs="12">
                        <v-autocomplete
                            v-model="entreSetores.responsavel"
                            :disabled="!verificaPermissaoEdicao"
                            :items="responsaveis"
                            item-text="nome"
                            item-value="id"
                            no-data-text="Não há responśaveis com esse nome"
                            name="responsavel"
                            :class="{desativado: !verificaPermissaoEdicao}"
                            label="Responsável"
                            :error-messages="errors.collect('responsavel')"
                            placeholder="Selecione o responsável"
                            @change="editarEntreSetores">
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
    import {mapActions} from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcoesBotoesContinuarVoltar from '@/views/components/acoes/AcoesBotoesContinuarVoltar'
    import AutoComplete from '@/views/components/autocomplete/AutoComplete'

    export default {
        name: 'EdicaoDadosGeraisentreSetores',
        components: {AutoComplete, AcoesBotoesContinuarVoltar},
        data() {
            return {
                entreSetores: {
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
            await this.buscarEntreSetores()
            await this.buscarOrgaosOrigem()
            await this.buscarResponsaveis()
        },
        computed: {
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            },
            podeContinuar() {
                if (this.entreSetores.orgao && this.entreSetores.setorOrigem && this.entreSetores.setorDestino) {
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
                actionTypes.COMUM.BUSCAR_SETORES_NAO_ALMOXARIFADO,
                actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.ATUALIZAR_ENTRE_SETORES,
                actionTypes.MOVIMENTACAO_INTERNA.ENTRE_SETORES.BUSCAR_ENTRE_SETORES_POR_ID
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
                if (this.entreSetores.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresNaoAlmoxarifado(this.entreSetores.orgao)
                    this.habilitarLoadingGlobal()
                    this.setoresOrigem = resultado.items
                    this.selecionaSetorSeArrayConterApenasUmSetorOrigem(this.setoresOrigem)
                }
            },
            async buscarSetoresDestino() {
                this.setoresDestino = []
                if (this.entreSetores.orgao) {
                    this.desabilitarLoadingGlobal()
                    const resultado = await this.buscarSetoresNaoAlmoxarifado(this.entreSetores.orgao)
                    this.habilitarLoadingGlobal()
                    this.setoresDestino = resultado.items
                }
            },
            async buscarResponsaveis() {
                const resposta = await this.buscarTodosResponsaveis()
                this.responsaveis = resposta.items
            },
            selecionaSetorSeArrayConterApenasUmSetorOrigem(setores) {
                if ((setores.length === 1) && !this.entreSetores.setorOrigem) {
                    this.entreSetores.setorOrigem = setores[0].id
                    this.editarEntreSetores()
                }
            },
            async selecionaOrgaoSeArrayConterApenasUmOrgaoOrigem(orgaos) {
                if (orgaos.length === 1 && !this.entreSetores.orgao) {
                    this.entreSetores.orgao = orgaos[0].id
                }
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            async editarEntreSetores() {
                this.desabilitarLoadingGlobal()
                try {
                    const dadosAtualizados = await this.atualizarEntreSetores(this.entreSetores)
                    this.setarDadosAtualizados(dadosAtualizados)
                } catch (err) {
                    this.mostrarNotificacaoErro(err.response.data.message)
                    this.$store.commit(mutationTypes.LOKI.DISABLE_AUTO_SAVING)
                    await this.buscarEntreSetores()
                }
                this.habilitarLoadingGlobal()
            },
            async buscarEntreSetores() {
                this.desabilitarLoadingGlobal()
                this.entreSetores = await this.buscarEntreSetoresPorId(this.$route.params.movimentacaoInternaId)
                this.habilitarLoadingGlobal()
            },
            async trataEventoSelecaoDeOrgaoOrigem() {
                await this.editarEntreSetores()
                await this.buscarSetoresOrigem()
                await this.buscarSetoresDestino()
            },
            async tratarEventoMudancaOrgaoOrigem() {
                this.anulaSetoresAoMudarOrgao()
                this.limparListaSetores()
                await this.trataEventoSelecaoDeOrgaoOrigem()
            },
            anulaSetoresAoMudarOrgao() {
                this.entreSetores.setorOrigem = null
                this.entreSetores.setorDestino = null
            },
            limparListaSetores() {
                this.setoresOrigem = []
                this.setoresDestino = []
            },
            setarDadosAtualizados(dados) {
                this.verificarMudancasPropriedades(dados, this.dadosDeEntrada, this.entreSetores)
            },
            alterarValor(chave, valor) {
                this.entreSetores[chave] = valor
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
