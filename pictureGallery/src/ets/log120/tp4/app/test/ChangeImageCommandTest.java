package ets.log120.tp4.app.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ets.log120.tp4.app.Command;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ChangeImageCommand;;

public class ChangeImageCommandTest {
	
	@Before
	public void setUp() throws Exception {
		perspective = PerspectiveFactory.makePerspective("image.png");
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testDoChangeCommand() {
		assertEquals("image.png", perspective.getImage());
		
		Command command = new ChangeImageCommand(perspective, "newImage.png");
		command.doCommand();
		
		assertEquals("newImage.png", perspective.getImage());
	}
	
	@Test
	public void testUndoChangeCommand() {
		assertEquals("image.png", perspective.getImage());
		
		Command command = new ChangeImageCommand(perspective, "newImage.png");
		command.doCommand();
		command.undoCommand();
		
		assertEquals("image.png", perspective.getImage());
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Perspective perspective;
}
