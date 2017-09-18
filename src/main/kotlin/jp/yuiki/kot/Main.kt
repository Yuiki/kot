package jp.yuiki.kot

import java.io.File
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
    val responseBuilder = Response.Builder().status(Status.OK)
    if (request.isGet()) {
        responseBuilder.body(File("./src/main/resources/${request.path}"))
    } else if (request.isPost()) {
        responseBuilder.body(request.body)
    }
    val output = socket.getOutputStream()
    responseBuilder.build().send(output)
    input.close()
    output.close()
    socket.close()
    server.close()
    println("<<< end")
}