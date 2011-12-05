package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveUtil;
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

public class PerspectiveGraphicalView extends JPanel {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public PerspectiveGraphicalView(Controller controller, int width, int height) {
		super(new BorderLayout());
		this.controller = controller;
		
		add(imageComponent = new ImageComponent(width, height), BorderLayout.CENTER);
		add(getToolBar(), BorderLayout.PAGE_START);
		
		imageComponent.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				PerspectiveGraphicalView.this.controller.performCommand(new ZoomCommand(perspective,
						-1 * event.getWheelRotation() * 0.05 * perspective.getZoom()));
			}
		});
		
		DragAndDropListener listener = new DragAndDropListener();
		imageComponent.addMouseListener(listener);
		imageComponent.addMouseMotionListener(listener);
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	public void setPerspective(Perspective value) {
		perspective = value;
		imageComponent.setImage(perspective.getImage(), perspective.getZoom(), perspective.getPosition());
	}

	// --------------------------------------------------
	// Méthodes(s)
	// --------------------------------------------------

	private JToolBar getToolBar() {
		JToolBar toolBar = new JToolBar();
		
		JButton zoomInButton = new JButton(new ImageIcon("icon/zoom-in.png"));
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 0.2 * perspective.getZoom()));
			}
		});
		toolBar.add(zoomInButton);
		
		JButton zoomOutButton = new JButton(new ImageIcon("icon/zoom-out.png"));
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, -0.2 * perspective.getZoom()));
			}
		});
		toolBar.add(zoomOutButton);
		
		JButton zoomOriginalButton = new JButton(new ImageIcon("icon/zoom-original.png"));
		zoomOriginalButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 1 - perspective.getZoom()));
			}
		});
		toolBar.add(zoomOriginalButton);
		
		JButton zoomFitBestButton = new JButton(new ImageIcon("icon/zoom-fit-best.png"));
		zoomFitBestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				double bestZoom = 1.0;
					
				if (imageComponent.getSize().getHeight() < imageComponent.getSize().getWidth())
					bestZoom = imageComponent.getSize().getHeight() / perspective.getImage().getHeight();
				else
					bestZoom = imageComponent.getSize().getWidth() / perspective.getImage().getWidth();
					
				controller.performCommand(new ZoomCommand(perspective, bestZoom - perspective.getZoom()));
			}
		});
		toolBar.add(zoomFitBestButton);
		
		return toolBar;
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Controller controller;
	private Perspective perspective;
	private ImageComponent imageComponent;
	
	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------
	
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
			if (buttonPressed && e.getButton() == MouseEvent.BUTTON1) {
				buttonPressed = false;
				
				Point releasedPosition = e.getPoint();
				
				int horizontalTranslation = pressedPosition.x - releasedPosition.x;
				int verticalTranslation = pressedPosition.y - releasedPosition.y;
				
				Point translation = PerspectiveUtil.getTranslationToFitDisplay(
						perspective,
						imageComponent.getSize().width, imageComponent.getSize().width,
						new Point(horizontalTranslation, verticalTranslation));
				
				if (translation.x != 0 || translation.y != 0)
					controller.performCommand(new TranslationCommand(perspective, translation.x, translation.y));
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (buttonPressed) {
				
			}
		}
		
		private boolean buttonPressed;
		private Point pressedPosition;
	}
}
