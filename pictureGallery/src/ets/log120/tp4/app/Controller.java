package ets.log120.tp4.app;

/**
 * Controlleur de l'application
 */
public class Controller {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public Controller() {
		pastCommands = new ets.util.containers.Stack<Command>();
		futurCommands = new ets.util.containers.Stack<Command>();
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	/**
	 * Exécute la commande passée en paramètre
	 */
	public void performCommand(Command command) {
		command.doCommand();
		pastCommands.push(command);
		futurCommands.clear();
	}

	/**
	 * Défait (annule) la dernière commande exécutée
	 */
	public void undo() {
		if (pastCommands.size() > 0) {
			Command command = pastCommands.top();
			pastCommands.pop();
			command.undoCommand();
			futurCommands.push(command);
		}
	}

	/**
	 * Refait la dernière commande annulée
	 */
	public void redo() {
		if (futurCommands.size() > 0) {
			Command command = futurCommands.top();
			futurCommands.pop();
			command.doCommand();
			pastCommands.push(command);
		}
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private ets.util.containers.Stack<Command> pastCommands;
	private ets.util.containers.Stack<Command> futurCommands;
}
