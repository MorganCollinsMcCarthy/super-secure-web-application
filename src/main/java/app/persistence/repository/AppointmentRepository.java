package app.persistence.repository;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    Appointment findByCentreAndDateAndTime(Centre centre, String date, String time);
}
