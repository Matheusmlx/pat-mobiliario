export default class FileManagerUtils {

    static createThumbnailUrl(uri) {
        return (uri) ? `/hal/public/arquivos?uri=${uri}&thumbnail=true` : ''
    }

    static createUrl(uri) {
        return (uri) ? `/hal/public/arquivos?uri=${uri}&thumbnail=false` : ''
    }
}
