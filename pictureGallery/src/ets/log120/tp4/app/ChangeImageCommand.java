package ets.log120.tp4.app;

import java.awt.image.BufferedImage;

/**
 * Commande permettant de changer l'image de la perspective.
 * @author Martin Desharnais
 *
 */
public class ChangeImageCommand implements Command {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public ChangeImageCommand(Perspective subject, String newImageName,
			BufferedImage newImage) {
		this.subject = subject;
		this.newImageName = newImageName;
		this.newImage = newImage;
	}

	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------

	/**
	 * Exécute la commande
	 */
	@Override
	public void doCommand() {
		oldImage = subject.getImage();
		oldImageName = subject.getImageName();
		subject.setImage(newImageName, newImage);
	}

	/**
	 * Défait la commande
	 */
	@Override
	public void undoCommand() {
		if (oldImageName != null && oldImage != null)
			subject.setImage(oldImageName, oldImage);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Perspective subject;
	private String newImageName;
	private String oldImageName;
	private BufferedImage newImage;
	private BufferedImage oldImage;
}
