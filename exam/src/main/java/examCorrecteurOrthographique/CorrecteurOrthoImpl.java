package examCorrecteurOrthographique;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*
Question 1
Sachant que la creation d'une `ArrayList`, meme vide, provoque la reservation 
en memoire de 10 emplacements par defaut, quelle amelioration proposeriez-vous 
pour gagner de l'espace en memoire dans la gestion du correcteur ?

On pourrait la remplacer par une HashMap<char, CorrecteurOrthoImpl>
ce qui permettrait aussi de gagner en performance car on aurait pas besoin de passer sur toutes la listes,
mais seulement de faire un get(...) si la clé existe


Question 2
Un autre developpeur a choisi de realiser son correcteur en utilisant une table 
de Hachage `HashMap<String, Boolean>` oÃ¹ les mots sont stockes comme cle et la 
valeur booleene n'est pas utilisee (mise a vrai par defaut). Il pretend obtenir 
de meilleurs "performances" qu'avec votre systeme. Selon vous, quels sont les 
avantages et inconvenients de sa proposition par rapport a la votre ?


 */

/**
 * Correcteur orthographique fonde sur un arbre de caracteres.
 *
 * @author Prenom Nom
 */
public class CorrecteurOrthoImpl implements CorrecteurOrthographique {

    private final char caractere; // Le caractere du noeud
    private final ArrayList<CorrecteurOrthoImpl> arbresCarSuivants; // les noeuds enfant (caracteres possibles suivant le caractere de l'instance)
    private boolean finMot; // indicateur de fin de mot (le caractere de l'instance est un caractere de fin de mot)

    /**
     * Factory publique de construction d'un nouveau correcteur orthographique
     *
     * @return un nouveau correcteur vide
     */
    public static CorrecteurOrthoImpl creerNouveauCorrecteur() {
        // Le correcteur est un arbre de caractere dont la racine est un caractere inutilise (ne represente rien)
        return new CorrecteurOrthoImpl('-');
    }

    /**
     * Constructeur protege pour creer un noeud du correcteur
     *
     * @param caractere le caractere du noeud
     */
    protected CorrecteurOrthoImpl(char caractere) {
        this.caractere = caractere;
        this.arbresCarSuivants = new ArrayList<>(); // la foret n'est jamais null mais est vide
        this.finMot = false;
    }

    /**
     * Calcul le nombre de noeuds presents dans l'arbre.
     *
     * @return le nombre de noeuds.
     */
    @Override
    public long getNbNoeuds() {
        int res = 1;
        
        for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
        	res += node.getNbNoeuds();
        }
        
        return res;
    }

    /**
     * Calcul le nombre de mots presents dans l'arbre. Le nombre de mots est
     * equivalent au nombre de noeuds avec l'indicateur finMot a vrai.
     *
     * @return le nombre de mots.
     */
    @Override
    public long getNbMots() {
    	int res = this.finMot == true ? 1 : 0;
        
        for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
        	res += node.getNbMots();
        }
        
        return res;
    }

    /**
     * Ajoute un mot au dictionnaire. Si le mot est null, vide (ne contient
     * aucun caractere ou que des caracteres blanc), ne fait rien et retourne
     * faux. Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" :
     * enlever les caracteres blancs de part et d'autre du mot et le passer en
     * caracteres minuscules. Si le mot une fois nettoye est deja present dans
     * le dictionnaire, retourne faux, sinon retourne vrai.
     *
     * @param mot le mot a ajouter.
     * @return vrai si le mot a ete ajoute, faux sinon (mot null, vide, blanc ou
     * deja existant).
     */
    @Override
    public boolean ajouterMot(String mot) {
    	if(mot == null) return false;
    	mot = mot.strip().toLowerCase();
    	if(mot.isEmpty()) return false;
        
        return this.ajouterMotRecursif(mot, 0);
    }

    /**
     * Methode interne d'ajout d'un mot "nettoye" permettant le traitement de
     * l'ajout recursivement.
     *
     * @param motNettoye le mot nettoye : non null ni vide, en minuscule et sans
     * caractere blanc de part et d'autre
     * @param idxCaractere l'index du caractere a ajouter dans
     * arbresCarSuivants.
     * @return vrai le mot a ete ajoute (nouveau mot), faux sinon (mot deja
     * present).
     */
    private boolean ajouterMotRecursif(String motNettoye, int idxCaractere) {
    	char lettre = motNettoye.charAt(idxCaractere);
    	
    	// On regarde si c'est la derniere lettre
    	if(idxCaractere + 1 == motNettoye.length()) {
    		// On regarde si la derniere lettre est deja dans la liste
    		for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
    			// Si oui, alors le mot existe déja
	        	if(node.getCaractere() == lettre) {
	        		return false;
	        	}
	        }
    		
			this.arbresCarSuivants.add(new CorrecteurOrthoImpl(lettre, true, new ArrayList<>()));
			return true;
    	}
    	
    	boolean found = false;
    	CorrecteurOrthoImpl nextTree = null; // pour pas avoir d'erreur, la variables sera initialisé quoi qu'il arrive
    	
    	for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
        	if(node.getCaractere() == lettre) {
        		found = true;
        		nextTree = node;
        		break;
        	}
        }
    	if(!found) {
    		nextTree = new CorrecteurOrthoImpl(lettre);
    		this.arbresCarSuivants.add(nextTree);
    	}

    	return nextTree.ajouterMotRecursif(motNettoye, idxCaractere + 1);
    }

    /**
     * Test si un mot existe dans le dictionnaire. Si le mot est null, vide (ne
     * contient aucun caractere ou que des caracteres blanc), retourne faux.
     * Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" : enlever
     * les caracteres blancs de part et d'autre du mot et le passer en
     * caracteres minuscules. Si le mot une fois nettoye est present dans le
     * dictionnaire, retourne vrai, sinon retourne faux.
     *
     * @param mot le mot a tester
     * @return vrai si le mot est present, faux sinon
     */
    @Override
    public boolean motExiste(String mot) {
    	if(mot == null) return false;
    	mot = mot.strip().toLowerCase();
    	if(mot.isEmpty()) return false;
        
        return this.motExisteRecursif(mot, 0);
    }

    /**
     * Methode interne de test de presence d'un mot "nettoye" permettant le
     * traitement recursif.
     *
     * @param motNettoye le mot nettoye : non null ni vide, en minuscule et sans
     * caractere blanc de part et d'autre
     * @param idxCaractere l'index du caractere a tester dans arbresCarSuivants
     * @return vrai le mot est present, faux sinon.
     */
    private boolean motExisteRecursif(String motNettoye, int idxCaractere) {
    	char lettre = motNettoye.charAt(idxCaractere);
    	
    	// si c'est la derniere lettre
		if(idxCaractere + 1 == motNettoye.length()) {
			// si la lettre existe
			for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
	        	if(node.getCaractere() == lettre) {
	        		return true;
	        	}
	        }
			return false;
		}
		
    	for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
        	if(node.getCaractere() == lettre) {
        		return node.motExisteRecursif(motNettoye, idxCaractere + 1);
        	}
        }
    	
    	return false;
    }

    /**
     * Retire un mot du dictionnaire. Si le mot est null, vide (ne contient
     * aucun caractere ou que des caracteres blanc), ne fait rien et retourne
     * faux. Sinon tente d'ajouter le mot en l'ayant au prealable "nettoye" :
     * enlever les caracteres blancs de part et d'autre du mot et le passer en
     * caracteres minuscules. Si le mot une fois nettoye est absent du
     * dictionnaire, retourne faux, sinon retourne vrai apres l'avoir retire. Le
     * retrait d'un mot entraine egalement le "nettoyage" de l'arbre : le
     * retrait de tout noeud caractere qui ne serait pas une fin de mot et dont
     * la foret serait vide.
     *
     * @param mot le mot a retirer
     * @return vrai si le mot a ete retire, faux sinon (mot null, vide, blanc ou
     * absent)
     */
    @Override
    public boolean retirerMot(String mot) {
    	if(mot == null) return false;
    	mot = mot.strip().toLowerCase();
    	if(mot.isEmpty()) return false;
        
        return this.retirerMotRecursif(mot, 0);
    }

    /**
     * Methode interne de retrait d'un mot "nettoye" permettant le traitement du
     * recursivement.
     *
     * @param motNettoye le mot nettoye : non null ni vide, en minuscule et sans
     * caractere blanc de part et d'autre
     * @param idxCaractere l'index du caractere a tester pour retrait dans
     * arbresCarSuivants.
     * @return vrai le mot a ete retire, faux sinon.
     */
    private boolean retirerMotRecursif(String motNettoye, int idxCaractere) {
    	char lettre = motNettoye.charAt(idxCaractere);
    	
    	if(idxCaractere + 1 == motNettoye.length()) {
	    	for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
	        	if(node.getCaractere() == lettre && node.getArbresCarSuivants().size() == 1) {
	        		this.arbresCarSuivants.remove(node);
	        		return true;
	        	}
	        }
	    	return false;
    	}
    	
    	
    	for(CorrecteurOrthoImpl node : this.arbresCarSuivants) {
        	if(node.getCaractere() == lettre) {
        		if(!node.motExisteRecursif(motNettoye, idxCaractere + 1)) {
        			return false;
        		}
        		if(!node.getArbresCarSuivants().isEmpty()) {
        			return false;
        		}
    			this.arbresCarSuivants.remove(node);
        		return true;
        	}
        }
    	
    	return false;
    }
    
    public char getCaractere() { return this.caractere; }
    public boolean isFinMot() { return finMot; }
    public List<CorrecteurOrthoImpl> getArbresCarSuivants() { return this.arbresCarSuivants; }

    /**
     * Constructeur prive uniquement utilise pour les tests
     *
     * @param caractere
     * @param finMot
     * @param arbresCarSuivants
     */
    private CorrecteurOrthoImpl(char caractere, boolean finMot, ArrayList<CorrecteurOrthoImpl> arbresCarSuivants) {
        this.caractere = caractere;
        this.finMot = finMot;
        this.arbresCarSuivants = arbresCarSuivants;
    }

    /**
     * Point d'entree de test de l'arbre binaire de recherche
     *
     * @param args arguments d'execution (inutilises)
     */
    public static void main(String[] args) {
        TestSuite.applyTestSuite();
    }

    static class MyTestException extends RuntimeException {

        public MyTestException() {
        }

        public MyTestException(String message) {
            super(message);
        }

        public MyTestException(String message, Throwable cause) {
            super(message, cause);
        }

    }

    static class AssertThat {

        public static void isNotNull(Object value, String message) {
            if (value == null) {
                throw new MyTestException(message);
            }
        }

        public static void isNull(Object value, String message) {
            if (value != null) {
                throw new MyTestException(message + " Got: " + Objects.toString(value) + ".");
            }
        }

        public static void isTrue(boolean value, String message) {
            if (value == false) {
                throw new MyTestException(message);
            }
        }

        public static void isFalse(boolean value, String message) {
            if (value == true) {
                throw new MyTestException(message);
            }
        }

        public static void isEquals(Object expected, Object tested, String message) {
            if (!Objects.equals(expected, tested)) {
                throw new MyTestException(message + " Expected: " + Objects.toString(expected) + ". got: " + Objects.toString(tested) + ".");
            }
        }

        public static void isSame(Object expected, Object tested, String message) {
            if (expected != tested) {
                throw new MyTestException(message + " Expected: " + Objects.toString(expected) + ". got: " + Objects.toString(tested) + ".");
            }
        }

        public static void fails(Runnable test, Class<? extends Throwable> expectedExeption, String message) {
            try {
                test.run();
                throw new MyTestException(message + " (no exception thrown)");
            } catch (MyTestException ex) {
                throw ex;
            } catch (Throwable ex) {
                if (!expectedExeption.isInstance(ex)) {
                    throw new MyTestException(message + " (Unexpected exception of type " + ex.getClass().getName() + ")");
                }
            }
        }
    }

    static class TestSuite {

        @Target(value = {ElementType.ANNOTATION_TYPE, ElementType.METHOD})
        @Retention(value = RetentionPolicy.RUNTIME)
        public static @interface MyTest {

            public String value() default "";
        }

        public static void applyTestSuite() {
            System.out.println("DEBUT DES TESTS");
            System.err.println();
            int nbTests = 0;
            int nbPassedTests = 0;

            for (Method method : TestSuite.class.getDeclaredMethods()) {
                // Verifie que la methode soit annotee @MyTest et recuperation de l'annotation
                final TestSuite.MyTest myTest = method.getDeclaredAnnotation(TestSuite.MyTest.class);
                if (myTest == null) {
                    continue;
                }
                // Verifie que la methode soit static
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                // Verifie que la methode ne necessite pas de parametre pour etre invoquee
                if (method.getParameterCount() > 0) {
                    continue;
                }
                // Calcul le nom du test : valeur de l'annotation si non vide, nom de la methode sinon
                final String testName = myTest.value() != null && !myTest.value().isEmpty() ? myTest.value() : method.getName();
                // Execution de la methode de test
                try {
                    System.out.println(testName);
                    method.invoke(null);
                    System.out.println("  Succes.");
                    nbTests++;
                    nbPassedTests++;
                } catch (IllegalAccessException | IllegalArgumentException | NullPointerException | ExceptionInInitializerError ex) {
                    // Erreur d'invocation de la methode: ne devrait pas arriver
                    System.err.println("TEST INVOK ERROR of " + method.getName() + " : " + ex.getMessage());
                } catch (InvocationTargetException ex) {
                    String messagePrefix = "  Echec : ";
                    // Si cause est exception de test : affichage de son message
                    if (ex.getCause() instanceof MyTestException) {
                        System.out.println(messagePrefix + ex.getCause().getMessage());
                    } else {
                        System.out.println(messagePrefix + " exception levee (" + ex.getCause().getMessage() + ").");
                    }
                    nbTests++;
                }
            }
            System.out.println(String.format("RESUME : %d test(s) passe(s), %d test(s) echoue(s).", nbPassedTests, nbTests - nbPassedTests));
        }

        private static CorrecteurOrthographique genererTestArbreDico() {
            return new CorrecteurOrthoImpl('-', false, new ArrayList<>(List.of(new CorrecteurOrthoImpl('m', false, new ArrayList<>(List.of(new CorrecteurOrthoImpl('a', true, new ArrayList<>(List.of(new CorrecteurOrthoImpl('t', true, new ArrayList<>())
            ))),
                    new CorrecteurOrthoImpl('i', false, new ArrayList<>(List.of(new CorrecteurOrthoImpl('e', true, new ArrayList<>())
                    )))
            ))),
                    new CorrecteurOrthoImpl('t', false, new ArrayList<>(List.of(new CorrecteurOrthoImpl('u', true, new ArrayList<>())
                    )))
            )));
        }

        @MyTest("TEST de la methode getNbNoeuds.")
        public static void testGetNbNoeuds() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            long expectedNbNoeuds = 8;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
        }

        @MyTest("TEST de la methode getNbMots.")
        public static void testGetNbMots() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            long expectedNbMots = 4;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode ajouterMot avec des mots nouveaux.")
        public static void testAjoutNouveauxMots() {
            CorrecteurOrthographique arbreDico = creerNouveauCorrecteur();
            boolean res = arbreDico.ajouterMot("salut");
            AssertThat.isTrue(res, "L'ajoute du nouveau mot 'salut' devrait retourner vrai.");
            res = arbreDico.ajouterMot("salutation");
            AssertThat.isTrue(res, "L'ajoute du nouveau mot 'salutation' devrait retourner vrai.");
            res = arbreDico.ajouterMot("super");
            AssertThat.isTrue(res, "L'ajoute du nouveau mot 'super' devrait retourner vrai.");
            long expectedNbNoeuds = 15;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect apres ajout: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
            long expectedNbMots = 3;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect apres ajout: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode ajouterMot avec des mots null ou vide.")
        public static void testAjoutMotNullOuVide() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.ajouterMot(null);
            AssertThat.isFalse(res, "L'ajoute du mot null devrait retourner false.");
            res = arbreDico.ajouterMot("");
            AssertThat.isFalse(res, "L'ajoute du mot vide '' devrait retourner false.");
            res = arbreDico.ajouterMot("   ");
            AssertThat.isFalse(res, "L'ajoute du mot vide '' devrait retourner false.");
            res = arbreDico.ajouterMot(" \t \t  ");
            AssertThat.isFalse(res, "L'ajoute du mot vide ' \t \t  ' devrait retourner false.");
        }

        @MyTest("TEST de la methode ajouterMot avec des mots existant.")
        public static void testAjoutMotExistant() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.ajouterMot("ma");
            AssertThat.isFalse(res, "L'ajoute du mot 'ma' devrait retourner false.");
            res = arbreDico.ajouterMot("mat");
            AssertThat.isFalse(res, "L'ajoute du mot 'mat' devrait retourner false.");
        }

        @MyTest("TEST de la methode ajouterMot avec des mots existant de casse differentes et/ou avec des espaces.")
        public static void testAjoutMotExistantCaseSpace() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.ajouterMot("MA");
            AssertThat.isFalse(res, "L'ajout du mot 'MA' devrait retourner false.");
            res = arbreDico.ajouterMot("mA");
            AssertThat.isFalse(res, "L'ajout du mot 'mA' devrait retourner false.");
            res = arbreDico.ajouterMot("  mA");
            AssertThat.isFalse(res, "L'ajout du mot '  mA' devrait retourner false.");
            res = arbreDico.ajouterMot("Ma  ");
            AssertThat.isFalse(res, "L'ajout du mot 'Ma  ' devrait retourner false.");
            res = arbreDico.ajouterMot("  MA  ");
            AssertThat.isFalse(res, "L'ajout du mot '  MA  ' devrait retourner false.");
        }

        @MyTest("TEST de la methode motExiste avec des mots present, avec des casse differentes et/ou avec des espaces.")
        public static void testMotExistePresent() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.motExiste("mat");
            AssertThat.isTrue(res, "le mot 'mat' devrait etre present.");
            res = arbreDico.motExiste("mAt");
            AssertThat.isTrue(res, "le mot 'mAt' devrait etre present.");
            res = arbreDico.motExiste("ma");
            AssertThat.isTrue(res, "le mot 'ma' devrait etre present.");
            res = arbreDico.motExiste("  mAt");
            AssertThat.isTrue(res, "le mot '  mAt' devrait etre present.");
            res = arbreDico.motExiste("mAt  ");
            AssertThat.isTrue(res, "le mot 'mAt  ' devrait etre present.");
            res = arbreDico.motExiste("  mAt  ");
            AssertThat.isTrue(res, "le mot '  mAt  ' devrait etre present.");
        }

        @MyTest("TEST de la methode motExiste avec un mot null ou vide.")
        public static void testMotInexistantNullOuVide() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.motExiste(null);
            AssertThat.isFalse(res, "le mot null devrait etre absent.");
            res = arbreDico.motExiste("");
            AssertThat.isFalse(res, "le mot '' devrait etre absent.");
            res = arbreDico.motExiste("   ");
            AssertThat.isFalse(res, "le mot '   ' devrait etre absent.");
            res = arbreDico.motExiste(" \t \t ");
            AssertThat.isFalse(res, "le mot ' \t \t ' devrait etre absent.");
        }

        @MyTest("TEST de la methode motExiste avec un mot inexistant.")
        public static void testMotExisteAbsent() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.motExiste("soupe");
            AssertThat.isFalse(res, "le mot 'soupe' devrait etre absent.");
        }

        @MyTest("TEST de la methode motExiste avec un mot inexistant mais dont un prefixe existe et est un mot lui-meme.")
        public static void testMotInexistantRadicalPresent() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.motExiste("max");
            AssertThat.isFalse(res, "le mot 'max' devrait etre absent.");
        }

        @MyTest("TEST de la methode retirerMot avec un mot existant sans provoquer de changement de structure.")
        public static void testRetraitMotPresentSansRetraitNoeud() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot("ma");
            AssertThat.isTrue(res, "le mot 'ma' devrait etre retire.");
            long expectedNbNoeuds = 8;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect apres retrait: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
            long expectedNbMots = 3;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect apres retrait: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode retirerMot avec un mot existant sans provoquer de changement de structure, avec des casse differentes et/ou avec des espaces.")
        public static void testRetraitMotPresentSansRetraitNoeudCaseSpace() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot("  MA  ");
            AssertThat.isTrue(res, "le mot '  MA  ' devrait etre retire.");
            long expectedNbNoeuds = 8;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect apres retrait: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
            long expectedNbMots = 3;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect apres retrait: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode retirerMot avec un mot existant avec changement de structure.")
        public static void testRetraitMotPresentAvecRetraitNoeuds() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot("mie");
            AssertThat.isTrue(res, "le mot 'ma' devrait etre retire.");
            long expectedNbNoeuds = 6;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect apres retrait: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
            long expectedNbMots = 3;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect apres retrait: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode retirerMot avec un mot existant avec changement de structure, avec des casse differentes et/ou avec des espaces.")
        public static void testRetraitMotPresentAvecRetraitNoeudsCaseSpace() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot("  MIE  ");
            AssertThat.isTrue(res, "le mot 'ma' devrait etre retire.");
            long expectedNbNoeuds = 6;
            long computedNbNoeuds = arbreDico.getNbNoeuds();
            AssertThat.isEquals(expectedNbNoeuds, computedNbNoeuds, String.format("Nombre de noeuds incorrect apres retrait: %d attendus, %d calcules.", expectedNbNoeuds, computedNbNoeuds));
            long expectedNbMots = 3;
            long computedNbMots = arbreDico.getNbMots();
            AssertThat.isEquals(expectedNbMots, computedNbMots, String.format("Nombre de mots incorrect apres retrait: %d attendus, %d calcules.", expectedNbMots, computedNbMots));
        }

        @MyTest("TEST de la methode retirerMot avec un mot absent.")
        public static void testRetraitMotAbsent() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot("mix");
            AssertThat.isFalse(res, "le mot 'mix' ne devrait pas etre retire car absent.");
            res = arbreDico.retirerMot("mies");
            AssertThat.isFalse(res, "le mot 'mies' ne devrait pas etre retire car absent.");
            res = arbreDico.retirerMot("mue");
            AssertThat.isFalse(res, "le mot 'mue' ne devrait pas etre retire car absent.");
        }

        @MyTest("TEST de la methode retirerMot avec un mot null ou vide.")
        public static void testRetraitMotNullOuVide() {
            CorrecteurOrthographique arbreDico = genererTestArbreDico();
            boolean res = arbreDico.retirerMot(null);
            AssertThat.isFalse(res, "le mot null ne devrait pas etre retire car absent.");
            res = arbreDico.retirerMot("");
            AssertThat.isFalse(res, "le mot '' ne devrait pas etre retire car absent.");
            res = arbreDico.retirerMot("   ");
            AssertThat.isFalse(res, "le mot '   ' ne devrait pas etre retire car absent.");
            res = arbreDico.retirerMot(" \t \t ");
            AssertThat.isFalse(res, "le mot ' \t \t ' ne devrait pas etre retire car absent.");
        }
    }
}
