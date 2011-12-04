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
		
		JButton button1 = new JButton(new ImageIcon("icon/zoom-in.png"));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 0.2 * perspective.getZoom()));
			}
		});
		toolBar.add(button1);
		
		JButton button2 = new JButton(new ImageIcon("icon/zoom-out.png"));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, -0.2 * perspective.getZoom()));
			}
		});
		toolBar.add(button2);
		
		JButton button3 = new JButton(new ImageIcon("icon/zoom-original.png"));
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 1 - perspective.getZoom()));
			}
		});
		toolBar.add(button3);
		
		JButton button4 = new JButton(new ImageIcon("icon/zoom-fit-best.png"));
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Not implemented yet");
			}
		});
		toolBar.add(button4);
		
		return toolBar;
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Controller controller;
	private Perspective perspective;
	private ImageComponent imageComponent;
}
