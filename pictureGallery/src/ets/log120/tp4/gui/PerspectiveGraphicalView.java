package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ets.log120.tp4.app.Perspective;

public class PerspectiveGraphicalView extends JPanel {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public PerspectiveGraphicalView(Perspective perspective, int width, int height) {
		super(new BorderLayout());

		add(imageComponent = new ImageComponent(256, 256), BorderLayout.CENTER);
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	public void setPerspective(Perspective value) {
		perspective = value;
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(perspective.getImage()));
		} catch (IOException e) {
			// Nothing to do
		} finally {
			imageComponent.setImage(image, perspective.getZoom());
		}
	}

	public int getCurrentImageHeight() {
		return currentImageHeight;

	}

	public int getCurrentImageWidth() {
		return currentImageWidth;

	}

	// --------------------------------------------------
	// Méthodes(s)
	// --------------------------------------------------

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private int currentImageWidth;
	private int currentImageHeight;
	private Perspective perspective;

	private ImageComponent imageComponent;
}
