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
	public GraphicalPerspectiveView(Controller controller,
			Perspective perspective) {
		this.controller = controller;
		this.perspective = perspective;
		initView();
	}

	private void initView() {
		initPanelAspect();
		initButton();
		initToolBar();
		initScrollbars();
		initImage();

		add(toolBar, BorderLayout.NORTH);
		add(image, BorderLayout.CENTER);
		add(horizontalTranslationScrollbar, BorderLayout.SOUTH);
		add(verticalTranslationScrollbar, BorderLayout.EAST);

		PerpectiveChanged listener = new PerpectiveChanged();
		perspective.imageChanged.addObserver(listener);
		perspective.zoomChanged.addObserver(listener);
		perspective.positionChanged.addObserver(listener);

		updateScrollbarsMaxValue();
	}

	private void initImage() {
		image = new ImageComponent(perspective.getImage().getWidth(),
				perspective.getImage().getHeight());
		updateImage();
		image.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent event) {
				controller.performCommand(new ZoomCommand(perspective, -1
						* event.getWheelRotation() * 0.01));
				updateScrollbarsMaxValue();
			}
		});
	}

	private void initPanelAspect() {
		setBackground(Color.BLACK);
		setLayout(new BorderLayout());
	}

	private void initToolBar() {
		toolBar = new JToolBar();
		toolBar.add(buttonZoomIn);
		toolBar.add(buttonZoomOut);
		toolBar.add(buttonOriginalZoom);
		toolBar.add(buttonUndo);
		toolBar.add(buttonRedo);
	}

	private void initScrollbars() {
		horizontalTranslationScrollbar = new JScrollBar(JScrollBar.HORIZONTAL,
				0, 0, 0, 0);
		horizontalTranslationScrollbar
				.addAdjustmentListener((new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent ce) {
						int currentValue = horizontalTranslationScrollbar
								.getValue();
						controller.performCommand(new TranslationCommand(
								perspective,
								-(currentValue - hScrollbarOldValue), 0));
						hScrollbarOldValue = currentValue;
					}
				}));

		verticalTranslationScrollbar = new JScrollBar(JScrollBar.VERTICAL, 0,
				0, 0, 0);
		verticalTranslationScrollbar
				.addAdjustmentListener(new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent ce) {
						int currentValue = verticalTranslationScrollbar
								.getValue();
						controller.performCommand(new TranslationCommand(
								perspective, 0,
								-(currentValue - vScrollbarOldValue)));
						vScrollbarOldValue = currentValue;
					}
				});
	}

	private void initButton() {
		Icon iconeZoomIn = new ImageIcon("icon/zoom-in.png");
		buttonZoomIn = new JButton("", iconeZoomIn);
		buttonZoomIn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, 0.1));
				updateScrollbarsMaxValue();
			}
		});

		Icon iconeZoomOut = new ImageIcon("icon/zoom-out.png");
		buttonZoomOut = new JButton("", iconeZoomOut);
		buttonZoomOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective, -0.1));
				updateScrollbarsMaxValue();
			}
		});

		Icon iconeOriginal = new ImageIcon("icon/zoom-original.png");
		buttonOriginalZoom = new JButton("", iconeOriginal);
		buttonOriginalZoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.performCommand(new ZoomCommand(perspective,
						1 - perspective.getZoom()));
			}
		});

		Icon iconeUndo = new ImageIcon("icon/undo.png");
		buttonUndo = new JButton("", iconeUndo);
		buttonUndo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.undo();
				updateScrollbarsMaxValue();
			}
		});

		Icon iconeRedo = new ImageIcon("icon/redo.png");
		buttonRedo = new JButton("", iconeRedo);
		buttonRedo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.redo();
				updateScrollbarsMaxValue();
			}
		});
	}

	private void updateImage() {
		image.setImage(perspective.getImage(), perspective.getZoom(),
				perspective.getPosition());
	}

	private int getImagePanelWidthDiff() {
		return getWidth() - image.getScaledWidth();
	}

	private int getImagePanelHeightDiff() {
		return getHeight() - image.getScaledHeight() - toolBar.getHeight();
	}

	private void updateScrollbarsMaxValue() {
		double zoom;
		zoom = perspective.getZoom();
		int maxH;
		int maxV;
		int panelImageWidthDiff = getImagePanelWidthDiff();
		int panelImageHeightDiff = getImagePanelHeightDiff();

		horizontalTranslationScrollbar.setAutoscrolls(true);
		if (panelImageWidthDiff >= 0) {
			maxH = 0;
			horizontalTranslationScrollbar.setVisible(false);
		} else {
			maxH = Math.abs(panelImageWidthDiff);
			horizontalTranslationScrollbar.setVisible(true);
		}

		if (panelImageHeightDiff >= 0) {
			maxV = 0;
			verticalTranslationScrollbar.setVisible(false);
		} else {
			maxV = Math.abs(panelImageHeightDiff);
			verticalTranslationScrollbar.setVisible(true);
		}

		horizontalTranslationScrollbar.setMaximum(zoom < 0 ? 0 : maxH);
		verticalTranslationScrollbar.setMaximum(zoom < 0 ? 0 : maxV);
	}

	private Perspective perspective;
	private Controller controller;
	private ImageComponent image;
	private JButton buttonZoomIn;
	private JButton buttonZoomOut;
	private JButton buttonUndo;
	private JButton buttonRedo;
	private JButton buttonOriginalZoom;
	private JScrollBar horizontalTranslationScrollbar;
	private JScrollBar verticalTranslationScrollbar;
	private int vScrollbarOldValue = 0;
	private int hScrollbarOldValue = 0;
	private JToolBar toolBar;

	private class PerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			updateImage();
			image.repaint();
		}
	}
}