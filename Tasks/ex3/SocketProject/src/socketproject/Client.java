package socketproject;

/* 
 java.net example (1.1): 
    Client-server connection-oriented communication.
    Client part.

   (written by Petr Grygarek, petr.grygarek@vsb.cz)
*/

import java.net.*;
import java.io.*;

public class Client {
 public static void main(String[] args) {

 /* if (args.length!=1) 
   { System.out.println("Syntax: Client servername"); System.exit(-1); }
*/
  Socket s=null;

  try {

   s=new Socket("pcfeib425t.vsb.cz",8000);
   System.out.println("Client: Set Input");
   BufferedReader sis = new BufferedReader(new InputStreamReader(System.in));
   BufferedWriter os = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
   BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));

   System.out.println("Client: Connection established.");
   System.out.println("Client: Server commands:\n 'exit' - close the connection, 'down' - downs the server");
   String output=null;
   String l;
   do {
    
    
       System.out.println("Client: Type a line to send to the server.");
        l=sis.readLine();
        os.write(l);
        os.newLine();
        os.flush();
        s.setSoTimeout(500);
        try 
    {
        System.out.println("Server: " + is.readLine()); 
    }
    catch(SocketTimeoutException e)
    {
     System.out.println("Timeout reached!!! ");
       
    }
   
   
   } while (!l.equalsIgnoreCase("exit") && !l.equalsIgnoreCase("down"));

   s.close();
  } catch (IOException e) { System.out.println(e); }
 }

}