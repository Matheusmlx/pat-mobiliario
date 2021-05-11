<template>
    <v-btn color="primary" depressed @click="salvar">
        Salvar
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoSalvar',
        methods:{
            salvar(){
                this.$emit('salvar')
            }
        }
    }
</script>
