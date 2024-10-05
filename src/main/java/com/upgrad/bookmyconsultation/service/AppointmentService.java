package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.entity.User;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.exception.SlotUnavailableException;
import com.upgrad.bookmyconsultation.repository.AppointmentRepository;
import com.upgrad.bookmyconsultation.repository.UserRepository;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {
	
	//mark it autowired
	@Autowired
	private AppointmentRepository appointmentRepository;
	//create an instance of AppointmentRepository called appointmentRepository

	@Autowired
	private UserRepository userRepository;

	public String appointmentName(Appointment appointment) throws SlotUnavailableException,InvalidInputException{

		ValidationUtils.validate(appointment);

		Appointment doctorExists = appointmentRepository.findByDoctorIdAndTimeSlotAndAppointmentDate(
				appointment.getDoctorId(),appointment.getTimeSlot(), appointment.getAppointmentDate());

		if(doctorExists.getTimeSlot().equals(appointment.getTimeSlot())) throw new SlotUnavailableException();

		Appointment save = appointmentRepository.save(appointment);

		return save.getAppointmentId();
	}
	//create a method name appointment with the return type of String and parameter of type Appointment
	//declare exceptions 'SlotUnavailableException' and 'InvalidInputException'
		//validate the appointment details using the validate method from ValidationUtils class
		//find if an appointment exists with the same doctor for the same date and time
		//if the appointment exists throw the SlotUnavailableException
		//save the appointment details to the database
		//return the appointment id

	public Optional<Appointment> getAppointment(String appointmentId){
		Optional<Appointment> appointment = Optional.ofNullable(appointmentRepository.findById(appointmentId)
				.orElseThrow(ResourceUnAvailableException::new));

        return appointment;
    }
	


	//create a method getAppointment of type Appointment with a parameter name appointmentId of type String
		//Use the appointmentid to get the appointment details
		//if the appointment exists return the appointment
		//else throw ResourceUnAvailableException
		//tip: use Optional.ofNullable(). Use orElseThrow() method when Optional.ofNullable() throws NULL
	
	public List<Appointment> getAppointmentsForUser(String userId) {
		return appointmentRepository.findByUserId(userId);
	}
}
