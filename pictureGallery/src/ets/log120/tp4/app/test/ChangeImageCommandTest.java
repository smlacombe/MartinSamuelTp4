package ets.log120.tp4.app.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import ets.log120.tp4.app.Command;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ChangeImageCommand;;

public class ChangeImageCommandTest {
	
	@Before
	public void setUp() throws Exception {
		perspective = PerspectiveFactory.makePerspective("image.png", new BufferedImage(64, 64, BufferedImage.TYPE_CUSTOM));
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testDoChangeCommand() {
		assertEquals("image.png", perspective.getImageName());
		
		Command command = new ChangeImageCommand(perspective, "newImage.png", new BufferedImage(64, 64, BufferedImage.TYPE_CUSTOM));
		command.doCommand();
		
		assertEquals("newImage.png", perspective.getImageName());
	}
	
	@Test
	public void testUndoChangeCommand() {
		assertEquals("image.png", perspective.getImageName());
		
		Command command = new ChangeImageCommand(perspective, "newImage.png", new BufferedImage(64, 64, BufferedImage.TYPE_CUSTOM));
		command.doCommand();
		command.undoCommand();
		
		assertEquals("image.png", perspective.getImageName());
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Perspective perspective;
}
