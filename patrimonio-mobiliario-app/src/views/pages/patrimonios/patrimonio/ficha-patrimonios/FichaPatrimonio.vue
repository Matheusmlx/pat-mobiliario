<template>
    <div>
    <az-back-button :route="{ name: 'PatrimoniosListagem' }" text="Voltar para listagem"/>
        <az-container>
        <v-tabs>
            <v-tab :to="{name:'FichaPatrimonioDadosGerais'}">DADOS GERAIS</v-tab>
            <v-tab :to="{name:'FichaPatrimonioMovimentacoes'}">MOVIMENTAÇÕES</v-tab>
        </v-tabs>
            <router-view
                :dadosFicha="patrimonio"
                :dadosIncorporacao="patrimonio.incorporacao"
                @buscarPatrimonio="buscarPatrimonio"
                @buscarIncorporacao="buscarPatrimonio"
            />
        </az-container>
    </div>
</template>

<script>
    import {mapActions} from 'vuex'
    import {actionTypes} from '@/core/constants'

    export default {
        name: 'fichaPatrimonio',
        data() {
            return {
                patrimonio: {
                    incorporacao:{}
                }
            }
        },
        methods: {
            ...mapActions([
                actionTypes.PATRIMONIO.BUSCAR_PATRIMONIO_POR_ID_FICHA
            ]),
            async buscarPatrimonio() {
                const resultado = await this.buscarPatrimonioPorIdFicha(this.$route.params.patrimonioId)
                if (resultado) {
                    this.patrimonio = resultado
                }
            }
        }
    }
</script>
