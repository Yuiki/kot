package jp.yuiki.kot

import java.net.ServerSocket

fun main(args: Array<String>) {
    println("start >>>")
    val server = ServerSocket(8080)
    val socket = server.accept()
    val request = Request(socket.getInputStream())
    println(request.requestLine)
    println(request.headers)
    println(request.body)
    println("<<< end")
}