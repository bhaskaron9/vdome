/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package messengerclient;

/**
 *
 * @author  prakhar imsherlock
 */
import java.net.*;
import java.io.*;
//ClientManager clientManager;

public class NewClass implements Runnable
{
    String fileName;
    Socket clientsock;
    String to;
    String from;
    ClientManager clientManager;
  NewClass(String file,Socket clientSock,ClientManager getclientManager,String To,String From)
  {
      fileName=file;
      to=To;
      from =From;
      clientManager=getclientManager;
      clientsock=clientSock;
      
    // create socket
    
    }

  public void run()
  {
        try {
            System.out.println("message for filesending about to be sent");
             //message = "bhaifilebhejo"; 
            //String message="bhaifilebhejo"+" "+to +" "+from;
            String message="bhaifilebhejoplease"+" "+to +" "+clientsock.getLocalAddress().toString();
      //      ClientManager.MessageSender(message);
           // clientManager.sendMessage sm = new sendmessage(message,clientsock);
            clientManager.sendMessage(message);
            ServerSocket servsock = new ServerSocket(13267);
            System.out.println("Waiting...");
    //        sendmessage();
            Socket sock = servsock.accept();
            System.out.println("Accepted connection : " + sock);
            // sendfile
            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(mybytearray, 0, mybytearray.length);
            OutputStream os = sock.getOutputStream();
            System.out.println("Sending...");
            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
            sock.close();
        } 
        catch (IOException ex)
        {
        }
      }
 // public void sendmessage(){
  
  //}
}
