package ets.log120.tp4.app;

import java.awt.Point;

public class PerspectiveUtil {
	public static double getZoomToFitDisplay(Perspective p, int width, int height) {
		double bestZoom = 1.0;

		if (height < width)
			bestZoom = height / p.getImage().getHeight();
		else
			bestZoom = (double) width / p.getImage().getWidth();

		return Math.min(bestZoom, 1.0);
	}

	public static Point getTranslationToFitDisplay(Perspective p, int width, int height, Point potentialTranslation) {
		int halfScreenWidth = width / 2;
		int halfScreenHeight = height / 2;
		
		int imageWidth = (int) (p.getZoom() * p.getImage().getWidth());
		int imageHeight = (int) (p.getZoom()* p.getImage().getHeight());
		
		Point potentialPoint = new Point(
				p.getPosition().x + potentialTranslation.x,
				p.getPosition().y + potentialTranslation.y);
		
		if (potentialPoint.x  + halfScreenWidth > imageWidth) {
			potentialPoint.x = imageWidth - halfScreenWidth;
		} else if (potentialPoint.x  - halfScreenWidth < 0) {
			potentialPoint.x = halfScreenWidth;
		}
		
		if (potentialPoint.y  + halfScreenHeight > imageHeight) {
			potentialPoint.y = imageHeight - halfScreenHeight;
		} else if (potentialPoint.y  - halfScreenHeight < 0) {
			potentialPoint.y = halfScreenHeight;
		}
		
		return new Point(potentialPoint.x - p.getPosition().x, potentialPoint.y - p.getPosition().y);
	}
}
