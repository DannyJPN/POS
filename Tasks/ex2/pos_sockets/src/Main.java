import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Thread clientThread = new Thread(() -> {
            try {
                Client client = new Client("158.196.135.66", 8000);
                String message;
                BufferedReader consoleInputStream = new BufferedReader(new InputStreamReader(System.in));

                do {
                    message = consoleInputStream.readLine();
                    client.sendMessage(message);
                } while (!message.equalsIgnoreCase("exit"));

                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread serverThread = new Thread(() -> {
            try {
                Server server = new Server(8010);
                server.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
        clientThread.start();

        serverThread.join();
        clientThread.join();
    }
}
