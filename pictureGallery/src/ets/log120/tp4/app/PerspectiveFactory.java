package ets.log120.tp4.app;

/**
 * Classe implémentant le patron « fabrique » permettant d'obtenir des instances pré-configurées de
 * la classe Perspective.
 */
public class PerspectiveFactory {

	/**
	 * Retourne une nouvelle perspective.
	 */
	public static Perspective makePerspective() {
		return makePerspective("");
	}
	
	/**
	 * Retourne une nouvelle perspective contenant une image définie.
	 */
	public static Perspective makePerspective(String image) {
		return makePerspective(image, 0.0);
	}

	/**
	 * Retourne une nouvelle perspective contenant une image et un zoom définis.
	 */
	public static Perspective makePerspective(String image, double zoom) {
		return makePerspective(image, zoom, new java.awt.Point(0, 0));
	}

	/**
	 * Retourne une nouvelle perspective contenant une image, un zoom et une position définis.
	 */
	public static Perspective makePerspective(String image, double zoom, java.awt.Point position) {
		Perspective p = new Perspective();
		p.setImage(image);
		p.setZoom(zoom);
		p.setPosition(position);
		return p;
	}
}
