<template>
    <div>
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
        name: 'Convenio',
        data() {
            return {
                convenioId: null,
                rotaOrigem: this.$store.state.convenio.rota.origem
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

<style lang="stylus">
    .pr-abas
        .az-tabs
            padding 0
            margin 0

        &__aba
            &-item .v-tabs__item
                border-right 1px solid #fff
                border-left 1px solid #fff
                border-top 1px solid #fff
                border-bottom: 1px solid #ccc

            &-item .active
                border-right 1px solid #ccc
                border-top 1px solid #ccc
                border-left 1px solid #ccc
                border-bottom 1px solid #fff

            &-item .v-tabs__item--disabled
                border 0

    @media (max-width: 960px)
        .pr-abas .az-tabs
            padding 20px
</style>
