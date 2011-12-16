package ets.log120.tp4.app;

import java.awt.Point;

public class PerspectiveUtil {
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
