import accounting from 'accounting'

export default (valor) => {
    if (!valor) {
        return '-'
    }
    if (!isNaN(valor)) {
        const moneyConfig = {
            decimal: ',',
            thousands: '.',
            prefix: 'R$ ',
            suffix: '',
            precision: 2,
            masked: true
        }
        return accounting.formatMoney(valor, moneyConfig.prefix, moneyConfig.precision, moneyConfig.thousands, moneyConfig.decimal)
    }
    return valor
}

