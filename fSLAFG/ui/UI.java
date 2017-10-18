package org.fleen.bread.fSLAFG.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.fleen.bread.fSLAFG.Generator;

public class UI extends JFrame {

	private static final long serialVersionUID = -2749846443106819716L;
	
	Generator generator;
  public Viewer viewer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI(null,0,0);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UI(Generator generator,int w,int h){
	  this.generator=generator;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50,50,1600,800);
		viewer=new Viewer(generator);
		setContentPane(viewer);
		setVisible(true);}
	
}
