package org.fleen.bread.app.simpleAutomata000.production;

import javax.swing.JFrame;

public class UI extends JFrame{
  
  private static final long serialVersionUID=2628897319187494442L;
  
  Test test;
  public Viewer viewer;

  public UI(Test test){
    this.test=test;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(60,60,800,800);
    viewer=new Viewer(this);
    setContentPane(viewer);
    setVisible(true);}

}
