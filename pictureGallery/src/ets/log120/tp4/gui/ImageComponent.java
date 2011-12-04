package ets.log120.tp4.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageComponent extends JComponent {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public ImageComponent(int width, int height) {
		super();
		setBackground(Color.BLACK);
		displaySize = new Dimension(width, height);
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	@Override
	public Dimension getSize() {
		return displaySize;
	}

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
		double bestZoom = 1.0;
		if (displaySize.height < displaySize.width)
			bestZoom = displaySize.height / newImage.getHeight();
		else
			bestZoom = (double) displaySize.width / newImage.getWidth();

		setImage(newImage, Math.min(bestZoom, 1.0));
	}

	public void setImage(BufferedImage newImage, double zoom) {
		this.image = newImage;
		this.zoom = zoom;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, displaySize.width, displaySize.height);

		if (image != null) {
			int imageWidth = (int) (zoom * image.getWidth());
			int imageHeight = (int) (zoom * image.getHeight());

			if (imageWidth > displaySize.width || imageHeight > displaySize.height) {
				// Image is larger than display area

				int displayImageWidth = (int) (displaySize.width / zoom);
				int displayImageHeight = (int) (displaySize.height / zoom);
				int imageCenterX = image.getWidth() / 2;
				int imageCenterY = image.getHeight() / 2;

				int x = imageCenterX - (displayImageWidth / 2);
				int y = imageCenterY - (displayImageHeight / 2);

				g.drawImage(image,
						0, 0, displaySize.width, displaySize.height,
						x, y, x + displayImageWidth, y + displayImageHeight,
						null);
			} else {
				// Image is smaller than display area

				int x = (displaySize.width - imageWidth) / 2;
				int y = (displaySize.height - imageHeight) / 2;

				g.drawImage(image,
						x, y, x + imageWidth, y + imageHeight,
						0, 0, image.getWidth(), image.getHeight(),
						null);
			}
		} else {
			g.setColor(Color.WHITE);
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
	private double zoom;
}
