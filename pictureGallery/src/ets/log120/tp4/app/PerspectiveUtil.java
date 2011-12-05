package ets.log120.tp4.app;

public class PerspectiveUtil {
	public static double zoomToFit(Perspective p, int width, int height) {
		double bestZoom = 1.0;
		
		if (height < width)
			bestZoom = height / p.getImage().getHeight();
		else
			bestZoom = (double) width / p.getImage().getWidth();

		return Math.min(bestZoom, 1.0);
	}
}
