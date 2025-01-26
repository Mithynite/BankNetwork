package bank.com.server;

import bank.com.service.ClientHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerManager {
    private final int port;
    private final ExecutorService threadPool;

    public ServerManager(int port, int threadPoolSize) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
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

            try (ServerSocket serverSocket = new ServerSocket(port, 50, localAddress)) {
                System.out.println("Server started. Listening on IP: " + localAddress.getHostAddress() + ", Port: " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("New client connected: " + clientSocket.getInetAddress());

                    // Directly handle the client with a new ClientHandler
                    threadPool.execute(() -> {
                        try {
                            new ClientHandler(clientSocket).handleClient();
                        } catch (Exception e) {
                            System.err.println("Error handling client: " + e.getMessage());
                        }
                    });
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
     * @throws IOException If there's an error retrieving the LAN IP address.
     */
    private InetAddress getLocalIpAddress() throws IOException {
        InetAddress localAddress = null;

        // Attempt to get the LAN IP address (skipping localhost)
        for (InetAddress address : InetAddress.getAllByName(InetAddress.getLocalHost().getHostName())) {
            if (address.isSiteLocalAddress()) {
                localAddress = address;
                break;
            }
        }

        return localAddress;
    }
}
