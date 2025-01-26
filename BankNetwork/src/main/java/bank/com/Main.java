package bank.com;
import bank.com.server.ServerManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
public class Main {
    public static void main(String[] args) {
        ServerManager serverManager = new ServerManager(65525, 5);
        serverManager.startServer();
    }
}