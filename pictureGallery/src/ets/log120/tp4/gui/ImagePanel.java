package ets.log120.tp4.gui;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ets.log120.tp4.app.Perspective;

// http://stackoverflow.com/questions/299495/java-swing-how-to-add-an-image-to-a-jpanel
public class ImagePanel extends JPanel {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public ImagePanel(Perspective perspective, int width, int height) {
	       try {                
	    	  setPerspective(perspective);
	    	  this.width = width;
	    	  this.height = height;
	    	  
	          image = ImageIO.read(new File(perspective.getImage()));
	          //setSize(image.getWidth(), image.getHeight());
	          //this.setBackground(java.awt.Color.RED);
	       } catch (IOException ex) {
	            // handle exception...
	       }
	    }
	
	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	//pas nécessaire d'être publique, la perspective est référencée.
	private void setPerspective(Perspective perspective) {
		this.perspective = perspective;
	}

	// --------------------------------------------------
	// Méthodes(s)
	// --------------------------------------------------
	
	@Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.drawImage(image.getSubimage(perspective.getPosition().x, perspective.getPosition().y, (int) (image.getWidth() - perspective.getZoom()) , (int) (image.getHeight() - perspective.getZoom())), 0, 0, width, height, this);
    System.out.println("x " + perspective.getPosition().x + " y " + perspective.getPosition().y + " width " + ((int) (image.getWidth() - perspective.getZoom()) + " height " + (int) (image.getHeight() - perspective.getZoom())));
	
	}
 	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
		
	private java.awt.image.BufferedImage image;
	private Perspective perspective;
	private int height;
	private int width;
}
