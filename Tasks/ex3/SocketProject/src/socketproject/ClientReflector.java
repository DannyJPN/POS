/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socketproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author cnap
 */
public class ClientReflector {

    public static void main(String[] args) {

        Thread serverthread = new Thread() {

            public void run() {

                try {
                    DatagramSocket s = new DatagramSocket(8010);
                    System.out.println("Reflector ready to reflect packets at " + InetAddress.getLocalHost());
                    DatagramPacket p;
                    String msg;
                    int timestamp;

                    do {
                        p = new DatagramPacket(new byte[512], 512);
                        // musi se vytvaret vzdy znova, neumi prepsat jednou nastavenou delku
                        s.receive(p);
                        //msg = new String(p.getData(),0,p.getLength());
                        msg = new String(p.getData(), 4 + 4 + 1, p.getData().length - 9);
                        timestamp = p.getData()[4] * 256 * 256 * 256;
                        timestamp += p.getData()[5] * 256 * 256;
                        timestamp += p.getData()[6] * 256;
                        timestamp += p.getData()[7];

                        System.out.println("Datagram arrived from " + p.getAddress() + " : " + p.getPort() + " > " + msg + " at " + new Date(new Timestamp(timestamp).getTime()));
                        p.setData(msg.toUpperCase().getBytes());
                        p.setLength(msg.length());
                        //s.send(p);
                    } while (!msg.equalsIgnoreCase("down"));

                    s.close();
                    System.out.println("Shutting the server down");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }

        };

        Thread clientthread = new Thread() {
            public void run() {
                /* if (args.length!=1) 
   { System.out.println("Syntax: Client servername"); System.exit(-1); }
                 */
                Socket s = null;

                try {

                    s = new Socket("pcfeib425t.vsb.cz", 8000);
                    System.out.println("Client: Set Input");
                    BufferedReader sis = new BufferedReader(new InputStreamReader(System.in));
                    BufferedWriter os = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));

                    System.out.println("Client: Connection established.");
                    System.out.println("Client: Server commands:\n 'exit' - close the connection, 'down' - downs the server");
                    String output = null;
                    String l;
                    do {

                        System.out.println("Client: Type a line to send to the server.");
                        l = sis.readLine();
                        os.write(l);
                        os.newLine();
                        os.flush();
                        s.setSoTimeout(500);
                        try {
                            System.out.println("Server: " + is.readLine());
                        } catch (SocketTimeoutException e) {
                            System.out.println("Timeout reached!!! ");

                        }

                    } while (!l.equalsIgnoreCase("exit") && !l.equalsIgnoreCase("down"));

                    s.close();
                } catch (IOException e) {
                    System.out.println(e);
               