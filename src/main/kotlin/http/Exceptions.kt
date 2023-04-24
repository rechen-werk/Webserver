package http

open class HTTPException(val msg: String): Exception(msg)
class NoRequestLineException: HTTPException("Could not retrieve request line from request.")
class UnknownRequestMethod: HTTPException("Request Method unknown.")
class UnknownProtocolException: HTTPException("HTTP protocol unknown.")
class TooManyHeaderFieldsException: HTTPException("Too many header fields in request.")
class InvalidHeaderLineException: HTTPException("Invalid header line.")
class NoHostFieldException: HTTPException("No \"Host\" field in headers.")