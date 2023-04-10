abstract class Response(private val code: Int, private val payload: String) {

    override fun toString(): String {
        return  "HTTP/1.1 $code ${javaClass.simpleName}\n" +
                "Content-Type: text/html\n" +
                "\n" +
                payload
    }
}


class Continue(payload: String): Response(100, payload)
class `Switching Protocols`(payload: String): Response(101, payload)
class Processing(payload: String): Response(102, payload)
class `Early Hints`(payload: String): Response(103, payload)

class OK(payload: String): Response(200, payload)
class Created(payload: String): Response(201, payload)
class Accepted(payload: String): Response(202, payload)
class `Non-Authoritative Information`(payload: String): Response(203, payload)
class `No Content`(payload: String): Response(204, payload)
class `Reset Content`(payload: String): Response(205, payload)
class `Partial Content`(payload: String): Response(206, payload)
class `Multi-Status`(payload: String): Response(207, payload)
class `Already Reported`(payload: String): Response(208, payload)
class `IM Used`(payload: String): Response(226, payload)

class `Multiple Choices`(payload: String): Response(300, payload)
class `Moved Permanently`(payload: String): Response(301, payload)
class Found(payload: String): Response(302, payload)
class `See Other`(payload: String): Response(303, payload)
class `Not Modified`(payload: String): Response(304, payload)
class `Use Proxy`(payload: String): Response(305, payload)
class `Switch Proxy`(payload: String): Response(306, payload)
class `Temporary Redirect`(payload: String): Response(307, payload)
class `Permanent Redirect`(payload: String): Response(308, payload)

class `Bad Request`(payload: String): Response(400, payload)
class Unauthorized(payload: String): Response(401, payload)
class `Payment Required`(payload: String): Response(402, payload)
class Forbidden(payload: String): Response(403, payload)
class `Not Found`(payload: String): Response(404, payload)
class `Method Not Allowed`(payload: String): Response(405, payload)
class `Not Acceptable`(payload: String): Response(406, payload)
class `Proxy Authentication Required`(payload: String): Response(407, payload)
class `Request Timeout`(payload: String): Response(408, payload)
class Conflict(payload: String): Response(409, payload)
class Gone(payload: String): Response(410, payload)
class `Length Required`(payload: String): Response(411, payload)
class `Precondition Failed`(payload: String): Response(412, payload)
class `Payload Too Large`(payload: String): Response(413, payload)
class `URI Too Long`(payload: String): Response(414, payload)
class `Unsupported Media Type`(payload: String): Response(415, payload)
class `Range Not Satisfiable`(payload: String): Response(416, payload)
class `Expectation Failed`(payload: String): Response(417, payload)
class `I'm a teapot`(payload: String): Response(418, payload)
class `Misdirected Request`(payload: String): Response(421, payload)
class `Unprocessable Entity`(payload: String): Response(422, payload)
class Locked(payload: String): Response(423, payload)
class `Failed Dependency`(payload: String): Response(424, payload)
class `Too Early`(payload: String): Response(425, payload)
class `Upgrade Required`(payload: String): Response(426, payload)
class `Precondition Required`(payload: String): Response(428, payload)
class `Too Many Requests`(payload: String): Response(429, payload)
class `Request Header Fields Too Large`(payload: String): Response(431, payload)
class `Unavailable For Legal Reasons`(payload: String): Response(451, payload)

class `Internal Server Error`(payload: String): Response(500, payload)
class `Not Implemented`(payload: String): Response(501, payload)
class `Bad Gateway`(payload: String): Response(502, payload)
class `Service Unavailable`(payload: String): Response(503, payload)
class `Gateway Timeout`(payload: String): Response(504, payload)
class `HTTP Version Not Supported`(payload: String): Response(505, payload)
class `Variant Also Negotiates`(payload: String): Response(506, payload)
class `Insufficient Storage `(payload: String): Response(507, payload)
class `Loop Detected`(payload: String): Response(508, payload)
class `Not Extended`(payload: String): Response(510, payload)
class `Network Authentication Required`(payload: String): Response(511, payload)


