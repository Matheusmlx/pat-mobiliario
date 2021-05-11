import Vue from 'vue'
import upperFirst from 'lodash/upperFirst'
import camelCase from 'lodash/camelCase'

class RegisterGlobalComponentsListener {
    async execute() {
        return new Promise((resolve) => {
            const requireComponent = require.context(
                '../../views/components',
                true,
                /[A-Z]\w+\.(vue|js)$/
            )
            requireComponent.keys().forEach(fileName => {
                const componentConfig = requireComponent(fileName)
                const fileNameWithoutDots = fileName.replace(/^\.\/(.*)\.\w+$/, '$1')
                const componentName = upperFirst(
                    camelCase(
                        fileNameWithoutDots
                    )
                )
                Vue.component(componentName, componentConfig.default || componentConfig)
            })
            resolve()
        })
    }
}

export default new RegisterGlobalComponentsListener()
