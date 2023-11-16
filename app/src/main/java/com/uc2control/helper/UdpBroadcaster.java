package com.uc2control.helper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class UdpBroadcaster {

    private static final int BROADCAST_PORT = 12345; // Your desired port
    private static final String BROADCAST_ADDR = "255.255.255.255"; // Broadcast address

    public void startBroadcasting() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                broadcastIP();
            }
        }, 0, 1000); // Broadcast every 1000 ms
    }

    private void broadcastIP() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            String ipAddress = getDeviceIPAddress();
            byte[] buffer = ipAddress.getBytes();

            DatagramPacket packet = new DatagramPacket(
                    buffer, buffer.length, InetAddress.getByName(BROADCAST_ADDR), BROADCAST_PORT);
            socket.send(packet);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getDeviceIPAddress() {
        try {
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (intf.getName().equalsIgnoreCase("wlan0")) { // For WiFi connection
                    for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                        if (!addr.isLoopbackAddress()) {
                            return addr.getHostAddress();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
}
