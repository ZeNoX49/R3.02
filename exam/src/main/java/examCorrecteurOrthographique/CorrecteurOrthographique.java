package examCorrecteurOrthographique;

/**
 *
 * @author Remi Venant
 */
public interface CorrecteurOrthographique {

    /**
     * Calcul le nombre de noeuds presents dans l'arbre.
     *
     * @return le nombre de noeuds.
     */
    long getNbNoeuds();

    /**
     * Calcul le nombre de mots presents dans l'arbre. Le nombre de mots est
     * equivalent au nombre de noeuds avec l'indicateur finMot à vrai.
     *
     * @return le nombre de mots
     */
    long getNbMots();

    /**
     * Ajoute un mot au dictionnaire. Si le mot est null, vide (ne contient
     * aucun caractère ou que des caractères blanc), ne fait rien et retourne
     * faux. Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" :
     * enlever les caractères blancs de part et d'autre du mot et le passer en
     * caractères minuscules. Si le mot une fois nettoye est dejà present dans
     * le dictionnaire, retourne faux, sinon retourne vrai.
     *
     * @param mot le mot à ajouter.
     * @return vrai si le mot a ete ajoute, faux sinon (mot null, vide, blanc ou
     * dejà existant).
     */
    boolean ajouterMot(String mot);

    /**
     * Test si un mot existe dans le dictionnaire. Si le mot est null, vide (ne
     * contient aucun caractère ou que des caractères blanc), retourne faux.
     * Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" : enlever
     * les caractères blancs de part et d'autre du mot et le passer en
     * caractères minuscules. Si le mot une fois nettoye est present dans le
     * dictionnaire, retourne vrai, sinon retourne faux.
     *
     * @param mot le mot à tester
     * @return vrai si le mot est present, faux sinon
     */
    boolean motExiste(String mot);

    /**
     * Retire un mot du dictionnaire. Si le mot est null, vide (ne contient
     * aucun caractère ou que des caractères blanc), ne fait rien et retourne
     * faux. Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" :
     * enlever les caractères blancs de part et d'autre du mot et le passer en
     * caractères minuscules. Si le mot une fois nettoye est absent du
     * dictionnaire, retourne faux, sinon retourne vrai après l'avoir retire. Le
     * retrait d'un mot entraine egalement le "nettoyage" de l'arbre : le
     * retrait de tout noeud caractère qui ne serait pas une fin de mot et dont
     * la forêt serait vide.
     *
     * @param mot le mot à retirer
     * @return vrai si le mot a ete retire, faux sinon (mot null, vide, blanc ou
     * absent)
     */
    boolean retirerMot(String mot);

}
