import axios from 'axios'
import store from '@/core/store'
import { LoadingScreen } from '@/core/utils'
import ApiErrorValidations from '@/core/exceptions/ApiErrorValidations'
import exceptionHandler from '@/core/exceptions/ExceptionHandler'
import {mutationTypes} from '../../core/constants'


const loading = new LoadingScreen(store)

class InterceptadorHttp {
    async execute() {
        return new Promise((resolve) => {
            this.registrarInterceptadores()
            this.setarVariaveisFixasNoHeaderDaRequest()
            resolve()
        })
    }

    registrarInterceptadores() {
        axios.interceptors.request.use(this.tratarRequest, this.tratarErros)
        axios.interceptors.response.use(this.tratarResponse, this.tratarErros)
    }

    setarVariaveisFixasNoHeaderDaRequest() {
        axios.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest'
    }

    tratarRequest(config) {
        loading.start()
        return config
    }

    tratarResponse(response) {
        loading.stop()
        return response
    }

    tratarErros(error) {
        loading.stop()
        const intercept = new InterceptadorHttp()
        if (error.response.data && error.response.status !== 401) {
            intercept.mostrarFalhaSalvamentoAutomaticoSeAtivo()
            const apiErrorValidations = new ApiErrorValidations(error.response.data.message, error.response)
            exceptionHandler.execute(apiErrorValidations)
        } else {
            exceptionHandler.handleUnauthorized()
        }
    }

    mostrarFalhaSalvamentoAutomaticoSeAtivo() {
        if(store.state.loki.autoSave.saving){
            store.commit(mutationTypes.COMUM.MOSTRAR_FALHA_SALVAMENTO_AUTOMATICO)
        }
    }

}

export default new InterceptadorHttp()
