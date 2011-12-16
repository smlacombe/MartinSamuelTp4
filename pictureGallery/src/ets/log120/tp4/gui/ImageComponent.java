package ets.log120.tp4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageComponent extends JComponent {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	/**
	 * Initialise une nouvelle image.
	 */
	public ImageComponent(int width, int height) {
		super();
		displaySize = new Dimension(width, height);
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	/**
	 * Retourne la dimension d'affichage de l'image.
	 */
	@Override
	public Dimension getSize() {
		return displaySize;
	}

	/**
	 * Retourne la dimension minimum d'affichage de l'image.
	 */
	@Override
	public Dimension getMinimumSize() {
		return displaySize;
	}

	/**
	 * Retourne la dimension maximum d'affichage de l'image.
	 */
	@Override
	public Dimension getMaximumSize() {
		return displaySize;
	}

	/**
	 * Retourne la dimension préférée d'affichage de l'image.
	 */
	@Override
	public Dimension getPreferredSize() {
		return displaySize;
	}

	/**
	 * Retourne la largeur à l'échelle de l'image.
	 */
	public int getScaledWidth() {
		if (image != null)
			return (int) (zoom * image.getWidth());
		else
			return 0;
	}

	/**
	 * Retourne la hauteur à l'échelle de l'image.
	 */
	public int getScaledHeight() {
		if (image != null)
			return (int) (zoom * image.getHeight());
		else
			return 0;
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	/**
	 * Définie l'image et ses propriétés.
	 */
	public void setImage(BufferedImage image, double zoom, Point position) {
		this.image = image;
		this.zoom = zoom;
		this.displayCenter = position;
		repaint();
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	/**
	 * Méthode de dessin de l'image.
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getSize().width, getSize().height);

		if (image != null) {
			int imageWidth = getScaledWidth();
			int imageHeight = getScaledHeight();

			if (imageWidth > getSize().width || imageHeight > getSize().height) {
				// Image is larger than display area

				int displayImageWidth = (int) (getSize().width / zoom);
				int displayImageHeight = (int) (getSize().height / zoom);
				int x = displayCenter.x - (displayImageWidth / 2);
				int y = displayCenter.y - (displayImageHeight / 2);

				g.drawImage(image, 0, 0, getSize().width, getSize().height, x,
						y, x + displayImageWidth, y + displayImageHeight, null);
			} else {
				// Image is smaller than display area

				int x = (getSize().width - imageWidth) / 2;
				int y = (getSize().height - imageHeight) / 2;

				g.drawImage(image, x, y, x + imageWidth, y + imageHeight, 0, 0,
						image.getWidth(), image.getHeight(), null);
			}
		} else {
			g.setColor(Color.WHITE);
			g.drawLine(0, 0, getSize().width, getSize().height);
			g.drawLine(0, getSize().height, getSize().width, 0);
		}
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Dimension displaySize;
	private double zoom;
	private Point displayCenter;
	private BufferedImage image;
}
