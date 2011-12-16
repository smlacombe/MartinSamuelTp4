package ets.util.containers;

/**
 * Pile pour stocker les action à défaire et à refaire.
 */
public class Stack<T> {
	
	// --------------------------------------------------
	// Constructeur(s)
	// --------------------------------------------------
	
	/**
	 * Initialise la pile.
	 */
	public Stack() {
		this(new java.util.LinkedList<T>());
	}
	
	/**
	 * Initialise la pile à l'aide d'un Deque.
	 */
	public Stack(java.util.Deque<T> container) {
		this.container = container;
	}
	
	// --------------------------------------------------
	// Accesseur(s)
	// --------------------------------------------------
	
	/**
	 * Retourne si la pile est vide.
	 */
	public boolean empty() {
		return size() == 0;
	}
	
	/**
	 * Retourne la taille de la pile.
	 */
	public int size() {
		return container.size();
	}
	
	// --------------------------------------------------
	// Mutateur(s)
	// --------------------------------------------------
	
	/**
	 * Retourne l'élément sur la pile.
	 */
	public T top() {
		return container.getLast();
	}
	
	/**
	 * Ajoute une élément sur la pile
	 */
	public void push(T newElement) {
		container.addLast(newElement);
	}
	
	/**
	 * Supprime un élément sur la pile.
	 */
	public void pop() {
		container.removeLast();
	}
	
	/**
	 * Vide la pile.
	 */
	public void clear() {
		container.clear();
	}
	
	// --------------------------------------------------
	// Attribut(s)
	// --------------------------------------------------
	
	private java.util.Deque<T> container;
}
