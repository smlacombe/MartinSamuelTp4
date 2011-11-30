package ets.log120.tp4.app;

public class Perspective {
	
	public ImageChangedEvent    imageChanged    = new ImageChangedEvent();
	public ZoomChangedEvent     zoomChanged     = new ZoomChangedEvent();
	public PositionChangedEvent positionChanged = new PositionChangedEvent();
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Perspective() {
		this.image = "";
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
		
		imageChanged.setChanged();
		imageChanged.notifyObservers();
	}
	
	public void setZoom(double value) {
		zoom = value;
		
		zoomChanged.setChanged();
		zoomChanged.notifyObservers();
	}
	
	public void setPosition(java.awt.Point value) {
		position = value;
		
		positionChanged.setChanged();
		positionChanged.notifyObservers();
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
