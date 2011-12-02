package ets.log120.tp4.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
		setLayout(new FlowLayout());
		
		add(imageView = new JTextArea(5, 20));
		imageView.setEditable(false);
		// Permet d'utiliser la molette de la sourie pour agrendir ou réduire la taille de l'image
		imageView.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				controller.performCommand(new ZoomCommand(image, -1 * event.getWheelRotation() * 0.5));
			}
		});
		
		add(button1 = new JButton("Image"));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ChangeImageCommand(image, "image" + ++n + ".png"));
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
		
		// Ajoute un menu contextuel au bouton « Annuler »
		undoMenu = new JPopupMenu();
		undoMenuItems = new LinkedList<JMenuItem>();
		
		for (int i = 1; i <= 10; ++i) {
			undoMenuItems.addLast(new JMenuItem("Item " + i));
			undoMenuItems.getLast().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					System.out.println("Item ? clicked.");
				}
			});
			undoMenu.add(undoMenuItems.getLast());
		}
		
		add(undoButton = new JButton("Annuler"));
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		undoButton.addMouseListener(new PopupListener(undoMenu));
		
		// Bouton « Refaire »
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
	
	int n = 0;
	private JButton button1;
	private JButton button2;
	private JButton button3;
	
	private JButton undoButton;
	private JButton redoButton;
	private JPopupMenu undoMenu;
	private LinkedList<JMenuItem> undoMenuItems;
	
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
	
	class PopupListener extends MouseAdapter {
        JPopupMenu popup;
 
        PopupListener(JPopupMenu popupMenu) {
            popup = popupMenu;
        }
 
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }
 
        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(),
                           e.getX(), e.getY());
            }
        }
    }
}
