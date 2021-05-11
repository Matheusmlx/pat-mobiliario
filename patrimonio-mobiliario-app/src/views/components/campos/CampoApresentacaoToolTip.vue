<template>
    <v-tooltip top nudge-top max-width="800" :open-delay="openDelay">
        <template v-slot:activator="{ on }">
            <div v-on="on">
                <p class="mb-0 text-truncate texto-campo-apresentacao-tooltip">{{texto | textoSemValorSimples}}</p>
            </div>
        </template>
        <p class="mb-0">{{texto | textoSemValorSimples}}</p>
    </v-tooltip>
</template>

<script>
    export default {
        name: 'CampoApresentacaoToolTip',
        props: {
            texto: {
                required: true
            },
            openDelay: {
                type: Number,
                default: 0
            }
        }
    }
</script>

<style scoped lang="stylus">
    .texto-campo-apresentacao-tooltip
        max-width 800px
</style>
