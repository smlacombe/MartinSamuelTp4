package ets.log120.tp4.gui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

public class ImageComponent extends JComponent {
	public ImageComponent(int width, int height) {
		this.width  = width;
		this.height = height;
	}	
	
	public void setImage(BufferedImage image, double zoom, Point position) {
		this.image = image;
		this.zoom = zoom;
		this.position = position;
	}
	
	public int getScaledWidth() {
		return (int) (width*zoom);
	}
	
	public int getScaledHeight() {
		return (int) (height*zoom);
	}
	
	@Override
	public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.drawImage(image.getScaledInstance((int) getScaledWidth(), (int) getScaledHeight(), Image.SCALE_FAST), position.x, position.y, null);
	}
	
	private int height;
	private int width;
	private double zoom;
	private Point position;
	private BufferedImage image;
}