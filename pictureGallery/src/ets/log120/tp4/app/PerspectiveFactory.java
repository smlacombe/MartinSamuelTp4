package ets.log120.tp4.app;

import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * Classe implémentant le patron « fabrique » permettant d'obtenir des instances pré-configurées de
 * la classe Perspective.
 */
public class PerspectiveFactory {

	/**
	 * Retourne une nouvelle perspective.
	 */
	public static Perspective makePerspective() {
		return makePerspective("", null);
	}
	
	/**
	 * Retourne une nouvelle perspective contenant une image définie.
	 */
	public static Perspective makePerspective(String imageName, BufferedImage image) {
		return makePerspective(imageName, image, 1);
	}

	/**
	 * Retourne une nouvelle perspective contenant une image et un zoom définis.
	 */
	public static Perspective makePerspective(String imageName, BufferedImage image, double zoom) {
		return makePerspective(imageName, image, zoom, new Point(0, 0));
	}

	/**
	 * Retourne une nouvelle perspective contenant une image, un zoom et une position définis.
	 */
	public static Perspective makePerspective(String imageName, BufferedImage image, double zoom, Point position) {
		Perspective p = new Perspective();
		p.setImage(imageName, image);
		p.setZoom(zoom);
		p.setPosition(position);
		return p;
	}
}
