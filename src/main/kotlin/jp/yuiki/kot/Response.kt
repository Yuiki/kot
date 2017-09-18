package jp.yuiki.kot

import java.io.File
import java.io.OutputStream
import java.nio.file.Files

class Response(val status: Status, val headers: Headers) {
    var bodyStr: String? = null
    var bodyFile: File? = null

    fun send(out: OutputStream) {
        val io = IOWrapper(out)
        io.println("HTTP/1.1 ${this.status}")

        headers.headers.forEach { k, v -> io.println("$k: $v") }

        if (bodyStr != null) {
            io.println()
            io.print(bodyStr!!)
        } else if (bodyFile != null) {
            io.println()
            Files.copy(bodyFile!!.toPath(), out)
        }
    }

    class Builder {
        var status = Status.OK
        var headers = Headers()
        var bodyStr: String? = null
        var bodyFile: File? = null

        fun status(status: Status): Builder {
            this.status = status
            return this
        }

        fun body(bodyStr: String): Builder {
            this.bodyStr = bodyStr
            return this
        }

        fun body(bodyFile: File): Builder {
            this.bodyFile = bodyFile
            return this
        }

        fun addHeader(name: String, value: String): Builder {
            headers.headers[name] = value
            return this
        }

        fun build(): Response {
            val response = Response(status, headers)
            if (bodyStr != null) {
                response.bodyStr = bodyStr
                addHeader("Content-Type", ContentType.TEXT_PLAIN.toString())
            } else if (bodyFile != null) {
                response.bodyFile = bodyFile
                addHeader("Content-Type", ContentType.ofByExtension(bodyFile!!.extension).toString())
            }
            return response
        }
    }
}