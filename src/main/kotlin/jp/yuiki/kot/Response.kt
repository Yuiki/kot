package jp.yuiki.kot

import java.io.File
import java.io.OutputStream
import java.nio.file.Files

class Response(val status: Status, val headers: Headers) {
    var bodyStr: String? = null
    var bodyFile: File? = null

    fun send(outStream: OutputStream) {
        val output = ResponseOutput(outStream)
        output.println("HTTP/1.1 ${this.status}")

        headers.headers.forEach { output.println("${it.key}: ${it.value}") }

        if (bodyStr != null) {
            output.println()
            output.print(bodyStr!!)
        } else if (bodyFile != null) {
            output.println()
            Files.copy(bodyFile!!.toPath(), outStream)
        }
    }

    class Builder {
        var status: Status? = null
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
            if (status == null) {
                throw IllegalStateException()
            }

            val response = Response(status!!, headers)
            if (bodyStr != null) {
                response.bodyStr = bodyStr
                addHeader("Content-Type", ContentType.TEXT_PLAIN.toString())
            } else if (bodyFile != null) {
                response.bodyFile = bodyFile
                addHeader("Content-Type", ContentType.valueOfByExtension(bodyFile!!.extension).toString())
            }
            return response
        }
    }
}