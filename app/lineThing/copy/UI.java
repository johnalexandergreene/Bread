package org.fleen.bread.app.lineThing.copy;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class UI extends JFrame{

  public ImagePanel imagepanel;
  
  public UI(Main main){
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(30,30,1000,1000);
    imagepanel=new ImagePanel();
    setContentPane(imagepanel);
    setVisible(true);}

}
