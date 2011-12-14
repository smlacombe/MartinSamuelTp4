package ets.log120.tp4.gui;

import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ets.log120.tp4.app.Perspective;

public class TextualPerspectiveView extends JPanel {
	
	public TextualPerspectiveView(Perspective perspective) {
		super(new java.awt.GridBagLayout());
		this.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		addProperty(0, 0, "Nom :", nameField = new javax.swing.JTextField(15));
		addProperty(1, 0, "Zoom :", zoomField = new javax.swing.JTextField(15));
		addProperty(2, 0, "Position :", positionField = new javax.swing.JTextField(15));
		
		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);
		
		setMaximumSize(new java.awt.Dimension(getMaximumSize().width, getPreferredSize().height));
	}
	
	private void updateText(Perspective p) {
		 nameField.setText(p.getImageName());
		 zoomField.setText(percentage.format(p.getZoom()));
		 positionField.setText(p.getPosition().toString());
	}
	
	 private void addProperty(int x, int y, String labelText, javax.swing.JTextField field) {
		javax.swing.JLabel label = new javax.swing.JLabel(labelText, javax.swing.JLabel.TRAILING);
		label.setLabelFor(field);
		field.setEditable(false);

		final int MARGIN = 1;
		java.awt.GridBagConstraints labelConstraint = new java.awt.GridBagConstraints();
		labelConstraint.insets = new java.awt.Insets(MARGIN, MARGIN, MARGIN, MARGIN);
		labelConstraint.anchor = java.awt.GridBagConstraints.LINE_END;
		labelConstraint.gridx = x;
		labelConstraint.gridx = y;

		java.awt.GridBagConstraints fieldConstraint = new java.awt.GridBagConstraints();
		fieldConstraint.insets = new java.awt.Insets(MARGIN, MARGIN, MARGIN, MARGIN);
		fieldConstraint.anchor = java.awt.GridBagConstraints.LINE_START;
		fieldConstraint.gridx = x;
		fieldConstraint.gridx = y + 1;

		add(label, labelConstraint);
		add(field, fieldConstraint);
	 }
	
	private javax.swing.JTextField nameField;
	private javax.swing.JTextField zoomField;
	private javax.swing.JTextField positionField;
	
	private java.text.DecimalFormat percentage = new java.text.DecimalFormat("0.0#%");
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateText((Perspective) arg1);
		}
	}
}
