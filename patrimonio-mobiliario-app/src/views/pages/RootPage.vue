<template>
  <az-template-default id="template" show-notification class="template-root-page"
                       :class="{'esconde-quantidade-notificacao' : (quantidadeNaoVisualizadasAtual <= 0),
                         'ajusta-ver-mais' : mostraVerMais}"
                       @openNotifications="tratarEventoAbrirNotificacoes"
                       @closeNotifications="tratarEventoFecharNotificacoes"
                       @paginateNotifications="tratarEventoPaginarNotificacoes"
                       @refreshNotifications="tratarEventoAtualizarNotificacoes"
                       @readNotifications="tratarEventoNotificacoesLidas"
                       @visitNotification="tratarEventoAcessarNotificacao">

    <router-view v-if="retornaPermissao"/>

    <acesso-negado v-if="!retornaPermissao"/>

    <chat/>
  </az-template-default>
</template>

<script>
    import { actionTypes, mutationTypes } from '@/core/constants'
    import { mapActions, mapMutations, createNamespacedHelpers } from 'vuex'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcessoNegado from './commons/acesso-negado/AcessoNegado'
    import Chat from '@/views/components/Chat'

    const {mapActions: mapActionsNamespaced} = createNamespacedHelpers('rotulosPersonalizados')

    export default {
        components: { AcessoNegado, Chat },
        data () {
            return {
                quantidadeNaoVisualizadasAtual: 0,
                mostraVerMais: false,
                notificacaoFechada: true,
            }
        },
        async mounted() {
            this.redirecionaSeAcessoNegado()
            await this.getAllRotulosPersonlizados()
            this.desabilitarLoadingGlobal()
            this.quantidadeNaoVisualizadasAtual = await this.buscarQuantidadeNotificacoesNaoVisualizadas()
            this.habilitarLoadingGlobal()
        },
        computed: {
            retornaPermissao() {
                if(this.$route.meta.authorities && this.$store.state.loki.user.authorities){
                    return AzHasPermissions(this.$store.state.loki.user.authorities, this.$route.meta.authorities)
                }
                return true
            },
        },
        methods: {
            ...mapActions([
                actionTypes.NOTIFICACAO.BUSCAR_NOTIFICACOES,
                actionTypes.PATRIMONIO.INCORPORACAO.BUSCAR_SITUACAO_INCORPORACAO,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_SITUACAO_MOVIMENTACAO_INTERNA,
                actionTypes.NOTIFICACAO.BUSCAR_QUANTIDADE_NOTIFICACOES_NAO_VISUALIZADAS,

            ]),
            ...mapActionsNamespaced([
                actionTypes.CONFIGURACAO.ROTULOS_PERSONALIZADOS.GET_ALL_ROTULOS_PERSONALIZADOS
            ]),
            ...mapMutations([
                mutationTypes.NOTIFICACAO.RESETA_PAGE_NOTIFICACAO,
                mutationTypes.NOTIFICACAO.RESETA_NOTIFICACAO_ITENS,
                mutationTypes.NOTIFICACAO.OCULTA_NO_NOTIFICATION_TEXT,
                mutationTypes.NOTIFICACAO.ACRESCENTA_PAGE_NOTIFICACAO,
                mutationTypes.NOTIFICACAO.VOLTA_NO_NOTIFICATION_TEXT_PADRAO,
            ]),
            redirecionaSeAcessoNegado() {
                if (!this.retornaPermissao) {
                    this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
                    this.$router.push({ name: 'AcessoNegado' })
                }
            },
            async tratarEventoAbrirNotificacoes() {
                this.notificacaoFechada = false
            },
            tratarEventoFecharNotificacoes() {
                this.notificacaoFechada = true
                this.resetaPageNotificacao()
                this.resetaNotificacaoItens()
            },
            async tratarEventoPaginarNotificacoes() {
                this.acrescentaPageNotificacao()
                this.desabilitarLoadingGlobal()
                const retorno = await this.buscarNotificacoes()
                this.habilitarLoadingGlobal()
                if (retorno) {
                    this.mostraVerMais = retorno.mostraVerMais
                    this.quantidadeNaoVisualizadasAtual = retorno.quantidadeNaoVisualizadasAtual
                }
            },
            async tratarEventoAtualizarNotificacoes() {
                this.desabilitarLoadingGlobal()
                this.quantidadeNaoVisualizadasAtual = await this.buscarQuantidadeNotificacoesNaoVisualizadas()
                this.habilitarLoadingGlobal()
            },
            async tratarEventoNotificacoesLidas() {
                if (this.notificacaoFechada) {
                    this.ocultaNoNotificationText()
                    const retorno = await this.buscarNotificacoes()
                    this.voltaNoNotificationTextPadrao()
                    if (retorno) {
                        this.mostraVerMais = retorno.mostraVerMais
                        this.quantidadeNaoVisualizadasAtual = retorno.quantidadeNaoVisualizadasAtual
                    }
                }
            },
            async tratarEventoAcessarNotificacao(notificacao) {
                if (notificacao.origem === 'INCORPORACAO') {
                    const situacao = await this.buscarSituacaoDaIncorporacao(notificacao.origemId)
                    await this.redirecionarIncorporacaoSeFinalizado(situacao, notificacao.origemId)
                } else {
                    const situacao = await this.buscarSituacaoDaMovimentacaoInterna(notificacao.origemId)
                    await this.redirecionarMovimentacaoInternaSeFinalizado(situacao, notificacao.origemId)
                }
            },
            async buscarSituacaoDaIncorporacao(origemId) {
                this.desabilitarLoadingGlobal()
                const situacao = await this.buscarSituacaoIncorporacao(origemId)
                this.habilitarLoadingGlobal()
                return situacao
            },
            async buscarSituacaoDaMovimentacaoInterna(origemId) {
                this.desabilitarLoadingGlobal()
                const situacao = await this.buscarSituacaoMovimentacaoInterna(origemId)
                this.habilitarLoadingGlobal()
                return situacao
            },
            async redirecionarIncorporacaoSeFinalizado(situacao, origemId) {
                if(situacao === 'FINALIZADO'){
                    await this.$router.push(
                        { name: 'VisualizarRegistroIncorporacao', params: { incorporacaoId: origemId } })
                }
            },
            async redirecionarMovimentacaoInternaSeFinalizado(situacao, origemId) {
                if(situacao === 'FINALIZADO'){
                    await this.$router.push({
                        name: 'VisualizarRegistroMovimentacaoInterna', params: { movimentacaoInternaId: origemId } })
                }
            },
        },
    }
</script>

<style lang="stylus">
.listagem-tabela
    min-height 62vh
.template-root-page .notification-icon
  color grey!important

.template-root-page .notification__body
  max-height 300px

.ajusta-ver-mais .list-item-more
  padding 0 !important

.esconde-quantidade-notificacao .badge-number .v-badge__badge
  display none

.template-root-page .badge-number .primary
  background-color #E57373 !important

.template-root-page .badge-number span
  margin-top -2px!important
  margin-left 14px!important

.template-root-page .badge-number span span
  margin 0!important

.template-root-page .badge-number .v-badge__badge
  height 15px !important
  min-width 9px !important
  font-size 10px !important



.template-root-page .notification__body .v-list-item .when
  padding-top 32px !important
  margin-left 39px !important

.checkbox_input
  .v-input--selection-controls__ripple
    left -7px

  .v-input--checkbox
    margin-left !important
    margin-right 30px !important

    i
      font-size 16px

.v-input--is-disabled:not(.v-input--checkbox)
  color grey
  pointer-events auto

  .v-input__slot
    background-color #EAEAEA
    border-style none

    .v-select__slot
      cursor not-allowed

    input
      cursor not-allowed

html, body, .application
  font-family 'Open Sans', sans-serif

.notification i.white--text
  color rgb(119, 119, 119) !important

.az-table-list tbody tr
  border-bottom 1px solid #e9e9e9

  td
    font-weight 400
    height 40px
    color #777
    font-size 13px

.az-menu .v-list-item
  border-bottom 1px solid rgba(255, 255, 255, .2)

.az-menu .v-list-item__action i
  font-size 15px

.az-container .az-table-list thead tr:first-child th
  white-space normal

.table-actions
  text-align center !important

  a
    color #777

  i
    font-size 16px

.v-tooltip__content
  padding 10px
  font-size 12px
  opacity 1 !important
  background #333333 !important
  text-align justify !important
  line-height 1.4

  span
    overflow-wrap break-word !important

.v-tooltip__content.menuable__content__active
  max-width 500px

.az-search .input-search
  background-color #fff !important

.az-logo .main
  width 125px
  transition all .2s linear

.symbol
  width 45px !important
  transition all .2s linear

.az-about__version
  font-size 9px

.az-checkbox
  .v-icon.material-icons.theme--light
    margin-left 1.8px
    margin-top 1.8px

  .v-input--selection-controls__ripple
    margin-top 0
    margin-left 0

.az-form-content
  margin-top 0

  .v-text-field
    .v-label
      font-size 14px !important
      top -5px

  .az-date
    .v-text-field
      .v-label
        top 0

  .v-input--radio-group__input
    .v-label
      font-size 14px !important
      top -5px

  .col
    padding 8px 12px !important

  .v-textarea textarea
    max-height 100px
    overflow-y auto

.area-conteudo .v-stepper
  box-shadow none

  &__header
    align-items center
    flex-wrap nowrap
    position relative
    box-shadow none

  &__step
    text-align center
    flex-direction column

  &__step__step
    height 40px
    min-width 40px
    width 40px
    font-size 16px
    margin 0 0 5px

  &__step--active .v-stepper__label, &__step--complete .v-stepper__label
    color var(--v-primary-base) !important
    font-weight 600
    text-shadow none !important

.az-table-list .v-text-field > .v-input__control > .v-input__slot:before
  border-color transparent


.simple-table
  th, td
    border 1px solid #e7e7e7
    border-left none

  td
    border-top none

  th:first-child, td:first-child
    border-left 1px solid #e7e7e7

  th
    height 38px

  tr:hover
    background unset !important

.btn-disabled
  pointer-events visible !important
  cursor not-allowed

@media (max-width 500px)
  .area-conteudo__linha
    .v-stepper__header
      height 102px

    .v-stepper__step
      padding 0

  .az-container
    margin 0

  .az-pagination .az-select-pagination
    height 50px

  .az-back-button
    margin-bottom 25px

  .az-form-content, .az-form
    padding 10px

  .v-content
    padding-bottom 0

.btn-disabled
  cursor not-allowed

.erro-inicializacao
  width 100%
  padding 0
  text-align center
  color #555
  height 80vh
  display flex
  justify-content center
  align-items center

  i
    font-size 36px

  p
    font-size 18px
    margin 10px 0 0
    font-weight 600

.az-template-default .toolbar
  -webkit-box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2) !important
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.2) !important
  background-color: #fff !important
</style>
