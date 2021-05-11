export default (value) => {
    switch (value) {
        case 'VEICULO':
            return 'Veículo'
        case 'ARMAMENTO':
            return 'Armamento'
        case 'MOVEL':
            return 'Móvel'
        case 'EQUIPAMENTO':
            return 'Equipamento'
    }
    return value
}

