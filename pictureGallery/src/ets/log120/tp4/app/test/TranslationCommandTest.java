package ets.log120.tp4.app.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ets.log120.tp4.app.Command;
import ets.log120.tp4.app.Perspective;
import ets.log120.tp4.app.TranslationCommand;
import ets.log120.tp4.app.ZoomCommand;

public class TranslationCommandTest {

	@Before
	public void setUp() throws Exception {
		perspective = new Perspective("image.png");
	}
	
	// --------------------------------------------------
	// Test(s)
	// --------------------------------------------------
	
	@Test
	public void testDoHorizontalTranslation() {
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
		
		Command command = new TranslationCommand(perspective, 10, 0);
		command.doCommand();
		
		assertEquals(new java.awt.Point(10, 0), perspective.getPosition());
	}
	
	@Test
	public void testDoVerticalTranslation() {
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
		
		Command command = new TranslationCommand(perspective, 0, 10);
		command.doCommand();
		
		assertEquals(new java.awt.Point(0, 10), perspective.getPosition());
	}
	
	@Test
	public void testDoTranslation() {
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
		
		Command command = new TranslationCommand(perspective, 10, 10);
		command.doCommand();
		
		assertEquals(new java.awt.Point(10, 10), perspective.getPosition());
	}
	
	@Test
	public void testDoNegativeTranslation() {
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
		
		Command command = new TranslationCommand(perspective, -10, -10);
		command.doCommand();
		
		assertEquals(new java.awt.Point(-10, -10), perspective.getPosition());
	}
	
	@Test
	public void testUndoTranslation() {
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
		
		Command command = new TranslationCommand(perspective, 10, 10);
		command.doCommand();
		command.undoCommand();
		
		assertEquals(new java.awt.Point(0, 0), perspective.getPosition());
	}

	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private Perspective perspective;
}
