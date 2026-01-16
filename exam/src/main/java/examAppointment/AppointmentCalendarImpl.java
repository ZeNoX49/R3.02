package examAppointment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.TreeSet;

/*
Question 1
Quelle est la complexitÃ© algorithmique de la mÃ©thode add ?
O(log(n))

Question 2
Si nous souhaitons implÃ©menter une mÃ©thode pour modifier le nom et/ou la date 
de dÃ©but d'un rdv, et partant du principe que la classe Appointment propose 
un setter pour ces deux propriÃ©tÃ©s, pensez-vous que l'approche suivante soit correcte ?

public boolean updateName(localDateTime start, String oldName, localDateTime newStart, String newName) throws IllegalArgumentException {
  // vÃ©rifier la validitÃ© des paramÃ¨tres (non null, et non vide ou blanc pour oldName et newName)
  if (Ã©chec vÃ©rification) {
    throw new IllegalArgumentException("PropriÃ©tÃ© de rendez-vous invalide");
  }

  Appointment apt;
  // RÃ©cupÃ©rer apt: l'instance du rdv correspondant au dÃ©but start tronquÃ© Ã  la minute et au nom oldName.trim()

  if (apt == null) {
    return false;
  }

  // Modifier les propriÃ©tÃ© de apt
  apt.setStart(newStart tronquÃ© Ã  la minutÃ©);
  apt.setName(newName, tronquÃ© de ses caractÃ¨res blancs avant et aprÃ¨s);

  return true;
}
 */
/**
 * Gestionnaire de calendrier de rendez-vous.
 *
 * @author Ewen ATIDE-KLEIN
 */
public class AppointmentCalendarImpl implements AppointmentCalendar {

    // Le calendrier : Structure de rendez-vous
    // plus d'information sur https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/TreeSet.html
    private final TreeSet<Appointment> appointments = new TreeSet<>();

    /**
     * Tronque un dateTime donnÃ© Ã  la minute (les seconde, microsecondes, etc
     * sont Ã  0).
     *
     * @param dateTime le dateTime fourni.
     * @return la version tronquÃ©e Ã  la minute de dateTime ou null si dateTime
     * est null.
     */
    private LocalDateTime truncateDateTimeToMinutes(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * CrÃ©er un nouveau rendez-vous, l'ajoute au rendez-vous et le retourne. le
     * dÃ©but start fourni sera tronquÃ© Ã  la minute dans le rendez-vous crÃ©Ã©. la
     * durÃ©e de rendez-vous doit Ãªtre strictement positive et infÃ©rieure ou
     * Ã©gale Ã  24h. Le nom name fourni ne peut pas Ãªtre null ou vide (de taille
     * 0 ou constituÃ© que de caractÃ¨re "blancs" (espaces, saut de ligne,
     * tabulation...)) et sera tronquÃ© de part et d'autre de ses possibles
     * caractÃ¨res blancs.
     *
     * @param start le dÃ©but du rende-vous.
     * @param durationMinutes la durÃ©e en minute du rendez-vous.
     * @param name le nom du rendez-vous.
     * @return le rendez-vous crÃ©Ã© et ajoutÃ© au calendrier.
     * @throws IllegalArgumentException si start est null, durationMinutes est
     * infÃ©rieur ou Ã©gale Ã  0 ou strictement supÃ©rieur Ã  24h, si name est null
     * ou vide, ou si un rendez-vous de mÃªme dÃ©but et nom (sans respect de la
     * casse) est dÃ©jÃ  prÃ©sent.
     */
    @Override
    public Appointment add(LocalDateTime start, int durationMinutes, String name) throws IllegalArgumentException {
        // TODO : Ã  implÃ©menter
    	if (start == null || durationMinutes <= 0 || durationMinutes > 1440 || name == null || name.strip().isEmpty()) {
    		throw new IllegalArgumentException("Argements invalides");
    	}
    	
    	LocalDateTime truncatedStart = truncateDateTimeToMinutes(start);
    	String cleanedName = name.strip();
    	Appointment newRdv = new Appointment(truncatedStart, durationMinutes, cleanedName);
    	
    	if (appointments.contains(newRdv)) {
    		throw new IllegalArgumentException("T'as déjà créé le même rdv");
    	} else {
    		appointments.add(newRdv);
    		return newRdv;
    	}
    }

    /**
     * Retire un rendez-vous de calendrier d'aprÃ¨s son dÃ©but et son nom. le
     * dÃ©but start fourni sera tronquÃ© Ã  la minute pour la recherche du
     * rendez-vous. Le nom name fourni ne peut pas Ãªtre null ou vide (de taille
     * 0 ou constituÃ© que de caractÃ¨re "blancs" (espaces, saut de ligne,
     * tabulation...)) et sera tronquÃ© de part et d'autre de ses possibles
     * caractÃ¨res blancs.
     *
     * @param start le dÃ©but du rende-vous.
     * @param name le nom du rendez-vous.
     * @return vrai si le rendez-vous a Ã©tÃ© supprimÃ©, faux si ce dernier
     * n'existe pas.
     * @throws IllegalArgumentException si start est null, ou si name est null
     * ou vide.
     */
    @Override
    public boolean remove(LocalDateTime start, String name) throws IllegalArgumentException {
        // TODO : Ã  implÃ©menter
    	if (start == null || name == null || name.strip().isEmpty()) {
    		throw new IllegalArgumentException("Arguments invalides");
    	}
    	
    	LocalDateTime truncatedStart = truncateDateTimeToMinutes(start);
    	String cleanedName = name.strip();
    	
    	for (Appointment rdv: appointments) {
    		if (rdv.getName().equals(cleanedName) && rdv.getStart().equals(truncatedStart)) {
    			appointments.remove(rdv);
    			return true;
    		}
    	}
    	
    	return false;
    }

    /**
     * Retourne une collection non modifiable de rendez-vous dÃ©butant entre
     * start (inclus) et end (exlus) tronquÃ©s tous les deux Ã  la minute. les
     * paramÃ¨tre start et end peuvent Ãªtre null mais au moins l'un deux doit
     * Ãªtre donnÃ© non null. Si start est null, la collection retournÃ©e Ã©quivaut
     * Ã  tous les rendez-vous dÃ©butant avant end tronquÃ© Ã  la minute (exclus).
     * Si end est null, la collection retournÃ©e Ã©quivaut Ã  tous les rendez-vous
     * dÃ©butant aprÃ¨s start tronquÃ© Ã  la minute (inclus).
     *
     * @param start la borne de recherche antÃ©rieure.
     * @param end la borne de recherche postÃ©rieure.
     * @return la collection de rendez-vous non modifiable.
     * @throws NullPointerException si start et end sont nulls tous les deux.
     * @throws IllegalArgumentException si start et end sont non nulls et si
     * start est postÃ©rieure Ã  null (start > null).
     */
    @Override
    public Collection<Appointment> findAllStartingBetween(LocalDateTime start, LocalDateTime end) throws NullPointerException, IllegalArgumentException {
        // TODO : Ã  implÃ©menter
    	if (start == null && end == null) {
    		throw new NullPointerException();
    	}
    	
    	if (start != null && end != null && start.isAfter(end)) {
    		throw new IllegalArgumentException();
    	}
    	
    	LocalDateTime truncatedStart = truncateDateTimeToMinutes(start);
    	LocalDateTime truncatedEnd = truncateDateTimeToMinutes(end);

    	Appointment from = new Appointment(truncatedStart, 0, "");
    	Appointment to = new Appointment(truncatedEnd, 0, "");
    	
    	return Collections.unmodifiableCollection(appointments.subSet(from, true, to, true));
    }

    /**
     * Retourne une collection non modifiable de rendez-vous dÃ©butant tous au
     * dateTime start tronquÃ© Ã  la minute.
     *
     * @param start le dÃ©but des rendez-vous.
     * @return la collection de rendez-vous non modifiable.
     * @throws NullPointerException si start est null.
     */
    @Override
    public Collection<Appointment> findAllAtSameStart(LocalDateTime start) throws NullPointerException {
        if (start == null) {
    		throw new NullPointerException();
    	}

    	LocalDateTime truncatedStart = truncateDateTimeToMinutes(start);
    	
    	Appointment from = new Appointment(truncatedStart, 0, "");
    	Appointment to = new Appointment(truncatedStart.plusMinutes(1), 0, "");
    	
    	return Collections.unmodifiableCollection(appointments.subSet(from, to));
    }

    /**
     * Identifie si des rendez-vous se chevauchent (l'un dÃ©bute alors que
     * l'autre n'est pas terminÃ©) pour tous les rendez-vous dont le dÃ©but est
     * compris entre start (tronquÃ© Ã  la minute, inclus) et end (tronquÃ© Ã  la
     * minute, exlus). les paramÃ¨tre start et end peuvent Ãªtre null. Si start et
     * end sont nuls, tous les rendez-vous sont pris en compte. Sinon si start
     * est null, les rendez-vous pris en compte sont ceux dÃ©butant avant end
     * tronquÃ© Ã  la minute (exclus). Sinon si end est null, les rendez-vous pris
     * en compte sont ceux dÃ©butant aprÃ¨s start tronquÃ© Ã  la minute (inclus).
     * Sinon seuls les rendez-vous dont le dÃ©but est compris entre start
     * (inclus) et end (exlus) tronquÃ©s Ã  la minute sont pris en compte.
     *
     * @param start la borne de recherche antÃ©rieure (inclue).
     * @param end la borne de recherche postÃ©rieure (exclue).
     * @return vrai si au moins un rendez-vous en chevauche un autre, faux
     * sinon.
     */
    @Override
    public boolean hasOverlap(LocalDateTime start, LocalDateTime end) {
        // TODO : Ã  implÃ©menter

    	
    	return false;
    }

    /**
     * Trouve un crÃ©neau de temps disponible (sans rendez-vous) dÃ©butant au plus
     * tot au dateTime start (tronquÃ© Ã  la minute) et d'une durÃ©e
     * durationMinutes. Ce crÃ©neau peut se trouver dÃ¨s start (si aucun
     * rendez-vous n'est en court ou dÃ©bute pendant le temps du crÃ©neau libre
     * escomptÃ©), entre deux rendez-vous ou bien dans le pire des cas, Ã  la fin
     * du dernier rendez-vous.
     *
     * @param start le dÃ©but Ã  partir duquel le crÃ©neau de temps disponible peut
     * commencer.
     * @param durationMinutes la durÃ©e du crÃ©neau de temps disponible dÃ©sirÃ©e
     * (en minutes), strictement positive.
     * @return le dateTime du dÃ©but de crÃ©neau libre. Ne sera jamais null.
     * @throws IllegalArgumentException si start est null ou si durationMinutes
     * est infÃ©rieur ou Ã©gal Ã  0.
     */
    @Override
    public LocalDateTime findClosestFreeTime(LocalDateTime start, int durationMinutes) throws IllegalArgumentException {
        // TODO : Ã  implÃ©menter
        throw new UnsupportedOperationException("MÃ©thode Ã  implÃ©menter");
    }

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
                // VÃ©rifie que la mÃ©thode soit annotÃ©e @MyTest et rÃ©cupÃ©ration de l'annotation
                final TestSuite.MyTest myTest = method.getDeclaredAnnotation(TestSuite.MyTest.class);
                if (myTest == null) {
                    continue;
                }
                // VÃ©rifie que la mÃ©thode soit static
                if (!Modifier.isStatic(method.getModifiers())) {
                    continue;
                }
                // VÃ©rifie que la mÃ©thode ne nÃ©cessite pas de paramÃ¨tre pour Ãªtre invoquÃ©e
                if (method.getParameterCount() > 0) {
                    continue;
                }
                // Calcul le nom du test : valeur de l'annotation si non vide, nom de la mÃ©thode sinon
                final String testName = myTest.value() != null && !myTest.value().isEmpty() ? myTest.value() : method.getName();
                // Execution de la mÃ©thode de test
                try {
                    System.out.println(testName);
                    method.invoke(null);
                    System.out.println("  SuccÃ¨s.");
                    nbTests++;
                    nbPassedTests++;
                } catch (IllegalAccessException | IllegalArgumentException | NullPointerException | ExceptionInInitializerError ex) {
                    // Erreur d'invocation de la mÃ©thode: ne devrait pas arriver
                    System.err.println("TEST INVOK ERROR of " + method.getName() + " : " + ex.getMessage());
                } catch (InvocationTargetException ex) {
                    String messagePrefix = "  Echec : ";
                    // Si cause est exception de test : affichage de son message
                    if (ex.getCause() instanceof MyTestException) {
                        System.out.println(messagePrefix + ex.getCause().getMessage());
                    } else {
                        System.out.println(messagePrefix + " exception levÃ©e (" + ex.getCause().getMessage() + ").");
                    }
                    nbTests++;
                }
            }
            System.out.println(String.format("RESUME : %d test(s) passÃ©(s), %d test(s) Ã©chouÃ©(s).", nbPassedTests, nbTests - nbPassedTests));
        }

        @MyTest("TEST de la mÃ©thode add sur les traitements attendus.")
        public static void testAdd() {
            // create initial calendar
            final AppointmentCalendarImpl cal = new AppointmentCalendarImpl();
            // test that initial calendar has no appointment
            AssertThat.isTrue(cal.appointments.isEmpty(), "Initial calendar should be empty.");
            // create valid appointment with seconds and nanos <> 0 and name with space before and after
            LocalDateTime testDt = LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30, 5, 5); // 2023-11-01T10:30:05:000005
            String testName = "   Apt   ";
            int testDuration = 30;
            Appointment apt = cal.add(testDt, testDuration, testName);
            // assert created appoint has second and lower time units at 0, and name trimmed()
            AssertThat.isEquals(testDt.truncatedTo(ChronoUnit.MINUTES), apt.getStart(), "Created appointment start dateTime should not contains seconds or lower time unit.");
            AssertThat.isEquals(testName.trim(), apt.getName(), "Created appointment name should not contain any space at the beginning or at the end.");
            AssertThat.isEquals(testDuration, apt.getDurationMinutes(), "Created appointment duration should be the one provided.");
            // assert calendar has 1 appointment
            AssertThat.isTrue(cal.appointments.size() == 1, "Calendar should now contain 1 appointment.");
            // attempt to create another appointment with same start and different name
            cal.add(testDt, testDuration, "Apt2");
            // assert calendar has 2 appointment
            AssertThat.isTrue(cal.appointments.size() == 2, "Calendar should now contain 2 appointments.");
            // attempt to create another appointment with different start and same name
            cal.add(testDt.plusMinutes(1), testDuration, testName);
            // assert calendar has 3 appointment
            AssertThat.isTrue(cal.appointments.size() == 3, "Calendar should now contain 3 appointments.");
            // attempt to create another appointment with different start and diff name and duration of 24h
            cal.add(testDt.plusMinutes(2), 24 * 60, testName);
            // assert calendar has 4 appointment
            AssertThat.isTrue(cal.appointments.size() == 4, "Calendar should now contain 4 appointments.");
        }

        @MyTest("TEST de la mÃ©thode add sur les exceptions levÃ©es.")
        public static void testAddExceptions() {
            // create initial calendar
            final AppointmentCalendarImpl cal = new AppointmentCalendarImpl();
            // attempt to create appoint with null start
            AssertThat.fails(() -> {
                cal.add(null, 30, "apt");
            }, IllegalArgumentException.class, "Adding an appointment with a null start dateTime should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (1).");

            // attempt to create appoint with 0 duration
            LocalDateTime testDt = LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30); // 2023-11-01T10:30:00:000000
            AssertThat.fails(() -> {
                cal.add(testDt, 0, "apt");
            }, IllegalArgumentException.class, "Adding an appointment with a 0 duration should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (2).");

            // attempt to create appoint with negative duration 
            AssertThat.fails(() -> {
                cal.add(testDt, -1, "apt");
            }, IllegalArgumentException.class, "Adding an appointment with a negative duration should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (3).");

            // attempt to create appoint with duration > 24h
            AssertThat.fails(() -> {
                cal.add(testDt, 60 * 24 + 1, "apt");
            }, IllegalArgumentException.class, "Adding an appointment with a duration strictly above 24h should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (4).");

            // attempt to create appoint with null name
            AssertThat.fails(() -> {
                cal.add(testDt, 30, null);
            }, IllegalArgumentException.class, "Adding an appointment with a null name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (5).");

            // attempt to create appoint with empty name
            AssertThat.fails(() -> {
                cal.add(testDt, 30, "");
            }, IllegalArgumentException.class, "Adding an appointment with an empty name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (6).");

            // attempt to create appoint with blank name
            AssertThat.fails(() -> {
                cal.add(testDt, 30, "   \t  ");
            }, IllegalArgumentException.class, "Adding an appointment with an blank name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.isEmpty(), "Calendar should still be empty (7).");

            // create valid appointment with minutes <> 0 and name with space before and after
            cal.add(testDt, 30, "APT");
            // assert calendar has 1 appointment
            AssertThat.isTrue(cal.appointments.size() == 1, "Calendar should now contain 1 appointment.");

            // attempt to create another appointment with same start (+ sec and nano) and name (with diff. case and with trailing blanck chars)
            AssertThat.fails(() -> {
                cal.add(testDt.plusSeconds(10).plusNanos(55), 30, "  \t  ApT  \t  ");
            }, IllegalArgumentException.class, "Adding an already existing appointment should fail with an IllegalArgumentException exception.");
            // assert calendar has still 1 appointment
            AssertThat.isTrue(cal.appointments.size() == 1, "Calendar should still contain 1 appointment.");
        }

        @MyTest("TEST de la mÃ©thode remove sur les traitements attendus.")
        public static void testRemove() {
            // gerenerate test calendar
            final AppointmentCalendarImpl cal = generateTestAptCalendar();
            // get number of appointment nInit
            int initialAptCount = cal.appointments.size();
            // remove one appointment
            Appointment aptToRemove = generateTestAppointments()[2];
            boolean removeRes = cal.remove(aptToRemove.getStart().plusSeconds(10).plusNanos(5), "  \t " + aptToRemove.getName() + "  \t  ");
            // assert remove is true
            AssertThat.isTrue(removeRes, "Remove should return true for a present appointment");
            // assert number of appointment is now nInit - 1
            AssertThat.isTrue(cal.appointments.size() == initialAptCount - 1, "Calendar should now contain 1 appointment less.");
            // remove the same appointment
            removeRes = cal.remove(aptToRemove.getStart(), aptToRemove.getName());
            // assert remove is false
            AssertThat.isFalse(removeRes, "Remove should return false for a absent appointment");
            // assert number of appointment is still nInit - 1
            AssertThat.isTrue(cal.appointments.size() == initialAptCount - 1, "Calendar should still contain 1 appointment less.");
        }

        @MyTest("TEST de la mÃ©thode remove sur les exceptions levÃ©es.")
        public static void testRemoveExceptions() {
            // create test calendar and get number of appointment nInit
            final AppointmentCalendarImpl cal = generateTestAptCalendar();
            int initialAptCount = cal.appointments.size();
            // attempt to remove apt with null start
            AssertThat.fails(() -> {
                cal.remove(null, "apt");
            }, IllegalArgumentException.class, "Removing an appointment with a null start dateTime should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.size() == initialAptCount, "Calendar should still contains all its appointments (1).");

            // attempt to remove apt with null name
            LocalDateTime dt = generateTestStarts()[2];
            AssertThat.fails(() -> {
                cal.remove(dt, null);
            }, IllegalArgumentException.class, "Removing an appointment with a null name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.size() == initialAptCount, "Calendar should still contains all its appointments (2).");

            // attempt to remove apt with empty name
            AssertThat.fails(() -> {
                cal.remove(dt, "");
            }, IllegalArgumentException.class, "Removing an appointment with an empty name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.size() == initialAptCount, "Calendar should still contains all its appointments (3).");

            // attempt to remove apt with blank name
            AssertThat.fails(() -> {
                cal.remove(dt, "  \t  ");
            }, IllegalArgumentException.class, "Removing an appointment with a blanck name should fail with an IllegalArgumentException exception.");
            AssertThat.isTrue(cal.appointments.size() == initialAptCount, "Calendar should still contains all its appointments (4).");
        }

        @MyTest("TEST de la mÃ©thode findAllStartingBetween sur les traitements attendus.")
        public static void testFindAllStartingBetween() {
            // gerenerate test calendar
            final AppointmentCalendarImpl cal = generateTestAptCalendar();
            int initialAptCount = cal.appointments.size();
            final LocalDateTime[] allDts = generateTestStarts();

            // find all starting with bounds including all apt
            Collection<Appointment> apts = cal.findAllStartingBetween(allDts[0].minusHours(1), allDts[allDts.length - 1].plusHours(1));
            // assert we got all apt
            AssertThat.isEquals(initialAptCount, apts.size(), "Apts startings between bounds including all apts from cal should match cal size.");
            HashSet<Appointment> testSet = new HashSet<>(apts);
            testSet.retainAll(cal.appointments);
            AssertThat.isEquals(testSet.size(), apts.size(), "pts startings between bounds including all apts from cal should be all apts.");
            // find all starting with bounds including subset of apt (check start is included, end excluded)
            apts = cal.findAllStartingBetween(allDts[3], allDts[6]);
            // assert we got all expected apt
            AssertThat.isEquals(3, apts.size(), "Apts starting between with bounds including subset of cal should match the excepted size.");

            // find all starting with bounds including no apt (check start is included, end excluded)
            apts = cal.findAllStartingBetween(allDts[5].plusMinutes(60 + 50), allDts[6]);
            // assert we got an empty list
            AssertThat.isTrue(apts.isEmpty(), "Apts starting between with bounds including no apt from call should be an empty list.");

            // find all starting with only start bound (check start is included)
            apts = cal.findAllStartingBetween(allDts[3], null);
            // assert we got all expected apt
            AssertThat.isEquals(4, apts.size(), "Apts starting between with only start bound including subset of cal should match the excepted size.");

            // find all starting with only start bound after all apt  (check start is included)
            apts = cal.findAllStartingBetween(allDts[6].plusMinutes(1), null);
            // assert we got an empty list
            AssertThat.isTrue(apts.isEmpty(), "Apts starting between with only start bound including no apt from call should be an empty list.");

            // find all starting with only end bound (check end is excluded)
            apts = cal.findAllStartingBetween(null, allDts[6]);
            // assert we got all expected apt
            AssertThat.isEquals(6, apts.size(), "Apts starting between with only end bound including subset of cal should match the excepted size.");

            // find all starting with only end bound before all apt (check end is excluded)
            apts = cal.findAllStartingBetween(null, allDts[0]);
            // assert we got an empty list
            AssertThat.isTrue(apts.isEmpty(), "Apts starting between with only end bound including no apt from call should be an empty list.");
        }

        @MyTest("TEST de la mÃ©thode findAllStartingBetween sur les exceptions levÃ©es.")
        public static void testFindAllStartingBetweenExceptions() {
            // create initial calendar
            final AppointmentCalendar cal = new AppointmentCalendarImpl();
            // attempt to find all starting with no bound
            AssertThat.fails(() -> {
                cal.findAllStartingBetween(null, null);
            }, NullPointerException.class, "Apts starting between with no bound at all should fail with an NullPointerException exception.");

            // attempt to find all starting with no bound
            AssertThat.fails(() -> {
                LocalDateTime start = LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 31);
                LocalDateTime end = LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30);
                cal.findAllStartingBetween(start, end);
            }, IllegalArgumentException.class, "Apts starting between with start bound after end bound should fail with an IllegalArgumentException exception.");
        }

        @MyTest("TEST de la mÃ©thode findAllAtSameStart sur les traitements attendus.")
        public static void testFindAllAtSameStart() {
            // gerenerate test calendar
            final AppointmentCalendar cal = generateTestAptCalendar();
            final LocalDateTime[] allDts = generateTestStarts();
            // find all at same start with a start that matches only one apt (check start is included)
            Collection<Appointment> apts = cal.findAllAtSameStart(allDts[2]);
            // assert we got all expected apt
            AssertThat.isEquals(1, apts.size(), "Apts startings at same start with a start that matches only one apt should be of size 1.");

            // find all at same start with a start that matches several apt (check start is included)
            apts = cal.findAllAtSameStart(allDts[4]);
            // assert we got all expected apt
            AssertThat.isEquals(2, apts.size(), "Apts startings at same start with a start that matches several apts should be of size 2.");

            // find all at same start with a start that matches no apt (check start is included)
            apts = cal.findAllAtSameStart(allDts[allDts.length - 1].plusMinutes(1));
            // assert we got an empty list
            AssertThat.isTrue(apts.isEmpty(), "Apts startings at same start with a start that matches no apt should be empty.");
        }

        @MyTest("TEST de la mÃ©thode findAllAtSameStart sur les exceptions levÃ©es.")
        public static void testFindAllAtSameStartExceptions() {
            // create initial calendar
            final AppointmentCalendar cal = new AppointmentCalendarImpl();
            // attempt to find all at same start with no start
            AssertThat.fails(() -> {
                cal.findAllAtSameStart(null);
            }, NullPointerException.class, "Apts starting at same start with a null start should fail with an NullPointerException exception.");
        }

        @MyTest("TEST de la mÃ©thode hasOverlap sur les traitements attendus.")
        public static void testHasOverlap() {
            // gerenerate test calendar
            final AppointmentCalendar cal = generateTestAptCalendar();
            final LocalDateTime[] allDts = generateTestStarts();

            // has overlap with no start and end
            boolean resOverlap = cal.hasOverlap(null, null);
            // assert we have overlap
            AssertThat.isTrue(resOverlap, "An overlap should exist over all test apts.");

            // has overlap with only a start before overlap (check start is included)
            resOverlap = cal.hasOverlap(allDts[4], null);
            // assert we have overlap
            AssertThat.isTrue(resOverlap, "An overlap should exist with only a start before overlap.");

            // has overlap with only a start after overlap (check start is included)
            resOverlap = cal.hasOverlap(allDts[6], null);
            // assert we do not have overlap
            AssertThat.isFalse(resOverlap, "An overlap should not exist with only a start after overlap.");

            // has overlap with only a end before overlap (check end is excluded)
            resOverlap = cal.hasOverlap(null, allDts[6]);
            // assert we do not have overlap
            AssertThat.isTrue(resOverlap, "An overlap should exist with only an end after overlap.");

            // has overlap with only a end after overlap (check end is excluded)
            resOverlap = cal.hasOverlap(null, allDts[3].minusMinutes(1));
            // assert we have overlap
            AssertThat.isFalse(resOverlap, "An overlap should not exist with only an end before overlap.");

            // has overlap with a start and a end not including overlap (check start is included, end excluded)
            resOverlap = cal.hasOverlap(allDts[0], allDts[3]);
            // assert we do not have overlap
            AssertThat.isFalse(resOverlap, "An overlap should not exist  with a start and a end not including overlap.");

            // has overlap with a start and a end including overlap because of 2 apts starting at the same time (check start is included, end excluded)
            resOverlap = cal.hasOverlap(allDts[5], allDts[6]);
            // assert we have overlap
            AssertThat.isTrue(resOverlap, "An overlap should exist with a start and a end including overlap because of 2 apts starting at the same time.");

            // has overlap with a start and a end including overlap because of 2 apts starting not at the same time but still overlapping (check start is included, end excluded)
            resOverlap = cal.hasOverlap(allDts[2], allDts[3].plusMinutes(3));
            // assert we have overlap
            AssertThat.isTrue(resOverlap, "An overlap should exist with a start and a end including overlap because of 2 apts starting not at the same time but still overlapping.");
        }

        @MyTest("TEST de la mÃ©thode findClosestFreeTime sur les traitements attendus.")
        public static void testFindClosestFreeTime() {
            // gerenerate test calendar
            final AppointmentCalendar cal = generateTestAptCalendar();
            final Appointment[] allApts = generateTestAppointments();
            // Will truck reference time as they are noisy

            // find closest free time that fits at starts (check start is included)
            LocalDateTime freeTimeStart = cal.findClosestFreeTime(allApts[0].getStart().minusMinutes(20), 20);
            // assert the return time is start (with minutes)
            AssertThat.isEquals(allApts[0].getStart().truncatedTo(ChronoUnit.MINUTES).minusMinutes(20), freeTimeStart, "closest free time that fits at starts should be equal to start.");

            // find closest free time that can fit first free time slot (check start is included)
            freeTimeStart = cal.findClosestFreeTime(allApts[0].getStart().minusMinutes(20), 30);
            // assert the return time is the one excepted
            AssertThat.isEquals(allApts[0].getStart().truncatedTo(ChronoUnit.MINUTES).plusMinutes(allApts[0].getDurationMinutes()), freeTimeStart, "closest free time that fits first free time slot should start at the end of first apt.");

            // find closest free time that can fit second slot with longer duration (check start is included)
            freeTimeStart = cal.findClosestFreeTime(allApts[0].getStart().minusMinutes(20), 45);
            // assert the return time is the one excepted
            AssertThat.isEquals(allApts[4].getStart().truncatedTo(ChronoUnit.MINUTES).plusMinutes(allApts[4].getDurationMinutes()), freeTimeStart, "closest free time that fits second free time slot should start at the end of fifth apt.");

            // find closest free time that cannot fit at start or between apts (check start is included)
            freeTimeStart = cal.findClosestFreeTime(allApts[0].getStart().minusMinutes(20), 46);
            // assert the return time is the one excepted
            AssertThat.isEquals(allApts[allApts.length - 1].getStart().truncatedTo(ChronoUnit.MINUTES).plusMinutes(allApts[allApts.length - 1].getDurationMinutes()), freeTimeStart, "closest free time that cannot fit at start or between apts should start at the end of last apt.");
        }

        @MyTest("TEST de la mÃ©thode findClosestFreeTime sur les exceptions levÃ©es.")
        public static void testFindClosestFreeTimeExceptions() {
            // create initial calendar
            final AppointmentCalendar cal = new AppointmentCalendarImpl();
            // attempt to find closest free time with null start
            AssertThat.fails(() -> {
                cal.findClosestFreeTime(null, 30);
            }, IllegalArgumentException.class, "findClosestFreeTime with a null start should fail with an IllegalArgumentException exception.");

            // attempt to find closest free time with 0 duration 
            AssertThat.fails(() -> {
                cal.findClosestFreeTime(LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30), 0);
            }, IllegalArgumentException.class, "findClosestFreeTime with a 0 duration should fail with an IllegalArgumentException exception.");

            // attempt to find closest free time with negative duration
            AssertThat.fails(() -> {
                cal.findClosestFreeTime(LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30), -1);
            }, IllegalArgumentException.class, "findClosestFreeTime with a negative duration should fail with an IllegalArgumentException exception.");
        }

        private static LocalDateTime[] generateTestStarts() {
            // Generate dateTimes for calendar test with noisy seconds and/or nanosec
            LocalDateTime[] starts = new LocalDateTime[7];
            starts[0] = LocalDateTime.of(2023, Month.NOVEMBER, 1, 10, 30); // 2023-11-01T10:30:00:000000
            starts[1] = starts[0].plusHours(1).plusMinutes(30); // 2023-11-01T12:00:00:000000
            starts[2] = starts[1].plusHours(1); // 2023-11-01T13:00:00:000000
            starts[3] = starts[2].plusMinutes(30); // 2023-11-01T13:30:00:000000
            starts[4] = starts[3].plusHours(1); // 2023-11-01T14:30:00:000000
            starts[5] = starts[4]; // 2023-11-01T14:30:00:000000
            starts[6] = starts[5].plusHours(1).plusMinutes(50 + 45); // 2023-11-01T17:05:00:000000
            // add noisy seconds and/or nanosec
            starts[1] = starts[1].plusSeconds(59);
            starts[3] = starts[3].plusSeconds(10).plusNanos(5);
            starts[4] = starts[4].plusNanos(5);
            starts[6] = starts[6].plusSeconds(59).plusNanos(500);
            return starts;
        }

        private static Appointment[] generateTestAppointments() {
            LocalDateTime[] starts = generateTestStarts();
            return new Appointment[]{
                new Appointment(starts[0].truncatedTo(ChronoUnit.MINUTES), 60, "Apt"), // end 2023-11-01T11:30:00:000000
                new Appointment(starts[1].truncatedTo(ChronoUnit.MINUTES), 60, "Apt"), // end 2023-11-01T13:00:00:000000
                new Appointment(starts[2].truncatedTo(ChronoUnit.MINUTES), 60, "Apt"), // end 2023-11-01T14:00:00:000000
                new Appointment(starts[3].truncatedTo(ChronoUnit.MINUTES), 60, "Apt"), // end 2023-11-01T14:30:00:000000
                new Appointment(starts[4].truncatedTo(ChronoUnit.MINUTES), 60 + 50, "Apt"), // end 2023-11-01T16:20:00:000000
                new Appointment(starts[5].truncatedTo(ChronoUnit.MINUTES), 60, "Apt2"), // end 2023-11-01T15:30:00:000000
                new Appointment(starts[6].truncatedTo(ChronoUnit.MINUTES), 60, "Apt") // end 2023-11-01T18:05:00:000000
            };
        }

        private static AppointmentCalendarImpl generateTestAptCalendar() {
            AppointmentCalendarImpl aptCal = new AppointmentCalendarImpl();
            aptCal.appointments.addAll(Arrays.asList(generateTestAppointments()));
            return aptCal;
        }
    }
}
