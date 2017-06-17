package main.java.kmlGridCreator.view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

abstract class MyMouseListener implements MouseListener{

	public abstract void mouseIsClicked(MouseEvent e);
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		mouseIsClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
		
	}
	
	

}
