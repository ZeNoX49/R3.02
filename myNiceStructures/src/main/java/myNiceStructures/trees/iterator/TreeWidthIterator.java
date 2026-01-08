package myNiceStructures.trees.iterator;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

import myNiceStructures.trees.Tree;

public class TreeWidthIterator<V> implements Iterator<V> {
	private final Queue<Tree<V>> workingQueue;
	
	public TreeWidthIterator(Tree<V> treeToIterate) {
		this.workingQueue = new ArrayDeque<Tree<V>>();
		this.workingQueue.add(treeToIterate);
	}
	
	@Override
	public boolean hasNext() {
		return !this.workingQueue.isEmpty();
	}

	@Override
	public V next() {
		if(!this.hasNext()) {
			throw new NoSuchElementException();
		}
		Tree<V> currentTree = this.workingQueue.poll(); // retire la tête
		this.workingQueue.addAll(currentTree.getForest()); // ajoute a la fin
		return currentTree.getRoot();
	}
}