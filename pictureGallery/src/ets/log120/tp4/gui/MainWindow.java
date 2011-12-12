package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import ets.log120.tp4.app.ChangeImageCommand;
import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ZoomCommand;
import ets.log120.tp4.app.TranslationCommand;

public class MainWindow extends JFrame {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public MainWindow() {
		super();
		initLang();
		setLayout(new BorderLayout());
	
		controller = new Controller();
		initPerspective();
		initTextView();
		initGraphicalView();
		initThumbnail();
		initMenuBar();
		addComponents();
				
		ThumbnailPerpectiveChanged listener = new ThumbnailPerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
				
		validate();
			
		setTitle(lang.getProperty("app.title"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	private void initPerspective() {
		perspective = PerspectiveFactory.makePerspective();
		
		try {                
			perspective.setImage("vincent.jpg", ImageIO.read(new File("vincent.jpg")));
	    } catch (IOException ex) {
	            
	    }
	}
	
	private void initTextView() {
		textView = new TextualPerspectiveView(perspective);
	}
	
	private void initGraphicalView() {
		graphicalView = new GraphicalPerspectiveView(controller, perspective);
	}
	
	/**
	 * Initialise le fichier de propriétés contenant le texte à afficher à l'utilisateur.
	 */
	private void initLang() {
		final String fileName = "lang.fr";
		
		try {
			lang = new java.util.Properties();
			lang.load(new java.io.FileInputStream("lang.fr"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Le fichier de langue " + fileName +" n'existe pas.");
			System.exit(ERROR);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Une erreur est survenue lors du chargement du fichier de langue");
		}
	}
	
	private void initThumbnail() {
		thumbnail = new ImageComponent(200,200);
		updateThumbnail();
	}
	private void updateThumbnail() {
		thumbnail.setImage(perspective.getImage(), 1, new Point(0,0));
	}
	private void addComponents() {
		add(graphicalView, BorderLayout.CENTER);
		textViewThumbnailBox = Box.createVerticalBox();
		textViewThumbnailBox.add(textView);
		textViewThumbnailBox.add(Box.createVerticalGlue());
		textViewThumbnailBox.add(thumbnail);
		add(textViewThumbnailBox, BorderLayout.WEST);
		
	}
	
	/**
	* Initialise la barre de menu.
	*/
	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(getFileMenu());
		menuBar.add(getImageMenu());
		setJMenuBar(menuBar);
	}
	
	/**
	 * Retourne le menu « Fichier » de l'application.
	 */
	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu(lang.getProperty("app.menu.file"));
		JMenuItem exitItem = new JMenuItem(lang.getProperty("app.menu.file.exit"));
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);
		
		return fileMenu;
	}
	
	/**
	 * Retourne le menu « Image » de l'application.
	 */
	private JMenu getImageMenu() {
		JMenu imageMenu = new JMenu(lang.getProperty("app.menu.image"));
		JMenuItem openImageItem = new JMenuItem(lang.getProperty("app.menu.image.open"));
		openImageItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new MyFilter());
			   			    
				int returnVal = fc.showOpenDialog(getContentPane());
			   
				if (returnVal == JFileChooser.APPROVE_OPTION) {
		        	String fileName = fc.getSelectedFile().getAbsolutePath();
		        	BufferedImage newImage = null;
		        	try {
		        		System.out.println(fileName);  
		        		newImage = ImageIO.read(new File(fileName));
		    	    } catch (IOException ex) {
		    	      System.out.println("fail");      
		    	    }
			        				        	
			        	controller.performCommand(new ChangeImageCommand(perspective,fileName,newImage));
			        } 
				else {
	
			        }
			   }
			});
		
		imageMenu.add(openImageItem);
		
		return imageMenu;
	}
	
	class MyFilter extends javax.swing.filechooser.FileFilter {
	    public boolean accept(File file) {
	        String filename = file.getName();
	        return filename.endsWith(".png") || filename.endsWith(".jpg") ||
	        filename.endsWith(".gif") || (file.isDirectory());
	    }
	    public String getDescription() {
	        return "*.png, *.jpg, *.gif";
	    }
	}
	
	private class ThumbnailPerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateThumbnail();
			thumbnail.repaint();
		}
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private java.util.Properties lang;
	private GraphicalPerspectiveView graphicalView;
	private TextualPerspectiveView textView;
	private Perspective perspective;
	private Controller controller;
	private ImageComponent thumbnail;
	private Box textViewThumbnailBox;
	private JMenu fileMenu;
	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------
	
    
}
