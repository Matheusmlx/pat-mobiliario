export default class ParamUtils {

    static ObjectToParams(obj) {
        var entries = Object.entries(obj)
        var params = ''

        entries.forEach(keyValuePair => {
            var key = keyValuePair[0]
            var value = keyValuePair[1]

            if (key === 'descending'){
                params += this._concatParam('sort', !value ? 'ASC' : 'DESC')
            }
            else if(this._isObject(value)) {
                params += this._concatParam(key, value)
            }
            else if(this._isParamArray(value)){
                value.forEach(arrayValue => {
                    params += this._concatParam(key, arrayValue)
                })
            }
        })

        return params.substring(0, params.length - 1)
    }

    static ObjectsToParams(objectArray) {
        var params = ''

        objectArray.forEach(obj => {
            params += this.ObjectToParams(obj) + '&'
        })

        return params.substring(0, params.length - 1)
    }

    static _concatParam(key, value){
        return `${key}=${value}&`
    }

    static _isObject(obj){
        return (obj && !(obj instanceof Object) || obj === 0)
    }

    static _isParamArray(obj){
        return (obj instanceof Array && this._isString(obj[0]))
    }

    static _isString(str) {
        return (typeof(str) === 'string' || str instanceof String)
    }
}
