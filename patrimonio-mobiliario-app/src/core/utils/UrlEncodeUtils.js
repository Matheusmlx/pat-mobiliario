export default class UrlEncodeUtils {

    constructor(value) {
        this.value = value
    }

    encode() {
        if (this.value && typeof this.value === 'string') {
            this.replaceBackSlash()
            this.replaceBackTick()
            this.replaceCaretCharacter()
            this.replaceCurlyBrackets()
            this.replacePipeCharacter()
            this.replaceSquareBrackets()
        }

        return this.value
    }

    replaceBackSlash() {
        this.value = this.value.replace(/\\/g, '%5C')
    }

    replaceBackTick() {
        this.value = this.value.replace(/`/g, '%60')
    }

    replaceCaretCharacter() {
        this.value = this.value.replace(/\^/g, '%5E')
    }

    replaceCurlyBrackets() {
        this.value = this.value
            .replace(/{/g, '%7B')
            .replace(/}/g, '%7D')
    }

    replacePipeCharacter() {
        this.value = this.value.replace(/\|/g, '%7C')
    }

    replaceSquareBrackets() {
        this.value = this.value
            .replace(/\[/g, '%5B')
            .replace(/]/g, '%5D')
    }
}
