package ets.log120.tp4.app;

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
		Perspective p = new Perspective();
		p.setImage(imageName, image);
		return p;
	}
}
