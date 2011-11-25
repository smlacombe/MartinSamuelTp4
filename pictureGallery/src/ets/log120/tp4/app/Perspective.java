package ets.log120.tp4.app;

public class Perspective {
	
	public java.util.Observable imageUpdated = new java.util.Observable();
	public java.util.Observable zoomUpdated = new java.util.Observable();
	public java.util.Observable positionUpdated = new java.util.Observable();
	
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
	}
	
	public void setZoom(double value) {
		zoom = value;
	}
	
	public void setPosition(java.awt.Point value) {
		position = value;
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
