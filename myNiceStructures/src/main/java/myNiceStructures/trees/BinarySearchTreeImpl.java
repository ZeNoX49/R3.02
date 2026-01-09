package myNiceStructures.trees;

import java.util.List;

import myNiceStructures.trees.exceptions.ImpossibleDeletion;
import myNiceStructures.trees.exceptions.ImpossibleInsertion;
import myNiceStructures.trees.exceptions.ValueNotFound;

public final class BinarySearchTreeImpl<K extends Comparable<K>> implements BinarySearchTree<K> {
	
	private K root;
	private BinarySearchTreeImpl<K> leftSubTree;
	private BinarySearchTreeImpl<K> rightSubTree;
	
	public BinarySearchTreeImpl(K root) {
		if(root == null) throw new NullPointerException("root ne peut pas être null");
		this.root = root;
	}
	
	public BinarySearchTreeImpl(K root, BinarySearchTreeImpl<K> left, BinarySearchTreeImpl<K> right) {
		if(root == null) throw new NullPointerException("root ne peut pas être null");
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
		throw new UnsupportedOperationException("Impossible de mettre des valeurs dans BinarySearchTreeImpl -> setRoot()");
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
		if(key == null) throw new IllegalArgumentException("key ne peut pas être null");
		
		BinarySearchTree<K> currentTree = this;
		int resComp = 1;
		
		do {
			resComp = currentTree.getRoot().compareTo(key);
			if(resComp < 0) {   // key plus grande que moi
				currentTree = currentTree.getRightSubTree();
			}
			else if(resComp > 0) {   // key plus petite que moi
				currentTree = currentTree.getLeftSubTree();
			}
		} while(resComp != 0 && currentTree != null);
		
		return currentTree;
	}

	@Override
	public boolean contains(K key) {
		if(key == null) throw new IllegalArgumentException("key ne peut pas être null");
		
		BinarySearchTree<K> currentTree = this;
		int resComp = 1;
		
		do {
			resComp = currentTree.getRoot().compareTo(key);
			if(resComp < 0) {   // key plus grande que moi
				currentTree = currentTree.getRightSubTree();
			}
			else if(resComp > 0) {   // key plus petite que moi
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
		if(key == null) throw new IllegalArgumentException("key ne peut pas être null");
		
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

	// @Override
	// public BinarySearchTree<K> remove(K key) throws ValueNotFound, ImpossibleDeletion {
	// 	if(key == null) throw new ImpossibleDeletion("key ne peut pas être null");
	// 	if(this.isLeaf()) throw new ImpossibleDeletion("l'arbre est une feuille");
	// 	if(!this.contains(key)) throw new ValueNotFound("la clé n'a pas été trouvé");

	// 	// on récupère l'arbre de la valeur et son parent
	// 	BinarySearchTree<K> parentTree = null;
	// 	BinarySearchTree<K> currentTree = this;
	// 	while(currentTree.getRoot() != key) {
	// 		int resComp = currentTree.getRoot().compareTo(key);
	// 		if(resComp < 0) {   // key plus grande que moi
	// 			parentTree = currentTree;
	// 			currentTree = currentTree.getRightSubTree();
	// 		}
	// 		else if(resComp > 0) {   // key plus petite que moi
	// 			parentTree = currentTree;
	// 			currentTree = currentTree.getLeftSubTree();
	// 		}
	// 	}

	// 	// on récupère le tree qui va remplacer et son père (pour le supprimer)
	// 	BinarySearchTree<K> parentTreeReplace = currentTree;
	// 	BinarySearchTree<K> currentTreeReplace = null;

	// 	// des arbres des 2 cotés
	// 	if(currentTree.getLeftSubTree() != null && currentTree.getRightSubTree() != null) {
	// 		K lMax = currentTree.getLeftSubTree().getMax();
	// 		K rMin = currentTree.getRightSubTree().getMin();
	// 		// pas sur de l'équilibrage ici
	// 		if(lMax.compareTo(key) < rMin.compareTo(key)) {
	// 			currentTreeReplace = currentTree.getLeftSubTree();
	// 			while(currentTreeReplace.getRoot() != lMax) {
	// 				if(currentTreeReplace.getRightSubTree() != null) {
	// 					parentTreeReplace = currentTreeReplace.getRightSubTree();
	// 				} else {
	// 					parentTreeReplace = currentTreeReplace.getLeftSubTree();
	// 				}
	// 			}
	// 			// TODO
	// 		} else {
	// 			currentTreeReplace = currentTree.getRightSubTree();
	// 			while(currentTreeReplace.getRoot() != rMin) {
	// 				if(currentTreeReplace.getLeftSubTree() != null) {
	// 					parentTreeReplace = currentTreeReplace.getLeftSubTree();
	// 				} else {
	// 					parentTreeReplace = currentTreeReplace.getRightSubTree();
	// 				}
	// 			}
	// 			// TODO
	// 		}
	// 	} 
		
	// 	// un arbre uniquement à gauche
	// 	else if(currentTree.getLeftSubTree() != null) {
	// 		K lMax = currentTree.getLeftSubTree().getMax();
	// 		currentTreeReplace = currentTree.getLeftSubTree();
	// 		while(currentTreeReplace.getRoot() != lMax) {
	// 			if(currentTreeReplace.getRightSubTree() != null) {
	// 				parentTreeReplace = currentTreeReplace.getRightSubTree();
	// 			} else {
	// 				parentTreeReplace = currentTreeReplace.getLeftSubTree();
	// 			}
	// 		}
	// 		// TODO
			
	// 	}
		
	// 	// un arbre uniquement à droite
	// 	else {
	// 		K rMin = currentTree.getRightSubTree().getMin();
	// 		currentTreeReplace = currentTree.getRightSubTree();
	// 		while(currentTreeReplace.getRoot() != rMin) {
	// 			if(currentTreeReplace.getLeftSubTree() != null) {
	// 				parentTreeReplace = currentTreeReplace.getLeftSubTree();
	// 			} else {
	// 				parentTreeReplace = currentTreeReplace.getRightSubTree();
	// 			}
	// 		}
	// 		// TODO
	// 	}

	// 	if(parentTree == null) {
	// 		// TODO
	// 		return this;
	// 	} else {
	// 		// TODO
	// 		// pas bon ca
	// 		return new BinarySearchTreeImpl<K>(
	// 			currentTreeReplace.getRoot(),
	// 			(BinarySearchTreeImpl<K>) currentTree.getLeftSubTree(),
	// 			(BinarySearchTreeImpl<K>) currentTree.getRightSubTree()
	// 		);
	// 	}
	// }

	/* Ya un moyen de le faire en gardant -> final K root */
	@Override
	public BinarySearchTree<K> remove(K key) throws ValueNotFound, ImpossibleDeletion {
		if(key == null) throw new ImpossibleDeletion("key ne peut pas être null");

		// on récupère l'arbre de la valeur et son parent
		BinarySearchTreeImpl<K> nodeToDelete = this;
		BinarySearchTreeImpl<K> parentNodeToDelete = null;
		int resComp;
		do {
			resComp = nodeToDelete.getRoot().compareTo(key);
			if(resComp < 0) {   // key plus grande que moi
				parentNodeToDelete = nodeToDelete;
				nodeToDelete = nodeToDelete.getRightSubTree();
			}
			else if(resComp > 0) {   // key plus petite que moi
				parentNodeToDelete = nodeToDelete;
				nodeToDelete = nodeToDelete.getLeftSubTree();
			}
		} while(resComp != 0 && nodeToDelete != null);

		// Si nodeToDelete est null => clé à suppr absente
		if(nodeToDelete ==  null) throw new ValueNotFound("la clé n'a pas été trouvé");

		// si nodeToDelete est une feuille
		// On peut le supprimer si il a un parent (sinon pas posible)
		if(nodeToDelete.isLeaf()) {
			if(parentNodeToDelete != null) {
				if(parentNodeToDelete.leftSubTree == nodeToDelete) {
					parentNodeToDelete.leftSubTree = null;
				} else {
					parentNodeToDelete.rightSubTree = null;
				}
			} else {
				throw new ImpossibleDeletion("l'arbre est une feuille");
			}
		} 
		
		else if(nodeToDelete.rightSubTree == null) {
			nodeToDelete.root = nodeToDelete.getLeftSubTree().getRoot();
			nodeToDelete.leftSubTree = nodeToDelete.getLeftSubTree().getLeftSubTree();
			nodeToDelete.rightSubTree = nodeToDelete.getLeftSubTree().getRightSubTree();
		} 
		
		else if(nodeToDelete.leftSubTree == null) {
			nodeToDelete.root = nodeToDelete.getRightSubTree().getRoot();
			nodeToDelete.leftSubTree = nodeToDelete.getRightSubTree().getLeftSubTree();
			nodeToDelete.rightSubTree = nodeToDelete.getRightSubTree().getRightSubTree();
		}
		
		else {
			// Aller chercher le max de leftSubTree
			BinarySearchTreeImpl<K> maxNodeLeftSt = nodeToDelete.leftSubTree;
			BinarySearchTreeImpl<K> parentMaxNodeLeftSt = nodeToDelete;
			while(maxNodeLeftSt.rightSubTree != null) {
				parentMaxNodeLeftSt = maxNodeLeftSt;
				maxNodeLeftSt = maxNodeLeftSt.rightSubTree;
			}

			// supression du max node leftSt
			if(parentMaxNodeLeftSt == nodeToDelete) {
				nodeToDelete.leftSubTree = maxNodeLeftSt.leftSubTree;
			} else {
				parentMaxNodeLeftSt.rightSubTree = maxNodeLeftSt.leftSubTree;
			}

			// remplacer la racine de nodeToDelete par celle de maxNodeLeftSt
			nodeToDelete.root = maxNodeLeftSt.root;
		}

		return this;
	}
}