/*
 * To change this license header, choutStreame License Headers in Project Properties.
 * To change this template file, choutStreame Tools | Templates
 * and open the template in the editor.
 */
package CentralServer;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 *
 * @author N0727751
 */
public class CentralServer {

   //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 9876;
    
    private static ArrayList<FieldData> fields;
    private static ArrayList<SensorData> sensors;
    
    public static void main(String args[]) throws IOException, ClassNotFoundException{
        //create the socket server object        
        server = new ServerSocket(port);
        
        fields = new ArrayList<>();
        sensors = new ArrayList<>();      
        
        //keep listens indefinitely until receives 'exit' call or program terminates
        while(true){
            System.out.println("Waiting for the client request");
            // wait for new client connection
            Socket socket = server.accept();
            
            // read from socket to ObjectInputStream object
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            
            // parse object received as string
            String message = (String) inStream.readObject();
            System.out.println("Message Received: " + message);
            
            //ProcessCommand(message);                      
            
            // create ObjectOutputStream object
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            
            // write return message to Socket
            outStream.writeObject("Copy: " + message);
            
            // close resources
            inStream.close();
            outStream.close();
            socket.close();            
        }
    }
    
    static String ProcessCommand(String args_) 
    {        
        String[] args = args_.split("*");
        String message = "";
        
        switch (args[0]) {
            
            case "SENSOR":
                // Sensor commands
                switch(args[1]) {
                    case "INIT":
                        AddSensor(args[2]);
                        break;
                    case "TEMP":
                        System.out.printf("Current temp from %s is %s celcius"
                                );
                        break;
                    case "HUMID":
                        break;
                    case "WIND":
                        break;
                    default:
                        break;
                }
                break;
                
            case "TERMINAL":
                // Terminal commands
                switch(args[1]) {
                    case "INIT":
                        break;
                    case "ADDFIELD":
                        break;
                    case "LISTFIELD":
                        // create a list of fields for the user
                        message = "FIELD LIST:\n";
                        message += "[ID]\t[NAME]\t[#SENSORS]";
                        for (FieldData field : fields) {                        
                            message += "[" + field.getID() + "]\t" ;
                            message += field.getName() + "\t";
                            message += field.getSensors().size() + "\n";                                                       
                        }
                        break;
                    default:
                        break;       
                }         
                break;
        }       
        
        return message;
    }   
    
    static void AddSensor(String fieldName_) { 
        sensors.add(new SensorData(sensors.size()));
        for (FieldData field : fields) {
            if (field.getName().equals(fieldName_)) {
                field.addSensor(sensors.get(sensors.size()));
                return;
            }            
        } 
        
        // else field not found >> create a new field
        AddField(fieldName_);        
    }   
    
    
    static void AddField(String fieldName_) {
        fields.add(new FieldData(fields.size(), fieldName_));        
    }
    
    static void UpdateSensor(int sensorID_, String type_, String data_) {
        // update sensor's saved data
        switch (type_) {
            case "TEMP":
                break;
            case "HUMID":
                break;
            case "WIND":
                break;
            default:
                break;
        }
    }
    
}

class SensorData {
    private int id;
        public SensorData(int id_) {
            id = id_;
        } 
    public int getID() { return id; }
}

class FieldData {
    // Field Data //
    // Stores data for "fields", which contain multiple sensors
    private ArrayList<SensorData> sensors;
    private int id;
    private String name; 
    
    public FieldData(int id_, String name_) {        
        sensors = new ArrayList<>();
        id = id_;
        name = name_;
    }
    
    public void addSensor(SensorData sensor_) {
        // add new sensor
        sensors.add(sensor_);
    }
    
    public void removeSensor(int sensorID_) {
        // find sensor and remove it from list
        for (SensorData sensor : sensors) {
            if (sensor.getID() == sensorID_) {
                // break to avoid nullptr
                sensors.remove(sensor);
                break;
            }
        }
    }
    
    // getters and setters
    public int getID() { return id; }
    public void setID(int id_) { id = id_; }
    
    public String getName() { return name; }
    public void setName(String name_) { name = name_;}
    
    public ArrayList<SensorData> getSensors() { return sensors; }
    
}