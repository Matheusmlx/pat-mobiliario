export default {
    methods: {
        criarLinkDownload(uri, thumbnail) {
            return `${this.$store.state.loki.file.api}?uri=${uri}&thumbnail=${thumbnail === true}`
        },
        criarLinkDownloadTemporario(uri, thumbnail) {
            return `${this.$store.state.loki.file.api}/temporario?uri=${uri}&thumbnail=${thumbnail === true}`
        },
        setApiArquivos(api) {
            this.$store.commit('SET_FILES_API', api)
        },
        resetUploadedFiles() {
            this.$store.commit('SET_UPLOADED_FILES', [])
        },
        async uploadFile(payload) {
            return await this.$store.dispatch('uploadFile', payload)
        },
        tratarErroEnvioArquivo() {
            this.mostrarNotificacaoErro(`O arquivo selecionado deve ter no máximo ${this.$store.state.loki.file.maxSize}.`)
        }
    }
}
