package jp.yuiki.kot

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URLDecoder
import java.util.*

class Request(input: InputStream) {
    val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
    var requestLine = ""
    var method: Method? = null
    var path: String? = null
    val headers = Headers()
    var body = ""

    init {
        readRequestLine()
        readHeaders()
        readBody()
    }

    fun readRequestLine() {
        val line = reader.readLine()
        if (line.isNotEmpty()) {
            requestLine = line
            val splits = requestLine.split("\\s".toRegex())
            method = Method.valueOf(splits[0].toUpperCase())
            path = URLDecoder.decode(splits[1], "UTF-8")
        }
    }

    fun readHeaders() {
        val headersStr = StringBuilder()
        var line = reader.readLine()
        while (line.isNotEmpty()) {
            headersStr.append(line).append("\n")
            line = reader.readLine()
        }
        // 行末の改行を削除
        headersStr.setLength(headersStr.length - 1)

        headers.makeHeadersMap(headersStr.toString())
    }

    fun readBody() {
        var bodyChars = CharArray(headers.getContentLength())
        reader.read(bodyChars)
        bodyChars = deleteTrailingNullChars(bodyChars)
        body = String(bodyChars)
    }

    fun deleteTrailingNullChars(chars: CharArray): CharArray {
        var size = chars.size
        for (i in chars.size - 1 downTo 0) {
            val char = chars[i]
            if (char == '\u0000') {
                size = i
            } else {
                break
            }
        }
        return Arrays.copyOf(chars, size)
    }

    fun isGet(): Boolean {
        return method == Method.GET
    }

    fun isPost(): Boolean {
        return method == Method.POST
    }
}