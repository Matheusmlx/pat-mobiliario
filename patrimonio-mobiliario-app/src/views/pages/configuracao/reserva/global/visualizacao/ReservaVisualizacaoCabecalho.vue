<template>
    <v-row class="primary ml-0 mr-0">
        <v-col cols="12"
               class="pl-5 pt-2 pb-2 cabecalho"
               :style="{gridTemplateColumns: `repeat(${EhParcial?8:7}, 1fr)`}">
            <div class="headerCabecalho">
                <span class="font-weight-bold">Reserva</span>
                <p class="mb-0">{{ reserva.codigo | textoSemValorSimples }}</p>
            </div>
            <div class="headerCabecalho">
                <span class="font-weight-bold">Situação</span>
                <p class="mb-0">{{ reserva.situacao | azEnum(situacoesReservas) }}</p>
            </div>
            <div class="headerCabecalho pl-3">
                <span class="font-weight-bold">Preenchimento</span>
                <p class="mb-0">{{ reserva.preenchimento | azEnum(preenchimentoReserva) }}</p>
            </div>
            <div class="headerCabecalho pl-3">
                <span class="font-weight-bold">Quantidade</span>
                <p class="mb-0">{{ reserva.quantidadeReservada | formataValor }}</p>
            </div>
            <div class="headerCabecalho" v-if="EhParcial">
                <span class="font-weight-bold">Restante</span>
                <p class="mb-0">{{ reserva.quantidadeRestante | formataValor }}</p>
            </div>
            <div class="headerCabecalho ml-2">
                <span class="font-weight-bold">Intervalo</span>
                <p class="mb-0">{{ reserva.numeroInicio }} - {{ reserva.numeroFim }}</p>
            </div>
            <div/>
            <div class="d-flex justify-end">
                <v-btn dark text v-if="EhParcial" @click="adicionarOrgaos(reserva)">
                    <span class="text-capitalize mt-2">
                        <v-icon :size="15" class="mb-1">add_circle</v-icon>
                        Adicionar órgãos
                    </span>
                </v-btn>
                <botao-remover v-if="!possuiNumerosUtilizados" @remover='removerReserva' colorPrimary='white'/>
            </div>
        </v-col>
    </v-row>
</template>

<script>
    import BotaoRemover from '@/views/components/botoes/BotaoRemover'
    import {actionTypes} from '@/core/constants'
    import { mapActions } from 'vuex'
    export default {
        name: 'ReservaVisualizacaoCabecalho',
        props: ['reserva'],
        data(){
            return {
                possuiNumerosUtilizados:false,
            }
        },
        components: {
            BotaoRemover
        },
        computed:{
            EhParcial() {
                return this.reserva.situacao === 'PARCIAL'
            },
        },
        updated(){
            this.verificarNumeroReservaUtilizando()
        },
        methods:{
            ...mapActions([
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.VERIFICAR_RESERVA_POSSUI_NUMERO_UTILIZADO,
                actionTypes.CONFIGURACAO.RESERVA.GLOBAL.DELETAR_RESERVA]),

            async verificarNumeroReservaUtilizando() {
                let {possuiNumerosUtilizados} = await this.verificarReservaPossuiNumeroUtilizado(this.reserva.id)
                this.possuiNumerosUtilizados = possuiNumerosUtilizados
            },
            async adicionarOrgaos(reserva){
                await this.$router.push({name:'ModalAdicionarIntervalos', params:{id:reserva.id}})
            },
            async removerReserva(){
                await this.deletarReserva(this.reserva.id)
                await this.$router.push({name:'ReservaListagem'})
                this.mostrarNotificacaoSucessoDefault()
            }
        }
    }
</script>

<style scoped lang="stylus">
.headerCabecalho
    font-size 0.9em
    color: white
    max-height 65px

.cabecalho
    display grid

</style>
