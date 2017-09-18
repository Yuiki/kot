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
    val responseBuilder = Response.Builder()
    if (request.isGet()) {
        val file = File("./src/main/resources/${request.path}")

        if (file.exists() && file.isFile) {
            responseBuilder.body(file)
                    .status(Status.OK)
        } else {
            responseBuilder.body("404 Not Found")
                    .status(Status.NOT_FOUND)
        }
    } else if (request.isPost()) {
        responseBuilder.body(request.body)
                .status(Status.OK)
    }
    val output = socket.getOutputStream()
    responseBuilder.build().send(output)
    input.close()
    output.close()
    socket.close()
    server.close()
    println("<<< end")
}