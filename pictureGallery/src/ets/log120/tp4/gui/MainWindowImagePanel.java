package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Panel;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ets.log120.tp4.app.ChangeImageCommand;
import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ZoomCommand;
import ets.log120.tp4.app.TranslationCommand;

public class MainWindowImagePanel extends JFrame {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public MainWindowImagePanel() {
		super();
		initLang();
		//setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));		
		setLayout(new BorderLayout());
		
	    Panel panelLeft = new Panel();
	    panelLeft.setLayout(new BorderLayout());
	    panelLeft.add(getJTextArea(),BorderLayout.NORTH);
		//box1.add(Box.createVerticalGlue());
	    panelLeft.add(getThumbPanel(),BorderLayout.CENTER);
		add(panelLeft,BorderLayout.WEST);
		
		Panel panelMiddle = new Panel();
		panelMiddle.setLayout(new BorderLayout());
		panelMiddle.add(getButtonPanel(), BorderLayout.NORTH);
		panelMiddle.add(getImagePanel(), BorderLayout.CENTER);
		
		hTranslate_min = 0;
		hTranslate_max = 0;
		horizontalTranslationSlider = new JSlider(JSlider.HORIZONTAL,
		hTranslate_min , hTranslate_max, 0);
		horizontalTranslationSlider.addChangeListener(new ChangeListener() {
		  public void stateChanged(ChangeEvent ce){
			  int currentValue = horizontalTranslationSlider.getValue();
			  controller.performCommand(new TranslationCommand(imagePerspective1, currentValue-h_oldValue, 0));
			  h_oldValue = currentValue;
		  }
		});
		
		
		panelMiddle.add(horizontalTranslationSlider,BorderLayout.SOUTH);
						
		vTranslate_min = 0;
		vTranslate_max = 0;
		verticalTranslationSlider = new JSlider(JSlider.VERTICAL,
		vTranslate_min , vTranslate_max, 0);
		verticalTranslationSlider.setInverted(true);
		verticalTranslationSlider.addChangeListener(new ChangeListener() {
		  public void stateChanged(ChangeEvent ce){
			  int currentValue = verticalTranslationSlider.getValue();
			  controller.performCommand(new TranslationCommand(imagePerspective1, 0, (currentValue-v_oldValue)));
			  v_oldValue = currentValue;
		  }
		  });
		panelMiddle.add(verticalTranslationSlider, BorderLayout.EAST);
		add(panelMiddle, BorderLayout.CENTER);
		
		addMenus();
				
		validate();
		
		setTitle(lang.getProperty("app.title"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		imgPanel.setFocusable(true);
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	private void addMenus() {
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
	}
	
	private JPanel getImagePanel() {
		imgPanel = new ImagePanel(imagePerspective1, 400, 400);
		
		imgPanel.setBackground(Color.RED);		
		// Permet d'utiliser la molette de la sourie pour agrandir ou réduire la taille de l'image
		imgPanel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				controller.performCommand(new ZoomCommand(imagePerspective1, -1 * event.getWheelRotation() * 4));
				updateSlidersMaxValues();
			}
		});
		
		return imgPanel;
	}
	
	private void updateSlidersMaxValues() {
		int zoom;
		zoom = (int) imagePerspective1.getZoom();
		horizontalTranslationSlider.setMaximum(zoom < 0 ? 0 : zoom);
		verticalTranslationSlider.setMaximum(zoom < 0 ? 0 : zoom);
	}
	
	private void initImagePerspective() {
		PerpectiveChanged listener = new PerpectiveChanged();
		imagePerspective1 = PerspectiveFactory.makePerspective();
		imagePerspective1.imageChanged.addObserver(listener);
		imagePerspective1.zoomChanged.addObserver(listener);
		imagePerspective1.positionChanged.addObserver(listener);
		imagePerspective1.setImage("vincent.jpg");
	}
	
	private JPanel getThumbPanel() {
		initImagePerspective();
		imageThumbPerspective = PerspectiveFactory.makePerspective(imagePerspective1.getImage());
	
		ImagePanel imgThumb = new ImagePanel(imageThumbPerspective, THUMB_WIDTH, THUMB_HEIGHT);
		imgThumb.setAlignmentX(Component.LEFT_ALIGNMENT);
		imgThumb.setPreferredSize(new Dimension(THUMB_WIDTH,THUMB_HEIGHT));
		imgThumb.setMaximumSize(imgThumb.getPreferredSize());
		imgThumb.setMinimumSize(imgThumb.getPreferredSize());
		imgThumb.setSize(new Dimension(THUMB_WIDTH,THUMB_HEIGHT));
		return imgThumb;
	}
	
	private JPanel getJTextArea() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);	
		panel.add(imageView = new JTextArea(5, 20));
		imageView.setEditable(false);
		return panel;
	}
	
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);	
		panel.add(button1 = new JButton("Image"));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ChangeImageCommand(imagePerspective1, "image" + ++n + ".png"));
			}
		});
		
		panel.add(button2 = new JButton("Zoom"));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(imagePerspective1, 0.5));
			}
		});
		
		panel.add(buttonLeft = new JButton("←"));
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective1, -10, 0));
			}
		});
		
		panel.add(buttonRight = new JButton("→"));
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective1, 10, 0));
			}
		});
		
		panel.add(buttonUp = new JButton("↑"));
		buttonUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective1, 0, -10));
			}
		});
		
		panel.add(buttonDown = new JButton("↓"));
		buttonDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective1, 0, 10));
			}
		});

		panel.add(undoButton = new JButton("Annuler"));
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});
		undoButton.addMouseListener(new PopupListener(undoMenu));
		
		// Bouton « Refaire »
		panel.add(redoButton = new JButton("Refaire"));
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.redo();
			}
		});
		panel.setAlignmentX(JButton.RIGHT_ALIGNMENT);
	
		panel.setMaximumSize(panel.getPreferredSize());
		return panel;
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
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private java.util.Properties lang;
	
	int n = 0;
	private JButton button1;
	private JButton button2;
	private JButton buttonLeft;
	private JButton buttonRight;
	private JButton buttonUp;
	private JButton buttonDown;
	
	private JButton undoButton;
	private JButton redoButton;
	private JPopupMenu undoMenu;
	private LinkedList<JMenuItem> undoMenuItems;
	
	private JSlider verticalTranslationSlider;
	private int vTranslate_max = 0;
	private int vTranslate_min = 0;
	private int v_oldValue = 0;
	
	private JSlider horizontalTranslationSlider;
	private int hTranslate_max = 0;
	private int hTranslate_min = 0;
	private int h_oldValue = 0;	
	
	private ImagePanel imgPanel;
	
	private JTextArea imageView;
	private Perspective imagePerspective1;
	private Perspective imageThumbPerspective;
	
	private Controller controller = new Controller();
	
	private static final int THUMB_WIDTH = 200;
	private static final int THUMB_HEIGHT = 200;
	
	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			imageView.setText("Image: " + imagePerspective1.getImage()
				+ "\nZoom: " + imagePerspective1.getZoom()
				+ "\nPosition: (" + imagePerspective1.getPosition().getX() + ", " + imagePerspective1.getPosition().getY() + ")");
			validate();
			repaint();
			if (imgPanel != null)
				imgPanel.repaint();
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
