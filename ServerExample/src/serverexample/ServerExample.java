/*
 * To change this license header, choutStreame License Headers in Project Properties.
 * To change this template file, choutStreame Tools | Templates
 * and open the template in the editor.
 */
package serverexample;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author N0727751
 */
public class ServerExample {

   //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for the client request");
            //creating socket and waiting for client connection
            Socket socket = server.accept();
            //read from socket to ObjectInputStream object
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
            String message = (String) inStream.readObject();
            System.out.println("Message Received: " + message);
            //create ObjectOutputStream object
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
            outStream.writeObject("Current temp is " + message);
            //close resources
            inStream.close();
            outStream.close();
            socket.close();
            //terminate the server if client sends exit request
            if(message.equalsIgnoreCase("exit")) break;
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
    
}
