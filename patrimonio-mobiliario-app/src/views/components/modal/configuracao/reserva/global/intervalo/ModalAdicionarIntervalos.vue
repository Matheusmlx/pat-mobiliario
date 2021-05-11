<template>
  <v-dialog persistent v-model="dialog" width="1100">
    <v-card>

      <modal-adicionar-intervalos-cabecalho
          :reserva="reserva"
          :filtros-interno="filtrosInterno"
          :restante="restante"
          @fecharModal="fecharModal"
      />

      <v-responsive class="overflow-y-auto" max-height="50vh">
        <v-data-table
            :headers="colunasTabela"
            :items="reservaIntervalos"
            :options.sync="paginacaoInterna"
            :server-items-length="totalItens"
            item-key="id"
            ref="table"
            class="pr-tabela-listagem-orgaos-reserva "
            hide-default-footer
            show-select
            no-data-text="Não há órgãos a serem adicionados">
          <template v-slot:header.data-table-select="{ on, props }">
            <v-simple-checkbox
                :ripple="false"
                color="grey"
                v-bind="props"
                v-on="on"
                @click="trtarSelecionarTodasReservaIntervalos(props)"/>
          </template>
          <template v-slot:item="item">
            <modal-adicionar-intervalos-form
                :reservaIntervalo="item.item"
                :maior-numero-fim-intervalo="maiorNumeroFimIntervalo"
                @buscarProximoIntervalo="buscarProximoIntervalo"
                @selecionaReservaIntervalo="selecionaReservaIntervalo"
                @deselecionaReservaIntervalo="deselecionaReservaIntervalo"
                @atualizarMaiorNumeroFimIntervalo="verificarMaiorNumeroFimIntervalo"/>
          </template>
        </v-data-table>
      </v-responsive>

      <paginacao class="ml-5 mr-5" :paginacao-interna="paginacaoInterna" :paginas="paginas"
                 @resetaPage="resetaPage"/>


      <v-divider></v-divider>
      <modal-adicionar-intervalos-acoes
          :quantidade-selecionada-igual-zero="quantidadeSelecionadaIgualZero"
          :existe-intervalos-sem-preenchimento="existeIntervalosSemPreenchimento"
          :quantidade-selecionados="quantidadeSelecionados"
          @adicionar="tratarEventoAdicionar"
          @fecharModal="fecharModal"
      />
    </v-card>
  </v-dialog>

</template>

<script>

    import { mapActions, mapMutations } from 'vuex'
    import { actionTypes, mutationTypes } from '@/core/constants'
    import ModalAdicionarIntervalosCabecalho from './ModalAdicionarIntervalosCabecalho'
    import ModalAdicionarIntervalosForm from './ModalAdicionarIntervalosForm'
    import ModalAdicionarIntervalosAcoes from './ModalAdicionarIntervalosAcoes'
    import Paginacao from '@/views/components/tabela/Paginacao'
    import _ from 'lodash'

    export default {
        name: 'ModalAdicionarIntervalos',
        components: {
            ModalAdicionarIntervalosAcoes,
            ModalAdicionarIntervalosCabecalho,
            Paginacao,
            ModalAdicionarIntervalosForm,
        },
        data () {
            return {
                dialog: true,
                colunasTabela: [],
                reservaId: this.$route.params.id,
                reserva: {},
                reservaIntervalos: [],
                reservaIntervalosSelecionados: [],
                maiorNumeroFimIntervalo: 0,
                paginas: 0,
                totalItens: 0,
                filtrosInterno: this.getFiltros(),
                paginacaoInterna: this.getPaginacao(),
            }
        },
        watch: {
            paginacaoInterna: {
                handler (novoValor) {
                    this.tratarEventoPaginar(novoValor)
                },
                deep: true,
            },
            quantidadeSelecionadaIgualZero(val) {
                if(val){
                    this.maiorNumeroFimIntervalo = 0
                }
            }
        },
        computed: {
            quantidadeSelecionadaIgualZero () {
                return this.quantidadeSelecionados === 0
            },
            existeIntervalosSemPreenchimento () {
                return this.reservaIntervalosSelecionados.filter(
                    reservaIntervalo => !reservaIntervalo.quantidadeReservada || !reservaIntervalo.numeroInicio ||
                        !reservaIntervalo.numeroFim).length > 0
            },
            quantidadeSelecionados () {
                return this.reservaIntervalosSelecionados.length
            },
            restante () {
                if (this.reservaIntervalosSelecionados.length === 0) {
                    return this.reserva.quantidadeRestante
                } else {
                    let totalQuantidadeInserida = 0
                    this.reservaIntervalosSelecionados.forEach(reservaIntervalo => {
                        if (reservaIntervalo.quantidadeReservada && reservaIntervalo.quantidadeReservada > 0) {
                            totalQuantidadeInserida += parseInt(reservaIntervalo.quantidadeReservada)
                        }
                    })
                    if (totalQuantidadeInserida > this.reserva.quantidadeRestante) {
                        return this.reserva.quantidadeRestante
                    }
                    return this.reserva.quantidadeRestante - totalQuantidadeInserida
                }
            },
        },
        async mounted () {
            this.criarColunasTabela()
            await this.buscarReserva()
        },
        methods: {
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_TODOS_ORGAOS_PAGINADO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.BUSCAR_RESERVA_POR_ID,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.BUSCAR_INTERVALO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.INTERVALO.SALVAR_INTERVALO,
            ]),
            ...mapMutations([
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_FILTRO_BUSCA_ORGAOS_RESERVA,
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.SET_PAGINACAO_BUSCA_ORGAOS_RESERVA,
                mutationTypes.CONFIGURACAO.RESERVA.GLOBAL.RESETA_PAGE_ORGAOS_RESERVA,
            ]),
            criarColunasTabela () {
                this.colunasTabela = this.criarColunas(
                    4,
                    ['Descrição', 'Preenchimento', 'Quantidade', 'Intervalo'],
                    ['descricao', 'preenchimento', 'quantidadeReservada', 'intervalo'],
                    [],
                    [],
                    [],
                    'gray--text')
            },
            async buscarReserva () {
                this.reserva = await this.buscarReservaPorId(this.reservaId)
            },
            async buscar () {
                this.setFiltroBuscaOrgaosReserva(this.getFiltrosInterno())
                await this.buscarTodosOrgaosReserva()
            },
            async tratarEventoBuscaSimples (valor) {
                this.resetaPageOrgaosReserva()
                this.filtrosInterno.conteudo.value = valor
                await this.buscar()
            },
            async buscarTodosOrgaosReserva () {
                const resultado = await this.buscarTodosOrgaosPaginado()
                if (resultado) {
                    this.orgaos = resultado.items
                    this.paginas = resultado.totalPages
                    this.totalItens = resultado.totalElements
                    this.criarReservaIntervalos(this.orgaos)
                    this.atualizaReservaIntervalosComSelecionados()
                }
            },
            criarReservaIntervalos (orgaos) {
                this.reservaIntervalos = []
                orgaos.forEach(orgao => {
                    let reservaIntervalo = {
                        reservaId: this.$route.params.id,
                        orgaoId: orgao.id,
                        descricao: orgao.descricao,
                        quantidadeReservada: null,
                        numeroInicio: '',
                        numeroFim: '',
                        preenchimento: 'AUTOMATICO',
                        selecionado: false,
                    }
                    this.reservaIntervalos.push(reservaIntervalo)
                })
            },
            atualizaReservaIntervalosComSelecionados () {
                for (let index = 0; index < this.reservaIntervalos.length; index++) {
                    const reservaIntervaloSelecionado = this.reservaIntervalosSelecionados.find(
                        selecionado => selecionado.orgaoId === this.reservaIntervalos[index].orgaoId)
                    if (reservaIntervaloSelecionado) {
                        this.reservaIntervalos[index] = reservaIntervaloSelecionado
                    }
                }
            },
            trtarSelecionarTodasReservaIntervalos (props) {
                if(!props.value) {
                    this.selecionarTodasReservasIntervalos()
                }else{
                    this.deselecionaTodasReservasIntervalos()
                }
                this.reservaIntervalos.forEach(reservaIntervalo => reservaIntervalo.selecionado = !props.value)
            },
            selecionarTodasReservasIntervalos(){
                const reservasIntervaloNaoSelecionadas = this.reservaIntervalos.filter( reservaIntervalo => !reservaIntervalo.selecionado)
                this.reservaIntervalosSelecionados.push(...reservasIntervaloNaoSelecionadas)
            },
            deselecionaTodasReservasIntervalos() {
                this.reservaIntervalos.forEach( reservaIntervalo => {
                    this.deselecionaReservaIntervalo(reservaIntervalo)
                })
            },
            deselecionaReservaIntervalo (reservaIntervalo) {
                this.excluirReservaIntervaloDosSelecionados(reservaIntervalo)
                this.resetarReservaIntervalos(reservaIntervalo)
            },
            excluirReservaIntervaloDosSelecionados (reservaIntervalo) {
                const indexReservaIntervaloExcluir = this.reservaIntervalosSelecionados.findIndex(
                    selecionado => selecionado.orgaoId === reservaIntervalo.orgaoId)
                this.reservaIntervalosSelecionados.splice(indexReservaIntervaloExcluir, 1)
            },
            resetarReservaIntervalos (reservaIntervalo) {
                const indexReservaIntervaloAtualizar = this.reservaIntervalos.findIndex(
                    reservaInter => reservaInter.orgaoId === reservaIntervalo.orgaoId)
                this.reservaIntervalos[indexReservaIntervaloAtualizar].preenchimento = 'AUTOMATICO'
                this.reservaIntervalos[indexReservaIntervaloAtualizar].quantidadeReservada = null
                this.reservaIntervalos[indexReservaIntervaloAtualizar].numeroInicio = ''
                this.reservaIntervalos[indexReservaIntervaloAtualizar].numeroFim = ''
                this.reservaIntervalos[indexReservaIntervaloAtualizar].selecionado = false
            },
            async buscarProximoIntervalo (reservaInter) {
                const entidadeBuscaIntervalo = this.criarEntidadeBuscaIntervalo(reservaInter)
                const intervalo = await this.buscarIntervalo(entidadeBuscaIntervalo)
                if(intervalo) {
                    this.reservaIntervalos.forEach(reservaIntervalo => {
                        if (reservaIntervalo.orgaoId === reservaInter.orgaoId) {
                            reservaIntervalo.numeroInicio = intervalo.numeroInicio
                            reservaIntervalo.numeroFim = intervalo.numeroFim
                            this.verificarMaiorNumeroFimIntervalo(reservaIntervalo.numeroFim)
                        }
                    })
                }
            },
            verificarMaiorNumeroFimIntervalo (numeroFim) {
                if (numeroFim) {
                    if (this.maiorNumeroFimIntervalo < numeroFim) {
                        this.maiorNumeroFimIntervalo = numeroFim
                    }
                } else {
                    this.maiorNumeroFimIntervalo = Math.max.apply(Math, this.reservaIntervalosSelecionados.map(
                        function (selecionado) {return selecionado.numeroFim}))
                }
            },
            criarEntidadeBuscaIntervalo (reservaInter) {
                return {
                    items: this.reservaIntervalosSelecionados,
                    reservaId: reservaInter.reservaId,
                    orgaoId: reservaInter.orgaoId,
                    quantidadeReservada: reservaInter.quantidadeReservada,
                }
            },
            selecionaReservaIntervalo (reservaIntervalo) {
                const indexReservaIntervaloSelecionada = this.reservaIntervalosSelecionados.findIndex(
                    selecionado => selecionado.orgaoId === reservaIntervalo.orgaoId)
                if (indexReservaIntervaloSelecionada !== -1) {
                    this.reservaIntervalosSelecionados[indexReservaIntervaloSelecionada] = reservaIntervalo
                } else {
                    this.reservaIntervalosSelecionados.push(reservaIntervalo)
                }
            },
            async tratarEventoAdicionar () {
                if (await this.validarDadosFormulario()) {
                    await this.salvarIntervalo({ reservaId: this.reservaId, intervalos: this.reservaIntervalosSelecionados })
                    this.recarregarDadosReserva()
                    await this.fecharModal()
                }
            },
            async validarDadosFormulario () {
                return await this.$validator.validateAll()
            },
            async fecharModal () {
                await this.$router.replace({ name: 'ReservaEdicao', params: { id: this.$route.params.id } })
            },
            tratarEventoPaginar (paginacao) {
                this.setPaginacaoBuscaOrgaosReserva(paginacao)
                this.buscar()
            },
            resetaPage () {
                this.resetaPageOrgaosReserva()
            },
            getPaginacao () {
                return _.cloneDeep(this.$store.state.reserva.todosOrgaos.paginacao)
            },
            getFiltros () {
                return _.cloneDeep(this.$store.state.reserva.todosOrgaos.filtros)
            },
            getFiltrosInterno () {
                return _.cloneDeep(this.filtrosInterno)
            },
            async tratarEventoRemoverFiltro (propriedade) {
                if (this.filtrosInterno[propriedade]) {
                    this.filtrosInterno[propriedade].value = this.filtrosInterno[propriedade].default
                }
                await this.buscar()
            },
            recarregarDadosReserva() {
                this.$emit('recarregarDadosReserva')
            }
        }
    }
</script>
<style lang="stylus">
.pr-tabela-listagem-orgaos-reserva
  .v-input__slot
    margin-bottom 0 !important

  th
    font-size 13px !important
    font-weight bold
    color #666666 !important

  tr:hover
    background transparent !important

  tr.v-data-table__selected
    background white !important

  tr.v-data-table__selected td .v-icon
    color grey !important



</style>
