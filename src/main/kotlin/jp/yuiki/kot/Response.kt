package jp.yuiki.kot

import java.io.OutputStream

class Response(val status: Status, val headers: HashMap<String, String>, val body: String) {
    fun send(out: OutputStream) {
        val io = IOWrapper(out)
        io.println("HTTP/1.1 ${this.status}")

        headers.forEach { k, v -> io.println("$k: $v") }

        io.println()
        io.print(body)
    }

    class Builder {
        var status = Status.OK
        var headers = HashMap<String, String>()
        var body = ""

        fun status(status: Status): Builder {
            this.status = status
            return this
        }

        fun body(body: String): Builder {
            this.body = body
            return this
        }

        fun addHeader(name: String, value: String): Builder {
            headers[name] = value
            return this
        }

        fun build(): Response {
            return Response(status, headers, body)
        }
    }
}