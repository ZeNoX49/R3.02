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

import java.util.List;

/**
 *
 * @author Rémi Venant
 */
public final class TestedTreeImplementationFactory {

    public final static <V> Tree<V> createTree(V valeur) {
        return new SimpleTreeImpl<V>(valeur);
    }

    public final static <V> Tree<V> createTree(V valeur, List<Tree<V>> foret) {
    	return new SimpleTreeImpl<V>(valeur, foret);
    }

    public final static BinarySearchTree<Integer> createBinarySearchTree(Integer valeur) {
    	return new BinarySearchTreeImpl<Integer>(valeur);
    }

    public final static BinarySearchTree<Integer> createBinarySearchTree(Integer valeur,
            BinarySearchTree<Integer> sag, BinarySearchTree<Integer> sad) {
        return new BinarySearchTreeImpl<Integer>(valeur,
        		(BinarySearchTreeImpl<Integer>) sag,
        		(BinarySearchTreeImpl<Integer>) sad);
    }

    private TestedTreeImplementationFactory() {
    }
}
