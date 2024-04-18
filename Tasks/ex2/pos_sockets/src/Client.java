import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private BufferedWriter socketOutputStream;

    public Client(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.socketOutputStream = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        System.out.println("Client: Connection established");
        System.out.println("Server commands:\n 'exit' - close the connection, 'down' - downs the server");
    }

    public void sendMessage(String message) {
        try {
            this.socketOutputStream.write(message);
            this.socketOutputStream.newLine();
            this.socketOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        this.socket.close();
    }
}