<template>
    <div class="div">
        <v-row class="row" align="center" justify="center" >
            <v-card class="card1" elevation="0">
                <v-card-text>
                    <v-col>
                        <v-icon size="60">thumb_down_alt</v-icon>
                    </v-col>
                    <v-col>
                        <h2>Você não possui permissão para acessar este sistema!</h2>
                    </v-col>
                </v-card-text>
            </v-card>
        </v-row>
    </div>
</template>

<script>
    export default {
        name: 'AcessoNegado',
    }
</script>

<style scoped lang="stylus">
    .div
        width 100%
        text-align center
    .row
        height 80vh

    .card1 {
        background-color: transparent!important;
        border-color: transparent!important;
        text-align center
    }

</style>
