/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

import java.io.ObjectInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author mah_sanskriti
 */


public class NewClass1 {
    
    ClientManager clientManager;
    String from,to;
    ObjectInputStream input;
    ExecutorService clientExecutor;
    public NewClass1(String getto,String getfrom,ClientManager getClientManager)
    {
         from=getfrom;
        to=getto;
      //  initComponents();
       // setTitle(to);
        clientManager=getClientManager;
        clientExecutor=Executors.newCachedThreadPool();
    int idi=1;
    int six=6;
    
    while( idi <six)
    {
    String sFileName = "";
    
    if(sFileName != "")
    {
    	NewClass pFileSend = new NewClass(sFileName,clientManager.getsocket(),clientManager,to,from);
    	clientExecutor.execute(pFileSend);
    }
    idi++;
    }
    }
    
}
