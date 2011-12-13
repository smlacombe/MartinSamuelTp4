package ets.log120.tp4.gui;

import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ets.log120.tp4.app.Perspective;

public class TextualPerspectiveView extends JPanel {
	
	public TextualPerspectiveView(Perspective perspective) {
		add(textView = getJTextArea());
		
		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);
		
		updateText(perspective);
	}
	
	private JTextArea getJTextArea() {
		JTextArea area = new JTextArea(5, 20);
		area.setEditable(false);
		area.setLineWrap(true);
		return area;
	}
	
	private void updateText(Perspective p) {
		textView.setText("Image: " + p.getImageName()
				+ "\nZoom: " + Math.round(p.getZoom()*100) + "%"
				+ "\nPosition: (" + p.getPosition().getX() + ", " + p.getPosition().getY() + ")");
	}
	
	private JTextArea textView;
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateText((Perspective) arg1);
		}
	}
}
