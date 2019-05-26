package com.brad.latchserver;

import com.brad.latchConnect.serialization.data.LCData;
import com.brad.latchConnect.serialization.data.LCDatabase;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Server {

    private int port;
    private Thread listenThread;
    private boolean listening = false;
    private DatagramSocket socket;

    private final int MAX_PACKET_SIZE = 1024;
    private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE * 10];

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        listening = true;
        listenThread = new Thread(this::listen, "LatchConnectServer-ListenThread");
        listenThread.start();
    }

    private void listen() {
        while (listening) {
            DatagramPacket packet = new DatagramPacket(receivedDataBuffer, MAX_PACKET_SIZE);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            process(packet);
        }
    }

    private void process(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        dumpPacket(packet);


        if (true) return;

        // Check if using LCDB packets
        if (new String(data, 0, 4).equals("LCDB")) {
            LCDatabase database = LCDatabase.Deserialize(data);
            String username = database.findObject("root").findString("username").getName();
            process(database);
        } else {
            // Switch to determine what other kinds of packets are being sent
            switch (data[0]) {
                case 1:
                    // Connection packet
                    break;
                case 2:
                    // Ping packet
                    break;
                case 3:
                    // Login attempt packet
                    break;
            }
        }
    }

    private void process(LCDatabase database) {

    }

    @SuppressWarnings("Duplicates")
    public void send(byte[] data, InetAddress address, int port) {
        assert(socket.isConnected());
        DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dumpPacket(DatagramPacket packet) {
        byte[] data = packet.getData();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        System.out.println("------------------");
        System.out.println("PACKET:");
        System.out.println("\t" + address.getHostAddress() + ":" + port);
        System.out.println("\tContents:");
        System.out.println("\t\t" + new String(data));
        System.out.println("------------------");

    }

}
