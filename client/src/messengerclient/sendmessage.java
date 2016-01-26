/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

import java.io.IOException;
import java.io.ObjectOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import static messengerclient.ClientConstant.*;
import messengerclient.ServerSettings.*;


/**
 *
 * @author mah_sanskriti
 */
class sendmessage implements Runnable
    {
        Socket cs;
        ObjectOutputStream output;
        boolean flageoutput;
        
        String message;
        public sendmessage(String getMessage,Socket clientSocket)
        {
            cs = clientSocket;
            
            if(flageoutput)
            {
                try
                {
                    output = new ObjectOutputStream(clientSocket.getOutputStream());
                    output.flush();
                    flageoutput=false;
                }
                catch (IOException ex)
                {
                }
            }
            message=getMessage;
            System.out.println("user is sending   "+ message);
        }
        
        public void run()
        {
            try
            {
                output.writeObject(message);
                output.flush();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

