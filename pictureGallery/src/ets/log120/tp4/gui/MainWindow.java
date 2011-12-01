package ets.log120.tp4.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

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
		setLayout(new FlowLayout());
		
		add(imageView = new JTextArea(5, 20));
		imageView.setEditable(false);
		
		add(button1 = new JButton("Image"));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				image.setImage("image2.png");
			}
		});
		
		add(button2 = new JButton("Zoom"));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(image, 0.5));
			}
		});
		
		add(button3 = new JButton("Position"));
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(image, 10, 10));
			}
		});
		
		add(undoButton = new JButton("Annuler"));
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		
		add(redoButton = new JButton("Refaire"));
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.redo();
			}
		});
		
		validate();
		
		PerpectiveChanged listener = new PerpectiveChanged();
		image = PerspectiveFactory.makePerspective();
		image.imageChanged.addObserver(listener);
		image.zoomChanged.addObserver(listener);
		image.positionChanged.addObserver(listener);
		image.setImage("image.png");
		
		setTitle(lang.getProperty("app.title"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 500);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
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
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private java.util.Properties lang;
	
	private JButton button1;
	private JButton button2;
	private JButton button3;
	private JButton undoButton;
	private JButton redoButton;
	private JTextArea imageView;
	private Perspective image;
	private Controller controller = new Controller();;
	
	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			imageView.setText("Image: " + image.getImage()
				+ "\nZoom: " + image.getZoom()
				+ "\nPosition: (" + image.getPosition().getX() + ", " + image.getPosition().getX() + ")");
		}
	}
}
