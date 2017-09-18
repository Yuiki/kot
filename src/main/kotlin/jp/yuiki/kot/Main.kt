package jp.yuiki.kot

import java.net.ServerSocket

fun main(args: Array<String>) {
    println("start >>>")
    val server = ServerSocket(8080)
    val socket = server.accept()
    val input = socket.getInputStream()
    val request = Request(input)
    println(request.requestLine)
    println(request.headers)
    println(request.body)
    val response = Response.Builder()
            .status(Status.OK)
            .addHeader("Content-Type", ContentType.TEXT_HTML.toString())
            .body("<h1>Hello, World!</h1>")
            .build()
    val output = socket.getOutputStream()
    response.send(output)
    input.close()
    output.close()
    socket.close()
    server.close()
    println("<<< end")
}