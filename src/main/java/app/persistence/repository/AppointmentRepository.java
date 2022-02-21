package app.persistence.repository;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import app.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    List<Appointment> findAllByCentreAndUser(Centre centre, User user);
    Appointment findById(int id);
}
