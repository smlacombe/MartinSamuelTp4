package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

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
		setLayout(new BorderLayout());
		
		JPanel panelLeft = new JPanel();
	    panelLeft.setLayout(new javax.swing.BoxLayout(panelLeft, javax.swing.BoxLayout.PAGE_AXIS));
		panelLeft.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    panelLeft.add(textualView = new PerspectiveTextualView());
	    panelLeft.add(javax.swing.Box.createVerticalGlue());
		panelLeft.add(graphicalView = new ImageComponent(THUMB_WIDTH, THUMB_HEIGHT));
		add(panelLeft,BorderLayout.WEST);
		
		initImagePerspective();
		
		Panel panelMiddle = new Panel();
		panelMiddle.setLayout(new BorderLayout());
		panelMiddle.add(getButtonPanel(), BorderLayout.NORTH);
		panelMiddle.add(getImagePanel(), BorderLayout.CENTER);
		
		hTranslate_min = 0;
		hTranslate_max = 0;
		horizontalTranslationScrollbar = new JScrollBar(JScrollBar.HORIZONTAL,0,0,hTranslate_min, hTranslate_max);
		horizontalTranslationScrollbar.addAdjustmentListener((new AdjustmentListener() {
		  public void adjustmentValueChanged(AdjustmentEvent ce) {
			  int currentValue = horizontalTranslationScrollbar.getValue();
			  
			  //if (currentValue != horizontalTranslationSlider.getMaximum());
			  	controller.performCommand(new TranslationCommand(imagePerspective, -(currentValue-h_oldValue), 0));
			  
			  h_oldValue = currentValue;
			  System.out.println("h_value " + currentValue);
			  System.out.println(currentValue != horizontalTranslationScrollbar.getMaximum());
		  }
		}));
		
		
		panelMiddle.add(horizontalTranslationScrollbar,BorderLayout.SOUTH);
						
		vTranslate_min = 0;
		vTranslate_max = 0;
		verticalTranslationScrollbar = new JScrollBar(JScrollBar.VERTICAL,0,0,vTranslate_min, vTranslate_max);
		verticalTranslationScrollbar.addAdjustmentListener(new AdjustmentListener() {
		  public void adjustmentValueChanged(AdjustmentEvent ce){
			  int currentValue = verticalTranslationScrollbar.getValue();
			  controller.performCommand(new TranslationCommand(imagePerspective, 0, -(currentValue-v_oldValue)));
			  v_oldValue = currentValue;
		  }
		  });
		panelMiddle.add(verticalTranslationScrollbar, BorderLayout.EAST);
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
		imgPanel = new PerspectiveGraphicalView(imagePerspective, 400, 400);
		
		imgPanel.setBackground(Color.RED);		
		// Permet d'utiliser la molette de la sourie pour agrandir ou réduire la taille de l'image
		imgPanel.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				controller.performCommand(new ZoomCommand(imagePerspective, -1 * event.getWheelRotation() * 0.01));
				updateScrollbarsMaxValue();
			}
		});
		
		return imgPanel;
	}
	
	private void updateScrollbarsMaxValue() {
		double zoom;
		zoom = imagePerspective.getZoom();
		int maxH;
		int maxV;
		int panelImageHeightDiff = imgPanel.getHeight() - imgPanel.getCurrentImageHeight();
		int panelImageWidthDiff = imgPanel.getWidth() - imgPanel.getCurrentImageWidth();
		
		if (panelImageWidthDiff >= 0)
			maxH = 0;
		else
			maxH = Math.abs(panelImageWidthDiff);

		if (panelImageHeightDiff >= 0)
			maxV = 0;
		else
			maxV = Math.abs(panelImageHeightDiff);
		
		horizontalTranslationScrollbar.setMaximum(zoom < 0 ? 0 : maxH);
		verticalTranslationScrollbar.setMaximum(zoom < 0 ? 0 : maxV);
		//System.out.println("maxh: " + maxH + " " + "maxv: " + maxV + " " + "valv: " + verticalTranslationSlider.getValue() + " " + "valh: " + horizontalTranslationSlider.getValue() + " ");

	}
	
	public String loadFile (Frame f, String title, String defDir, String fileType) {
	    FileDialog fd = new FileDialog(f, title, FileDialog.LOAD);
	    fd.setFile(fileType);
	    fd.setDirectory(defDir);
	    fd.setLocation(50, 50);
	    fd.show();
	    
	    return fd.getFile();
    }
	
	private void initImagePerspective() {
		thumbnailPerspective = PerspectiveFactory.makePerspective();
		thumbnailPerspective.imageChanged.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(thumbnailPerspective.getImage()));
				} catch (IOException e) {
					// Nothing to do
				} finally {
					graphicalView.setImage(image);	
				}
			}
		});
		
		thumbnailPerspective.setImage("vincent.jpg");
		
		
		PerpectiveChanged listener = new PerpectiveChanged();
		imagePerspective = PerspectiveFactory.makePerspective();
		imagePerspective.zoomChanged.addObserver(listener);
		imagePerspective.positionChanged.addObserver(listener);
		imagePerspective.setImage("vincent.jpg");
	}
	
	private JPanel getButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLUE);	
		panel.add(button1 = new JButton("Image"));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ChangeImageCommand(imagePerspective, "image" + ++n + ".png"));
			}
		});
		
		panel.add(button2 = new JButton("Zoom"));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(imagePerspective, 0.5));
			}
		});
		
		panel.add(buttonLeft = new JButton("←"));
		buttonLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective, -10, 0));
			}
		});
		
		panel.add(buttonRight = new JButton("→"));
		buttonRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective, 10, 0));
			}
		});
		
		panel.add(buttonUp = new JButton("↑"));
		buttonUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective, 0, -10));
			}
		});
		
		panel.add(buttonDown = new JButton("↓"));
		buttonDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new TranslationCommand(imagePerspective, 0, 10));
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
	
	private JScrollBar verticalTranslationScrollbar;
	private int vTranslate_max = 0;
	private int vTranslate_min = 0;
	private int v_oldValue = 0;
	
	private JScrollBar horizontalTranslationScrollbar;
	private int hTranslate_max = 0;
	private int hTranslate_min = 0;
	private int h_oldValue = 0;	
	
	private PerspectiveGraphicalView imgPanel;
	
	private Perspective imagePerspective;
	private Perspective thumbnailPerspective;
	
	private PerspectiveTextualView textualView;
	private ImageComponent graphicalView;
	
	private Controller controller = new Controller();
	
	private static final int THUMB_WIDTH = 256;
	private static final int THUMB_HEIGHT = 256;
	
	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------
	
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			textualView.update(imagePerspective);
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
