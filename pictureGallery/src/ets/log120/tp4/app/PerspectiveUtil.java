package ets.log120.tp4.app;

public class PerspectiveUtil {

	/**
	 * Retourne le zoom nécessaire pour afficher complètement l'image de la perspective dans une
	 * zone dont la taille est données en paramètre.
	 */
	public static double getZoomToFitDisplay(Perspective p, int width, int height) {
		double bestZoom = BEST_ZOOM;

		if (height < width)
			bestZoom = (double) height / p.getImage().getHeight();
		else
			bestZoom = (double) width / p.getImage().getWidth();

		return Math.min(bestZoom, BEST_ZOOM);
	}

	private final static double BEST_ZOOM = 1.0;
}
