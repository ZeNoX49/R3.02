/*
 * Copyright (C) 2023 IUT Laval - Le Mans Université.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import optimisateuratelier.atelier.AtelierSimple;
import optimisateuratelier.atelier.ChaineImpl;
import optimisateuratelier.atelier.PosteImpl;

/**
 *
 * @author rvenant
 */
public class AtelierDescriptor {

    public static AtelierSimple generateAtelierSimple(
            int entreC0, int entreC1,
            int sortieC0, int sortieC1,
            int[] chaine0, int[] chaine1,
            int[] tempsChangementC0C1, int[] tempsChangementC1C0) {
        return new AtelierSimple(
                new ChaineImpl(new ArrayList<>(Arrays.stream(chaine0).mapToObj(PosteImpl::new).collect(Collectors.toList())), entreC0, sortieC0),
                new ChaineImpl(new ArrayList<>(Arrays.stream(chaine1).mapToObj(PosteImpl::new).collect(Collectors.toList())), entreC1, sortieC1),
                tempsChangementC0C1, tempsChangementC1C0);
    }

    public static ResultatOptimisation generateResultatOptimisation(int meilleurTemps, Integer[] meilleurParcours) {
        return new ResultatOptimisation(Arrays.asList(meilleurParcours), meilleurTemps);
    }
}
