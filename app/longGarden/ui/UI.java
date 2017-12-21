package org.fleen.bread.app.longGarden.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.fleen.bread.app.longGarden.LongGarden;

@SuppressWarnings("serial")
public class UI extends JFrame{

  /*
   * ################################
   * CONSTRUCTOR
   * ################################
   */
  
  public UI(final LongGarden lg){
    this.lg=lg;
    //
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        lg.exit();}});
    //
    setDefaultWindowBounds();
    initViewport();
    initControls();
    setVisible(true);
    requestFocus();}
  
  /*
   * ################################
   * LONG GARDEN
   * ################################
   */
  
  LongGarden lg;
  
  /*
   * ################################
   * VIEWPORT
   * 
   * controls
   *   esc=quit
   *   s=stop or start
   *   f=flip between fullscreen and nonfullscreen
   * 
   * ################################
   */
  
  private Viewport viewport;
  
  private void initViewport(){
    viewport=new Viewport(lg);
    viewport.setBorder(null);
    setContentPane(viewport);}
  
  /*
   * ################################
   * CONTROLS
   * 
   * controls
   *   x=exit this app
   *   s=stop or start the flow
   *   f=flip between fullscreen and nonfullscreen
   * 
   * ################################
   */
  
  static final char 
    CHAR_EXIT='x',
    CHAR_STOPSTART='s',
    CHAR_FLIP='f';
  
  private void initControls(){
    addKeyListener(new KeyAdapter() {
      public void keyTyped(KeyEvent e){
        char a=e.getKeyChar();
        if(a==CHAR_EXIT){
          lg.exit();
        }else if(a==CHAR_STOPSTART){
          if(lg.run)
            lg.stop();
          else
            lg.start();
        }else if(a==CHAR_FLIP){
          if(fullscreenmode)
            exitFullScreenMode();
          else
            enterFullScreenMode();
          }}});}
  
  /*
   * ################################
   * SCREEN MODE CONTROL
   * ################################
   */
  
  boolean fullscreenmode=false;
  private GraphicsDevice device;
  DisplayMode originaldm;
  
  private void enterFullScreenMode(){
    fullscreenmode=true;
    GraphicsEnvironment env = GraphicsEnvironment.
        getLocalGraphicsEnvironment();
    GraphicsDevice[] devices = env.getScreenDevices();
    device=devices[0];
    originaldm = device.getDisplayMode();
    boolean isfullscreenable = device.isFullScreenSupported();
    //get rid of the frame decor
    setVisible(false);
    dispose();
    setUndecorated(isfullscreenable);
    setVisible(true);
    //
    setResizable(!isfullscreenable);
    //get the focus so we can exit on keypress
    requestFocus();
    //do fullscreen
    if(isfullscreenable){
      device.setFullScreenWindow(this);
    //can't do fullscreen so do this other thing
    }else{
      setVisible(true);}
    validate();
    hideMouse();}
  
  private void exitFullScreenMode(){
    fullscreenmode=false;
    device.setDisplayMode(originaldm);
    //put the frame decor back
    setVisible(false);
    dispose();
    setUndecorated(false);
    setVisible(true);
    //
    setDefaultWindowBounds();
    //
    showMouse();
    //
    requestFocus();
    //
    setResizable(true);}
  
  /*
   * ++++++++++++++++++++++++++++++++
   * MOUSE
   * ++++++++++++++++++++++++++++++++
   */
  
  private Cursor originalmousecursor;
  
  private void hideMouse(){
    originalmousecursor=getContentPane().getCursor();
    BufferedImage cursorimage=new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
    //create blank cursor.
    Cursor blankCursor= 
      Toolkit.getDefaultToolkit().createCustomCursor(
      cursorimage, new Point(0, 0), "blank cursor");
    //blank cursor
    getContentPane().setCursor(blankCursor);}
  
  private void showMouse(){
    getContentPane().setCursor(originalmousecursor);}
  
  /*
  * ################################
  * NONFULLSCREEN WINDOW SIZE
  * ################################
  */
 
 private static final double WINDOWSIZEPROPORTION=0.6;
 
 void setDefaultWindowBounds(){
   setExtendedState(NORMAL);
   Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
   int 
     h=(int)(d.height*WINDOWSIZEPROPORTION),
     w=(int)(d.width*WINDOWSIZEPROPORTION),
     x=(d.width-w)/2,
     y=(d.height-h)/2;
   setBounds(x,y,w,h);
   validate();}

}
