/*
 * To change this license header, choutStreame License Headers in Project Properties.
 * To change this template file, choutStreame Tools | Templates
 * and open the template in the editor.
 */
package clientexample;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author N0727751
 */
public class ClientExample {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        
        ArrayList<Client> clients = new ArrayList<>();
        for (int i = 0; i < 5; i++) clients.add(new Client(i));      
        
        while(true){
            for (Client client : clients) client.run();            
        }
    }
}

class Client {
    InetAddress host;
    Socket socket;
    ObjectOutputStream outStream;
    ObjectInputStream inStream;
    Random rand;
    int temp;
    int id;
    
    public Client(int _id) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
        host = InetAddress.getLocalHost();
        socket = null;
        outStream = null;
        inStream = null;
        temp = 0;
        id = _id;
        rand = new Random(id);
        System.out.printf("New Client with ID[%d]\n", id);
    }
    
    public void run ()throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException { 
        temp = rand.nextInt(30);
        //establish socket connection to server
        socket = new Socket(host.getHostName(), 9876);
        //write to socket using ObjectOutputStream
        outStream = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("Sending request to Socket Server");

        outStream.writeObject(""+ temp + " celcius\t["+id+"]");
        //read the server response message
        inStream = new ObjectInputStream(socket.getInputStream());
        String message = (String) inStream.readObject();
        System.out.println("Message: " + message);
        //close resources
        inStream.close();
        outStream.close();
        Thread.sleep(1000);  
                
    }
}


