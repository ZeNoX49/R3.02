# L'Optimisateur d'atelier de production de guitares

## Contexte

L'entreprise de guitares Menfer vous a mandaté pour optimiser son atelier de production lorsqu'elle doit concevoir en urgence une guitare pour un artiste.
L'entreprise dispose d'ateliers composés de chaines de montage.

Dans un atelier, toutes les chaînes possèdent le même nombre de postes. Des postes au même emplacement (même indice) ont la même fonction.
Pour construire une guitare entièrement il est donc nécessaire de parcourir les postes de chaque position.
Il est possible de passer par tous les postes d'une même chaîne (ce que fait l'entreprise classiquement), mais également de changer de chaîne pour passer d'un poste i à un poste i+1.

Un parcours peut être donc : <Chaine 0, poste 0>, <Chaine 0, poste 1>, <Chaine 1, poste 2>, <Chaine 1, poste 3>, <Chaine 0, poste 4>...

Chaque poste a un temps de traitement, et chaque chaine a un temps d'entrée (temps nécessaire pour accéder au premier poste) et de sortie (temps nécessaire pour quitter la chaîne lorsque la guitare est terminée).
Le temps de changement d'un poste à l'autre d'une même chaine est négligeable.
Le temps de changement d'un poste à l'autre de 2 chaines différente ne l'est pas.

Le temps complet de construction d'une guitare est donc le temps d'entrée (dépendant de la chaîne choisie pour le premier poste), 
la somme des temps des différents postes utilisés avec possiblement les temps de changement d'une chaine à l'autre selon les postes parcourus, 
et le temps de sortie de la chaîne du dernier poste.

## Objectifs

Vous devez implémenter l'algorithme permettant de calculer le temps minimal pour construire une guitare dans un atelier 
ainsi qu'un des parcours permettant d'atteindre ce temps minimal (sous la forme d'une liste d'indices de chaines).

Dans un premier temps, il vous est demandé de concevoir l'optimisateur pour un atelier "simple", c'est-à-dire un atelier ne possédant que 2 chaînes de production.
Dans un second temps, vous devez fournir une implémentation de l'optimiseur pour un atelier "complexe",  c'est-à-dire un atelier possédant un nombre quelconque de chaines (≥1).

Le modèle (interfaces et classes de poste, chaine, et atelier), la base des optimisateurs (interface et structure de classe) et la base de tests unitaires vous sont déjà fournis.

## Tâche à effectuer

1. implémentation possible de tests unitaires pour vérifier le code de la classe OptimisateurAtelierSimple.
2. implémentation de la classe optimisateuratelier.optimisation.OptimisateurAtelierSimple.
3. implémentation possible de tests unitaires pour vérifier le code de la classe OptimisateurAtelierComplexe.
4. implémentation de la classe optimisateuratelier.optimisation.OptimisateurAtelierComplexe.
5. rédaction d'un rapport de complexité : la justification de la complexité algorithmique et de l'empreinte mémoire des algorithmes d'optimisation de OptimisateurAtelierSimple et OptimisateurAtelierComplexe.
