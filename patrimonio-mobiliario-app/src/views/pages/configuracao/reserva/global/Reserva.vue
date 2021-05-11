<template>
    <div class="containerReservaVisualizacao">
        <az-back-button :route="{name: rotaOrigem}" text="Voltar para listagem"/>
        <az-container>
            <router-view/>
        </az-container>
    </div>
</template>

<script>
    import {mapState} from 'vuex'
    import {mutationTypes} from '@/core/constants'

    export default {
        name: 'Reserva',
        data() {
            return {
                rotaOrigem: this.$store.state.reserva.rota.origem
            }
        },
        computed: {
            ...mapState(['loki', 'convenio'])
        },
        mounted() {
            this.retrairMenu()
        },
        destroyed() {
            this.expandirMenu()
        },
        methods: {
            expandirMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_EXPANDIR_MENU)
            },
            retrairMenu() {
                this.$store.commit(mutationTypes.COMUM.SET_RETRAIR_MENU)
            }
        }
    }
</script>

<style scoped lang="stylus">
    .containerReservaVisualizacao
        max-height 60vh

</style>
