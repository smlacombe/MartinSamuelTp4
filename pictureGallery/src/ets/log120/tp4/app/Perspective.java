package ets.log120.tp4.app;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.Serializable;


/**
 * Élément modèle du patron MVC utilisé pour stocker les attributs des
 * transformations d'une image.
 */
public class Perspective implements Serializable {

	public ImageChangedEvent imageChanged       = new ImageChangedEvent();
	public ZoomChangedEvent zoomChanged         = new ZoomChangedEvent();
	public PositionChangedEvent positionChanged = new PositionChangedEvent();

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	/**
	 * Initialise la perspective.
	 */
	public Perspective() {
		this.imageName = "";
		this.image = null;
		this.zoom = DEFAULT_ZOOM;
		this.position = new Point(0, 0);
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	/**
	 * Retourne l'image de la perspective.
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Retourne le chemin d'accès de l'image (perspective).
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Retourne le zoom actuel de l'image (perspective).
	 */
	public double getZoom() {
		return zoom;
	}

	/**
	 * Retourne la position de l'image (perspective).
	 */
	public Point getPosition() {
		return position;
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	/**
	 * Définit l'image de la perspective.
	 */
	public void setImage(String imageName, BufferedImage image) {
		this.imageName = imageName;
		this.image = image;

		if (image != null)
			setPosition(new Point(image.getWidth() / 2, image.getHeight() / 2));
		
		imageChanged.setChanged();
		imageChanged.notifyObservers(this);
	}

	/**
	 * Définit le zoom de l'image (perspective).
	 */
	public void setZoom(double value) {
		if (value > 0) {
			zoom = value;
			zoomChanged.setChanged();
			zoomChanged.notifyObservers(this);
		}
	}

	/**
	 * Définit la position de l'image (perspective).
	 */
	public void setPosition(Point value) {
		position = value;
		positionChanged.setChanged();
		positionChanged.notifyObservers(this);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private transient BufferedImage image;
	private String imageName;
	private double zoom;
	private Point position;
	private static final long serialVersionUID = 1L;
	private static double DEFAULT_ZOOM = 1.0;
}
