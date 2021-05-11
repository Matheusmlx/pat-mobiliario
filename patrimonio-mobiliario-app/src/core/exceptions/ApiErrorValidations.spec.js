import ApiErrorValidations from './ApiErrorValidations'

describe('ApiErrorValidations', () => {
    it('Deve verificar um erro de disconneted', () => {
        const apiErrorValidations = new ApiErrorValidations('mensagem')
        expect(apiErrorValidations.disconnected()).toBeTruthy()
    })

    it('Deve verificar um erro de internal error', () => {
        const apiErrorValidations = new ApiErrorValidations('mensagem', { status: 500 })
        expect(apiErrorValidations.internalError()).toBeTruthy()
    })

    it('Deve verificar um erro de not found', () => {
        const apiErrorValidations = new ApiErrorValidations('mensagem', { status: 404 })
        expect(apiErrorValidations.notFound()).toBeTruthy()
    })

    it('Deve verificar um erro de unauthorized', () => {
        const apiErrorValidations = new ApiErrorValidations('mensagem', { status: 401 })
        expect(apiErrorValidations.unauthorized()).toBeTruthy()
    })

    it('Deve verificar um erro de multiple errors', () => {
        const apiErrorValidations = new ApiErrorValidations('mensagem', { data: { errorMessages: 'Mensagem 2' } })
        expect(apiErrorValidations.multipleErrors()).toBeTruthy()
    })
})
