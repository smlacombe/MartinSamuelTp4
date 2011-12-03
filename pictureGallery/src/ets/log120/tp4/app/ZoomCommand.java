package ets.log120.tp4.app;

public class ZoomCommand
		implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public ZoomCommand(Perspective subject, double augmentation) {
		this.subject = subject;
		this.augmentation = augmentation;
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
		if (subject.getZoom() + augmentation >= 0)
			subject.setZoom(subject.getZoom() + augmentation);
	}

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
