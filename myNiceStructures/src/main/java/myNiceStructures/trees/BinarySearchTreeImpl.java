package myNiceStructures.trees;

import java.util.List;

import myNiceStructures.trees.exceptions.ImpossibleDeletion;
import myNiceStructures.trees.exceptions.ImpossibleInsertion;
import myNiceStructures.trees.exceptions.ValueNotFound;

public final class BinarySearchTreeImpl<K extends Comparable<K>> implements BinarySearchTree<K> {
	
	private final K root;
	private BinarySearchTreeImpl<K> leftSubTree;
	private BinarySearchTreeImpl<K> rightSubTree;
	
	public BinarySearchTreeImpl(K root) {
		if(root == null) {
			throw new IllegalArgumentException("root ne peut pas être null");
		}
		this.root = root;
	}
	
	public BinarySearchTreeImpl(K root, BinarySearchTreeImpl<K> left, BinarySearchTreeImpl<K> right) {
		if(root == null) {
			throw new IllegalArgumentException("root ne peut pas être null");
		}
		this.root = root;
		this.leftSubTree = left;
		this.rightSubTree = right;
	}
	
	@Override
	public K getRoot() {
		return this.root;
	}

	@Override
	public final void setRoot(K valeur) {
		throw new IllegalAccessError("Impossible de mettre des valeurs dans BinarySearchTreeImpl -> setRoot()");
	}

	@Override
	public List<Tree<K>> getForest() {
		if (this.leftSubTree == null && this.rightSubTree == null) {
			return List.of();
		} else if (this.leftSubTree == null) {
			return List.of(this.rightSubTree);
		} else if (this.rightSubTree == null) {
			return List.of(this.leftSubTree);
		} else {
			return List.of(this.leftSubTree, this.rightSubTree);
		} 
	}

	@Override
	public BinarySearchTreeImpl<K> getLeftSubTree() {
		return this.leftSubTree;
	}

	public void setLeftSubTree(BinarySearchTreeImpl<K> left) {
		this.leftSubTree = left;
	}
	
	@Override
	public BinarySearchTreeImpl<K> getRightSubTree() {
		return this.rightSubTree;
	}

	public void setRightSubTree(BinarySearchTreeImpl<K> right) {
		this.rightSubTree = right;
	}

	@Override
	public BinarySearchTree<K> findSubTree(K key) {
		if(key == null) {
			throw new IllegalArgumentException("key ne peut pas être null");
		}
		
		BinarySearchTree<K> currentTree = this;
		int resComp = 1;
		
		do {
			resComp = currentTree.getRoot().compareTo(key);
			if(resComp < 0) {
				// key plus grand que moi
				currentTree = currentTree.getRightSubTree();
			}
			else if(resComp > 0) {
				// key plus petite que moi
				currentTree = currentTree.getLeftSubTree();
			}
		} while(resComp != 0 && currentTree != null);
		
		return currentTree;
		
		/*
		int resComp = this.getRoot().compareTo(key);
		if(resComp == 0) {
			return this;
		}
		else if(resComp < 0) {
			// key plus grand que moi
			return this.getRightSubTree() != null ? this.getRightSubTree().findSubTree(key) : null;
		}
		else {
			// key plus petite que moi
			return this.getLeftSubTree() != null ? this.getLeftSubTree().findSubTree(key) : null;
		}
		*/
	}

	@Override
	public boolean contains(K key) {
		if(key == null) {
			throw new IllegalArgumentException("key ne peut pas être null");
		}
		
		BinarySearchTree<K> currentTree = this;
		int resComp = 1;
		
		do {
			resComp = currentTree.getRoot().compareTo(key);
			if(resComp < 0) {
				// key plus grand que moi
				currentTree = currentTree.getRightSubTree();
			}
			else {
				// key plus petite que moi
				currentTree = currentTree.getLeftSubTree();
			}
		} while(resComp != 0 && currentTree != null);
		
		return currentTree != null;
	}

	@Override
	public K getMin() {		
		BinarySearchTree<K> currentTree = this;
		
		do {
			if(currentTree.getLeftSubTree() == null) {
				return currentTree.getRoot();
			}
			
			currentTree = currentTree.getLeftSubTree();
		} while(currentTree != null);
		
		return this.getRoot();
	}

	@Override
	public K getMax() {
		BinarySearchTree<K> currentTree = this;
		
		do {
			if(currentTree.getRightSubTree() == null) {
				return currentTree.getRoot();
			}
			
			currentTree = currentTree.getRightSubTree();
		} while(currentTree != null);
		
		return this.getRoot();
	}

	@Override
	public BinarySearchTree<K> add(K key) throws ImpossibleInsertion {
		if(key == null) {
			throw new IllegalArgumentException("key ne peut pas être null");
		}
		/*
		if(this.contains(key)) {
			throw new ImpossibleInsertion("La valeur existe déja dans l'arbre");
		}
		*/
		
		BinarySearchTreeImpl<K> currentTree = this;
		int resComp;
		do {
			resComp = this.getRoot().compareTo(key);
			if(resComp == 0) {
				throw new ImpossibleInsertion("La valeur existe déja dans l'arbre");
			}
			
			else if(resComp > 0) {
				if(currentTree.getLeftSubTree() != null) {
					currentTree = currentTree.getLeftSubTree();
				} else {
					currentTree.setLeftSubTree(new BinarySearchTreeImpl<>(key));
					return this;
				}
			}
			else {
				if(currentTree.getRightSubTree() != null) {
					currentTree = currentTree.getRightSubTree();
				} else {
					currentTree.setRightSubTree(new BinarySearchTreeImpl<>(key));
					return this;
				}
			}
		} while(true);
	}

	@Override
	public BinarySearchTree<K> remove(K key) throws ValueNotFound, ImpossibleDeletion {
		if(key == null) {
			throw new IllegalArgumentException("key ne peut pas être null");
		}
		
		
		if(!this.contains(key)) return null;
		
		BinarySearchTree<K> parentTree = null;
		BinarySearchTree<K> currentTree = this;
		
		do {
			resComp = currentTree.getRoot().compareTo(key);
			if(resComp < 0) {
				// key plus grand que moi
				currentTree = currentTree.getRightSubTree();
				parentTree = currentTree;
			}
			else if(resComp > 0) {
				// key plus petite que moi
				currentTree = currentTree.getLeftSubTree();
				parentTree = currentTree;
			}
		} while(resComp != 0 && currentTree != null);
		
		return this;
	}

}
