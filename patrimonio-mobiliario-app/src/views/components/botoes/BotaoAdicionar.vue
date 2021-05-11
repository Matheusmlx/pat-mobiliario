<template>
    <v-btn
        @click="adicionar"
        color="primary"
        :loading="loading"
        depressed>
        <span >Adicionar ({{quantidade}})</span>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoAdicionar',
        props:{
            quantidade:{
                type: Number,
                requided: true
            }
        },
        data(){
            return {
                loading:false
            }
        },
        methods:{
            adicionar(){
                this.$emit('adicionar')
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
