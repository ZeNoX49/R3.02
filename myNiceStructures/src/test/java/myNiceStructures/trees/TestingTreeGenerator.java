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

/**
 *
 * @author Rémi Venant
 */
public final class TestingTreeGenerator {

	public static Tree<Boolean> generateGrandArbre() {
		final Tree<Boolean> arbre = TestedTreeImplementationFactory.createTree(true);
		
		Tree<Boolean> arbreParent = arbre;
		Tree<Boolean> arbreEnfant;
		for(int i = 0; i < 100000; i++) {
			arbreEnfant = TestedTreeImplementationFactory.createTree(i%2 == 0);
			arbreParent.getForest().add(arbreEnfant);
			arbreParent = arbreEnfant;
		}
		return arbre;
	}
	
    public static Tree<String> generateArbreTest1() {
        /*
                   a
              b        f   h
           c    d      g
                e
         */
        final Tree<String> a = TestedTreeImplementationFactory.createTree("a");
        final Tree<String> b = TestedTreeImplementationFactory.createTree("b");
        final Tree<String> c = TestedTreeImplementationFactory.createTree("c");
        final Tree<String> d = TestedTreeImplementationFactory.createTree("d");
        final Tree<String> e = TestedTreeImplementationFactory.createTree("e");
        final Tree<String> f = TestedTreeImplementationFactory.createTree("f");
        a.getForest().add(0, b);
        a.getForest().add(1, f);
        a.getForest().add(2, TestedTreeImplementationFactory.createTree("h"));
        b.getForest().add(0, c);
        b.getForest().add(1, d);
        d.getForest().add(0, e);
        f.getForest().add(0, TestedTreeImplementationFactory.createTree("g"));
        return a;
    }

    public static int getHauteurArbreTest1() {
        return 3;
    }

    public static float getHauteurMoyenneArbreTest1() {
        return (2F + 3F + 2F + 1F) / 4F;
    }

    public static int getNbNoeudsArbreTest1() {
        return 8;
    }

    public static int getNbFeuillesArbreTest1() {
        return 4;
    }

    public static String parcoursPrefixe() {
        return "a, b, c, d, e, f, g, h";
    }

    public static String parcoursSuffixe() {
        return "c, e, d, b, g, f, h, a";
    }

    public static String parcoursLargeur() {
        return "a, b, f, h, c, d, g, e";
    }

    public static BinarySearchTree<Integer> generateArbreTestABR() {
        /*
                30
           20         40
         18   22     35  45
          19 21 23  33  44  46
                 27    43     47
         */
        return TestedTreeImplementationFactory.createBinarySearchTree(30,
                TestedTreeImplementationFactory.createBinarySearchTree(20,
                        TestedTreeImplementationFactory.createBinarySearchTree(18, null, TestedTreeImplementationFactory.createBinarySearchTree(19)),
                        TestedTreeImplementationFactory.createBinarySearchTree(22,
                                TestedTreeImplementationFactory.createBinarySearchTree(21),
                                TestedTreeImplementationFactory.createBinarySearchTree(23, null, TestedTreeImplementationFactory.createBinarySearchTree(27))
                        )
                ),
                TestedTreeImplementationFactory.createBinarySearchTree(40,
                        TestedTreeImplementationFactory.createBinarySearchTree(35, TestedTreeImplementationFactory.createBinarySearchTree(33), null),
                        TestedTreeImplementationFactory.createBinarySearchTree(45,
                                TestedTreeImplementationFactory.createBinarySearchTree(44, TestedTreeImplementationFactory.createBinarySearchTree(43), null),
                                TestedTreeImplementationFactory.createBinarySearchTree(46, null, TestedTreeImplementationFactory.createBinarySearchTree(47))
                        )
                )
        );
    }

    public static int getHauteurArbreTestABR() {
        return 4;
    }

    public static float getHauteurMoyenneArbreTestABR() {
        return (3F + 3F + 4F + 3F + 4F + 4F) / 6F;
    }

    public static int getNbNoeudsArbreTestABR() {
        return 16;
    }

    public static int getNbFeuillesArbreTestABR() {
        return 6;
    }

    public static String parcoursPrefixeABR() {
        return "30, 20, 18, 19, 22, 21, 23, 27, 40, 35, 33, 45, 44, 43, 46, 47";
    }

    public static String parcoursSuffixeABR() {
        return "19, 18, 21, 27, 23, 22, 20, 33, 35, 43, 44, 47, 46, 45, 40, 30";
    }

    public static String parcoursLargeurABR() {
        return "30, 20, 40, 18, 22, 35, 45, 19, 21, 23, 33, 44, 46, 27, 43, 47";
    }

    public static Integer getABRMin() {
        return 18;
    }

    public static Integer getABRMax() {
        return 47;
    }

    private TestingTreeGenerator() {

    }
}
