package app.service;

import app.persistence.model.Appointment;
import app.persistence.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AdminService implements IAdminService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    public void updateApptVaccineReceived(int appointmentID, String vaccineReceived) {
        appointmentRepository.updateVaccineReceived(appointmentID, vaccineReceived);
        appointmentRepository.updateReceived(appointmentID, !vaccineReceived.equals(""));
    }
}
