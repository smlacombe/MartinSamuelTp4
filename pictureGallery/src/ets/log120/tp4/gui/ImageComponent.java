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
	public ImageComponent(int width, int height) {
		displaySize = new Dimension(width, height);
	}	
	
	public void setImage(BufferedImage image, double zoom, Point position) {
		this.image = image;
		this.zoom = zoom;
		this.displayCenter = position;
		repaint();
	}
	
	public int getScaledWidth() {
		return (int) (displaySize.width * zoom);
	}
	
	public int getScaledHeight() {
		return (int) (displaySize.height * zoom);
	}
	
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

				int x = displayCenter.x - (displayImageWidth / 2);
				int y = displayCenter.y - (displayImageHeight / 2);

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
			g.drawLine(0, 0, getSize().width, getSize().height);
			g.drawLine(0, getSize().height, getSize().width, 0);
		}
	}
	
	private Dimension displaySize;
	private double zoom;
	private Point displayCenter;
	private BufferedImage image;
}
