/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static messengerclient.ClientConstant.*;

public class MessageRecever implements Runnable
{
    ObjectInputStream input;
    boolean keepListening=true;
    ClientListListener clientListListener;
    ClientWindowListener clientWindowListener;
    ClientManager clientManager;
    Socket clientSocket;
    ExecutorService clientExecutor;
    String valid;
   


    MessageRecever(Socket getClientSocket,ClientListListener getClientListListener ,ClientWindowListener getClientWindowListener,ClientManager getClientManager)
    {
        clientExecutor=Executors.newCachedThreadPool();
        clientManager=getClientManager;
        clientSocket=getClientSocket;
        try
        {
            input = new ObjectInputStream(getClientSocket.getInputStream());
        }
        catch (IOException ex)
        {}
        clientListListener=getClientListListener;
        clientWindowListener=getClientWindowListener;
    }
    public void run()
    {
        String message,name="",ips = "";
        while(keepListening)
        {
            try
            {
                message = (String) input.readObject();
                System.out.println("user is receiving "+ message);
                StringTokenizer tokens=new StringTokenizer(message);

                String header=tokens.nextToken();
                if(tokens.hasMoreTokens())
                    name=tokens.nextToken();
                
                if(tokens.hasMoreTokens())
                    valid = tokens.nextToken();
     
                	
                if(header.equalsIgnoreCase("login"))
                {
                    clientListListener.addToList(name,valid);
 
                }
                else if(valid.equalsIgnoreCase("audioreceive"))
                {
                    System.out.println("audio receiving");
                   // FileReceiver fr = new FileReceiver(clientSocket.getPort(),clientSocket.getInetAddress().toString());
                    //clientExecutor.execute(fr);
                    LVC2 lv2= new LVC2(clientSocket.getPort(),name);
                    clientExecutor.execute(lv2);
                    
 
                }
                else if(valid.equalsIgnoreCase("bhailena"))
                {
                    System.out.println("here we go ");
                   // FileReceiver fr = new FileReceiver(clientSocket.getPort(),clientSocket.getInetAddress().toString());
                    //clientExecutor.execute(fr);
                    FileReceiver fr = new FileReceiver(clientSocket.getPort(),name);
                    clientExecutor.execute(fr);
                    
 
                }
                else if(header.equalsIgnoreCase(DISCONNECT_STRING))
                {
                    clientListListener.removeFromList(name);
                }
                else if(header.equalsIgnoreCase("server"))
                {
                    clientWindowListener.closeWindow(message);
                }
                
                // Video 
                
               
                else
                {
                    clientWindowListener.openWindow(message);
                }
            }
            catch (IOException ex)
            {
                clientListListener.removeFromList(name);
            }
            catch (ClassNotFoundException ex)
            {

            }
        }
    }

    void stopListening()
    {
        keepListening=false;
    }
}
