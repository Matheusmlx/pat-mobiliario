<template>
    <div class="movimentacao-edicao-tipo">
            <div class="az-form-content">
                <p class="selecao-titulo">Selecione o tipo de Movimentação Interna:</p>

                <div class="selecao-linha">
                    <v-card
                        :class="{ primary: tipoSelecionado === tipo.nome}"
                        :key="tipo.id"
                        @click="selecionarTipo(tipo)"
                        class="selecao-card elevation-0"
                        height="100%"
                        min-height="170px"
                        v-for="tipo in tiposMovimentacaoLinha1">
                        <v-icon
                            :class="{rotaciona : tipo.id === 1}"
                            :color="tipoSelecionado === tipo.nome ? 'white' : 'gray'"
                            size="50px">
                            {{ tipo.icon }}
                        </v-icon>
                        <v-card-text
                            :class="{ selecionado: tipoSelecionado === tipo.nome }"
                            class="selecao-card__titulo">
                            {{ tipo.label }}
                        </v-card-text>
                    </v-card>
                </div>

                <div class="selecao-segundaLinha">
                    <v-card
                        :class="{ primary: tipoSelecionado === tipo.nome, 'mr-md-16 mr-sm-16': tipo.id === 4}"
                        :key="tipo.id"
                        @click="selecionarTipo(tipo)"
                        class="selecao-card elevation-0"
                        height="100%"
                        hover
                        min-height="170px"
                        v-for="tipo in tiposMovimentacaoLinha2">
                        <v-icon
                            :color="tipoSelecionado === tipo.nome ? 'white' : 'gray'"
                            size="45px">
                            {{ tipo.icon }}
                        </v-icon>
                        <v-card-text
                            :class="{ selecionado: tipoSelecionado === tipo.nome }"
                            class="selecao-card__titulo">
                            {{ tipo.label }}
                        </v-card-text>
                    </v-card>
                </div>
            </div>
            <acoes-botoes-continuar-cancelar :controleContinuar="!!(tipoSelecionado)" @continuar="continuar" @cancelar="cancelar"/>
        </div>
</template>

<script>
    import {mapActions, mapState} from 'vuex'
    import actionTypes from '@/core/constants/actionTypes'
    import AzHasPermissions from '@azinformatica/loki/src/utils/AzHasPermissions'
    import AcoesBotoesContinuarCancelar from '@/views/components/acoes/AcoesBotoesContinuarCancelar'

    export default {
        name: 'MovimentacaoInternaEdicaoTipo',
        components: {AcoesBotoesContinuarCancelar},
        data() {
            return {
                tipoSelecionado: null
            }
        },
        async mounted() {
            await this.buscarTipoMovimentacaoSelecionado()
        },
        computed: {
            ...mapState({tiposMovimentacaoLinha1: state => state.movimentacaointerna.tiposMovimentacaoLinha1,
                         tiposMovimentacaoLinha2: state => state.movimentacaointerna.tiposMovimentacaoLinha2}),
            verificaPermissaoEdicao() {
                return AzHasPermissions(this.$store.state.loki.user.authorities, ['Mobiliario.Movimentacoes'])
            }
        },
        methods: {
            ...mapActions([
                actionTypes.MOVIMENTACAO_INTERNA.EDITAR_TIPO_MOVIMENTACAO_INTERNA,
                actionTypes.MOVIMENTACAO_INTERNA.BUSCAR_TIPO_MOVIMENTACAO_INTERNA
            ]),
            cancelar(){
                this.$router.replace({name:'IncorporacaoListagem'})
            },
            continuar(){
                if(this.tipoSelecionado){
                    this.$router.replace({
                        name: 'MovimentacaoInternaEdicaoDadosGerais',
                        params: {movimentacaoInternaId: this.$route.params.movimentacaoInternaId}
                    })
                }
            },
            async selecionarTipo(tipo) {
                if (this.verificaPermissaoEdicao) {
                    this.desabilitarLoadingGlobal()
                    this.tipoSelecionado = tipo.nome
                    const movimentacao = {id:this.$route.params.movimentacaoInternaId,tipo: this.tipoSelecionado}
                    const novaMovimentacao = await this.editarTipoMovimentacaoInterna(movimentacao)
                    await this.$router.replace({
                        name: 'MovimentacaoInternaEdicaoTipo',
                        params: {movimentacaoInternaId: novaMovimentacao.id}
                    })
                    this.habilitaPasso2()
                    this.desabilitaPasso3()
                    this.desabilitaPasso4()
                    this.habilitarLoadingGlobal()
                }
            },
            async buscarTipoMovimentacaoSelecionado(){
                this.tipoSelecionado = await this.buscarTipoMovimentacaoInterna(this.$route.params.movimentacaoInternaId)
                if(this.tipoSelecionado){
                    this.habilitaPasso2()
                }
            },
            habilitaPasso2(){
                this.$emit('habilitaPasso2')
            },
            desabilitaPasso3(){
                this.$emit('desabilitaPasso3')
            },
            desabilitaPasso4(){
                this.$emit('desabilitaPasso4')
            }
        }
    }
</script>

<style scoped lang="stylus">
.rotaciona
    transform: rotate(-45deg);

.az-form-content
    margin-top 3vh
    margin-bottom 5vh

.movimentacao-edicao-tipo
    min-height 65vh

.selecao
    &-titulo
        font-size 16px
        color #999
        font-weight bold
        text-align center
        margin-bottom 35px !important

    &-linha
        margin 0 auto 30px
        width 80%
        display flex
        justify-content space-between
        align-items center

    &-segundaLinha
        margin 0 auto 30px
        width 70vw
        display flex
        align-items center
        justify-content center

    &-card
        align-items center
        text-align center
        display flex
        justify-content center
        flex-direction column
        width 28%
        border 1px solid rgba(0, 0, 0, .12) !important

        &__titulo
            text-transform uppercase
            font-size 15px


.selecao-card:hover
    color #7f7f7f


@media (min-width 1400px)
    .selecao-linha
        width 70%

@media (max-width 998px)
    .selecao-linha
        width 90%

@media (max-width 768px)
    .selecao
        &-linha
            width 100%

        &-segundaLinha
            width 100%

        &-card
            width 30%

@media (max-width 600px)
    .selecao
        &-linha
            flex-direction column

        &-segundaLinha
            flex-direction column

        &-card
            width 100%
            margin 10px 0

>>>
    .selecionado, .selecao-card__titulo.selecionado
        color #fff !important

</style>
