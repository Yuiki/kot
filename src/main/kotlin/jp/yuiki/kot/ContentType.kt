package jp.yuiki.kot

enum class ContentType(val type: String, vararg val extensions: String) {
    APPLICATION_JAVASCRIPT("application/javascript", "js"),
    IMAGE_GIF("image/gif", "gif"),
    IMAGE_JPEG("image/jpeg", "jpg", "jpeg"),
    IMAGE_PNG("image/png", "png"),
    TEXT_PLAIN("text/plain", "txt"),
    TEXT_HTML("text/html", "html", "htm"),
    TEXT_CSS("text/css", "css");

    override fun toString() = type

    companion object {
        val mapping = HashMap<String, ContentType>()

        init {
            values().forEach { type -> type.extensions.forEach { mapping.put(it, type) } }
        }

        fun valueOfByExtension(target: String): ContentType {
            return mapping.getOrElse(target) { TEXT_PLAIN }
        }
    }
}

