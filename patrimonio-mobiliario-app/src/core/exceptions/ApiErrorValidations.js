import HttpStatus from '@/core/utils/HttpStatus'

export default class ApiErrorValidations extends Error {
    constructor(message, response) {
        super(message)
        this.name = 'ApiErrorValidations'
        this.response = response
        this.httpStatus = new HttpStatus(response)

        if (Error.captureStackTrace) {
            Error.captureStackTrace(this, ApiErrorValidations)
        }
    }

    disconnected() {
        return this.httpStatus.disconnected()
    }

    internalError() {
        return this.httpStatus.internalError()
    }

    unauthorized() {
        return this.httpStatus.unauthorized()
    }

    notFound() {
        return this.httpStatus.notFound()
    }

    multipleErrors() {
        return this.response.data && this.response.data.errorMessages
    }
}
