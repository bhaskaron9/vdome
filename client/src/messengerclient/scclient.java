/*
 * To change this license header, choose License Headers in ProjectProperties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messengerclient;

/**
 *
 * @author mah_sanskriti
 */

 import java.awt.image.*;
 import javax.swing.*;
 import java.io.*;
 import java.net.*;
 import java.awt .*;
 import java.awt.event.*;
 public class scclient extends Thread implements KeyListener, MouseListener
 {
 Socket s = null;
 ObjectInputStream ois = null;
 JLabel l = new JLabel();
 JWindow win = new JWindow();
 ImageIcon icon = new ImageIcon();
 JFrame fr = new JFrame();
 String add = null;
 public scclient() {
 try {
 add = JOptionPane.showInputDialog(null,"Server Address","127.0.0.1");
 s = new Socket(add, 2020);
 s.getOutputStream();
 ois = new ObjectInputStream(s.getInputStream());
 fr.setTitle("Displaying "+add+" port 2020");
 fr.addWindowListener(new WindowCloser());
 fr.getContentPane().add(l);
 l.setIcon(icon);
 Dimension d = fr.getToolkit().getScreenSize();
 fr.setSize(300*d.width/d.height,300);
 fr.addKeyListener(this);
 win.addMouseListener(this);
 fr.setVisible(true);
 this.start();
 }
 catch(Exception ex) { ex.printStackTrace(); }
 }

 public void keyPressed(KeyEvent ke) { }
 public void keyReleased(KeyEvent ke) {
 int code = ke.getKeyCode();
 if(code == KeyEvent.VK_F)
 switchDisplay();
else if(code == KeyEvent.VK_X) {
 try { s.close(); } catch(Exception ex) {}
 System.exit(0);
 }
 }
 public void keyTyped(KeyEvent ke) { }
 public void mouseClicked(java.awt.event.MouseEvent me) {
 switchDisplay();
 }
 public void mouseEntered(java.awt.event.MouseEvent me) {}
 public void mouseExited(java.awt.event.MouseEvent me) {}
 public void mousePressed(java.awt.event.MouseEvent me) {}
 public void mouseReleased(java.awt.event.MouseEvent me) {}
 private void switchDisplay() {
 if(fr.isVisible()) {
 fr.setVisible(false);
 fr.getContentPane().removeAll();
 win.getContentPane().removeAll();
 win.getContentPane().add(l);
 win.setSize(win.getToolkit().getScreenSize());
 win.setVisible(true);
 win.requestFocusInWindow();
 }
 else {
 win.setVisible(false);
 win.getContentPane().removeAll();
 fr.getContentPane().removeAll();
 fr.getContentPane().add(l);
 fr.setVisible(true);
fr.requestFocus();
 }
 }
 public void run() {
 Dimension d = null;
 BufferedImage i = null;
 while(true) {
 try {
 d = fr.getContentPane().getSize();
 icon = (ImageIcon)ois.readObject();
 if(d == null || icon == null) continue;
 if(d.width>0 && d.height>0 && (d.width != icon.getIconWidth() || d.height != icon.getIconHeight())) 
 icon.setImage(icon.getImage().getScaledInstance(d.width, d.height, i.SCALE_FAST));
 l.setIcon(icon);
 l.validate();
 fr.validate();
 } catch(Exception ex) { ex.printStackTrace(); }
 }
 }
 class WindowCloser extends WindowAdapter {
 public void windowClosing(WindowEvent we) {
 try { s.close(); } catch(Exception ex) { ex.printStackTrace(); }
 System.exit(0);
 }
 }
 public static void main(String arg[]) {
 new scclient();
 }
 }