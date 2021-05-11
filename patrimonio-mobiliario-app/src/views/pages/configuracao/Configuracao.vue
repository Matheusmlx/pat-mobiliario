<template>
    <router-view/>
</template>

<script>
    import {mapState} from 'vuex'

    export default {
        name: 'Configuracao',
        computed: {
            ...mapState(['loki']),
            ehRotaConfiguracao() {
                return this.$router.currentRoute.matched[0].name === 'configuracao'
            }
        },
        created() {
            this.validarRotaConfiguracao()
        },
        methods: {
            validarRotaConfiguracao() {
                if (this.ehRotaConfiguracao) {
                    this.loki.menuActions[3].expanded = true
                    this.loki.menuActions[3].selected = true
                }
            }
        }
    }
</script>
