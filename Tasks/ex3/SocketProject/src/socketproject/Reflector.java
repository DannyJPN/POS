package socketproject;

/* 
 java.net example (2.1): 
    Datagram service communication.
    Receiver's side.

   (written by Petr Grygarek, petr.grygarek@vsb.cz)
*/

import java.net.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class Reflector {
 public static void main(String[] args) {

  try {
  DatagramSocket s=new DatagramSocket(8010);
  System.out.println("Reflector ready to reflect packets at " + InetAddress.getLocalHost());
  DatagramPacket p;
  String msg;
  int timestamp;

  do {
    p=new DatagramPacket(new byte[512],512);
    // musi se vytvaret vzdy znova, neumi prepsat jednou nastavenou delku
    s.receive(p);
    //msg = new String(p.getData(),0,p.getLength());
    msg = new String(p.getData(),4+4+1,p.getData().length-9);
    timestamp = p.getData()[4]*256*256*256;
    timestamp += p.getData()[5]*256*256;
    timestamp += p.getData()[6]*256;
    timestamp += p.getData()[7];
    
    
    System.out.println("Datagram arrived from " + p.getAddress() + " : " + p.getPort() + " > " + msg + "\n at " +new Date(new Timestamp(timestamp).getTime()));
    p.setData(msg.toUpperCase().getBytes());
    p.setLength(msg.length());
    //s.send(p);
  } while (!msg.equalsIgnoreCase("down"));

  s.close();
  System.out.println("Shutting the server down");
 } catch (IOException e) { System.out.println(e); }
 }
}