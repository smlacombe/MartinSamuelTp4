package ets.log120.tp4.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ets.log120.tp4.app.Perspective;

// http://stackoverflow.com/questions/299495/java-swing-how-to-add-an-image-to-a-jpanel
public class PerspectiveGraphicalView extends JPanel {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public PerspectiveGraphicalView(Perspective perspective, int width, int height) {
	       try {                
	    	  setPerspective(perspective);
	    	  this.originalWidth = width;
	    	  this.originalHeight = height;
	    	 
	    	 // this.setPreferredSize(new Dimension(width,height));
	    	  //this.setMaximumSize(this.getPreferredSize());
	    	 // this.setMinimumSize(this.getPreferredSize());
	    	 // this.setSize(new Dimension(width,height));
	    	  
	          originalImage = ImageIO.read(new File(perspective.getImage()));
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
	
	public int getCurrentImageHeight() {
		return currentImageHeight;
		
	}
	
	public int getCurrentImageWidth() {
		return currentImageWidth;
		
	}

	// --------------------------------------------------
	// Méthodes(s)
	// --------------------------------------------------
	
	@Override
    public void paintComponent(Graphics g) {
    	Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
    	
    	int positionToShowX = perspective.getPosition().x;
    	int positionToShowY = perspective.getPosition().y;
    	currentImageWidth = (int) (originalImage.getWidth() * perspective.getZoom());
    	currentImageHeight = (int) (originalImage.getHeight() * perspective.getZoom()); 
       	//AffineTransform transformationZoom = AffineTransform.getScaleInstance(perspective.getZoom(), perspective.getZoom());
        
    	//g2.translate(perspective.getPosition().x, perspective.getPosition().y);
    	//g2.setClip(0,0, 100, 100);
    	
    	//Utils.getScaledInstance(image, width, height, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR , false);
    	/*
    	if (perspective.getZoom() != 0)
    		g2.drawImage(image, transformationZoom, this);
    	else
    		g2.drawImage(image, 0, 0, this);
    	*/
    	g.translate(positionToShowX, positionToShowY);
    	g.drawImage(originalImage.getScaledInstance(currentImageWidth, currentImageHeight, Image.SCALE_FAST), 0, 0, null);
    	//g.drawImage(image.getSubimage(positionToShowX, positionToShowY, subImageWidth, subImageHeight), 0, 0, width, height, this);
	}
 	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
		
	private java.awt.image.BufferedImage originalImage;
	private int currentImageWidth;
	private int currentImageHeight;
	private Perspective perspective;
	private int originalHeight;
	private int originalWidth;
}
