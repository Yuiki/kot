package jp.yuiki.kot

import java.io.InputStream
import java.util.*
import kotlin.collections.HashMap

class Request(input: InputStream) {
    val scanner: Scanner = Scanner(input)

    var requestLine = ""
    val headers = HashMap<String, String>()
    var body = ""

    init {
        readRequestLine()
        readHeaders()
        readBody()
    }

    fun readRequestLine() {
        if (scanner.hasNextLine()) {
            requestLine = scanner.nextLine()
        }
    }

    fun readHeaders() {
        val headersStr = StringBuilder()
        while (scanner.hasNextLine()) {
            val headerStr = scanner.nextLine()
            if (headerStr.isEmpty()) {
                break
            }
            headersStr.append(headerStr).append("\n")
        }
        // 行末の改行を削除
        headersStr.setLength(headersStr.length - 1)

        makeHeadersMap(headersStr.toString())
    }

    fun readBody() {
        while (scanner.hasNextLine()) {
            body = scanner.nextLine()
        }
    }

    fun makeHeadersMap(headersStr: String) {
        headersStr.split("\n").forEach { headerStr ->
            val splits = headerStr.split(Regex(":\\s"))
            headers[splits[0]] = splits[1]
        }
    }
}