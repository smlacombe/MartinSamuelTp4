package ets.log120.tp4.app.test;


import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Before;
import org.junit.Test;

import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.PerspectiveFactory;

public class PerspectiveTest {
	
	private class TestingObserver implements Observer {
		public boolean methodExecuted = false;
		
		@Override
		public void update(Observable o, Object arg) {
			methodExecuted = true;
		}
	}
	
	@Before
	public void setUp() throws Exception {
		perspective = PerspectiveFactory.makePerspective();
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testImageChangedEvent() {
		TestingObserver functor = new TestingObserver();
		perspective.imageChanged.addObserver(functor);
		
		assertFalse(functor.methodExecuted);
		perspective.setImage("newImage.png");
		assertTrue(functor.methodExecuted);
	}
	
	@Test
	public void testZoomChangedEvent() {
		TestingObserver functor = new TestingObserver();
		perspective.zoomChanged.addObserver(functor);
		
		assertFalse(functor.methodExecuted);
		perspective.setZoom(1.5);
		assertTrue(functor.methodExecuted);
	}
	
	@Test
	public void testPositionChangedEvent() {
		TestingObserver functor = new TestingObserver();
		perspective.positionChanged.addObserver(functor);
		
		assertFalse(functor.methodExecuted);
		perspective.setPosition(new java.awt.Point(10, 10));
		assertTrue(functor.methodExecuted);
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Perspective perspective;
}
