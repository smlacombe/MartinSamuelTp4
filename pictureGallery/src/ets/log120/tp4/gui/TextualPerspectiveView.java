package ets.log120.tp4.gui;

import java.awt.Color;
import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;

public class TextualPerspectiveView extends JPanel {
	
	public TextualPerspectiveView(Perspective perspective) {
		this.perspective = perspective;
		initView();		
	}
	
	private void initView() {
		initJTextArea();
		add (textView);
		
		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);
		
		updateText();
	}
	
	private void initJTextArea() {
		textView = new JTextArea(5, 20);
		textView.setEditable(false);
	}
	
	private void updateText() {
		textView.setText("Image: " + perspective.getImageName()
				+ "\nZoom: " + perspective.getZoom()
				+ "\nPosition: (" + perspective.getPosition().getX() + ", " + perspective.getPosition().getY() + ")");
	}
		
	
	private JTextArea textView;
	private Perspective perspective;
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateText();
		}
	}
}
