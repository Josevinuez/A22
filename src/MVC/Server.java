package MVC;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final AtomicInteger clientCount = new AtomicInteger(0);  // For assigning unique IDs to each client
    private final AtomicInteger activeClientCount = new AtomicInteger(0);  // For tracking active clients
    private final PrintStream outStream;
    private volatile boolean isRunning = true;

    /**
     * Constructs a new game server with a specified port and output stream.
     * @param port The port on which the server will listen for client connections.
     * @param outStream The output stream for server messages.
     */
    public Server(int port, PrintStream outStream) {
        this.port = port;
        this.outStream = outStream;
    }
    /**
     * Starts the server to accept client connections.
     * @throws IOException if there's an error in network communication.
     */
    public void start() throws IOException {
        outStream.println("Starting the server...");
        serverSocket = new ServerSocket(port);
        try {
            while (isRunning) {
                outStream.println("Waiting for client to connect...");
                new ClientHandler(serverSocket.accept(), clientCount.incrementAndGet()).start();
                clientConnected();
            }
        } catch (SocketException e) {
            outStream.println("Server has been stopped.");
        }
    }
    /**
     * Stops the server from accepting further connections.
     * @throws IOException if there's an error in network communication.
     */
    public void stop() throws IOException {
        outStream.println("Stopping the server...");
        isRunning = false;
        serverSocket.close();
    }
    /**
     * Notifies that a client has connected to the server and increments the active client count.
     */
    public void clientConnected() {
        outStream.println("A client has connected. Total active clients: " + activeClientCount.incrementAndGet());
    }
    /**
     * Notifies that a client has disconnected from the server and decrements the active client count.
     */
    public void clientDisconnected() {
        int currentCount = activeClientCount.decrementAndGet();
        outStream.println("A client has disconnected. Total active clients: " + currentCount);
        if (currentCount == 0) {
            try {
                stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Represents a handler for individual client connections.
     */
    public class ClientHandler extends Thread {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private final int clientId;
        private final BlockingQueue<String> gameConfigurations = new LinkedBlockingQueue<>();

        /**
         * Constructs a new client handler for a given socket and client ID.
         *
         * @param socket The socket representing the client connection.
         * @param clientId The unique ID assigned to the client.
         */
        public ClientHandler(Socket socket, int clientId) {
            this.clientSocket = socket;
            this.clientId = clientId;
        }
        /**
         * The main execution method for handling client requests.
         */
        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                out.println(clientId);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    outStream.println("Received a message from client " + clientId + ": " + inputLine);
                    // Split the input into parts
                    String[] parts = inputLine.split(Config.PROTOCOL_SEPARATOR);
                    if (parts.length < 2) {
                        outStream.println("Invalid message format from client " + clientId + ": " + inputLine);
                        continue;
                    }
                    String protocol = parts[1];
                    switch (protocol) {
                        case Config.PROTOCOL_END:
                            outStream.println("Client " + clientId + " has ended the connection.");
                            // Respond back to the client before closing resources
                            out.println("ACK_END");

                            // Close client's resources
                            try {
                                if (out != null) {
                                    out.close();
                                }
                                if (in != null) {
                                    in.close();
                                }
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            clientDisconnected();

                            // Exit from the run method to end this thread
                            return;
                        case Config.PROTOCOL_SENDGAME:
                            try {
                                // Split the received data into dimension and game configuration
                                String[] gameData = parts[2].split(Config.FIELD_SEPARATOR);
                                int receivedDimension = Integer.parseInt(gameData[0]);
                                String receivedGameConfiguration = gameData[1];

                                gameConfigurations.add(receivedGameConfiguration);
                                outStream.println("Received game configuration with dimension " + receivedDimension + " from Client " + clientId + ": " + receivedGameConfiguration);
                                out.println("ACK");
                            } catch (Exception e) {
                                e.printStackTrace();
                                outStream.println("Error handling PROTOCOL_SENDGAME: " + e.getMessage());
                            }
                            break;
                        case Config.PROTOCOL_RECVGAME:
                            // This will block until there's a game configuration available
                            String gameConfig = gameConfigurations.take();

                            // Send the game configuration to the client
                            out.println(gameConfig);
                            break;
                        case Config.PROTOCOL_DATA:
                            try {
                                String playerName = parts[2];
                                int score = Integer.parseInt(parts[3]);

                                // Acknowledge the receipt of game results
                                out.println("ACK_GAME_RESULTS");

                                // Log the received data
                                outStream.println("Received game results from Client " + clientId + ". Player: " + playerName + ", Score: " + score);
                            } catch (Exception e) {
                                outStream.println("Error processing game results from Client " + clientId + ".");
                            }
                            break;
                    }
                }
                outStream.println("ClientHandler for client " + clientId + " has ended.");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                    if (clientSocket != null) {
                        clientSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
