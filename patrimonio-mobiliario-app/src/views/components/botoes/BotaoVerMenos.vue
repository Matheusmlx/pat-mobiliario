<template>
    <v-btn
        @click="verMenos"
        color="primary"
        depressed>
        <span class="text-capitalize">Ver Menos</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoVerMenos',
        methods:{
            verMenos(){
                this.$emit('verMenos')
            }
        }
    }
</script>

<style scoped lang="stylus">
    .botao-default
        border 0
        color #777 !important

        .v-icon
            color #777 !important

        i
            margin-right 0
</style>
