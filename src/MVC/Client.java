package MVC;

import java.io.*;
import java.net.Socket;

public class Client {
    private final String host;
    private final int port;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private int dimension= 5;
    private int clientId;
    private final GameModel gameModel;
    private final PrintStream outStream;

    /**
     * Constructs a new game client with specified host, port, game model, and output stream.
     * @param host The server's hostname or IP address.
     * @param port The port on which the server is listening.
     * @param gameModel The model representing the game state.
     * @param outStream The output stream for client messages.
     */
    public Client(String host, int port, GameModel gameModel, PrintStream outStream) {
        this.host = host;
        this.port = port;
        this.gameModel = gameModel;
        this.outStream = outStream;
    }
    /**
     * Sets the dimension of the game grid.
     * @param selectedDimension The selected dimension.
     */
    public void setDimension(int selectedDimension) {
        this.dimension = selectedDimension;
    }
    /**
     * Starts a connection to the game server.
     */
    public void startConnection() {
        try {
            outStream.println("Starting connection to the server...");

            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            clientId = Integer.parseInt(in.readLine());  // Receive the client ID from the server
            outStream.println("Connected to server. Assigned client ID: " + clientId);
        } catch (IOException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }
    }
    /**
     * Sends a message to the server and receives a response.
     * @param msg The message to be sent to the server.
     * @return The response received from the server.
     * @throws IOException if there's an error in network communication.
     */
    public String sendMessage(String msg) throws IOException {
        if(clientSocket.isClosed()) {
            throw new IOException("Socket is already closed.");
        }
        outStream.println("Sending message to server: " + msg);
        out.println(msg);
        String response = in.readLine();
        outStream.println("Received response from server: " + response);
        return response;
    }
    /**
     * Closes the connection to the server.
     * @throws IOException if there's an error in network communication.
     */
    public void stopConnection() throws IOException {
        outStream.println("Stopping connection to the server...");

        // Check if the socket is still open
        if (!clientSocket.isClosed()) {
            // Send a message to the server to indicate that this client is disconnecting
            sendMessage(clientId + Config.PROTOCOL_SEPARATOR + Config.PROTOCOL_END);

            // Close client's resources
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            clientSocket.close();
        }

        outStream.println("Disconnected from server.");
    }
    /**
     * Sends the current game configuration to the server. The game configuration is retrieved
     * from the game model and is then transmitted to the server using the appropriate protocol.
     *
     * @throws IOException if there's an error in network communication.
     */
    public void sendGameConfiguration() throws IOException {
        // Convert the game configuration to a string
        String gameConfiguration = gameModel.printGridsString();
        outStream.println("Sending game configuration to server...");
        sendMessage(clientId + Config.PROTOCOL_SEPARATOR + Config.PROTOCOL_SENDGAME + Config.PROTOCOL_SEPARATOR + dimension + Config.FIELD_SEPARATOR + gameConfiguration);
        outStream.println("Sent game configuration to server: " + gameConfiguration);
    }
    /**
     * Requests a game configuration from the server. The received configuration
     * is then printed to the output stream.
     *
     * @throws IOException if there's an error in network communication.
     */
    public void requestGameConfiguration() throws IOException {
        outStream.println("Requesting game configuration from server...");
        // Request a game configuration from the server
        String gameConfiguration = sendMessage(clientId + Config.PROTOCOL_SEPARATOR + Config.PROTOCOL_RECVGAME);
        outStream.println("Received game configuration from server: " + gameConfiguration);
    }
    /**
     * Sends the game results for a player to the server. The results include the player's name
     * and the points they've scored.
     *
     * @param playerName The name of the player.
     * @param points The points scored by the player.
     * @throws IOException if there's an error in network communication.
     */
    public void sendGameResults(String playerName, int points) throws IOException {
        outStream.println("Sending game results to server...");

        // Format the data as clientId#PROTOCOL_DATA#playerName#points
        String gameResults = clientId + Config.PROTOCOL_SEPARATOR + Config.PROTOCOL_DATA + Config.PROTOCOL_SEPARATOR + playerName + Config.PROTOCOL_SEPARATOR + points;

        String response = sendMessage(gameResults);
        if ("ACK_GAME_RESULTS".equals(response)) {
            outStream.println("Server successfully received game results.");
        } else {
            outStream.println("Failed to send game results to server.");
        }
        outStream.println("Sent game results to server: " + gameResults);
    }
}
