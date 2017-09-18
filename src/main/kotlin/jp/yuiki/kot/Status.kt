package jp.yuiki.kot

enum class Status(val code: String) {
    OK("200 OK");

    override fun toString() = code
}