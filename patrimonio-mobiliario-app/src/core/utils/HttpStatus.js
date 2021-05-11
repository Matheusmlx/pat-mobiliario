export default class HttpStatus {

    constructor(response) {
        this.response = response
    }

    disconnected() {
        return !(this.response)
    }

    internalError() {
        return this.response.status === 500
    }

    unauthorized() {
        return this.response.status === 401
    }

    notFound() {
        return this.response.status === 404
    }

}
