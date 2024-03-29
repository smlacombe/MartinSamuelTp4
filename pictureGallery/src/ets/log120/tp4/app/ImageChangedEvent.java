package ets.log120.tp4.app;

import java.io.Serializable;

/**
 * Événement déclenché lors d'un changement d'image de la perspective.
 */
public class ImageChangedEvent extends java.util.Observable implements Serializable {

	/**
	 * Marquer l'élément observable comme changé.
	 */
	@Override
	public void setChanged() {
		super.setChanged();
	}
	
	private static final long serialVersionUID = -7936210509471100985L;
}
