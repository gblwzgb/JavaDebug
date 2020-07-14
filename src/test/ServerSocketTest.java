package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketTest {

    public static void main(String[] args) {
        try {
            new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost", 20880));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
