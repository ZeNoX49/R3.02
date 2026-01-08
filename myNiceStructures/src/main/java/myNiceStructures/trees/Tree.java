/*
 * Copyright (C) 2022 IUT Laval - Le Mans Université.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package myNiceStructures.trees;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.function.Consumer;

import myNiceStructures.trees.iterator.TreeDefaultIterator;
import myNiceStructures.trees.iterator.TreePrefixIterator;
import myNiceStructures.trees.iterator.TreeSuffixIterator;
import myNiceStructures.trees.iterator.TreeWidthIterator;

/**
 *
 * @author Rémi Venant
 * @param <V> type de valeurs stockées dans l'arbre.
 */
public interface Tree<V> extends Iterable<V> {
	
    /**
     * Fournit la valeur de la racine de l'arbre.
     *
     * @return la racine
     */
    V getRoot();

    /**
     * Définit la valeur de la racine de l'arbe.
     *
     * @param valeur la valeur
     */
    void setRoot(V valeur);

    /**
     * Fournit les sous-arbres.
     *
     * @return la forêt
     */
    List<Tree<V>> getForest();

    /**
     * Indique si l'arbre est une feuille.
     *
     * @return vrai si la foret du noeud est vide, faux sinon
     */
    default boolean isLeaf() {
    	return this.getForest().isEmpty();
    }

    /**
     * Fournit le nombre de noeuds de l'arbre.
     *
     * @return le nombre de noeuds
     */
    default int getNumNodes() {
		if(this.isLeaf()) return 1;
		
		int numNodes = 1;
		for(Tree<V> a : this.getForest()) {
			numNodes += a.getNumNodes();
		}
		return numNodes;
	}

    /**
     * Fournit le nombre de feuilles de l'arbre.
     *
     * @return le nombre de feuilles
     */
    default int getNumLeaves() {
		if(this.isLeaf()) return 1;
		
		int numLeaves = 0;
		for(Tree<V> a : this.getForest()) {
			numLeaves += a.getNumLeaves();
		}
		return numLeaves;
	}

    /**
     * Calcul la hauteur de l'arbre. La hauteur est la longueur du chemin de
     * plus long entre la racine et les feuilles de l'arbre.
     *
     * @return la hauteur de l'arbre
     */
    default int getHeight() {
		if(this.isLeaf()) return 0;
		
		int height = 0;
		for(Tree<V> a : this.getForest()) {
			int count = a.getHeight();
			if(count > height) {
				height = count;
			}
		}
		return ++height;
	}

    /**
     * Indique si l'arbre contient la valeur (possiblement null).
     *
     * @param value la valeur à rechercher
     * @return vrai si au moins une valeur d'un noeud est égale à value, faux
     * sinon
     */
    default boolean contains(V value) {
		if(this.getRoot() == value) return true;
		
		for(Tree<V> a : this.getForest()) {
			if(a.contains(value)) {
				return true;
			}
		}
		
		return false;
	}

    /**
     * Parcours l'arbre en profondeur préfixe et applique le traitement fourni
     * sur chaque valeur de noeuds.
     *
     * @param nodeProcessor le traitement à appliquer à chaque noeud
     */
    default void processNodesPrefix(Consumer<V> nodeProcessor) {
		nodeProcessor.accept(this.getRoot());
		for(Tree<V> a : this.getForest()) {
			a.processNodesPrefix(nodeProcessor);
		}
	}
    
    default void processNodesPrefixQueue(Consumer<V> nodeProcessor) {
		final Deque<Tree<V>> workingQueue = new ArrayDeque<Tree<V>>();
		workingQueue.add(this);
		
		while(!workingQueue.isEmpty()) {
			Tree<V> currentTree = workingQueue.pollFirst();
			nodeProcessor.accept(currentTree.getRoot());
			ListIterator<Tree<V>> it = currentTree.getForest()
					.listIterator(currentTree.getForest().size());
			while(it.hasPrevious()) {
				workingQueue.addFirst(it.previous());
			}
		}
	}

    /**
     * Parcours l'arbre en profondeur suffixe et applique le traitement fourni
     * sur chaque valeur de noeuds.
     *
     * @param nodeProcessor le traitement à appliquer à chaque noeud
     */
    default void processNodesSuffix(Consumer<V> nodeProcessor) {
		for(Tree<V> a : this.getForest()) {
			a.processNodesSuffix(nodeProcessor);
		}
		nodeProcessor.accept(this.getRoot());
	}
    
    static class TreeForSuffix<V> {
    	public final Tree<V> tree;
    	public final Iterator<Tree<V>> childIterator;
    	
    	public TreeForSuffix(Tree<V> tree) {
    		this.tree = tree;
    		this.childIterator = tree.getForest().iterator();
    	}
    }
    
    default void processNodesSuffixQueue(Consumer<V> nodeProcessor) {
		final Deque<TreeForSuffix<V>> workingStack = new ArrayDeque<>();
		workingStack.add(new TreeForSuffix<>(this));
		
		// Traitement tant que de la pile n'est pas vide
		while(!workingStack.isEmpty()) {
			// Phase 1 : descente
			TreeForSuffix<V> currentTree = workingStack.element();
			while(currentTree.childIterator.hasNext()) {
				currentTree = new TreeForSuffix<>(
						workingStack.element().childIterator.next()
				);
				workingStack.addFirst(currentTree);
			}
			
			// Phase 2 : remontée
			while(!workingStack.isEmpty() && !workingStack.element().childIterator.hasNext()) {
				// retrait de la pile
				currentTree = workingStack.poll();
				// traitement du noeud
				nodeProcessor.accept(currentTree.tree.getRoot());
			}
		}
	}

    /**
     * Parcours l'arbre en largeur et applique le traitement fourni sur chaque
     * valeur de noeuds.
     *
     * @param nodeProcessor le traitement à appliquer à chaque noeud
     */
    default void processNodesWidth(Consumer<V> nodeProcessor) {
		final Queue<Tree<V>> workingQueue = new ArrayDeque<Tree<V>>();
		workingQueue.add(this);
		while(!workingQueue.isEmpty()) {
			Tree<V> currentTree = workingQueue.poll();
			nodeProcessor.accept(currentTree.getRoot());
			workingQueue.addAll(currentTree.getForest());
		}
	}
    
    default Iterator<V> iterator() {
    	return new TreeDefaultIterator<>(this);
	}
    
    /**
     * Génère un intérateur des valeurs des noeuds via un parcour préfixe.
     *
     * @return l'iterateur
     */
    default Iterator<V> iteratorPrefix() {
    	return new TreePrefixIterator<>(this);
	}

    /**
     * Génère un intérateur des valeurs des noeuds via un parcour suffixe.
     *
     * @return l'iterateur
     */
    default Iterator<V> iteratorSuffix() {
    	return new TreeSuffixIterator<>(this);
	}

    /**
     * Génère un intérateur des valeurs des noeuds via un parcour en largeur.
     *
     * @return l'iterateur
     */
    default Iterator<V> iteratorWidth() {
    	return new TreeWidthIterator<>(this);
    }
}