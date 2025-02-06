package bank.com.server;

import bank.com.service.AccountService;
import bank.com.service.TransactionService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class manages all connections to the server and creates ClientHandler(s) for each of them. Also maintains thread pool.
 * @author Jakub Hofman
 */
public class ServerManager {
    private final int port;
    private final int backlogSize;
    private final ExecutorService threadPool;
    private final AccountService accountService;
    private final TransactionService transactionService;

    public ServerManager(int port, int backlogSize,int threadPoolSize, AccountService accountService, TransactionService transactionService) {
        this.port = port;
        this.backlogSize = backlogSize;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    /**
     * Starts the server and binds it to the LAN IP address of the current machine.
     */
    public void startServer() {
        try {
            // Get the LAN IP address
            InetAddress localAddress = getLocalIpAddress();

            if (localAddress == null) {
                System.err.println("Could not determine the LAN IP address.");
                return;
            }

            try (ServerSocket serverSocket = new ServerSocket(port, backlogSize, localAddress)) {
                System.out.println("Server started. Listening on IP: " + localAddress.getHostAddress() + ", Port: " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.toString());

                    // Execute ClientHandler directly as a Runnable
                    threadPool.execute(new ClientHandler(clientSocket, accountService, transactionService, localAddress.getHostAddress()));
                }
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Shuts down the server's thread pool.
     */
    public void stopServer() {
        threadPool.shutdown();
        System.out.println("Server stopped.");
    }

    /**
     * Gets the local LAN IP address of the machine.
     *
     * @return The LAN IP address or null if it cannot be determined.
     */
    private InetAddress getLocalIpAddress() {
        try {
            for (InetAddress address : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
                if (address.isSiteLocalAddress()) {
                    return address;
                }
            }
        } catch (IOException e) {
            System.err.println("Error retrieving LAN IP address: " + e.getMessage());
        }
        return null;
    }
}
