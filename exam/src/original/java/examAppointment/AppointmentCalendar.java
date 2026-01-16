package examAppointment;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * Gestionnaire de calendrier de rendez-vous.
 *
 * @author Remi Venant
 */
public interface AppointmentCalendar {

    /**
     * Créer un nouveau rendez-vous, l'ajoute au rendez-vous et le retourne. le
     * début start fourni sera tronqué à la minute dans le rendez-vous créé. la
     * durée de rendez-vous doit être strictement positive et inférieure ou
     * égale à 24h. Le nom name fourni ne peut pas être null ou vide (de taille
     * 0 ou constitué que de caractère "blancs" (espaces, saut de ligne,
     * tabulation...)) et sera tronqué de part et d'autre de ses possibles
     * caractères blancs.
     *
     * @param start le début du rende-vous.
     * @param durationMinutes la durée en minute du rendez-vous.
     * @param name le nom du rendez-vous.
     * @return le rendez-vous créé et ajouté au calendrier.
     * @throws IllegalArgumentException si start est null, durationMinutes est
     * inférieur ou égale à 0 ou strictement supérieur à 24h, si name est null
     * ou vide, ou si un rendez-vous de même début et nom (sans respect de la
     * casse) est déjà présent.
     */
    Appointment add(LocalDateTime start, int durationMinutes, String name) throws IllegalArgumentException;

    /**
     * Retire un rendez-vous de calendrier d'après son début et son nom. le
     * début start fourni sera tronqué à la minute pour la recherche du
     * rendez-vous. Le nom name fourni ne peut pas être null ou vide (de taille
     * 0 ou constitué que de caractère "blancs" (espaces, saut de ligne,
     * tabulation...)) et sera tronqué de part et d'autre de ses possibles
     * caractères blancs.
     *
     * @param start le début du rende-vous.
     * @param name le nom du rendez-vous.
     * @return vrai si le rendez-vous a été supprimé, faux si ce dernier
     * n'existe pas.
     * @throws IllegalArgumentException si start est null, ou si name est null
     * ou vide.
     */
    boolean remove(LocalDateTime start, String name) throws IllegalArgumentException;

    /**
     * Retourne une collection non modifiable de rendez-vous débutant entre
     * start (inclus) et end (exlus) tronqués tous les deux à la minute. les
     * paramètre start et end peuvent être null mais au moins l'un deux doit
     * être donné non null. Si start est null, la collection retournée équivaut
     * à tous les rendez-vous débutant avant end tronqué à la minute (exclus).
     * Si end est null, la collection retournée équivaut à tous les rendez-vous
     * débutant après start tronqué à la minute (inclus).
     *
     * @param start la borne de recherche antérieure.
     * @param end la borne de recherche postérieure.
     * @return la collection de rendez-vous non modifiable.
     * @throws NullPointerException si start et end sont nulls tous les deux.
     * @throws IllegalArgumentException si start et end sont non nulls et si
     * start est postérieure à null (start > null).
     */
    Collection<Appointment> findAllStartingBetween(LocalDateTime start, LocalDateTime end) throws NullPointerException, IllegalArgumentException;

    /**
     * Retourne une collection non modifiable de rendez-vous débutant tous au
     * dateTime start tronqué à la minute.
     *
     * @param start le début des rendez-vous.
     * @return la collection de rendez-vous non modifiable.
     * @throws NullPointerException si start est null.
     */
    Collection<Appointment> findAllAtSameStart(LocalDateTime start) throws NullPointerException;

    /**
     * Identifie si des rendez-vous se chevauchent (l'un débute alors que
     * l'autre n'est pas terminé) pour tous les rendez-vous dont le début est
     * compris entre start (tronqué à la minute, inclus) et end (tronqué à la
     * minute, exlus). les paramètre start et end peuvent être null. Si start et
     * end sont nuls, tous les rendez-vous sont pris en compte. Sinon si start
     * est null, les rendez-vous pris en compte sont ceux débutant avant end
     * tronqué à la minute (exclus). Sinon si end est null, les rendez-vous pris
     * en compte sont ceux débutant après start tronqué à la minute (inclus).
     * Sinon seuls les rendez-vous dont le début est compris entre start
     * (inclus) et end (exlus) tronqués à la minute sont pris en compte.
     *
     * @param start la borne de recherche antérieure (inclue).
     * @param end la borne de recherche postérieure (exclue).
     * @return vrai si au moins un rendez-vous en chevauche un autre, faux
     * sinon.
     */
    boolean hasOverlap(LocalDateTime start, LocalDateTime end);

    /**
     * Trouve un créneau de temps disponible (sans rendez-vous) débutant au plus
     * tot au dateTime start (tronqué à la minute) et d'une durée
     * durationMinutes. Ce créneau peut se trouver dès start (si aucun
     * rendez-vous n'est en court ou débute pendant le temps du créneau libre
     * escompté), entre deux rendez-vous ou bien dans le pire des cas, à la fin
     * du dernier rendez-vous.
     *
     * @param start le début à partir duquel le créneau de temps disponible peut
     * commencer.
     * @param durationMinutes la durée du créneau de temps disponible désirée
     * (en minutes), strictement positive.
     * @return le dateTime du début de créneau libre. Ne sera jamais null.
     * @throws IllegalArgumentException si start est null ou si durationMinutes
     * est inférieur ou égal à 0.
     */
    LocalDateTime findClosestFreeTime(LocalDateTime start, int durationMinutes) throws IllegalArgumentException;

}
