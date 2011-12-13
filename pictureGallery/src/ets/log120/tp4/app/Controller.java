package ets.log120.tp4.app;

public class Controller {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Controller() {
		pastCommands = new ets.util.containers.Stack<Command>();
		futurCommands = new ets.util.containers.Stack<Command>();
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	public void performCommand(Command command) {
		command.doCommand();
		pastCommands.push(command);
		futurCommands.clear();
	}
	
	public void undo() {
		if (pastCommands.size() > 0) {
			Command command = pastCommands.top();
			pastCommands.pop();
			command.undoCommand();
			futurCommands.push(command);
		}
	}
	
	public void redo() {
		if (futurCommands.size() > 0) {
			Command command = futurCommands.top();
			futurCommands.pop();
			command.doCommand();
			pastCommands.push(command);
		}
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	ets.util.containers.Stack<Command> pastCommands;
	ets.util.containers.Stack<Command> futurCommands;
}
