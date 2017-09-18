package jp.yuiki.kot

enum class ContentType(val type: String, vararg val extensions: String) {
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_HTML("text/html", "html", "htm");

    override fun toString() = type

    companion object {
        fun ofByExtension(target: String): ContentType {
            values().forEach { type ->
                type.extensions.forEach { extension ->
                    if (extension == target) {
                        return type
                    }
                }
            }
            return TEXT_PLAIN
        }
    }
}