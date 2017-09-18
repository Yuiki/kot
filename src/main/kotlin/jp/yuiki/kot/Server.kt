package jp.yuiki.kot

import java.io.File
import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Server(val port: Int) {
    val executor: ExecutorService = Executors.newCachedThreadPool()
    fun start() {
        println("start >>>")
        ServerSocket(port).use {
            while (true) {
                process(it)
            }
        }
        println("<<< end")
    }

    fun process(server: ServerSocket) {
        val socket = server.accept()
        executor.execute({
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
        })
    }
}