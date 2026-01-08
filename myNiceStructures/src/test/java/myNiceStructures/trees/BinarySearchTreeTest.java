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
import java.util.Iterator;
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
public class BinarySearchTreeTest {

    public BinarySearchTreeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testConstruction() {
        BinarySearchTree<Integer> a = TestedTreeImplementationFactory.createBinarySearchTree(0);
        assertNotNull(a.getRoot(), "La racine de l'ABR ne devrait pas être nulle");
        assertNull(a.getLeftSubTree(), "Le SAG de l'ABR devrait être nulle");
        assertNull(a.getRightSubTree(), "Le SAD de l'ABR devrait être nulle");
    }

    @Test
    public void testConstructionRacineNulle() {
        assertThrows(NullPointerException.class, () -> {
            BinarySearchTree<Integer> a = TestedTreeImplementationFactory.createBinarySearchTree(null);
        }, "Il ne devrait pas être possible de construire un ABR avec une racine nulle");
    }

    @Test
    public void testGetRoot() {
        Integer v = 5;
        Tree arbre = TestedTreeImplementationFactory.createBinarySearchTree(v);
        assertEquals(v, arbre.getRoot(), "La racine n'a pas la bonne valeur");
    }

    @Test
    public void testSetRoot() {
        BinarySearchTree<Integer> a = TestedTreeImplementationFactory.createBinarySearchTree(0);
        Integer v2 = 2;
        assertThrows(UnsupportedOperationException.class, () -> {
            a.setRoot(v2);
        }, "Redéfinir la racine d'un ABR devrait être impossible");
    }

    @Test
    public void testGetForest() {
        BinarySearchTree<Integer> arbre = TestedTreeImplementationFactory.createBinarySearchTree(0);
        List<Tree<Integer>> foret = arbre.getForest();
        assertNull(arbre.getLeftSubTree(), "Le SAG devrait être nul");
        assertNull(arbre.getRightSubTree(), "Le SAD devrait être nul");
        assertNotNull(foret, "La forêt ne devrait pas être null");
        assertEquals(0, foret.size(), "l'arbre devrait avoir une foret de taille 0");
        assertTrue(arbre.isLeaf(), "L'arbre devrait être une feuille");
        arbre = TestingTreeGenerator.generateArbreTestABR();
        foret = arbre.getForest();
        assertNotNull(arbre.getLeftSubTree(), "Le SAG ne devrait pas être nul");
        assertNotNull(arbre.getRightSubTree(), "Le SAD ne devrait pas être nul");
        assertNotNull(foret, "La forêt ne devrait ne pas être null");
        assertEquals(2, foret.size(), "l'arbre devrait avoir une foret de taille 2");
        assertFalse(arbre.isLeaf(), "L'arbre ne devrait pas être une feuille");
    }

    @Test
    public void testUpdateForest() {
        BinarySearchTree<Integer> arbre = TestedTreeImplementationFactory.createBinarySearchTree(0);
        List<Tree<Integer>> foret = arbre.getForest();
        assertThrows(UnsupportedOperationException.class, () -> {
            foret.add(TestedTreeImplementationFactory.createBinarySearchTree(3));
        }, "Modifier la forêt d'un ABR devrait être impossible");
        assertThrows(UnsupportedOperationException.class, () -> {
            foret.remove(0);
        }, "Modifier la forêt d'un ABR devrait être impossible");
    }

    /**
     * Test of isLeaf method, of class ArbreImpl.
     */
    @Test
    public void testIsLeaf() {
        BinarySearchTree<Integer> arbre = TestedTreeImplementationFactory.createBinarySearchTree(0);
        assertTrue(arbre.isLeaf(), "l'arbre devrait être une feuille");
        arbre = TestedTreeImplementationFactory.createBinarySearchTree(2, TestedTreeImplementationFactory.createBinarySearchTree(0), null);
        assertFalse(arbre.isLeaf(), "l'arbre ne devrait pas être une feuille");
    }

    @Test
    public void testGetNumNodes() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        int expectedNbFeuilles = TestingTreeGenerator.getNbNoeudsArbreTestABR();
        assertEquals(expectedNbFeuilles, arbre.getNumNodes(), "Nombre de noeuds incorrect");
    }

    @Test
    public void testGetNumLeaves() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        int expectedNbFeuilles = TestingTreeGenerator.getNbFeuillesArbreTestABR();
        assertEquals(expectedNbFeuilles, arbre.getNumLeaves(), "Nombre de feuilles incorrect");
    }

    @Test
    public void testGetHeight() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        int expectedHauteurArbre = TestingTreeGenerator.getHauteurArbreTestABR();
        assertEquals(expectedHauteurArbre, arbre.getHeight(), "Hauteur incorrecte");
    }

    @Test
    public void testProcessNodesPrefix() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursPrefixeABR();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesPrefix((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testProcessNodesSuffix() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursSuffixeABR();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesSuffix((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testProcessNodesWidth() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursLargeurABR();
        final StringBuilder sb = new StringBuilder();
        arbre.processNodesWidth((v) -> sb.append(v).append(", "));
        final String computedValue = sb.substring(0, sb.length() - 2);
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testIteratorPrefix() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursPrefixeABR();
        StringBuilder sb = new StringBuilder();
        Iterator<Object> it = arbre.iteratorPrefix();
        while (it.hasNext()) {
            sb.append(it.next().toString()).append(", ");
        }
        String computedValue = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testIteratorSuffix() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursSuffixeABR();
        StringBuilder sb = new StringBuilder();
        Iterator<Object> it = arbre.iteratorSuffix();
        while (it.hasNext()) {
            sb.append(it.next().toString()).append(", ");
        }
        String computedValue = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testIteratorWidth() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursLargeurABR();
        StringBuilder sb = new StringBuilder();
        Iterator<Object> it = arbre.iteratorWidth();
        while (it.hasNext()) {
            sb.append(it.next().toString()).append(", ");
        }
        String computedValue = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testDefaultIterator() {
        Tree arbre = TestingTreeGenerator.generateArbreTestABR();
        String expectedValue = TestingTreeGenerator.parcoursPrefixeABR();
        StringBuilder sb = new StringBuilder();
        for (Object value : arbre) {
            sb.append(value.toString()).append(", ");
        }
        String computedValue = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
        assertEquals(expectedValue, computedValue, "Parcours invalide");
    }

    @Test
    public void testFindSubTree() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        BinarySearchTree<Integer> ssArbre;
        ssArbre = arbre.findSubTree(45);
        assertNotNull(ssArbre, "la recherche devrait être fructueuse");
        ssArbre = arbre.findSubTree(21);
        assertNotNull(ssArbre, "la recherche devrait être fructueuse");
        ssArbre = arbre.findSubTree(28);
        assertNull(ssArbre, "la recherche ne devrait pas être fructueuse");
    }

    @Test
    public void testContains() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        assertTrue(arbre.contains(45), "45 devrait être trouvé");
        assertTrue(arbre.contains(21), "21 devrait être trouvé");
        assertFalse(arbre.contains(28), "28 ne devrait pas être trouvé");
    }

    @Test
    public void testGetMin() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        assertEquals(TestingTreeGenerator.getABRMin(), arbre.getMin());
    }

    @Test
    public void testGetMax() {
        BinarySearchTree<Integer> arbre = TestingTreeGenerator.generateArbreTestABR();
        assertEquals(TestingTreeGenerator.getABRMax(), arbre.getMax());
    }

    @Test
    public void testAdd() throws ImpossibleInsertion {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(2);
        abr.add(3).add(4).add(5).add(1);
        assertEquals(2, abr.getRoot(), "Valeur de racine incohérente");
        assertNotNull(abr.getLeftSubTree(), "root->sag ne devrait pas être null");
        assertNotNull(abr.getRightSubTree(), "root->sad ne devrait pas être null");
        assertEquals(1, abr.getLeftSubTree().getRoot(), "Valeur incohérente");
        assertEquals(3, abr.getRightSubTree().getRoot(), "Valeur incohérente");
        assertTrue(abr.getLeftSubTree().getLeftSubTree() == null, "root->sag->sag devrait être null");
        assertTrue(abr.getLeftSubTree().getRightSubTree() == null, "root->sag->sad devrait être null");

        assertTrue(abr.getRightSubTree().getLeftSubTree() == null, "root->sad->sag devrait être null");
        assertNotNull(abr.getRightSubTree().getRightSubTree(), "root->sag->sad ne devrait pas être null");
        assertEquals(4, abr.getRightSubTree().getRightSubTree().getRoot(), "Valeur incohérente");

        assertTrue(abr.getRightSubTree().getRightSubTree().getLeftSubTree() == null, "root->sad->sad->sag devrait être null");
        assertNotNull(abr.getRightSubTree().getRightSubTree().getRightSubTree(), "root->sag->sad->sad ne devrait pas être null");
        assertEquals(5, abr.getRightSubTree().getRightSubTree().getRightSubTree().getRoot(), "Valeur incohérente");
    }

    @Test
    public void testImpossibleRootDeletion() {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(2);
        assertThrows(ImpossibleDeletion.class, () -> {
            abr.remove(2);
        }, "Retirer la valeur d'un ABR ne contenant que cette valeur devrait être impossible");
    }

    @Test
    public void testRootLSTDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(3)
                .add(2).add(1);
        abr.remove(3);
        assertEquals(2, abr.getRoot(), "La racine devrait être 2");
        assertNotNull(abr.getLeftSubTree(), "root->sag ne devrait pas être null");
        assertTrue(abr.getRightSubTree() == null, "root->sad devrait être null");
        assertTrue(abr.getLeftSubTree().getLeftSubTree() == null, "root->sag->sag devrait être null");
        assertTrue(abr.getLeftSubTree().getRightSubTree() == null, "root->sag->sad devrait être null");
        assertEquals(1, abr.getLeftSubTree().getRoot(), "root->sag devrait être 1");
    }

    @Test
    public void testRootRSTDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(1).add(2).add(3);
        abr.remove(1);
        System.out.println(abr.getRoot());
        assertEquals(2, abr.getRoot(), "La racine devrait être 2");
        System.out.println(abr.getRightSubTree());
        assertNotNull(abr.getRightSubTree(), "root->sad ne devrait pas être null");
        System.out.println(abr.getLeftSubTree() == null);
        assertTrue(abr.getLeftSubTree() == null, "root->sag devrait être null");
        System.out.println(abr.getRightSubTree().getLeftSubTree() == null);
        assertTrue(abr.getRightSubTree().getLeftSubTree() == null, "root->sad->sag devrait être null");
        System.out.println(abr.getRightSubTree().getRightSubTree() == null);
        assertTrue(abr.getRightSubTree().getRightSubTree() == null, "root->sad->sad devrait être null");
        System.out.println(abr.getRightSubTree().getRoot());
        assertEquals(3, abr.getRightSubTree().getRoot(), "root->sad devrait être 1");
    }

    @Test
    public void testRootLSTLSTDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(4)
                .add(2).add(6).add(1).add(3).add(5).add(7);
        abr.remove(4);
        assertEquals(3, abr.getRoot(), "La racine devrait être 3");
        final BinarySearchTree<Integer> rSag = abr.getLeftSubTree();
        final BinarySearchTree<Integer> rSad = abr.getRightSubTree();
        assertNotNull(rSag, "root->sag ne devrait pas être null");
        assertEquals(2, rSag.getRoot(), "root->sag devrait être égal à 2");
        assertNotNull(rSad, "root->sad ne devrait pas être null");
        assertEquals(6, rSad.getRoot(), "root->sad devrait être égal à 6");

        final BinarySearchTree<Integer> rSagSag = rSag.getLeftSubTree();
        final BinarySearchTree<Integer> rSagSad = rSag.getRightSubTree();
        assertNotNull(rSagSag, "root->sag->sag ne devrait pas être null");
        assertEquals(1, rSagSag.getRoot(), "root->sag->sad devrait être égal à 1");
        assertTrue(rSagSad == null, "root->sag->sad devrait être null");

        final BinarySearchTree<Integer> rSadSag = rSad.getLeftSubTree();
        final BinarySearchTree<Integer> rSadSad = rSad.getRightSubTree();
        assertNotNull(rSadSag, "root->sad->sag ne devrait pas être null");
        assertEquals(5, rSadSag.getRoot(), "root->sad->sag devrait être égal à 5");
        assertNotNull(rSadSad, "root->sad->sad ne devrait pas être null");
        assertEquals(7, rSadSad.getRoot(), "root->sad->sad devrait être égal à 7");
    }

    @Test
    public void testLSTLeafDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr
                = TestedTreeImplementationFactory.createBinarySearchTree(4).add(2).add(6)
                        .add(1).add(3).add(5).add(7);
        abr.remove(1);
        assertEquals(4, abr.getRoot(), "La racine devrait être 4");
        final BinarySearchTree<Integer> rSag = abr.getLeftSubTree();
        final BinarySearchTree<Integer> rSad = abr.getRightSubTree();
        assertNotNull(rSag, "root->sag ne devrait pas être null");
        assertEquals(2, rSag.getRoot(), "root->sag devrait être égal à 2");
        assertNotNull(rSad, "root->sad ne devrait pas être null");
        assertEquals(6, rSad.getRoot(), "root->sad devrait être égal à 6");

        final BinarySearchTree<Integer> rSagSag = rSag.getLeftSubTree();
        final BinarySearchTree<Integer> rSagSad = rSag.getRightSubTree();
        assertTrue(rSagSag == null, "root->sag->sag devrait être null");
        assertNotNull(rSagSad, "root->sag->sad ne devrait pas être null");
        assertEquals(3, rSagSad.getRoot(), "root->sag->sad devrait être égal à 3");

        final BinarySearchTree<Integer> rSadSag = rSad.getLeftSubTree();
        final BinarySearchTree<Integer> rSadSad = rSad.getRightSubTree();
        assertNotNull(rSadSag, "root->sad->sag ne devrait pas être null");
        assertEquals(5, rSadSag.getRoot(), "root->sad->sag devrait être égal à 5");
        assertNotNull(rSadSad, "root->sad->sad ne devrait pas être null");
        assertEquals(7, rSadSad.getRoot(), "root->sad->sad devrait être égal à 7");
    }

    @Test
    public void testLSTDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr
                = TestedTreeImplementationFactory.createBinarySearchTree(4).add(2).add(6)
                        .add(1).add(3).add(5).add(7);
        abr.remove(2);
        assertEquals(4, abr.getRoot(), "La racine devrait être 4");
        final BinarySearchTree<Integer> rSag = abr.getLeftSubTree();
        final BinarySearchTree<Integer> rSad = abr.getRightSubTree();
        assertNotNull(rSag, "root->sag ne devrait pas être null");
        assertEquals(1, rSag.getRoot(), "root->sag devrait être égal à 1");
        assertNotNull(rSad, "root->sad ne devrait pas être null");
        assertEquals(6, rSad.getRoot(), "root->sad devrait être égal à 6");

        final BinarySearchTree<Integer> rSagSag = rSag.getLeftSubTree();
        final BinarySearchTree<Integer> rSagSad = rSag.getRightSubTree();
        assertTrue(rSagSag == null, "root->sag->sag devrait être null");
        assertNotNull(rSagSad, "root->sag->sad ne devrait pas être null");
        assertEquals(3, rSagSad.getRoot(), "root->sag->sad devrait être égal à 3");

        final BinarySearchTree<Integer> rSadSag = rSad.getLeftSubTree();
        final BinarySearchTree<Integer> rSadSad = rSad.getRightSubTree();
        assertNotNull(rSadSag, "root->sad->sag ne devrait pas être null");
        assertEquals(5, rSadSag.getRoot(), "root->sad->sag devrait être égal à 5");
        assertNotNull(rSadSad, "root->sad->sad ne devrait pas être null");
        assertEquals(7, rSadSad.getRoot(), "root->sad->sad devrait être égal à 7");
    }

    @Test
    public void testRSTLeafDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr = TestedTreeImplementationFactory.createBinarySearchTree(4)
                .add(2).add(6)
                .add(1).add(3).add(5).add(7);
        abr.remove(7);
        assertEquals(4, abr.getRoot(), "La racine devrait être 4");
        final BinarySearchTree<Integer> rSag = abr.getLeftSubTree();
        final BinarySearchTree<Integer> rSad = abr.getRightSubTree();
        assertNotNull(rSag, "root->sag ne devrait pas être null");
        assertEquals(2, rSag.getRoot(), "root->sag devrait être égal à 2");
        assertNotNull(rSad, "root->sad ne devrait pas être null");
        assertEquals(6, rSad.getRoot(), "root->sad devrait être égal à 6");

        final BinarySearchTree<Integer> rSagSag = rSag.getLeftSubTree();
        final BinarySearchTree<Integer> rSagSad = rSag.getRightSubTree();
        assertNotNull(rSagSag, "root->sag->sag ne devrait pas être null");
        assertEquals(1, rSagSag.getRoot(), "root->sag->sag devrait être égal à 1");
        assertNotNull(rSagSad, "root->sag->sad ne devrait pas être null");
        assertEquals(3, rSagSad.getRoot(), "root->sag->sad devrait être égal à 3");

        final BinarySearchTree<Integer> rSadSag = rSad.getLeftSubTree();
        final BinarySearchTree<Integer> rSadSad = rSad.getRightSubTree();
        assertNotNull(rSadSag, "root->sad->sag ne devrait pas être null");
        assertEquals(5, rSadSag.getRoot(), "root->sad->sag devrait être égal à 5");
        assertTrue(rSadSad == null, "root->sad->sad devrait être null");
    }

    @Test
    public void testRSTDeletion() throws ImpossibleInsertion, ImpossibleDeletion, ValueNotFound {
        final BinarySearchTree<Integer> abr
                = TestedTreeImplementationFactory.createBinarySearchTree(4).add(2).add(6)
                        .add(1).add(3).add(5).add(7);
        abr.remove(6);
        assertEquals(4, abr.getRoot(), "La racine devrait être 4");
        final BinarySearchTree<Integer> rSag = abr.getLeftSubTree();
        final BinarySearchTree<Integer> rSad = abr.getRightSubTree();
        assertNotNull(rSag, "root->sag ne devrait pas être null");
        assertEquals(2, rSag.getRoot(), "root->sag devrait être égal à 2");
        assertNotNull(rSad, "root->sad ne devrait pas être null");
        assertEquals(5, rSad.getRoot(), "root->sad devrait être égal à 5");

        final BinarySearchTree<Integer> rSagSag = rSag.getLeftSubTree();
        final BinarySearchTree<Integer> rSagSad = rSag.getRightSubTree();
        assertNotNull(rSagSag, "root->sag->sag ne devrait pas être null");
        assertEquals(1, rSagSag.getRoot(), "root->sag->sag devrait être égal à 1");
        assertNotNull(rSagSad, "root->sag->sad ne devrait pas être null");
        assertEquals(3, rSagSad.getRoot(), "root->sag->sad devrait être égal à 3");

        final BinarySearchTree<Integer> rSadSag = rSad.getLeftSubTree();
        final BinarySearchTree<Integer> rSadSad = rSad.getRightSubTree();
        assertTrue(rSadSag == null, "root->sad->sag devrait être null");
        assertNotNull(rSadSad, "root->sad->sad ne devrait pas être null");
        assertEquals(7, rSadSad.getRoot(), "root->sad->sad devrait être égal à 7");
    }
}
