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
import ets.log120.tp4.app.PerspectiveUtil;
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
		panelLeft.add(thumbnailGraphicalView = new ImageComponent(THUMB_WIDTH, THUMB_HEIGHT));
		add(panelLeft,BorderLayout.WEST);
		
		Panel panelMiddle = new Panel();
		panelMiddle.setLayout(new BorderLayout());
		//panelMiddle.add(getButtonPanel(), BorderLayout.NORTH);
		panelMiddle.add(getImagePanel(), BorderLayout.CENTER);
		
		initImagePerspective();
		/*
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
		*/
		add(panelMiddle, BorderLayout.CENTER);
				
		validate();
		
		setTitle(lang.getProperty("app.title"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		graphicalView.setFocusable(true);
	}
	
	// --------------------------------------------------
	// Méthode(s)
	// --------------------------------------------------
	
	private JPanel getImagePanel() {
		graphicalView = new PerspectiveGraphicalView(controller, 400, 400);
		
		graphicalView.setBackground(Color.RED);		
		
		return graphicalView;
	}
	
	private void initImagePerspective() {
		try {
			String str = "vincent.jpg";
			//String str = "petiteImage.jpg";
			//String str = "imageEnHauteur.jpg";
			//String str = "imageEnHauteur.jpg";
			
			BufferedImage image = ImageIO.read(new File("cplusplus.png"));
			PerpectiveChanged listener = new PerpectiveChanged();
			
			imagePerspective = PerspectiveFactory.makePerspective("cplusplus.png", image);
			imagePerspective.setZoom(PerspectiveUtil.zoomToFit(imagePerspective, 400, 400));
			imagePerspective.imageChanged.addObserver(listener);
			imagePerspective.imageChanged.addObserver(new Observer() {
				@Override
				public void update(Observable arg0, Object arg1) {
					thumbnailPerspective.setImage(imagePerspective.getImageName(), imagePerspective.getImage());
				}
			});
			imagePerspective.zoomChanged.addObserver(listener);
			imagePerspective.positionChanged.addObserver(listener);
			
			thumbnailPerspective = PerspectiveFactory.makePerspective();
			thumbnailPerspective.imageChanged.addObserver(new Observer() {
				@Override
				public void update(Observable arg0, Object arg1) {
					thumbnailGraphicalView.setImage(thumbnailPerspective.getImage());	
				}
			});
			thumbnailPerspective.setImage(imagePerspective.getImageName(), imagePerspective.getImage());
			thumbnailPerspective.setZoom(PerspectiveUtil.zoomToFit(thumbnailPerspective, THUMB_WIDTH, THUMB_HEIGHT));
			
			graphicalView.setPerspective(imagePerspective);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private Perspective imagePerspective;
	private Perspective thumbnailPerspective;
	
	private PerspectiveTextualView textualView;
	private PerspectiveGraphicalView graphicalView;
	private ImageComponent thumbnailGraphicalView;
	
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
			graphicalView.setPerspective(imagePerspective);
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
