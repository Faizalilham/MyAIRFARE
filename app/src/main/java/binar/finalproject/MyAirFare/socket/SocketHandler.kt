package binar.finalproject.MyAirFare.socket

import io.socket.client.IO
import io.socket.client.Socket

object SocketHandler {

    var _socket : Socket = IO.socket("https://binarstudpenfinalprojectbe-production.up.railway.app/")

    @Synchronized
    fun setSocket(){
        try{
            _socket = IO.socket("https://binarstudpenfinalprojectbe-production.up.railway.app/") // url server
        }catch (e : Exception){
            print(e.message)
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return _socket
    }
}