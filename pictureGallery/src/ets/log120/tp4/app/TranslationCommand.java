package ets.log120.tp4.app;

public class TranslationCommand
	implements Command {

		// --------------------------------------------------
		// Constructeur(s)
		// --------------------------------------------------
		
		public TranslationCommand(Perspective subject, int x, int y) {
			this.subject = subject;
			this.x = x;
			this.y = y;
		}
		
		// --------------------------------------------------
		// Accesseur(s)
		// --------------------------------------------------
		
		// --------------------------------------------------
		// Mutateur(s)
		// --------------------------------------------------
		
		// --------------------------------------------------
		// Méthode(s)
		// --------------------------------------------------
		
		@Override
		public void doCommand() {
			java.awt.Point current = subject.getPosition();
			double newX = current.getX() + x;
			double newY = current.getY() + y;
			subject.getPosition().setLocation(newX, newY);
		}

		@Override
		public void undoCommand() {
			java.awt.Point current = subject.getPosition();
			double newX = current.getX() - x;
			double newY = current.getY() - y;
			subject.getPosition().setLocation(newX, newY);
		}
		
		// --------------------------------------------------
		// Attribut(s)
		// --------------------------------------------------

		private Perspective subject;
		private int x;
		private int y;
}
