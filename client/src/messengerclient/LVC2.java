/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

/**
 * @author Bhaskar
 */
import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

public class LVC2 implements Runnable
{
String address;

    public LVC2(int portno,String getAddress) {
        System.out.println("LVC2 called ");
        address=getAddress.replace('/',' ').trim();
        System.out.println(address);
    }
    boolean stopCapture = false;
    ByteArrayOutputStream byteArrayOutputStream;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;
    AudioInputStream audioInputStream;
    BufferedOutputStream out = null;
    BufferedInputStream in = null;
    Socket sock = null;
    SourceDataLine sourceDataLine;

    public void run()
    {
        LVC2 lvcc = new LVC2(1987,address);
        lvcc.captureAudio("192.168.137.1");
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void captureAudio(String s)
    {
        try
        {

            sock = new Socket(address, 1987);
            out = new BufferedOutputStream(sock.getOutputStream());
            in = new BufferedInputStream(sock.getInputStream());
            Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
            audioFormat = getAudioFormat();
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            Mixer mixer = AudioSystem.getMixer(mixerInfo[3]);
            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            Thread captureThread = new CaptureThread();
            captureThread.start();
            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
            Thread playThread = new PlayThread();
            playThread.start();
        }
        catch (Exception e)
        {
            System.out.println(e);
            System.exit(0);
        }
    }


    class CaptureThread extends Thread
    {
        byte tempBuffer[] = new byte[10000];
        public void run()
        {
            byteArrayOutputStream = new ByteArrayOutputStream();
            stopCapture = false;
            try
            {
                while (!stopCapture)
                {
                    int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
                    out.write(tempBuffer);
                    if (cnt > 0)
                    {
                        byteArrayOutputStream.write(tempBuffer, 0, cnt);
                    }
                }
                byteArrayOutputStream.close();
            }
            catch (Exception e)
            {
                System.out.println(e);
                System.exit(0);
            }
        }
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

    class PlayThread extends Thread
    {
        byte tempBuffer[] = new byte[10000];
        public void run()
        {
            try
            {
                while (in.read(tempBuffer) != -1)
                {
                    sourceDataLine.write(tempBuffer, 0, 10000);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}