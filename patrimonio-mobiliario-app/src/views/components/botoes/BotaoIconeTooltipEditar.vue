<template>
    <v-tooltip top>
        <template v-slot:activator="{ on }">
            <v-btn v-on="on" @click="ativarEdicao" icon>
                <v-icon size="18" >fas fa-pen</v-icon>
            </v-btn>
        </template>
        Editar
    </v-tooltip>
</template>

<script>
    export default {
        name: 'BotaoIconeTooltipEditar',
        methods: {
            ativarEdicao() {
                this.$emit('ativarEdicao')
            }
        }
    }
</script>

<style scoped lang="stylus">
</style>
