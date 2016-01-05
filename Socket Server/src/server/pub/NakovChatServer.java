package server.pub;
import java.net.*;
import java.util.ArrayList;

import com.parse.Parse;

import java.io.*;
 
public class NakovChatServer
{
    public static final int LISTENING_PORT = 2002;
 private static ArrayList<ServerDispatcher> dispatchers;
 
 
    
    
    public static void main(String[] args)
    {
    	
    	dispatchers = new ArrayList<ServerDispatcher>();
        // Open server socket for listening
        ServerSocket serverSocket = null;
        try {
           serverSocket = new ServerSocket(LISTENING_PORT);
           System.out.println("NakovChatServer started on port " + LISTENING_PORT);
        } catch (IOException se) {
           System.err.println("Can not start listening on port " + LISTENING_PORT);
           se.printStackTrace();
           System.exit(-1);
        }
        
     
        
 
        // Start ServerDispatcher thread
      
       // serverDispatcher.start();
 
        // Accept and handle client connections
        while (true) {
           try {
        	   
        	   
        	   if(dispatchers.size()==0||dispatchers.get(dispatchers.size()-1).getPlayersCount()>=2){
        		   
        		   ServerDispatcher serverDispatcher = new ServerDispatcher();
        	        dispatchers.add(serverDispatcher);
        	        dispatchers.get(dispatchers.size()-1).start();
                      System.out.println("opened new room ");
        	   }
        	
        	  
        	   Socket socket = serverSocket.accept();
               ClientInfo clientInfo = new ClientInfo();
               clientInfo.mSocket = socket;
               ClientListener clientListener =
                   new ClientListener(clientInfo, dispatchers.get(dispatchers.size()-1));
               ClientSender clientSender =
                   new ClientSender(clientInfo, dispatchers.get(dispatchers.size()-1));
               
               if(dispatchers.get(dispatchers.size()-1).getPlayersCount()==1){
            	    clientInfo.mKind ="Guest"; 
               }
           
               clientInfo.mClientListener = clientListener;
               clientInfo.mClientSender = clientSender;
               clientListener.start();
               clientSender.start();
               dispatchers.get(dispatchers.size()-1).addClient(clientInfo);
              
               
               
           } catch (IOException ioe) {
               ioe.printStackTrace();
           }
        }
    }
 
}