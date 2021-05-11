export default {
    methods: {
        removerMascaraTelefone(telefone) {
            return telefone ? telefone.replace(/\D/g, '') : ''
        }
    }
}
