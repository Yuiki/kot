package jp.yuiki.kot

import java.io.File
import java.io.InputStream
import java.net.ServerSocket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Server(val port: Int) {
    val executor: ExecutorService = Executors.newCachedThreadPool()

    var alive = true

    fun start() {
        ServerSocket(port).use {
            while (alive) {
                process(it)
            }
        }
    }

    fun stop() {
        alive = false
    }

    fun process(server: ServerSocket) {
        val socket = server.accept()
        executor.execute({
            val input = socket.getInputStream()
            val output = socket.getOutputStream()
            try {
                val request = processRequest(input)
                val response = makeResponse(request)
                response.send(output)
            } finally {
                input.close()
                output.close()
                socket.close()
            }
        })
    }

    fun processRequest(input: InputStream): Request {
        val request = Request(input)
        println(request.requestLine)
        println(request.headers)
        println(request.body)
        return request
    }

    fun makeResponse(request: Request): Response {
        val responseBuilder = Response.Builder()
        if (request.method == Method.GET) {
            processGet(responseBuilder, request.path)
        } else if (request.method == Method.POST) {
            processPost(responseBuilder, request.body)
        }
        return responseBuilder.build()
    }

    fun processGet(responseBuilder: Response.Builder, path: String) {
        val file = File("./src/main/resources/$path")
        if (file.exists() && file.isFile) {
            responseBuilder.body(file).status(Status.OK)
        } else {
            responseBuilder.body("404 Not Found").status(Status.NOT_FOUND)
        }
    }

    fun processPost(responseBuilder: Response.Builder, body: String) {
        responseBuilder.body(body).status(Status.OK)
    }
}