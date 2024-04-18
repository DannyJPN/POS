import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Date;

public class Server implements Runnable {
    private DatagramSocket socket;

    public Server(int port) throws SocketException {
        this.socket = new DatagramSocket(port);
        System.out.println("Server: Connection established");
    }

    @Override
    public void run() {
        try {
            String message;

            do {
                DatagramPacket packet = new DatagramPacket(new byte[512], 512);
                this.socket.receive(packet);
                message = new String(packet.getData(), 9, this.parseMessageLength(packet.getData()));

                System.out.println(this.formatOutput(packet));
            } while (!message.equalsIgnoreCase("down"));

            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatOutput(DatagramPacket packet) {

        return "[" + this.parseMessageTime(packet.getData()) + "] " +
                "Datagram from " + this.parseMessageIpAddress(packet.getData()) + ":" + packet.getPort() +
                " > " + new String(packet.getData(), 9, this.parseMessageLength(packet.getData())) +
                " (" + this.parseMessageLength(packet.getData()) + ")";
    }

    private String parseMessageIpAddress(byte[] bytes) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            result.append((int) bytes[i] & 0xFF);
            result.append(".");
        }

        return result.substring(0, result.length() - 1);
    }

    private Date parseMessageTime(byte[] bytes) {
        byte[] time = new byte[4];

        System.arraycopy(bytes, 4, time, 0, 4);

        ByteBuffer wrapped = ByteBuffer.wrap(time);
        int timestamp = wrapped.getInt();

        return new Date((long) timestamp * 1000);
    }

    private int parseMessageLength(byte[] bytes) {
        return bytes[8];
    }

    private void close() {
        this.socket.close();
    }
}
