package ets.log120.tp4.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ets.log120.tp4.app.ChangeImageCommand;
import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.PerspectiveUtil;

/**
 * Fenêtre principale de l'application.
 */
public class MainWindow extends JFrame {

	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------

	public MainWindow() {
		super();
		initLang();
		setLayout(new BorderLayout());

		controller = Controller.getInstance();
		initMenuBar();
		add(getLeftPanel(), BorderLayout.LINE_START);
		add(graphicalView = new GraphicalPerspectiveView(controller),
				BorderLayout.CENTER);

		setTitle(lang.getProperty("app.title"));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------

	/**
	 * Définie la perspective.
	 */
	private void setPerspective(Perspective p) {
		perspective = p;

		textView.setPerspective(perspective);
		graphicalView.setPerspective(perspective);

		perspective.imageChanged.addObserver(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				Perspective p = (Perspective) arg1;
				thumbnailPerspective.setImage(p.getImageName(), p.getImage());
			}
		});

		thumbnailPerspective = PerspectiveFactory.makePerspective();
		thumbnailPerspective.imageChanged
				.addObserver(new ThumbnailPerpectiveChanged());
		thumbnailPerspective.setImage(p.getImageName(), p.getImage());
	}

	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------

	/**
	 * Retourne le JPanel de droite.
	 */
	private JPanel getLeftPanel() {
		final int MARGIN = 5;

		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN,
				MARGIN));
		panel.add(textView = new TextualPerspectiveView());
		panel.add(Box.createVerticalGlue());
		panel.add(thumbnail = new ImageComponent(THUMB_WIDTH, THUMB_HEIGHT));

		return panel;
	}

	/**
	 * Retourne le menu « Fichier » de l'application.
	 */
	private JMenu getFileMenu() {
		JMenu fileMenu = new JMenu(lang.getProperty("app.menu.file"));
		JMenuItem exitItem = new JMenuItem(
				lang.getProperty("app.menu.file.exit"));
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		JMenuItem saveItem = new JMenuItem(
				lang.getProperty("app.menu.file.save"));
		saveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (perspective != null) {
					try {
						serializePerspective();
						JOptionPane.showMessageDialog(MainWindow.this,
								lang.getProperty("app.dialog.fileSaved"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {

				}
			}
		});

		fileMenu.add(saveItem);
		JMenuItem openPerspectiveItem = new JMenuItem(
				lang.getProperty("app.menu.file.openPerspective"));
		openPerspectiveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					unSerializePerspective();
				} catch (FileNotFoundException e) {
					JOptionPane.showMessageDialog(MainWindow.this,
							lang.getProperty("app.dialog.fileNotFound"));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(MainWindow.this,
							lang.getProperty("app.dialog.unrecoverableError"));
					System.exit(ERROR);
				}
			}
		});

		fileMenu.add(openPerspectiveItem);

		return fileMenu;
	}

	/**
	 * Retourne le menu « Image » de l'application.
	 */
	private JMenu getImageMenu() {
		JMenu imageMenu = new JMenu(lang.getProperty("app.menu.image"));
		JMenuItem openImageItem = new JMenuItem(
				lang.getProperty("app.menu.image.loadImage"));
		openImageItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new ImageFilter());

				int returnVal = fc.showOpenDialog(getContentPane());

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					String fileName = fc.getSelectedFile().getAbsolutePath();
					BufferedImage newImage = null;
					try {
						newImage = ImageIO.read(new File(fileName));
						if (perspective == null) {
							Perspective p = PerspectiveFactory.makePerspective(
									fileName, newImage);
							setPerspective(p);
						} else {
							controller.performCommand(new ChangeImageCommand(
									perspective, fileName, newImage));
						}
					} catch (IOException ex) {
						System.out.println("fail");
					}
				}
			}
		});
		imageMenu.add(openImageItem);
		return imageMenu;
	}

	// --------------------------------------------------
	// Méthodes(s)
	// --------------------------------------------------
	/**
	 * Initialise la barre de menu.
	 */
	private void initMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(getFileMenu());
		menuBar.add(getImageMenu());
		setJMenuBar(menuBar);
	}

	/**
	 * Sérialise la perspective.
	 */
	private void serializePerspective() throws IOException {
		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new SerialFilter());
		int returnVal = fc.showSaveDialog(getContentPane());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();

			if (!(fileName.endsWith(SERIALIZATION_FILE_EXTENSION)))
				fileName = fileName.concat(SERIALIZATION_FILE_EXTENSION);

			FileOutputStream file = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(file);
			out.writeObject(perspective);
			out.flush();
			out.close();
		}
	}

	/**
	 * Désérialise la perspective.
	 */
	private void unSerializePerspective() throws IOException,
			FileNotFoundException, ClassNotFoundException {

		final JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new SerialFilter());

		int returnVal = fc.showOpenDialog(getContentPane());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String fileName = fc.getSelectedFile().getAbsolutePath();
			FileInputStream file = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(file);
			perspective = (Perspective) in.readObject();
			perspective.setImage(perspective.getImageName(),
					ImageIO.read(new File(perspective.getImageName())));
			setPerspective(perspective);
		}

	}

	/**
	 * Initialise le fichier de propriétés contenant le texte à afficher à
	 * l'utilisateur.
	 */
	private void initLang() {
		final String fileName = "lang.fr";

		try {
			lang = new java.util.Properties();
			lang.load(new java.io.FileInputStream("lang.fr"));
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Le fichier de langue "
					+ fileName + " n'existe pas.");
			System.exit(ERROR);
		} catch (IOException e) {
			JOptionPane
					.showMessageDialog(this,
							"Une erreur est survenue lors du chargement du fichier de langue");
			System.exit(ERROR);
		}
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------

	private java.util.Properties lang;
	private GraphicalPerspectiveView graphicalView;
	private TextualPerspectiveView textView;
	private ImageComponent thumbnail;
	private Perspective perspective;
	private Perspective thumbnailPerspective;
	private Controller controller;
	private static final int WINDOW_WIDTH = 800;
	private static final int WINDOW_HEIGHT = 600;
	private static final int THUMB_WIDTH = 256;
	private static final int THUMB_HEIGHT = 256;
	private static final String SERIALIZATION_FILE_EXTENSION = ".ser";

	// --------------------------------------------------
	// Classe(s) interne(s)
	// --------------------------------------------------

	/**
	 * Classe définissant le filtre pour les image pour des boîtes de dialog.
	 */
	class ImageFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(".png") || filename.endsWith(".jpg")
					|| filename.endsWith(".gif") || (file.isDirectory())
					|| filename.endsWith(".jpeg") || filename.endsWith(".tif");
		}

		public String getDescription() {
			return "*.png, *.jpg, *.jpeg *.gif";
		}
	}

	/**
	 * Classe définissant le filtre pour les fichiers java sérialisés pour des
	 * boîtes de dialog.
	 */
	class SerialFilter extends javax.swing.filechooser.FileFilter {
		public boolean accept(File file) {
			String filename = file.getName();
			return filename.endsWith(SERIALIZATION_FILE_EXTENSION) || (file.isDirectory());
		}

		public String getDescription() {
			return SERIALIZATION_FILE_EXTENSION;
		}
	}

	/**
	 * Classe observatrice changeant la miniature en cas de mutation de la
	 * perspective.
	 */
	private class ThumbnailPerpectiveChanged implements java.util.Observer {
		@Override
		public void update(Observable arg0, Object arg1) {
			Perspective p = (Perspective) arg1;
			thumbnail.setImage(p.getImage(), PerspectiveUtil
					.getZoomToFitDisplay(p, thumbnail.getWidth(),
							thumbnail.getHeight()), p.getPosition());
		}
	}
}
