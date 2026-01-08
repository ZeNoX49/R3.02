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

import myNiceStructures.trees.exceptions.ValueNotFound;
import myNiceStructures.trees.exceptions.ImpossibleDeletion;
import myNiceStructures.trees.exceptions.ImpossibleInsertion;

/**
 *
 * @author Rémi Venant
 * @param <K> Type des valeurs des noeud de l'ABR (les valeurs doivent être comparable entre elles).
 */
public interface BinarySearchTree<K extends Comparable<K>> extends Tree<K> {

    /**
     * Fournit le sous-arbre gauche.
     *
     * @return le sag de l'ABR (null si l'ABR n'a pas de sag)
     */
    BinarySearchTree<K> getLeftSubTree();

    /**
     * Fournit le sous-arbre droit.
     *
     * @return le sad de l'ABR (null si l'ABR n'a pas de sag)
     */
    BinarySearchTree<K> getRightSubTree();

    /**
     * Recherche l'arbre (arbre courant ou sous-arbre) dont la racine est de valeur cle.
     *
     * @param key la clé recherchée
     * @return l'arbre dont la racine a pour valeur clé : null si aucun arbre correspond, ou l'arbre
     * ou un sous-arbre
     */
    BinarySearchTree<K> findSubTree(K key);

    /**
     * Indique si l'arbre contient la cle.
     *
     * @param key la clé à rechercher
     * @return vrai si la clé existe, faut sinon
     */
    boolean contains(K key);

    /**
     * Fournit la cle de valeur minimale de l'arbre.
     *
     * @return la clé de plus petite valeur contenue dans l'arbre
     */
    K getMin();

    /**
     * Fournit la cle de valeur maximale de l'arbre.
     *
     * @return la clé de plus grande valeur contenue dans l'arbre
     */
    K getMax();

    /**
     * Ajoute une clé dans l'arbre, si celle-ci n'est pas nulle ou n'est déjà présente.
     *
     * @param key la clé à add
     * @return l'arbre mis à jour
     * @throws ImpossibleInsertion si la clé est nulle
     */
    BinarySearchTree<K> add(K key) throws ImpossibleInsertion;

    /**
     * Retire une clé de l'arbre.
     *
     * @param key la clé à remove
     * @return l'arbre mis à jour
     * @throws ValueNotFound si la clé n'existe pas
     * @throws ImpossibleDeletion si l'arbre est une feuille de valeur clé ou si la clé
     * est nulle
     */
    BinarySearchTree<K> remove(K key) throws ValueNotFound,
            ImpossibleDeletion;
}
