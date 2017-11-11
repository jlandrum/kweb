package kws

import com.jlandrum.kweb.Config
import com.jlandrum.kweb.Response
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.nio.charset.Charset

class Server(port: Int, initialize: Config.()->Unit) {
    val REQUEST_HEADER = Regex("([A-Z]+)\\s([^\\s]+)+\\s.+")
    val UTF8 = Charset.forName("UTF-8")
    val socket = ServerSocket(port)
    val config = Config()

    init {
        initialize(config)
    }

    fun listen() {
        while (true) {
            val socket = socket.accept()
            val stream = InputStreamReader(socket.getInputStream())
            val reader = BufferedReader(stream)
            var line = reader.readLine()
            var response: Response? = null

            while (!line.isEmpty()) {
                val matcher = REQUEST_HEADER.toPattern().matcher(line)
                if (matcher.matches()) {
                    val route = config.findRoute(matcher.group(1), matcher.group(2))
                    response = route?.invoke() ?: config.findError(404).invoke()
                }
                line = reader.readLine()
            }
            response?.let {
                socket.getOutputStream().write(it.encoded)
            }
        }
    }
}