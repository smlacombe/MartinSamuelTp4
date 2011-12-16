package ets.log120.tp4.gui;

import java.util.Observable;

import javax.swing.JPanel;
import javax.swing.JTextArea;

import ets.log120.tp4.app.Perspective;

public class TextualPerspectiveView extends JPanel {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	/**
	 * Initialise la vue textuelle de la perspective.
	 */
	public TextualPerspectiveView() {
		super(new java.awt.GridBagLayout());
		setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

		addProperty(0, 0, "Nom :", nameField = new javax.swing.JTextField(15));
		addProperty(1, 0, "Zoom :", zoomField = new javax.swing.JTextField(15));
		addProperty(2, 0, "Position :",
				positionField = new javax.swing.JTextField(15));

		setMaximumSize(new java.awt.Dimension(getMaximumSize().width,
				getPreferredSize().height));
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	/**
	 * Modifie la perspective.
	 */
	public void setPerspective(Perspective p) {
		PerpectiveChanged listener = new PerpectiveChanged();
		p.imageChanged.addObserver(listener);
		p.zoomChanged.addObserver(listener);
		p.positionChanged.addObserver(listener);

		updateText(p);
	}

	/**
	 * Met à jour le texte de la vue.
	 */
	private void updateText(Perspective p) {
		nameField.setText(p.getImageName());
		zoomField.setText(percentage.format(p.getZoom()));
		positionField.setText("(" + p.getPosition().x + ";" + p.getPosition().y
				+ ")");
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	/**
	 * Régle les différents champs de la vue textuelle.
	 */
	private void addProperty(int x, int y, String labelText,
			javax.swing.JTextField field) {
		javax.swing.JLabel label = new javax.swing.JLabel(labelText,
				javax.swing.JLabel.TRAILING);
		label.setLabelFor(field);
		field.setEditable(false);

		final int MARGIN = 1;
		java.awt.GridBagConstraints labelConstraint = new java.awt.GridBagConstraints();
		labelConstraint.insets = new java.awt.Insets(MARGIN, MARGIN, MARGIN,
				MARGIN);
		labelConstraint.anchor = java.awt.GridBagConstraints.LINE_END;
		labelConstraint.gridx = x;
		labelConstraint.gridx = y;

		java.awt.GridBagConstraints fieldConstraint = new java.awt.GridBagConstraints();
		fieldConstraint.insets = new java.awt.Insets(MARGIN, MARGIN, MARGIN,
				MARGIN);
		fieldConstraint.anchor = java.awt.GridBagConstraints.LINE_START;
		fieldConstraint.gridx = x;
		fieldConstraint.gridx = y + 1;

		add(label, labelConstraint);
		add(field, fieldConstraint);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private javax.swing.JTextField nameField;
	private javax.swing.JTextField zoomField;
	private javax.swing.JTextField positionField;

	private java.text.DecimalFormat percentage = new java.text.DecimalFormat(
			"0.0#%");

	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------

	private class PerpectiveChanged implements java.util.Observer {
		/**
		 * Classe définissant le comportement lors de la mise à jour de la
		 * perspective.
		 */
		@Override
		public void update(Observable arg0, Object arg1) {
			updateText((Perspective) arg1);
		}
	}
}
