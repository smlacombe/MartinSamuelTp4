package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveUtil;
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

/**
 * Vue graphique de l'application.
 */
public class GraphicalPerspectiveView extends JPanel {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	/**
	 * Initialise une nouvelle vue graphique
	 */
	public GraphicalPerspectiveView(Controller controller) {
		super(new BorderLayout());
		setBackground(Color.BLACK);

		this.controller = controller;

		add(getToolBar(), BorderLayout.NORTH);
		add(imageComponent = getImageComponent(), BorderLayout.CENTER);
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	/**
	 * Retourne un nouveau composant image
	 */
	private ImageComponent getImageComponent() {
		ImageComponent component = new ImageComponent(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

		DragAndDropListener listener = new DragAndDropListener();
		component.addMouseListener(listener);
		component.addMouseMotionListener(listener);

		component.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective, -1
							* event.getWheelRotation() * MOUSE_ZOOM_INCREMENT));
				}
			}
		});
		return component;
	}

	/**
	 * Retourne la barre d'outils de l'application
	 */
	private JToolBar getToolBar() {
		initButton();

		JToolBar toolBar = new JToolBar();
		toolBar.add(zoomInButton);
		toolBar.add(zoomOutButton);
		toolBar.add(zoomOriginalButton);
		toolBar.add(zoomFitBestButton);
		toolBar.add(undoButton);
		toolBar.add(redoButton);

		return toolBar;
	}

	// --------------------------------------------------
	// Méthodes
	// --------------------------------------------------

	/**
	 * Initialise les boutons de la barres d'outils.
	 */
	private void initButton() {
		zoomInButton = new JButton(new ImageIcon("icon/zoom-in.png"));
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller
							.performCommand(new ZoomCommand(perspective, BUTTON_ZOOM_INCREMENT));
				}
			}
		});

		zoomOutButton = new JButton(new ImageIcon("icon/zoom-out.png"));
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller
							.performCommand(new ZoomCommand(perspective, -BUTTON_ZOOM_INCREMENT));
				}
			}
		});

		zoomOriginalButton = new JButton(
				new ImageIcon("icon/zoom-original.png"));
		zoomOriginalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective,
							1 - perspective.getZoom()));
				}
			}
		});

		zoomFitBestButton = new JButton(new ImageIcon("icon/zoom-fit-best.png"));
		zoomFitBestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					double zoom = PerspectiveUtil.getZoomToFitDisplay(
							perspective, imageComponent.getSize().width,
							imageComponent.getSize().height);
					controller.performCommand(new ZoomCommand(perspective, zoom
							- perspective.getZoom()));
				}
			}
		});

		undoButton = new JButton(new ImageIcon("icon/undo.png"));
		undoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
			}
		});

		redoButton = new JButton(new ImageIcon("icon/redo.png"));
		redoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.redo();
			}
		});
	}

	/**
	 * Met à jour l'image affichée selon la perspective.
	 */
	private void updateImage(Perspective p) {
		imageComponent.setImage(p.getImage(), p.getZoom(), p.getPosition());
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	/**
	 * Définit la perspective et tous ces observateurs.
	 */
	public void setPerspective(Perspective p) {
		perspective = p;

		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);

		updateImage(perspective);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private Perspective perspective;
	private Controller controller;
	private ImageComponent imageComponent;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton zoomOriginalButton;
	private JButton zoomFitBestButton;
	private static final int DEFAULT_IMAGE_WIDTH = 525;
	private static final int DEFAULT_IMAGE_HEIGHT = 512;
	private static final double MOUSE_ZOOM_INCREMENT = 0.05;
	private static final double BUTTON_ZOOM_INCREMENT = 0.1;
	
	/**
	 * Classe servant à représenter l'observateur de la perspective
	 */
	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			Perspective p = (Perspective) arg1;
			updateImage(p);
		}
	}

	/**
	 * Écouteur des événements de souris portés sur l'image.
	 */
	private class DragAndDropListener extends MouseAdapter {
		// --------------------------------------------------
		// Méthode(s)
		// --------------------------------------------------

		/**
		 * Méthode définissant l'action exécutée lors de l'événement de clique
		 * de la souris sur l'image.
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				buttonPressed = true;
				pressedPosition = e.getPoint();
			}
		}

		/**
		 * Méthode définissant l'action exécutée lors de l'événement de
		 * relâchement de la souris sur l'image.
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			if ((buttonPressed && e.getButton() == MouseEvent.BUTTON1)
					&& !(perspective == null)
					&& (imageComponent.getScaledWidth() > getSize().width || imageComponent
							.getScaledHeight() > getSize().height)) {
				buttonPressed = false;

				int horizontalTranslation = (int) ((pressedPosition.x - e
						.getPoint().x) / perspective.getZoom());
				int verticalTranslation = (int) ((pressedPosition.y - e
						.getPoint().y) / perspective.getZoom());

				controller.performCommand(new TranslationCommand(perspective,
						horizontalTranslation, verticalTranslation));
			}
		}

		/**
		 * Méthode définissant l'action exécutée lors de l'événement de souris
		 * draguée sur l'image.
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if ((buttonPressed) && !(perspective == null)) {
				int horizontalTranslation = (int) ((pressedPosition.x - e
						.getPoint().x) / perspective.getZoom());
				int verticalTranslation = (int) ((pressedPosition.y - e
						.getPoint().y) / perspective.getZoom());

				imageComponent.setImage(
						perspective.getImage(),
						perspective.getZoom(),
						new Point(perspective.getPosition().x
								+ horizontalTranslation, perspective
								.getPosition().y + verticalTranslation));
			}
		}

		// --------------------------------------------------
		// Attribut(s)
		// --------------------------------------------------

		private boolean buttonPressed;
		private Point pressedPosition;
	}
}
