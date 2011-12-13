package ets.log120.tp4.app;

/**
 * Classe définissant la commande de zoom de la perspective (image).
 */
public class ZoomCommand implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	/**
	 * Initialise la commande de zoom de la perspective (image).
	 */
	public ZoomCommand(Perspective subject, double augmentation) {
		this.subject = subject;
		this.augmentation = augmentation;
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	/**
	 * Exécute la commande de zoom.
	 */
	@Override
	public void doCommand() {
		if (subject.getZoom() + augmentation >= 0)
			subject.setZoom(subject.getZoom() + augmentation);
	}

	/**
	 * Défait la commande de zoom.
	 */
	@Override
	public void undoCommand() {
		subject.setZoom(subject.getZoom() - augmentation);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Perspective subject;
	private double augmentation;
}
