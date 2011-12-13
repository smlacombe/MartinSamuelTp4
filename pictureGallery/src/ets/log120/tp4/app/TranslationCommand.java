package ets.log120.tp4.app;

/**
 * Classe définissant la commande de translation de la perspective (image)
 */
public class TranslationCommand implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public TranslationCommand(Perspective subject, int x, int y) {
		this.subject = subject;
		this.x = x;
		this.y = y;
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	/**
	 * Exécute la commande de translation
	 */
	@Override
	public void doCommand() {
		java.awt.Point current = subject.getPosition();
		double newX = current.getX() + x;
		double newY = current.getY() + y;
		subject.setPosition(new java.awt.Point((int) newX, (int) newY));
	}

	/**
	 * Défait la commande de translation
	 */
	@Override
	public void undoCommand() {
		java.awt.Point current = subject.getPosition();
		double newX = current.getX() - x;
		double newY = current.getY() - y;
		subject.setPosition(new java.awt.Point((int) newX, (int) newY));
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Perspective subject;
	private int x;
	private int y;
}
