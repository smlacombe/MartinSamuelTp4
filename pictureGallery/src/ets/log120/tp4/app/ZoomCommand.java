package ets.log120.tp4.app;

public class ZoomCommand
		implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public ZoomCommand(Perspective subject, double augmentation) {
		if (augmentation < 0)
			throw new IllegalArgumentException("Augmentation cannot be negative.");
		
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