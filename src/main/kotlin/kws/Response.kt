package com.jlandrum.kweb

import java.nio.charset.Charset
import java.util.*

class Response(
        val output:String,
        server:String = "KWEB/0.1",
        type:String = "text/html") {
    val headers = ArrayList<ResponseHeader>()

    init {
        headers.add(ResponseHeader("Date", Date().toString()))
        headers.add(ResponseHeader("Server", server))
        headers.add(ResponseHeader("Content-Length", output.length.toString()))
        headers.add(ResponseHeader("Content-Type", type))
        headers.add(ResponseHeader("Connection", "closed"))
    }

    val response: String get()  {
        return "HTTP/1.1 200 OK\r\n" +
                "Date: ${Date()}\r\n" +
                "${headers.string}\r\n" + output;
    }

    val encoded: ByteArray = response.toByteArray(Charset.forName("UTF-8"))
}

class ResponseHeader(val name:String, var value:String = "") {
    val string : String get() = "$name: $value\r\n"
}

val ArrayList<ResponseHeader>.string : String get() = this.joinToString("") { it.string }