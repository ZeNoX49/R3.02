package myNiceStructures.trees;

public interface Forest<V> {
	
	public V getLongeur();
	public Tree<V> getIemeArbre(V i);
	public void ajouterArbre(V i, Tree<V> a);
	public void supprimerArbre(V i);
	
}