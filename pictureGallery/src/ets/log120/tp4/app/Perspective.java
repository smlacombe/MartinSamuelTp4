package ets.log120.tp4.app;

public class Perspective {
	
	public ImageChangedEvent    imageUpdated    = new ImageChangedEvent();
	public ZoomChangedEvent     zoomUpdated     = new ZoomChangedEvent();
	public PositionChangedEvent positionUpdated = new PositionChangedEvent();
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Perspective(String image) {
		this.image = image;
		this.zoom = 1.0;
		this.position = new java.awt.Point(0, 0);
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	public String getImage() {
		return image;
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public java.awt.Point getPosition() {
		return position;
	}
	
	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	public void setImage(String value) {
		image = value;
		
		imageUpdated.setChanged();
		imageUpdated.notifyObservers();
	}
	
	public void setZoom(double value) {
		zoom = value;
		
		zoomUpdated.setChanged();
		zoomUpdated.notifyObservers();
	}
	
	public void setPosition(java.awt.Point value) {
		position = value;
		
		positionUpdated.setChanged();
		positionUpdated.notifyObservers();
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private String image;
	private double zoom;
	private java.awt.Point position;
}
