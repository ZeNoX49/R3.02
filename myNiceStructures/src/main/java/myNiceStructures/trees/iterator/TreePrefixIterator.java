package myNiceStructures.trees.iterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import myNiceStructures.trees.Tree;

public class TreePrefixIterator<V> implements Iterator<V> {
	private final Deque<Tree<V>> workingStack;
	
	public TreePrefixIterator(Tree<V> treeToIterate) {
		this.workingStack = new ArrayDeque<Tree<V>>();
		this.workingStack.add(treeToIterate);
	}
	
	@Override
	public boolean hasNext() {
		return !this.workingStack.isEmpty();
	}
	
	@Override
    public V next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Tree<V> current = workingStack.pop();

        // Empiler les enfants en ordre inverse
        var forest = current.getForest();
        for (int i = forest.size() - 1; i >= 0; i--) {
        	workingStack.push(forest.get(i));
        }

        return current.getRoot();
    }
}