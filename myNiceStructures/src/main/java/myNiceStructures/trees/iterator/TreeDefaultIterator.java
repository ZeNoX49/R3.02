package myNiceStructures.trees.iterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import myNiceStructures.trees.Tree;

public class TreeDefaultIterator<V> implements Iterator<V> {
	private final Deque<Tree<V>> workingStack;
	
	public TreeDefaultIterator(Tree<V> treeToIterate) {
		this.workingStack = new ArrayDeque<Tree<V>>();
		this.workingStack.add(treeToIterate);
	}
	
	@Override
	public boolean hasNext() {
		return !this.workingStack.isEmpty();
	}

	@Override
	public V next() {
		if(!this.hasNext()) {
			throw new NoSuchElementException();
		}
		Tree<V> currentTree = this.workingStack.poll();
		List<Tree<V>> forest = currentTree.getForest();
		for(int i = 0; i < forest.size(); i++) {
			this.workingStack.addFirst(forest.get(forest.size() - i - 1));
		}
		return currentTree.getRoot();
	}
}