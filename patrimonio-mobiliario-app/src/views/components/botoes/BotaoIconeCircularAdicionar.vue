<template>
    <v-btn
        :disabled="desabilitado"
        v-if="ocultado"
        :class="{'btn-disabled': desabilitado}"
        icon @click="adicionar">
        <v-icon size="20">fas fa-plus-circle</v-icon>
    </v-btn>
</template>

<script>
    export default {
        name: 'BotaoIconeCircularAdicionar',
        props:{
            desabilitado:{
                type: Boolean,
                required: true
            },
            ocultado:{
                type: Boolean,
                required: true
            }
        },
        methods: {
            adicionar() {
                this.$emit('adicionar')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
