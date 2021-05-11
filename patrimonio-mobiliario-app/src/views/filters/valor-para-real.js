import accounting from 'accounting'

export default (valor) => {
    if (!valor) {
        return '-'
    }
    if (!isNaN(valor)) {
        const moneyConfig = {
            decimal: ',',
            thousands: '.',
            prefix: '',
            suffix: '',
            precision: 2,
            masked: true
        }
        return 'R$ ' + accounting.formatMoney(valor, moneyConfig.prefix, moneyConfig.precision, moneyConfig.thousands, moneyConfig.decimal)
    }
    return valor
}

