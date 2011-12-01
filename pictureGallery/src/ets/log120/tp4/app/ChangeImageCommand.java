package ets.log120.tp4.app;

public class ChangeImageCommand
		implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public ChangeImageCommand(Perspective subject, String newImage) {
		this.subject = subject;
		this.newImage = newImage;
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
		oldImage = subject.getImage();
		subject.setImage(newImage);
	}

	@Override
	public void undoCommand() {
		if (oldImage != null)
			subject.setImage(oldImage);
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Perspective subject;
	private String newImage;
	private String oldImage;
}
