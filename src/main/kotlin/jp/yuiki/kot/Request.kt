package jp.yuiki.kot

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class Request(input: InputStream) {
    val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
    var requestLine = ""
    val headers = HashMap<String, String>()
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

        makeHeadersMap(headersStr.toString())
    }

    fun readBody() {
        var bodyChars = CharArray(headers["Content-Length"]?.toIntOrNull() ?: 0)
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
                break
            }
        }
        return Arrays.copyOf(chars, size)
    }

    fun makeHeadersMap(headersStr: String) {
        headersStr.split("\n").forEach { headerStr ->
            val splits = headerStr.split(Regex(":\\s"))
            headers[splits[0]] = splits[1]
        }
    }
}