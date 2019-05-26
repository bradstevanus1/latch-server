package com.brad.latchserver;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LatchServer {

    public static void main(String[] args) {
        Server server = new Server(8192);
        server.start();

        byte[] data = {0, 1, 2};
        InetAddress address = null;
        try {
            address = InetAddress.getByName("192.168.1.10");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        int port = 8192;
        server.send(data, address, port);
    }

}
