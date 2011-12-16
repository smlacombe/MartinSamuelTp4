package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToolBar;

import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveUtil;
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

public class GraphicalPerspectiveView extends JPanel {
	public GraphicalPerspectiveView(Controller controller) {
		super(new BorderLayout());
		setBackground(Color.BLACK);
		
		this.controller = controller;

		add(getToolBar(), BorderLayout.NORTH);
		add(imageComponent = getImageComponent(), BorderLayout.CENTER);
	}

	private ImageComponent getImageComponent() {
		ImageComponent component = new ImageComponent(525, 512);
		
		DragAndDropListener listener = new DragAndDropListener();
		component.addMouseListener(listener);
		component.addMouseMotionListener(listener);
		
		component.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective, -1 * event.getWheelRotation() * 0.05));
				}
			}
		});
		return component;
	}

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

	private void initButton() {
		zoomInButton = new JButton(new ImageIcon("icon/zoom-in.png"));
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective, 0.1));
				}
			}
		});

		zoomOutButton = new JButton(new ImageIcon("icon/zoom-out.png"));
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective, -0.1));
				}
			}
		});

		zoomOriginalButton = new JButton(new ImageIcon("icon/zoom-original.png"));
		zoomOriginalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					controller.performCommand(new ZoomCommand(perspective, 1 - perspective.getZoom()));
				}
			}
		});
		
		zoomFitBestButton = new JButton(new ImageIcon("icon/zoom-fit-best.png"));
		zoomFitBestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (perspective != null) {
					double zoom = PerspectiveUtil.getZoomToFitDisplay(perspective, imageComponent.getSize().width, imageComponent.getSize().height);
					controller.performCommand(new ZoomCommand(perspective, zoom - perspective.getZoom()));
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

	private void updateImage(Perspective p) {
		imageComponent.setImage(p.getImage(), p.getZoom(), p.getPosition());
	}
	
	public void setPerspective(Perspective p) {
		perspective = p;
		
		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);
		
		updateImage(perspective);
	}

	private Perspective perspective;
	private Controller controller;
	private ImageComponent imageComponent;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton zoomOriginalButton;
	private JButton zoomFitBestButton;

	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			Perspective p = (Perspective) arg1;
			updateImage(p);
		}
	}
		
	private class DragAndDropListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				buttonPressed = true;
				pressedPosition = e.getPoint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if ((buttonPressed && e.getButton() == MouseEvent.BUTTON1) && !(perspective==null)) {
				buttonPressed = false;
		
				int horizontalTranslation = (int) ((pressedPosition.x - e.getPoint().x) / perspective.getZoom());
				int verticalTranslation = (int) ((pressedPosition.y - e.getPoint().y) / perspective.getZoom());
				
				controller.performCommand(new TranslationCommand(
						perspective,
						horizontalTranslation,
						verticalTranslation));
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if ((buttonPressed) && !(perspective==null)) {
				int horizontalTranslation = (int) ((pressedPosition.x - e.getPoint().x) / perspective.getZoom());
				int verticalTranslation = (int) ((pressedPosition.y - e.getPoint().y) / perspective.getZoom());

				imageComponent.setImage(perspective.getImage(), perspective.getZoom(), new Point(
						perspective.getPosition().x + horizontalTranslation,
						perspective.getPosition().y + verticalTranslation));
			}
		}

		private boolean buttonPressed;
		private Point pressedPosition;
	}
}
