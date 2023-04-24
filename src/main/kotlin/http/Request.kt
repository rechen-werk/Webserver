package http

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class GET(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HEAD(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class POST(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PUT(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class DELETE(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CONNECT(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class OPTIONS(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TRACE(val location: String)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PATCH(val location: String)

enum class Request(val anno: Class<out Annotation>) {
    GET(http.GET::class.java),
    HEAD(http.HEAD::class.java),
    POST(http.POST::class.java),
    PUT(http.PUT::class.java),
    DELETE(http.DELETE::class.java),
    CONNECT(http.CONNECT::class.java),
    OPTIONS(http.OPTIONS::class.java),
    TRACE(http.TRACE::class.java),
    PATCH(http.PATCH::class.java)
}
