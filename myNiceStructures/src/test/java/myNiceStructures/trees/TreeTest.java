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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Rémi Venant
 */
public class TreeTest {
	Tree<String> tree;

    public TreeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    	// tree = new Tree<>();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testGetRoot() {
        String v = "value";
        Tree arbre = TestedTreeImplementationFactory.createTree(v);
        assertEquals(v, arbre.getRoot(), "Racine incorrect");
    }

    @Test
    public void testGetForest() {
        Tree<String> arbre = TestedTreeImplementationFactory.createTree("value");
        assertNotNull(arbre.getForest(), "La forêt ne devrait pas être nulle");
        List<Tree<String>> f1 = new LinkedList<>();
        arbre = TestedTreeImplementationFactory.createTree("valeur", f1);
        assertSame(f1, arbre.getForest(), "Forêt incorrecte");
    }

    @Test
    public void testIsLeaf() {
        Tree<String> arbre = TestedTreeImplementationFactory.createTree("value");
        assertTrue(arbre.isLeaf(), "l'arbre devrait être une feuille");
        arbre.getForest().add(TestedTreeImplementationFactory.createTree("value2"));
        assertFalse(arbre.isLeaf(), "l'arbre ne devrait pas être une feuille");
    }

    @Test
    public void testGetNumNodes() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        Integer expectedNbFeuilles = TestingTreeGenerator.getNbNoeudsArbreTest1();
        assertEquals(expectedNbFeuilles, arbre.getNumNodes(), "Nombre de noeuds incorrect");
    }
    
    @Test
    public void testGetNumNodesBigTree() {
        Tree<Boolean> arbre = TestingTreeGenerator.generateGrandArbre();
        Integer expectedNbFeuilles = 100000;
        assertEquals(expectedNbFeuilles, arbre.getNumNodes(), "Nombre de noeuds incorrect");
    }

    @Test
    public void testGetNumLeaves() {
        Tree arbre = TestingTreeGenerator.generateArbreTest1();
        Integer expectedNbFeuilles = TestingTreeGenerator.getNbFeuillesArbreTest1();
        assertEquals(expectedNbFeuilles, arbre.getNumLeaves(), "Nombre de feuilles incorrect");
    }
    
    @Test
    public void testGetNumLeavesBigTree() {
        Tree arbre = TestingTreeGenerator.generateGrandArbre();
        Integer expectedNbFeuilles = 1;
        assertEquals(expectedNbFeuilles, arbre.getNumLeaves(), "Nombre de feuilles incorrect");
    }

    @Test
    public void testGetHeight() {
        Tree arbre = TestingTreeGenerator.generateArbreTest1();
        int expectedHauteurArbre = TestingTreeGenerator.getHauteurArbreTest1();
        assertEquals(expectedHauteurArbre, arbre.getHeight(), "Hauteur incorrecte");
    }
    
    @Test
    public void testGetHeightBigTree() {
        Tree arbre = TestingTreeGenerator.generateGrandArbre();
        int expectedHauteurArbre = 100000;
        assertEquals(expectedHauteurArbre, arbre.getHeight(), "Hauteur incorrecte");
    }

    @Test
    public void testContains() {
        Tree arbre = TestingTreeGenerator.generateArbreTest1();
        assertEquals(true, arbre.contains("e"), "\"e\" est contenu l'arbre");
        assertEquals(true, arbre.contains("h"), "\"h\" est contenu l'arbre");
        assertEquals(true, arbre.contains("a"), "\"a\" est contenu l'arbre");
        assertEquals(false, arbre.contains("A"), "\"A\" n'est pas contenu l'arbre");
        assertEquals(false, arbre.contains("z"), "\"z\" n'est contenu l'arbre");
    }

    @Test
    public void testProcessNodesPrefix() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursPrefixe();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesPrefix((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
    
    @Test
    public void testProcessNodesPrefixQueue() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursPrefixe();
        final StringBuilder sb = new StringBuilder();
        ((SimpleTreeImpl) arbre).processNodesPrefixQueue((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
    
    @Test
    public void testProcessNodesPrefixBigTree() {
        Tree<Boolean> arbre = TestingTreeGenerator.generateGrandArbre();
        arbre.processNodesWidth((v) -> {});
        assertTrue(true);
    }

    @Test
    public void testProcessNodesSuffix() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursSuffixe();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesSuffix((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
    
    @Test
    public void testProcessNodesSuffixQueue() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursSuffixe();
        final StringBuilder sb = new StringBuilder();
        ((SimpleTreeImpl) arbre).processNodesSuffixQueue((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
    
    @Test
    public void testProcessNodesSuffixBigTree() {
        Tree<Boolean> arbre = TestingTreeGenerator.generateGrandArbre();
        arbre.processNodesSuffix((v) -> {});
        assertTrue(true);
    }

    @Test
    public void testProcessNodesWidth() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursLargeur();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesWidth((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
    
    @Test
    public void testProcessNodesWidthBigTree() {
        Tree<Boolean> arbre = TestingTreeGenerator.generateGrandArbre();
        arbre.processNodesWidth((v) -> {});
        assertTrue(true);
    }

    @Test
    public void testIteratorPrefix() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursPrefixe();
        final StringBuilder sb = new StringBuilder();
        Iterator<String> it = arbre.iteratorPrefix();
        while (it.hasNext()) {
            sb.append(it.next()).append(", ");
        }
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testIteratorSuffix() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursSuffixe();
        final StringBuilder sb = new StringBuilder();
        Iterator<String> it = arbre.iteratorSuffix();
        while (it.hasNext()) {
            sb.append(it.next()).append(", ");
        }
        final String computedValue = sb.substring(0, sb.length() - 2);;
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testIteratorWidth() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursLargeur();
        final StringBuilder sb = new StringBuilder();
        Iterator<String> it = arbre.iteratorWidth();
        while (it.hasNext()) {
            sb.append(it.next()).append(", ");
        }
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testDefaultIterator() {
        Tree<String> arbre = TestingTreeGenerator.generateArbreTest1();
        String expectedValue = TestingTreeGenerator.parcoursPrefixe();
        final StringBuilder sb = new StringBuilder();
        for (String valeur : arbre) {
            sb.append(valeur).append(", ");
        }
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }
}
