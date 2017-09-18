package jp.yuiki.kot

import java.io.OutputStream

class IOWrapper(val out: OutputStream) {
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