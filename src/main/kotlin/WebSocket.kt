import java.io.Closeable
import java.io.PrintWriter
import java.net.ServerSocket
import java.util.*

class WebSocket(port: Int): Runnable, Closeable {
    private val server: ServerSocket
    private var running = true
    init {
        server = ServerSocket(port)
    }

    override fun run() {
        while (running) {
            val client = server.accept() //TODO: timeout after a few seconds
            if (client == null) {
                println("Accept failed")
                return
            }
            val inStream = Scanner(client.getInputStream())
            val outStream = PrintWriter(client.getOutputStream())

            val request = if(inStream.hasNextLine()) inStream.nextLine() ?: "ERROR"
            else "ERROR"

            val response = handleRequest(request)
            outStream.println(response)
            outStream.flush()
            outStream.close()
            client.close()
        }

        server.close()
    }


    override fun close() {
        running = false
    }

    private fun handleRequest(header: String): Response {
        if (header == "ERROR") return `Bad Request`("<p>Could not get REST header.</p>")

        val headerTokens = header.split(" ")
        val location = headerTokens[1]

        val type = when (headerTokens[0]) {
            "GET"       -> GET::class.java
            "HEAD"      -> HEAD::class.java
            "POST"      -> POST::class.java
            "PUT"       -> PUT::class.java
            "DELETE"    -> DELETE::class.java
            "CONNECT"   -> CONNECT::class.java
            "OPTIONS"   -> OPTIONS::class.java
            "TRACE"     -> TRACE::class.java
            "PATCH"     -> PATCH::class.java
            else        -> return `Bad Request`("<p>Unknown request type.</p>")
        }

        var klass: Class<Any> = javaClass

        while (klass != Any::class.java) {
            val x = klass.declaredMethods
                .firstOrNull { it.isAnnotationPresent(type) && it
                    .getAnnotation(type)::class.java
                    .getMethod("location")
                    .invoke(it.getAnnotation(type)) == location
                }
            when (x) {
                null -> klass = klass.superclass
                else -> return x.invoke(this) as Response
            }
        }
        return `Not Found`("Requested site could not be found!")
    }

    @GET("/")
    fun index(): Response {
        return OK("HIIIII")
    }

    @POST("/")
    fun indexPost(): Response {
        return OK("POSTHI")
    }

    @GET("/help")
    fun halp(out: PrintWriter): Response {
        return OK("helper")
    }

    @GET("/julijan")
    fun fasd(): Response {
        return OK("juuu")
    }
}