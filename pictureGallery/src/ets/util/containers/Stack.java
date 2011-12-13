package ets.util.containers;

public class Stack<T> {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	public Stack() {
		this(new java.util.LinkedList<T>());
	}
	
	public Stack(java.util.Deque<T> container) {
		this.container = container;
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	public boolean empty() {
		return size() == 0;
	}
	
	public int size() {
		return container.size();
	}
	
	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	public T top() {
		return container.getLast();
	}
	
	public void push(T newElement) {
		container.addLast(newElement);
	}
	
	public void pop() {
		container.removeLast();
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	java.util.Deque<T> container;
}
