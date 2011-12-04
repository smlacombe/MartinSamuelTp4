package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	public void setPerspective(Perspective value) {
		perspective = value;
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(perspective.getImage()));
		} catch (IOException e) {
			// Nothing to do
		} finally {
			imageComponent.setImage(image, perspective.getZoom());
		}
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
				BufferedImage image = null;
				try {
					image = ImageIO.read(new File(perspective.getImage()));
					double bestZoom = 1.0;
					
					if (imageComponent.getSize().getHeight() < imageComponent.getSize().getWidth())
						bestZoom = imageComponent.getSize().getHeight() / image.getHeight();
					else
						bestZoom = imageComponent.getSize().getWidth() / image.getWidth();
					
					controller.performCommand(new ZoomCommand(perspective, bestZoom - perspective.getZoom()));
				} catch (IOException e) {
					// Nothing to do
				}
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
}
