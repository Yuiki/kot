package jp.yuiki.kot

enum class ContentType(val type: String) {
    TEXT_HTML("text/html");

    override fun toString() = type
}