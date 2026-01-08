package myNiceStructures.trees.iterator;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

import myNiceStructures.trees.Tree;

public class TreeSuffixIterator<V> implements Iterator<V> {
	
	private static class Frame<V> {
        Tree<V> tree;
        int childIndex;

        Frame(Tree<V> tree) {
            this.tree = tree;
            this.childIndex = 0;
        }
    }
	
	private final Deque<Frame<V>> workingStack = new ArrayDeque<>();
	
	public TreeSuffixIterator(Tree<V> treeToIterate) {
        if (treeToIterate != null) {
        	workingStack.push(new Frame<>(treeToIterate));
        }
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

        while (true) {
            Frame<V> frame = workingStack.peek();

            // Il reste des enfants à visiter
            if (frame.childIndex < frame.tree.getForest().size()) {
                Tree<V> child = frame.tree.getForest().get(frame.childIndex++);
                workingStack.push(new Frame<>(child));
            }
            // Tous les enfants ont été visités → on retourne la racine
            else {
            	workingStack.pop();
                return frame.tree.getRoot();
            }
        }
    }
}