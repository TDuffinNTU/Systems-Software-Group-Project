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
    
    static final String PARAMS = "Paramaters: <(Integer)FieldID> <(Integer)Longitude> <Integer>";
    static final String HELP = "";
    static final String ERROR = "ERROR:";
    static int SENSOR_SLEEP;
    static final int SERVER_SLEEP = 100;
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {        
        host = InetAddress.getLocalHost();
        socket = null;
        outStream = null;
        inStream = null;
        temp = 0;
        fieldID = 0;
        rand = new Random(fieldID); 
        
        // random wait to simulate sensors sending data out of sync
        SENSOR_SLEEP = rand.nextInt(1000);
        
        // set field ID, server will group together similar sensors
        if (args.length != 1) {
            System.err.println(PARAMS);
            System.err.println("Enter a field ID for this sensor");  
        // set field id if we have right no. args
        } else {
            try {                
                SendData("INIT", args[0]);
            
            } catch (Exception e) {
                // error catching/parsing
                System.err.println(e.getMessage());
                System.err.println(PARAMS);    
                System.exit(0);
            }
        }          
        
        while(true) {
            run();            
            Thread.sleep(SENSOR_SLEEP);            
        }
    }
    
    public static void SendData(String command, String data) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {                
        // SANITIZE INPUTS and compile command message using * token
        // creates message like so: SENSOR*COMMAND*ARG*ARG*ARG... etc.
        String msg = "SENSOR*" + command.replace("*", "").toUpperCase() + "*" +
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
        System.out.println("Message from server: " + message);
        
        // close resources
        inStream.close();
        outStream.close();
        
        // halt thread for a bit to allow other processes to run
        Thread.sleep(SERVER_SLEEP);          
    }
    
    public static void run() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException { 
        // sensor stuff, for now just sends random temp data to simulate this
        SendData("TEMP", 
                "[PID]"+ProcessHandle.current().pid()
                + "//" + rand.nextInt(40));
    }
}




