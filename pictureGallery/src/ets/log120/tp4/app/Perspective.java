package ets.log120.tp4.app;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Perspective implements Serializable {

	public ImageChangedEvent    imageChanged    = new ImageChangedEvent();
	public ZoomChangedEvent     zoomChanged     = new ZoomChangedEvent();
	public PositionChangedEvent positionChanged = new PositionChangedEvent();
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Perspective() {
		this.imageName = "";
		this.image = null;
		this.zoom = 1.0;
		this.position = new Point(0, 0);
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	public BufferedImage getImage() {
		return image;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public Point getPosition() {
		return position;
	}
	
	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	public void setImage(String imageName, BufferedImage image) {
		this.imageName = imageName;
		this.image = image;
		
		imageChanged.setChanged();
		imageChanged.notifyObservers(this);
		
		if (image != null)
			setPosition(new Point(0, 0));
	}
	
	public void setZoom(double value) {
		zoom = value;
		
		zoomChanged.setChanged();
		zoomChanged.notifyObservers(this);
	}
	
	public void setPosition(Point value) {
		position = value;
		
		positionChanged.setChanged();
		positionChanged.notifyObservers(this);
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private transient BufferedImage image;
	private String imageName;
	private double zoom;
	private Point position;
	private static final long serialVersionUID = 1L;
}
