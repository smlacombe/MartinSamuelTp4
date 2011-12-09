package ets.log120.tp4.gui;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class ImageComponent extends Component {
	public ImageComponent(int width, int height) {
		this.width  = width;
		this.height = height;
	}	
	
	public void setImage(BufferedImage image, double zoom, Point position) {
		this.image = image;
		this.zoom = zoom;
		this.position = position;
	}
	
	public int getDrawedWidth() {
		return (int) (width*zoom);
	}
	
	public int getDrawedHeight() {
		return (int) (height*zoom);
	}
	
	public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
    	g2.translate(position.x, position.y);
    	g2.drawImage(image.getScaledInstance((int) (width*zoom), (int) (height*zoom), Image.SCALE_FAST), 0, 0, null);
    }
	
	private int height;
	private int width;
	private double zoom;
	private Point position;
	private BufferedImage image;
}