/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;


public class LVC1 implements Runnable
{
    Socket clientsock;
    String to;
    String from;
    ClientManager clientManager;
    
    
    public LVC1(Socket clientSock,ClientManager getclientManager,String To,String From) {
        to=To;
      from =From;
      clientManager=getclientManager;
      clientsock=clientSock;
    }

    ServerSocket MyService;
    Socket clientSocket = null;
    BufferedInputStream input;
    TargetDataLine targetDataLine;
    BufferedOutputStream out;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    SourceDataLine sourceDataLine;      
    byte tempBuffer[] = new byte[10000];
    
    public void run() 
    {
            try
        {
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo =  new DataLine.Info(SourceDataLine.class,audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            String message="audio"+" "+to +" "+clientsock.getLocalAddress().toString();
            clientManager.sendMessage(message);
            MyService = new ServerSocket(1987);
            clientSocket = MyService.accept();
            System.out.println("Accepted");
            ScaptureAudio();
            input = new BufferedInputStream(clientSocket.getInputStream());    
            out = new BufferedOutputStream(clientSocket.getOutputStream());
            while(input.read(tempBuffer)!=-1)
            {            
                sourceDataLine.write(tempBuffer,0,10000);
            }
            
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (LineUnavailableException ex) {
            Logger.getLogger(LVC1.class.getName()).log(Level.SEVERE, null, ex);
        }
             throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private AudioFormat getAudioFormat()
    {
        float sampleRate = 8000.0F;          
        int sampleSizeInBits = 16;           
        int channels = 1;            
        boolean signed = true;            
        boolean bigEndian = false;         
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

  
    private void ScaptureAudio()
    {
        try
        {
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            Thread ScaptureThread = new SCaptureThread();
            ScaptureThread.start();        
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(0);
        }
    }


    class SCaptureThread extends Thread
    {
        byte tempBuffer[] = new byte[10000];
        public void run()
        {            
            try
            {
                while (true)
                {
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    out.write(tempBuffer);                
                }
                
            }
            catch (Exception e)
            {
                System.out.println(e);
                System.exit(0);
            }
        }
    }

}