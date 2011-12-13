package ets.log120.tp4.app;

import java.io.Serializable;
/**
 * Événement déclenché lors d'un changement de position de la perspective.
 */
public class PositionChangedEvent extends java.util.Observable implements
		Serializable {
	private static final long serialVersionUID = 8798626482663687359L;

	/**
	 * Marquer l'élément observable comme changé
	 */
	@Override
	public void setChanged() {
		super.setChanged();
	}
}
