export default (value) => {
    switch (value) {
        case 'FINALIZADO':
            return 'Finalizado'
        case 'EM_ELABORACAO':
            return 'Em Elaboração'
    }
    return value
}
