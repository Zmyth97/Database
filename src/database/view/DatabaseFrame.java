package database.view;

import javax.swing.JFrame;

import database.controller.DatabaseAppController;


public class DatabaseFrame extends JFrame
{
	private DatabasePanel basePanel;
	
	public DatabaseFrame(DatabaseAppController baseController){
		basePanel = new DatabasePanel(baseController);
		
		setupFrame();
	}
	
	private void setupFrame(){
		this.setContentPane(basePanel);
		this.setSize(1200,800);
		this.setResizable(false);
		setVisible(true);
	}

}
