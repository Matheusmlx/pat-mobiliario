<template>
    <v-menu
        offset-y
        top
        left
        nudge-top="5"
        content-class="elevation-0 az-bnt-remove">
        <template v-slot:activator="{on}">
            <v-btn :disabled="desabilitado" :icon="!textButton" :class="stylus" v-on="on">
                <v-icon :color="color" :size="size" @mouseleave="estadoBotao" @mouseover="estadoBotao">{{icon}}</v-icon>
                {{textButton}}
            </v-btn>
        </template>
        <span class="az-bnt-remove-content">
            {{message}}
            <a @click="remove" class="remove ml-1">{{textButtonConfirm}}</a>
            <a class="cancel ml-1">{{textButtonCancel}}</a>
        </span>
    </v-menu>
</template>

<script>
    export default {
        name: 'BotaoRemover',
        props: {
            size: {
                type: String,
                default: '15'
            },
            stylus:{
                type:String
            },
            desabilitado:{
                type: Boolean,
                default: false
            },
            colorPrimary: {
                type: String,
                default: 'grey'
            },
            colorSecondary: {
                type: String,
                default: 'red'
            },
            icon: {
                type: String,
                default: 'far fa-trash-alt'
            },
            textButton: {
                type: String,
                default: ''
            },
            message: {
                type: String,
                default: 'Você tem certeza?'
            },
            textButtonConfirm: {
                type: String,
                default: 'Remover'
            },
            textButtonCancel: {
                type: String,
                default: 'Cancelar'
            }
        },
        data() {
            return {
                color : this.colorPrimary
            }
        },
        methods: {
            estadoBotao() {
                this.color = this.color === this.colorPrimary ? this.colorSecondary : this.colorPrimary
            },
            remove() {
                this.$emit('remover')
            }
        },
    }
</script>

<style lang="stylus" scoped>
    .az-bnt-remove
        min-width 240px!important
        max-width max-content
        overflow-y hidden

        &-content
            position: relative
            border-radius: .4em
            pointer-events: initial
            padding 5px
            background-color black
            font-size small
            color white

        .remove
            font-size small
            text-transform capitalize
            color red

        .cancel
            font-size small
            text-transform capitalize
            color grey

</style>
