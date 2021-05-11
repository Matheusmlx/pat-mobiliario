<template>
    <router-view :estorno="estorno"/>
</template>

<script>
    export default {
        name: 'ModalEstornarPatrimonios',
        data(){
            return {
                estorno: this.$store.state.patrimonio.estorno
            }
        },
        created() {
            this.redirecionaAoFaltarMotivo()
        },
        methods:{
            redirecionaAoFaltarMotivo(){
                if(!this.estorno.motivo){
                    this.$router.push({name:'ModalEstornarPatrimoniosMotivo'})
                }
            }
        }
    }
</script>

<style scoped>

</style>
