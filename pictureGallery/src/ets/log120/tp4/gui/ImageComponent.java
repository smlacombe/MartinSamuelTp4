package ets.log120.tp4.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JComponent;

import ets.log120.tp4.app.Perspective;

public class ImageComponent extends JComponent {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public ImageComponent(int width, int height) {
		super();
		displaySize = new Dimension(width, height);
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	@Override
	public Dimension getMinimumSize() {
		return displaySize;
	}
	
	@Override
	public Dimension getMaximumSize() {
		return displaySize;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return displaySize;
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	public void setImage(BufferedImage newImage) {
		image = newImage;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		if(image != null) {
			g.drawImage(image,
					0, 0, displaySize.width, displaySize.height,
					0, 0, image.getWidth(),  image.getHeight(),
					null);
		} else {
			g.drawLine(0, 0, displaySize.width, displaySize.height);
			g.drawLine(0, displaySize.height, displaySize.width, 0);
		}
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private BufferedImage image;
	private Dimension displaySize;
}
