package ets.log120.tp4.app.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ets.log120.tp4.app.Command;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ZoomCommand;

public class ZoomCommandTest {

	@Before
	public void setUp() throws Exception {
		perspective = PerspectiveFactory.makePerspective();
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testDoZoomIn() {
		assertEquals(1, perspective.getZoom(), 0.00001);
		
		Command command = new ZoomCommand(perspective, 0.5);
		command.doCommand();
		
		assertEquals(1.5, perspective.getZoom(), 0.00001);
	}
	
	@Test
	public void testDoZoomOut() {
		assertEquals(1, perspective.getZoom(), 0.00001);
		
		Command command = new ZoomCommand(perspective, -0.5);
		command.doCommand();
		
		assertEquals(0.5, perspective.getZoom(), 0.00001);
	}

	@Test
	public void testUndoZoomIn() {
		assertEquals(1, perspective.getZoom(), 0.00001);
		
		Command command = new ZoomCommand(perspective, 0.5);
		command.doCommand();
		command.undoCommand();
		
		assertEquals(1, perspective.getZoom(), 0.00001);
	}
	
	@Test
	public void testUndoZoomOut() {
		assertEquals(1, perspective.getZoom(), 0.00001);
		
		Command command = new ZoomCommand(perspective, -0.5);
		command.doCommand();
		command.undoCommand();
		
		assertEquals(1, perspective.getZoom(), 0.00001);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Perspective perspective;
}
