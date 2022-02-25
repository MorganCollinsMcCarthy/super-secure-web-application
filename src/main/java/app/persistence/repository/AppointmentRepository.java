package app.persistence.repository;

import app.persistence.model.Appointment;
import app.persistence.model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Integer> {
    Appointment findByCentreAndDateAndTime(Centre centre, String date, String time);

    @Transactional
    @Modifying
    @Query("UPDATE Appointment appt SET appt.vaccine_received = :vaccineReceived WHERE appt.id = :id")
    int updateVaccineReceived(@Param("id") int id, @Param("vaccineReceived") String vaccineReceived);

    @Transactional
    @Modifying
    @Query("UPDATE Appointment appt SET appt.received = :received WHERE appt.id = :id")
    int updateReceived(@Param("id") int id, @Param("received") boolean received);
}
