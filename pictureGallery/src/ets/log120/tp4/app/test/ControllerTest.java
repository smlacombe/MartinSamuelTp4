package ets.log120.tp4.app.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ets.log120.tp4.app.Controller;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;
import ets.log120.tp4.app.ZoomCommand;

public class ControllerTest {

	@Test
	public void testUndoMultipleZoom() {
		Perspective perspective = PerspectiveFactory.makePerspective();
		Controller controller = Controller.getInstance();
		
		assertEquals(1.0, perspective.getZoom(), 0.00001);
		
		controller.performCommand(new ZoomCommand(perspective, 0.1));
		controller.performCommand(new ZoomCommand(perspective, 0.2));
		controller.performCommand(new ZoomCommand(perspective, 0.3));
		controller.performCommand(new ZoomCommand(perspective, 0.4));
		controller.performCommand(new ZoomCommand(perspective, 0.5));
		
		assertEquals(2.5, perspective.getZoom(), 0.00001);
		
		controller.undo();
		assertEquals(2.0, perspective.getZoom(), 0.00001);
		
		controller.undo();
		assertEquals(1.6, perspective.getZoom(), 0.00001);
		
		controller.undo();
		assertEquals(1.3, perspective.getZoom(), 0.00001);
		
		controller.undo();
		assertEquals(1.1, perspective.getZoom(), 0.00001);
		
		controller.undo();
		assertEquals(1.0, perspective.getZoom(), 0.00001);
	}

}
