package jp.yuiki.kot

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URLDecoder
import java.util.*

class Request(input: InputStream) {
    val reader = BufferedReader(InputStreamReader(input, "UTF-8"))
    var requestLine: String
    var method: Method
    var path: String
    val headers = Headers()
    var body: String

    init {
        requestLine = reader.readLine()
        val splits = requestLine.split("\\s".toRegex())
        method = Method.valueOf(splits[0].toUpperCase())
        path = URLDecoder.decode(splits[1], "UTF-8")

        readHeaders()

        body = readBody()
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

    fun readBody(): String {
        var bodyChars = CharArray(headers.getContentLength())
        // TODO: マルチバイト文字がbodyに含まれている場合、無駄なバイトまで読み込んでいるので改善する必要がある
        reader.read(bodyChars)
        bodyChars = deleteTrailingNullChars(bodyChars)
        return String(bodyChars)
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
}