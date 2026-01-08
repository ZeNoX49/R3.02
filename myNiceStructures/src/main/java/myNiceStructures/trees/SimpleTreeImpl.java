package myNiceStructures.trees;

import java.util.ArrayList;
import java.util.List;

public class SimpleTreeImpl<V> implements Tree<V> {
	
	private V root;
	
	private List<Tree<V>> forest;

	public SimpleTreeImpl(V root)  {
		this.root = root;
		this.forest = new ArrayList<>();
	}
	
	public SimpleTreeImpl(V root, List<Tree<V>> forest)  {
		if(forest == null) {
			throw new IllegalArgumentException("forest est null");
		}
		
		this.root = root;
		this.forest = forest;
	}

	@Override
	public V getRoot() {
		return this.root;
	}

	@Override
	public void setRoot(V valeur) {
		this.root = valeur;
	}

	@Override
	public List<Tree<V>> getForest() {
		return this.forest;
	}
	
}