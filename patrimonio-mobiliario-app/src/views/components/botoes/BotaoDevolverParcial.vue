<template>
    <v-btn
        @click="devolverParcial"
        color="primary"
        :loading="loading"
        depressed>
        <span>Devolver Parcial ({{quantidade}})</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoDevolverParcial',
        props:{
            quantidade:{
                type: Number,
                required: true
            }
        },
        data(){
            return {
                loading: false
            }
        },
        methods:{
            devolverParcial() {
                this.$emit('devolverParcial')
                this.loading = true
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
