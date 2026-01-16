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

import java.util.Arrays;

import optimisateuratelier.atelier.AtelierSimple;
import optimisateuratelier.atelier.Chaine;

/**
 *
 * @author Rémi Venant
 */
public class OptimisateurAtelierSimple implements OptimisateurAtelier<AtelierSimple> {

    @Override
    public ResultatOptimisation optimiserAtelier(AtelierSimple atelier) {
        // Initiatialisation
        final Chaine c0 = atelier.getChaine(0);
        final Chaine c1 = atelier.getChaine(1);

        int tempsMiniC0 = c0.getTempsEntree() + c0.getPoste(0).getTempsTraitement();
        int tempsMiniC1 = c1.getTempsEntree() + c1.getPoste(0).getTempsTraitement();

        // idxPreC0[i] == indice de la chaine (0 ou 1) par laquelle je dois venir pour avoir le meilleur temps au poste i de la chaine 0
        Integer[] idxChPreC0 = new Integer[atelier.getLongueurChaines() - 1];
        // idxPreC1[i] == indice de la chaine (0 ou 1) par laquelle je dois venir pour avoir le meilleur temps au poste i de la chaine 1
        Integer[] idxChPreC1 = new Integer[atelier.getLongueurChaines() - 1];

        for(int i = 1; i < atelier.getLongueurChaines(); i++) {
            // Pour C0
            int tpsAutreChaine = tempsMiniC1 + atelier.getTempsChangement(1, 0, i - 1);
            if(tempsMiniC0 < tpsAutreChaine) {
                idxChPreC0[i - 1] = 0;
            } else {
                idxChPreC0[i - 1] = 1;
                tempsMiniC0 = tpsAutreChaine;
            }
            tempsMiniC0 += c0.getPoste(i).getTempsTraitement();
            
            // Pour C1
            tpsAutreChaine = tempsMiniC1 + atelier.getTempsChangement(0, 1, i - 1);
            if(tempsMiniC1 < tpsAutreChaine) {
                idxChPreC1[i - 1] = 1;
            } else {
                idxChPreC1[i - 1] = 0;
                tempsMiniC1 = tpsAutreChaine;
            }
            tempsMiniC1 += c1.getPoste(i).getTempsTraitement();
        }

        // Fin (sortie de boucle)
        Integer[] meilleurChemin = new Integer[atelier.getLongueurChaines()];
        int tempsOptimal;

        if(tempsMiniC0 + c0.getTempsSortie() < tempsMiniC1 + c1.getTempsSortie()) {
            tempsOptimal = tempsMiniC0 + c0.getTempsSortie();
            meilleurChemin[meilleurChemin.length - 1] = 0;
        } else {
            tempsOptimal = tempsMiniC1 + c1.getTempsSortie();
            meilleurChemin[meilleurChemin.length - 1] = 1;
        }

        // Remonte le meilleur chemin
        for (int i = meilleurChemin.length; i > 0; i--) {
            meilleurChemin[i - 1] = (meilleurChemin[i] == 0)
                ? idxChPreC0[i - 1] // désigne le poste i, mais stocké en i - 1 car idxChPreC0 est de taille longueurChaine - 1
                : idxChPreC1[i - 1];
        }

        return new ResultatOptimisation(Arrays.asList(meilleurChemin), tempsOptimal);
    }

}