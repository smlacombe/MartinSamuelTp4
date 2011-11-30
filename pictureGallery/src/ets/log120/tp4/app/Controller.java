package ets.log120.tp4.app;

public class Controller {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Controller() {
		pastCommands = new java.util.LinkedList<Command>();
		futurCommands = new java.util.LinkedList<Command>();
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	public void performCommand(Command command) {
		command.doCommand();
		pastCommands.addLast(command);
		futurCommands.clear();
	}
	
	public void undo() {
		if (pastCommands.size() > 0) {
			Command command = pastCommands.removeLast();
			command.undoCommand();
			futurCommands.addLast(command);
		}
	}
	
	public void redo() {
		if (futurCommands.size() > 0) {
			Command command = futurCommands.removeLast();
			command.doCommand();
			pastCommands.addLast(command);
		}
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	java.util.LinkedList<Command> pastCommands;
	java.util.LinkedList<Command> futurCommands;
}
