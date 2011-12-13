package ets.log120.tp4.app;

/**
 * Interface à utiliser pour définir une commande.
 */
public interface Command {
	/**
	 * Exécute la commande.
	 */
	public void doCommand();
	/**
	 * Défait la commande.
	 */
	public void undoCommand();
}
