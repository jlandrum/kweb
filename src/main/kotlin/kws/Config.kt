package com.jlandrum.kweb

class Config {
    private val unhandledError = "<html><body><i>A 404 error occurred, and a 404 error occurred trying to find the error page.</i></body></html>"
    private val routes = ArrayList<Route>()
    private val errors = ArrayList<Error>()

    fun route(path:String, method:String = "GET", response:()->String) {
        routes.add(Route(path,method,response))
    }

    fun error(code:Int, response:()->String) {
        errors.add(Error(code,response))
    }

    fun findRoute(method: String, path: String) : Handler? = routes.firstOrNull { it.path == path && it.method == method }
    fun findError(code: Int): Handler = errors.firstOrNull { it.code == code } ?: Error(0, {return@Error unhandledError})

    private class Route(val path:String, val method:String, private val action:()->String) : Handler {
        override fun invoke(): Response = Response(action())
    }

    private class Error(val code:Int, private val action:()->String) : Handler {
        override fun invoke(): Response = Response(action())
    }
}

interface Handler { fun invoke(): Response }

