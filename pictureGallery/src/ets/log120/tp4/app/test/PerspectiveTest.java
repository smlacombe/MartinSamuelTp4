package ets.log120.tp4.app.test;


import static org.junit.Assert.*;

import java.util.Observable;
import java.util.Observer;

import org.junit.Test;

import ets.log120.tp4.app.Perspective;

public class PerspectiveTest {
	
	private class TestingObserver implements Observer {
		public boolean methodExecuted = false;
		
		@Override
		public void update(Observable o, Object arg) {
			methodExecuted = true;
		}
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testImageLoadedEvent() {
		Perspective perspective = new Perspective("image.png");
		TestingObserver functor = new TestingObserver();
		perspective.imageUpdated.addObserver(functor);
		
		assertFalse(functor.methodExecuted);
		perspective.setImage("newImage.png");
		assertTrue(functor.methodExecuted);
	}

}
