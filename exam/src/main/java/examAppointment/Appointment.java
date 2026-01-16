package examAppointment;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Représentation d'un rendez-vous.
 * Un rendez-vous (appointment, apt) est comparable à un autre sur la base de datetime de départ et leur nom.
 * 
 * @author Remi Venant
 */
public class Appointment implements Comparable<Appointment> {

    private final LocalDateTime start; // datetime de début
    private final int durationMinutes; // durée en minutes
    private final String name; // nom

    public Appointment(LocalDateTime start, int durationMinutes, String name) {
        this.start = start;
        this.durationMinutes = durationMinutes;
        this.name = name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.start);
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Appointment other = (Appointment) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.start, other.start);
    }

    /**
     * compare un rendez-vous à un autre sur la base de leur datetime de début et leur nom
     * @param apt le rendez-vous à comparer
     * @return 0 si les 2 rdv on le même datetime et le même nom ; 
     * &lt; 0 si l'instance courante débute avant apt ou, si l'instance courant et apt ont le même datetime de début, 
     * si l'instance courante a un nom inférieur que celui de apt dans l'ordre lexicographique (en ignorant la casse) ;
     * &gt; 0 si l'instance courante débute après apt ou, si l'instance courant et apt ont le même datetime de début, 
     * si l'instance courante a un nom supérieur que celui de apt dans l'ordre lexicographique (en ignorant la casse).
     */
    @Override
    public int compareTo(Appointment apt) {
        int resComp = this.start.compareTo(apt.start);
        if (resComp == 0) {
            resComp = this.name.compareToIgnoreCase(apt.name);
        }
        return resComp;
    }

}
