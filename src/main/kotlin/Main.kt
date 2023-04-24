import http.GET
import http.OK
import http.POST
import http.Response
import socket.WebSocket

// for testing on port 80 on linux
// sudo iptables -t nat -A PREROUTING -p tcp --dport 80 -j REDIRECT --to-port 8080
// sudo iptables -t nat -I OUTPUT -p tcp -d 127.0.0.1 --dport 80 -j REDIRECT --to-ports 8080
// source: https://serverfault.com/questions/112795/how-to-run-a-server-on-port-80-as-a-normal-user-on-linux
fun main() {
    MyWebServer().start()
}

class MyWebServer: WebSocket(8080) {
    @GET("/")
    fun index(): Response {
        return OK("Adrian Vinojcic")
    }

    @POST("/")
    fun indexPost(): Response {
        return OK("POSTHI")
    }

    @GET("/help")
    fun halp(): Response {
        return OK("helper")
    }
}