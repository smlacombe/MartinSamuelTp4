package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Observable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JToolBar;

import ets.log120.tp4.app.ChangeImageCommand;
import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

public class GraphicalPerspectiveView extends JPanel {
	public GraphicalPerspectiveView(Controller controller, Perspective perspective) {
		this.controller = controller;
		this.perspective = perspective;
		
		initPanelAspect();

		add(toolBar = getToolBar(), BorderLayout.NORTH);
		add(image = getImageComponent(perspective), BorderLayout.CENTER);
		add(horizontalScrollBar = getHorizontalScrollBar(), BorderLayout.SOUTH);
		add(verticalScrollBar = getVerticalScrollBar(), BorderLayout.EAST);

		// Maintenant, la mise à jour des scrollBar sont gérées par la modification de la perspective
		PerpectiveChanged listener = new PerpectiveChanged();
		UpdateScrollBar scrollBarListener = new UpdateScrollBar();
		perspective.imageChanged.addObserver(listener);
		perspective.imageChanged.addObserver(scrollBarListener);
		perspective.zoomChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(scrollBarListener);
		perspective.positionChanged.addObserver(listener);
		perspective.positionChanged.addObserver(scrollBarListener);
		
		updateImage(perspective);
	}

	private ImageComponent getImageComponent(Perspective p) {
		ImageComponent component = new ImageComponent(perspective.getImage().getWidth(), perspective.getImage().getHeight());
		component.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				controller.performCommand(new ZoomCommand(perspective, -1 * event.getWheelRotation() * 0.01));
			}
		});
		return component;
	}

	private void initPanelAspect() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
	}

	private JToolBar getToolBar() {
		// Retourne un JToolBar plutôt que d'en modifier un directement
		initButton();
		
		JToolBar toolBar = new JToolBar();
		toolBar.add(zoomInButton);
		toolBar.add(zoomOutButton);
		toolBar.add(zoomOriginalButton);
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		
		return toolBar;
	}
	
	private JScrollBar getHorizontalScrollBar() {
		JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
		scrollBar.addAdjustmentListener(new ScrollBarHandler(new Functor() {
			@Override
			public void exec(int oldValue, int newValue) {
				controller.performCommand(new TranslationCommand(perspective, -(newValue - oldValue), 0));
			}
		}));
		return scrollBar;
	}
	
	private JScrollBar getVerticalScrollBar() {
		JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
		scrollBar.addAdjustmentListener(new ScrollBarHandler(new Functor() {
			@Override
			public void exec(int oldValue, int newValue) {
				controller.performCommand(new TranslationCommand(perspective, 0, -(newValue - oldValue)));
			}
		}));
		return scrollBar;
	}

	private void initButton() {
		// Les constructeurs des JButton reçoivent directement les ImageIcon
		
		zoomInButton = new JButton(new ImageIcon("icon/zoom-in.png"));
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 0.1));
			}
		});

		zoomOutButton = new JButton(new ImageIcon("icon/zoom-out.png"));
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, -0.1));
			}
		});

		zoomOriginalButton = new JButton(new ImageIcon("icon/zoom-original.png"));
		zoomOriginalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective,
						1 - perspective.getZoom()));
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
		// ce n'est pas à la perspective graphique de décider quand il faut rafraichir l'affichage
		image.setImage(p.getImage(), p.getZoom(), p.getPosition());
	}

	private Perspective perspective;
	private Controller controller;
	private ImageComponent image;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton zoomOriginalButton;
	private JScrollBar horizontalScrollBar;
	private JScrollBar verticalScrollBar;
	private JToolBar toolBar;

	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateImage((Perspective) arg1);
		}
	}
	
	private class ScrollBarHandler implements AdjustmentListener {
		public ScrollBarHandler(Functor f) {
			functor = f;
		}
		
		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			int currentValue = e.getValue();
			functor.exec(oldValue, currentValue);
			oldValue = currentValue;
		}
		
		Functor functor;
		int oldValue;
	}
	
	private interface Functor {
		void exec(int oldValue, int newValue);
	}
	
	private class UpdateScrollBar implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			int maxH;
			int maxV;
			int panelImageWidthDiff = getWidth() - image.getScaledWidth();
			int panelImageHeightDiff = getHeight() - image.getScaledHeight() - toolBar.getHeight();

			horizontalScrollBar.setAutoscrolls(true);
			if (panelImageWidthDiff >= 0) {
				maxH = 0;
				horizontalScrollBar.setVisible(false);
			} else {
				maxH = Math.abs(panelImageWidthDiff);
				horizontalScrollBar.setVisible(true);
			}

			if (panelImageHeightDiff >= 0) {
				maxV = 0;
				verticalScrollBar.setVisible(false);
			} else {
				maxV = Math.abs(panelImageHeightDiff);
				verticalScrollBar.setVisible(true);
			}

			horizontalScrollBar.setMaximum(Math.max(0, maxH)); // Math.max() remplace un opérateur ternaire
			verticalScrollBar.setMaximum(Math.max(0, maxV));
		}
	}
}