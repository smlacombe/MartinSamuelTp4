package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

public class GraphicalPerspectiveView extends JPanel {
	public GraphicalPerspectiveView(Controller controller) {
		this.controller = controller;
		
		initPanelAspect();

		add(getToolBar(), BorderLayout.NORTH);
		add(image = getImageComponent(), BorderLayout.CENTER);
		add(horizontalScrollBar = getHorizontalScrollBar(), BorderLayout.SOUTH);
		add(verticalScrollBar = getVerticalScrollBar(), BorderLayout.EAST);
	}

	private ImageComponent getImageComponent() {
		ImageComponent component = new ImageComponent(525, 512);
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

	private void initPanelAspect() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
	}

	private JToolBar getToolBar() {
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
		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (perspective != null && !isLoadingScrollBar) {
					int variation = (int) ((e.getValue() - horizontalScrollBarOldValue) / perspective.getZoom());
					controller.performCommand(new TranslationCommand(perspective, variation, 0));
					horizontalScrollBarOldValue = e.getValue();
				}
			}	
		});
		return scrollBar;
	}
	
	private JScrollBar getVerticalScrollBar() {
		JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);
		scrollBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (perspective != null && !isLoadingScrollBar) {
					controller.performCommand(new TranslationCommand(perspective, 0, e.getValue() - verticalScrollBarOldValue));
					verticalScrollBarOldValue = e.getValue();
				}
			}
		});
		return scrollBar;
	}

	private void initButton() {
		// Les constructeurs des JButton reçoivent directement les ImageIcon
		
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
		image.setImage(p.getImage(), p.getZoom(), p.getPosition());
	}
	
	public void setPerspective(Perspective p) {
		perspective = p;
		PerpectiveChanged listener = new PerpectiveChanged();
		UpdateScrollBar scrollBarListener = new UpdateScrollBar();
		perspective.imageChanged.addObserver(listener);
		perspective.imageChanged.addObserver(scrollBarListener);
		perspective.zoomChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(scrollBarListener);
		perspective.positionChanged.addObserver(listener);
		
		updateImage(perspective);
	}

	private Perspective perspective;
	private Controller controller;
	private ImageComponent image;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton zoomOriginalButton;
	private int horizontalScrollBarOldValue;
	private int verticalScrollBarOldValue;
	private JScrollBar horizontalScrollBar;
	private JScrollBar verticalScrollBar;
	private boolean isLoadingScrollBar = false;

	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateImage((Perspective) arg1);
		}
	}
	
	private class UpdateScrollBar implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			Perspective p = (Perspective) arg1;
			if (p.getImage() != null) {
				isLoadingScrollBar = true;
			
				System.out.println("imageWidth : " + image.getScaledWidth());
				System.out.println("windowWidth : " + image.getWidth());
				int displayImageWidth = (int) (image.getWidth() / p.getZoom());
				int widthSurplus = Math.max(0, image.getScaledWidth() - displayImageWidth);
				int heightSurplus = Math.max(0, image.getScaledHeight() - image.getHeight());
				System.out.println(widthSurplus);
				
				horizontalScrollBar.setVisible(widthSurplus > 0);
				verticalScrollBar.setVisible(heightSurplus > 0);
				
				horizontalScrollBar.setMaximum(widthSurplus);
				verticalScrollBar.setMaximum(heightSurplus);
				int horizontalValue = (int) (p.getPosition().getX() / p.getImage().getWidth() * widthSurplus);
				horizontalScrollBar.setValue(horizontalValue);
				horizontalScrollBarOldValue = horizontalValue;
				
				int verticalValue = (int) (p.getPosition().getY() / p.getImage().getHeight() * heightSurplus);
				verticalScrollBar.setValue(verticalValue);
				verticalScrollBarOldValue = verticalValue;
				
				System.out.println(horizontalScrollBar.getMinimum() + " < " + horizontalScrollBar.getValue() + " < " + horizontalScrollBar.getMaximum());
				
				isLoadingScrollBar = false;
			}
		}
	}
}
