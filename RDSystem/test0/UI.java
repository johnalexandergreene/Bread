package org.fleen.bread.RDSystem.test0;

import javax.swing.JFrame;

public class UI extends JFrame {

	private static final long serialVersionUID = -2749846443106819716L;
	
	Test0 test;
  public Viewer viewer;

	public UI(Test0 test){
	  this.test=test;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50,50,1000,1000);
		viewer=new Viewer(this);
		setContentPane(viewer);
		setVisible(true);}
	
}
