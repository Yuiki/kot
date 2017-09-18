package jp.yuiki.kot

import java.io.OutputStream

class ResponseOutput(val out: OutputStream) {
    fun println() {
        println("")
    }

    fun println(line: String) {
        print(line + "\n")
    }

    fun print(line: String) {
        out.write(line.toByteArray())
    }
}