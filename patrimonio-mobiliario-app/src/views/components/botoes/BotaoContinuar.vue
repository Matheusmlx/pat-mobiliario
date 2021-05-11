<template>
    <v-btn
        @click="continuar"
        color="primary"
        :loading="loading"
        depressed>
        <span :class="{'text-capitalize':textCapitalize}">Continuar</span>
        <v-icon v-if="!esconderIcone" size="15" class="ml-1">fas fa-arrow-right</v-icon>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoContinuar',
        props:{
            esconderIcone:{
                type: Boolean,
                default: false
            },
            textCapitalize:{
                type: Boolean,
                default: false
            }
        },
        data(){
            return {
                loading:false
            }
        },
        methods:{
            continuar(){
                this.$emit('continuar')
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
