package org.fleen.bread.hCellSystem.test0;

import javax.swing.JFrame;

public class UI extends JFrame {

	private static final long serialVersionUID = -2749846443106819716L;
	
	HCellTest_ForsythiaThing test;
  public Viewer viewer;

	public UI(HCellTest_ForsythiaThing test){
	  this.test=test;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50,50,1600,800);
		viewer=new Viewer(this);
		setContentPane(viewer);
		setVisible(true);}
	
}
