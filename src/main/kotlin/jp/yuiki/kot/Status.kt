package jp.yuiki.kot

enum class Status(val code: String) {
    OK("200 OK"),
    NOT_FOUND("404 Not Found");

    override fun toString() = code
}