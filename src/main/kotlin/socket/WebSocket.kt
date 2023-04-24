package socket

import http.*
import java.io.Closeable
import java.io.IOException
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.*

open class WebSocket(port: Int): Thread(), Closeable {
    private val server: ServerSocket
    private var running = true
    init {
        server = ServerSocket(port)
        server.soTimeout = 500
    }

    override fun run() {
        while (running) {
            tryAcceptAndHandle()
        }
    }

    private fun tryAcceptAndHandle() {
        try{
            ClientHandler(server.accept(), this).start()
        } catch (_: IOException) {
            //timeout - just ignore
        }
    }

    private class ClientHandler(val client: Socket, var webSocket: WebSocket): Thread() {
        override fun run() {
            try {
                val request = client.readRequest()
                logRequest(request)
                val response = handleRequest(request)
                client.respondRequest(response)
            } catch (e: TooManyHeaderFieldsException) {
                client.respondRequest(`Request Header Fields Too Large`(e.msg))
            } catch (e: HTTPException) {
                client.respondRequest(`Bad Request`(e.msg))
            } finally {
                client.close()
            }
        }

        private fun Socket.readRequest(): Request {
            val inputScanner =  Scanner(getInputStream())

            if (!inputScanner.hasNextLine()) throw NoRequestLineException()

            val requestLine = inputScanner.nextLine()
            val type = readRequestLineType(requestLine)
            val location = readRequestLineLocation(requestLine)
            val protocol = readRequestLineProtocol(requestLine)

            val headerFields = mutableMapOf<String, String>()
            var nFields = 0
            while (inputScanner.hasNextLine() && nFields < MAX_REQUEST_HEADER_FIELDS) {
                val headerField = inputScanner.nextLine()
                if (headerField.isBlank()) break

                val headerTokens = headerField.trim().split(": ")
                try {
                    headerFields[headerTokens[0]] = headerTokens.drop(1).joinToString(": ")
                } catch (e: Exception) {
                    throw InvalidHeaderLineException()
                }
                nFields++
            }
            if (nFields == MAX_REQUEST_HEADER_FIELDS) throw TooManyHeaderFieldsException()
            if (!headerFields.containsKey("Host")) throw NoHostFieldException()
            //val payload= StringBuilder()
            //while (inputScanner.hasNextLine() && nFields < MAX_REQUEST_HEADER_FIELDS) {
            //    payload.append(inputScanner.nextLine())
            //}
            return Request(client.remoteSocketAddress.toString(), type, location, protocol, headerFields, null)
        }

        private fun readRequestLineType(requestLine: String) =
            when (requestLine.split(" ")[0]) {
                "GET" -> http.Request.GET
                "HEAD" -> http.Request.HEAD
                "POST" -> http.Request.POST
                "PUT" -> http.Request.PUT
                "DELETE" -> http.Request.DELETE
                "CONNECT" -> http.Request.CONNECT
                "OPTIONS" -> http.Request.OPTIONS
                "TRACE" -> http.Request.TRACE
                "PATCH" -> http.Request.PATCH
                else -> throw UnknownRequestMethod()
            }

        private fun readRequestLineLocation(requestLine: String): String =
            requestLine.split(" ")[1]

        private fun readRequestLineProtocol(requestLine: String) =
            when (requestLine.split(" ")[2]) {
            "HTTP/0.9" -> HTTPProtocol.HTTP0_9
            "HTTP/1.0" -> HTTPProtocol.HTTP1_0
            "HTTP/1.1" -> HTTPProtocol.HTTP1_1
            "HTTP/2" -> HTTPProtocol.HTTP2
            "HTTP/3" -> HTTPProtocol.HTTP3
            else -> throw UnknownProtocolException()
        }

        private fun logRequest(request: Request) {
            println("from: ${request.ip}")
            println("method: ${request.requestMethod.name}")
            println("wants: ${request.location}")
            println("protocol: ${request.protocol}")
            request.headerFields.forEach { println("${it.key} -> ${it.value}") }
            println()
            println()
            println()
        }

        private fun handleRequest(request: Request): Response {
            if(request.protocol != HTTPProtocol.HTTP1_1) return `HTTP Version Not Supported`("Protocol not supported.")

            return webSocket::class.java.declaredMethods
                .firstOrNull { it.isAnnotationPresent(request.requestMethod.anno) && it
                    .getAnnotation(request.requestMethod.anno)::class.java
                    .getMethod("location")
                    .invoke(it.getAnnotation(request.requestMethod.anno)) == request.location
                }?.invoke(webSocket) as Response? ?: `Not Found`("Requested site could not be found!")
        }

        private fun Socket.respondRequest(response: Response) {
            val outStream = PrintWriter(getOutputStream())
            outStream.println(response)
            outStream.flush()
            outStream.close()
        }

        private data class Request(val ip: String,
                                   val requestMethod: http.Request,
                                   val location: String,
                                   val protocol: HTTPProtocol,
                                   val headerFields: Map<String, String>,
                                   val payload: String?)
    }

    override fun close() {
        running = false
        server.close()
    }

    companion object {
        const val MAX_REQUEST_HEADER_FIELDS = 50
    }
}