package jp.yuiki.kot

class Headers {
    val headers = HashMap<String, String>()

    fun makeHeadersMap(headersStr: String) {
        headersStr.split("\n").forEach { headerStr ->
            val splits = headerStr.split(':', limit = 2)
            headers[splits[0].trim()] = splits[1].trim()
        }
    }

    fun getContentLength(): Int {
        return headers["Content-Length"]?.toIntOrNull() ?: 0
    }
}