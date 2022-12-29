@file:Suppress("HasPlatformType", "ObjectPropertyName", "RedundantSemicolon",
    "MemberVisibilityCanBePrivate", "MemberVisibilityCanBePrivate", "unused", "unused", "unused",
    "unused", "unused", "unused", "unused", "unused", "unused", "unused"
)

package binar.finalproject.MyAirFare.socket

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URLEncoder

object SocketHandler {

    val a = URLEncoder.encode("https://binarfinalsocketserver-production.up.railway.app/", "UTF-8");
    var _socket : Socket? = IO.socket(a)

    @Synchronized
    fun setSocket(){
        try{
            _socket = IO.socket(a) // url server
        }catch (e : Exception){
            print(e.message)
        }
    }

    @Synchronized
    fun getSocket(): Socket? {
        return _socket
    }
}