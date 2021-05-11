<template>
    <v-btn :disabled="desabilitado"
           :class="{'btn-disabled': desabilitado}" icon @click="remover">
        <v-icon size="20">fas fa-minus-circle</v-icon>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoIconeCircularRemover',
        props:{
            desabilitado:{
                type: Boolean,
                required: true
            }
        },
        methods: {
            remover() {
                this.$emit('remover')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
