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
package optimisateuratelier.optimisation;

import optimisateuratelier.atelier.AtelierSimple;
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
public class OptimisateurAtelierSimpleTest {

    private OptimisateurAtelier<AtelierSimple> optimisateurTest;

    public OptimisateurAtelierSimpleTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        this.optimisateurTest = new OptimisateurAtelierSimple();
    }

    @AfterEach
    public void tearDown() {
    }

    public AtelierSimple creerAtelier2() {
        return AtelierDescriptor.generateAtelierSimple(5, 3, 5, 10,
                new int[]{15, 20}, new int[]{11, 20}, new int[]{5}, new int[]{2});
    }

    public ResultatOptimisation creerResultatsAtelier2() {
        return AtelierDescriptor.generateResultatOptimisation(41, new Integer[]{1, 0});
    }

    @Test
    public void atelierSimple2PostesTemps() {
        final AtelierSimple atelierTest = this.creerAtelier2();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier2();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getTempsTotal(),
                resultOpti.getTempsTotal(),
                "Le temps optimal calculé est incorrect.");
    }

    @Test
    public void atelierSimple2PostesParcours() {
        final AtelierSimple atelierTest = this.creerAtelier2();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier2();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getChaineParPoste(),
                resultOpti.getChaineParPoste(),
                "L'enchaînement des chaînes est incorrect.");
    }

    public AtelierSimple creerAtelier5() {
        return AtelierDescriptor.generateAtelierSimple(40, 30, 30, 37,
                new int[]{100, 50, 5, 20, 15}, new int[]{120, 10, 15, 20, 15},
                new int[]{5, 5, 2, 3}, new int[]{10, 25, 5, 10});
    }

    public ResultatOptimisation creerResultatsAtelier5() {
        return AtelierDescriptor.generateResultatOptimisation(240, new Integer[]{0, 1, 1, 0, 0});
    }

    @Test
    public void atelierSimple5PostesTemps() {
        final AtelierSimple atelierTest = this.creerAtelier5();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier5();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getTempsTotal(),
                resultOpti.getTempsTotal(),
                "Le temps optimal calculé est incorrect.");
    }

    @Test
    public void atelierSimple5PostesParcours() {
        final AtelierSimple atelierTest = this.creerAtelier5();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier5();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getChaineParPoste(),
                resultOpti.getChaineParPoste(),
                "L'enchaînement des chaînes est incorrect.");
    }

    public AtelierSimple creerAtelier20() {
        return AtelierDescriptor.generateAtelierSimple(12, 18, 5, 18,
                new int[]{12, 11, 16, 18, 1, 12, 17, 15, 17, 17, 15, 13, 9, 17, 17, 16, 5, 18, 11, 3},
                new int[]{2, 13, 8, 4, 12, 18, 20, 1, 15, 12, 19, 19, 8, 3, 3, 4, 13, 3, 12, 3},
                new int[]{10, 20, 9, 8, 11, 16, 11, 6, 3, 16, 16, 13, 4, 10, 4, 20, 6, 2, 6},
                new int[]{3, 6, 1, 7, 14, 20, 15, 18, 15, 20, 15, 12, 8, 17, 20, 20, 20, 7, 3});
    }

    public ResultatOptimisation creerResultatsAtelier20() {
        return AtelierDescriptor.generateResultatOptimisation(216,
                new Integer[]{1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0});
    }

    @Test
    public void atelierSimple20PostesTemps() {
        final AtelierSimple atelierTest = this.creerAtelier20();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier20();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getTempsTotal(),
                resultOpti.getTempsTotal(),
                "Le temps optimal calculé est incorrect.");
    }

    @Test
    public void atelierSimple20PostesParcours() {
        final AtelierSimple atelierTest = this.creerAtelier20();
        final ResultatOptimisation expectedResult = this.creerResultatsAtelier20();

        ResultatOptimisation resultOpti
                = this.optimisateurTest.optimiserAtelier(atelierTest);
        assertNotNull(resultOpti,
                "Le résultat de l'opti ne devrait pas être null.");
        assertEquals(expectedResult.getChaineParPoste(),
                resultOpti.getChaineParPoste(),
                "L'enchaînement des chaînes est incorrect.");
    }

}
