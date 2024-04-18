/* 
 java.net example (1.2): 
    Client-server connection-oriented communication.
    Multithreaded server implementation.

   (written by Petr Grygarek, petr.grygarek@vsb.cz)
*/

import java.net.*;
import java.io.*;
import java.util.Date;

public class Accept implements Runnable {

  static ServerSocket s;
  static DatagramSocket rs;
  static int clients=0;

  protected Socket cs;
  int client;

  Accept(Socket cs) { 
    this.cs=cs; 
    this.client = ++clients;
    (new Thread(this)).start();  
  }
 
  public static void main(String[] args) {

    try {
    s=new ServerSocket(8000);
    System.out.println("Server listening at " + s.getLocalSocketAddress());
    rs = new DatagramSocket();
    Socket cso;

      do {
        cso=s.accept();
        Accept ms=new Accept(cso);
      }  while (true);
    } catch (IOException e) {
     if (e instanceof SocketException) System.out.println("Shutting down after last connection terminates!");// "==Conections won't be accepted anymore=="); 
      else System.out.println(e); 
    }
    //rs.close();
  }

  public void run() {
    String msg;
    byte [] data = new byte[512];
    try {
     BufferedReader is = new BufferedReader(new InputStreamReader(cs.getInputStream()));
     System.out.println("New connection #" +  client + " from " + cs.getInetAddress() + " : " + cs.getPort());
     do {
      msg = is.readLine();
      System.out.println("#" + client + ": " + msg + " (" + msg.length() + ')');
      sendReply(msg, cs.getInetAddress());
     }  while (!msg.equalsIgnoreCase("exit") && !msg.equalsIgnoreCase("server_down"));
     cs.close();
     System.out.println("Closing connection #" + client + " from "+ cs.getInetAddress()+" : "+cs.getPort());
     if (msg.equalsIgnoreCase("server_down")) {
	s.close();
     }
   } catch (Exception e) { 
     if (e instanceof NullPointerException) System.out.println("Client #" +client + " died prematurely!"); 
     else System.out.println(e); 
   }
  }

  protected void sendReply(String msg, InetAddress ia) throws IOException
  {
     ByteArrayOutputStream ba = new ByteArrayOutputStream();
     DataOutputStream data = new DataOutputStream(ba);
     if (msg.length() >= 255)
     {
	msg = msg.substring(0,254);
     }
     data.write(ia.getAddress(),0,4);
     data.writeInt((int)((new Date()).getTime() / 1000));
     data.writeByte((char)msg.length());
     data.writeBytes(msg);
     data.flush();
     byte [] pdata = ba.toByteArray(); 
     DatagramPacket p=new DatagramPacket(pdata, pdata.length, InetAddress.getByName("255.255.255.255"),8010);
     rs.send(p);
  }
}
