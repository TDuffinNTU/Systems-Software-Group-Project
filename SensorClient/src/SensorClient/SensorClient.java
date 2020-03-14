/*
 * To change this license header, choutStreame License Headers in Project Properties.
 * To change this template file, choutStreame Tools | Templates
 * and open the template in the editor.
 */
package SensorClient;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author N0727751
 */
public class SensorClient {
    static InetAddress host;
    static Socket socket;
    static ObjectOutputStream outStream;
    static ObjectInputStream inStream;
    static Random rand;
    static int temp;
    static int fieldID;
    
    static final String PARAMS = "Paramaters: <(Integer)FieldID>";
    static final String HELP = "";
    static final String ERROR = "ERROR:";
    static final int SENSOR_SLEEP = 5000;
    static final int SERVER_SLEEP = 100;
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {        
        host = InetAddress.getLocalHost();
        socket = null;
        outStream = null;
        inStream = null;
        temp = 0;
        fieldID = 0;
        rand = new Random(fieldID);        
        
        // set field ID, server will group together similar sensors
        if (args.length != 1) {
            System.err.println(PARAMS);
            System.err.println("Enter a field ID for this sensor");  
        // set field id if we have right no. args
        } else {
            try {                 
                fieldID = Integer.parseInt(args[0]);
                SendData("ADDSENSOR", args[0]);
            // catching errors ie. not using 
            } catch (Exception e) {
                System.err.println(e.getMessage());
                System.err.println(PARAMS);                
            }
        }          
        
        while(true) {
            run();
            Thread.sleep(SENSOR_SLEEP);
        }
    }
    
    public static void SendData(String command, String data) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {                
        // sanitize inputs and compile command message
        String msg = command.replace("*", "").toUpperCase() + "*" +
                data.replace("*", "").toUpperCase();        
        
        // establish socket connection to server
        socket = new Socket(host.getHostName(), 9876);
        
        // write to socket using ObjectOutputStream
        outStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");
        
        // write message to server's stream
        outStream.writeObject(msg);   
        
        // read the server response message        
        inStream = new ObjectInputStream(socket.getInputStream());
        String message = (String) inStream.readObject();
        System.out.println("Message: " + message);
        
        // close resources
        inStream.close();

        outStream.close();
        Thread.sleep(SERVER_SLEEP);          
    }
    
    public static void run() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException { 
                    // sensor stuff   
    }
}

//class Client {
//    InetAddress host;
//    Socket socket;
//    ObjectOutputStream outStream;
//    ObjectInputStream inStream;
//    Random rand;
//    int temp;
//    int id;
//    
//    public Client(int _id) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
//        host = InetAddress.getLocalHost();
//        socket = null;
//        outStream = null;
//        inStream = null;
//        temp = 0;
//        id = _id;
//        rand = new Random(id);
//        System.out.printf("New Client with ID[%d]\n", id);
//    }
//    
//    public void run ()throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException { 
//        temp = rand.nextInt(30);
//        //establish socket connection to server
//        socket = new Socket(host.getHostName(), 9876);
//        //write to socket using ObjectOutputStream
//        outStream = new ObjectOutputStream(socket.getOutputStream());
//        System.out.println("Sending request to Socket Server");
//
//        outStream.writeObject(""+ temp + " celcius\t["+id+"]");
//        //read the server response message
//        inStream = new ObjectInputStream(socket.getInputStream());
//        String message = (String) inStream.readObject();
//        System.out.println("Message: " + message);
//        //close resources
//        inStream.close();
//        outStream.close();
//        Thread.sleep(1000);  
//                
//    }
//}


