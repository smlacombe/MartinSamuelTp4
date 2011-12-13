package ets.log120.tp4.app;

import java.io.Serializable;

/**
 * Classe représentant l'événement déclenché lors d'un changement d'échelle
 * (zoom) de la perspective.
 */
public class ZoomChangedEvent extends java.util.Observable implements
		Serializable {
	private static final long serialVersionUID = 8688868019822192184L;

	@Override
	public void setChanged() {
		super.setChanged();
	}
}